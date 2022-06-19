package com.example.documentseach.common.util;

/**
 * @author wangpengkai
 */
public class StringUtil {
    public static boolean isNotBlank(String s) {
        return s != null && s.length() != 0 && !s.isBlank();
    }
}
