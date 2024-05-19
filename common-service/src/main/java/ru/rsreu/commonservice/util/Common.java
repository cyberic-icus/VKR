package ru.rsreu.commonservice.util;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;

public class Common {

    private static final SerializeConfig mapping = new SerializeConfig();

    static {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    public static boolean isEquals(String a, String b) {
        if (a == null || b == null || a.equals("") || b.equals("")) {
            return false;
        }
        if (a == b || a.equals(b)) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String x) {
        if (x == null || x.equals("")) {
            return true;
        }
        return false;
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimeFromString(String time) {
        return Timestamp.valueOf(time);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}
