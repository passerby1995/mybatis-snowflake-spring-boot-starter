package com.passerby.handler;

import java.lang.reflect.Field;

public  abstract class Handler {
        Field field;

        Handler(Field field) {
            this.field = field;
        }

        abstract void handle(Field field, Object object) throws Throwable;

        private boolean checkField(Object object, Field field) throws IllegalAccessException {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //如果该注解对应的属性已经被赋值，那么就不用通过雪花生成的ID
            return field.get(object) == null;
        }

        public void accept(Object o) throws Throwable {
            if (checkField(o, field)) {
                handle(field, o);
            }
        }
    }