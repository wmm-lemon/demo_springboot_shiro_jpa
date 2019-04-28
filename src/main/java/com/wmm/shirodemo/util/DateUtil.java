package com.wmm.shirodemo.util;

import java.sql.Timestamp;

public class DateUtil {

    /**
     * 获取当前系统时间
     * @return
     */
    public static Timestamp currentDateTime(){
        return new Timestamp(System.currentTimeMillis());
    }
}
