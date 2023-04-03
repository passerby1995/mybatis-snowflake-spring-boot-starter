package com.passerby.plugin;

import com.google.common.base.Predicate;
import com.passerby.handler.Handler;
import com.passerby.handler.UUIDHandler;
import com.passerby.handler.UniqueLongHandler;
import com.passerby.handler.UniqueLongHexHandler;
import org.apache.ibatis.executor.Executor;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.reflections.ReflectionUtils;


import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {
        MappedStatement.class, Object.class
}))
public class AutoIdInterceptor implements Interceptor {
    private static Logger log = LoggerFactory.getLogger(AutoIdInterceptor.class);

    private Map<Class, List<Handler>> handlerMap = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //args数组即为上面@Signature中args参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        //实体类
        Object entity = args[1];
        //只针对insert语句生成分布式id
        if ("INSERT".equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            Set<Object> entitySet = getEntitySet(entity);
            //批量设置id
            for (Object object : entitySet) {
                process(object);
            }

        }
        return invocation.proceed();
    }


    /**
     * entity如果是单个对象，则entity就是当前对象
     * 如果是批量插入，则entity是一个map集合，key为"list",value是一个ArrayList集合
     *
     * @param entity
     * @return
     */
    private Set<Object> getEntitySet(Object entity) {
        Set<Object> set = new HashSet<>();
        //批量插入
        if (entity instanceof Map) {
            Collection values = (Collection) ((Map) entity).get("list");
            log.info("value={}", values);
            for (Object value : values) {
                if (value instanceof Collection) {
                    set.addAll((Collection) value);
                } else {
                    set.add(value);
                }

            }
        } else {
            set.add(entity);
        }
        return set;
    }

    private void process(Object object) throws Throwable {
        Class<?> handlerKey = object.getClass();
        List<Handler> handlerList = handlerMap.get(handlerKey);
        SYNC:
        if (handlerList == null) {
            synchronized (this) {
                handlerList = handlerMap.get(handlerKey);
                if (handlerList != null) {
                    break SYNC;
                }
            }
            handlerMap.put(handlerKey, handlerList = new ArrayList<>());
            Set<Field> allFields = ReflectionUtils.getAllFields(
                    object.getClass(),
                    (Predicate<? super Field>) input -> input != null && input.getAnnotation(AutoId.class) != null
            );

            for (Field field : allFields) {
                AutoId annotation = field.getAnnotation(AutoId.class);
                //1、添加UUID字符串作为主键
                if (field.getType().isAssignableFrom(String.class)) {
                    if (annotation.value().equals(AutoId.IdType.UUID)) {
                        handlerList.add(new UUIDHandler(field));
                        //2、添加String类型雪花ID
                    } else if (annotation.value().equals(AutoId.IdType.SNOWFLAKE)) {
                        handlerList.add(new UniqueLongHexHandler(field));
                    }
                } else if (field.getType().isAssignableFrom(Long.class)) {
                    //3、添加Long类型的雪花ID
                    if (annotation.value().equals(AutoId.IdType.SNOWFLAKE)) {
                        handlerList.add(new UniqueLongHandler(field));
                    }
                }
            }

        }
        for (Handler handler : handlerList) {
            handler.accept(object);
        }


    }








}
