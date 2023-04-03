package com.passerby.handler;

import com.passerby.plugin.SnowIdUtils;

import java.lang.reflect.Field;

public class UniqueLongHandler extends Handler {
    public UniqueLongHandler(Field field) {
        super(field);
    }

    /**
     * 2、插入Long类型雪花ID
     */
    @Override
    void handle(Field field, Object object) throws Throwable {
        field.set(object, SnowIdUtils.uniqueLong());
    }
}
