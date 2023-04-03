package com.passerby.handler;

import com.passerby.plugin.SnowIdUtils;

import java.lang.reflect.Field;

public  class UniqueLongHexHandler extends Handler {
        public UniqueLongHexHandler(Field field) {
            super(field);
        }

        /**
         * 3、插入String类型雪花ID
         */
        @Override
        void handle(Field field, Object object) throws Throwable {
            field.set(object, SnowIdUtils.uniqueLongHex());
        }
    }
