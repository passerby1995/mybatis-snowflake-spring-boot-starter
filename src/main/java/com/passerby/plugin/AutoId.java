package com.passerby.plugin;

import java.lang.annotation.*;

/**
 * 自动生成主键注解
 * @author sunjunyao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoId {
    /**
     * id类型：默认雪花算法
     * @return
     */
    IdType value() default IdType.SNOWFLAKE;

    /**
     * ID类型
     */
    enum IdType {
        /**
         * uuid
         */
        UUID,
        /**
         * 生成雪花算法id
         */
        SNOWFLAKE
    }
}
