package com.passerby.handler;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDHandler extends Handler {
        public UUIDHandler(Field field) {
            super(field);
        }

        /**
         * 1、插入UUID主键
         */
        @Override
        void handle(Field field, Object object) throws Throwable {
            field.set(object, UUID.randomUUID().toString().replace("-", ""));
        }
    }
