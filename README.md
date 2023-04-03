# mybatis-snowflake-spring-boot-starter
利用mybatis拦截器，自动生成分布式id
# 一款高性能的生成分布式id的springboot-starter组件

## Getting started

### Clone code 

git clone https://github.com/passerby1995/mybatis-snowflake-spring-boot-starter.git

1.代码clone到本地使用maven install到本地仓库,springboot项目中引入maven依赖：

#### Maven dependency

```xml
 <dependency>
   	<groupId>com.passerby</groupId>
    <artifactId>mybatis-snowflake-spring-boot-starter</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

2.在需要生成分布式id的字段上加入 @AutoId 注解即可

```java
public class User {
    @AutoId
    public Long uid;
    
    public String sex;
    
    private Integer age;
    //...get,set等等

}
```

