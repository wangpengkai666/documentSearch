package com.example.documentseach.common.util.container;

/**
 * @author wangpengkai
 */
public class StringUtil {
    public static boolean isNotBlank(String s) {
        return s != null && s.length() != 0 && !s.isBlank();
    }
    public static boolean isEmpty(String s) { return s==null||s.isEmpty();}
    public static boolean isBlank(String s) {
        return s==null||s.isBlank();
    }
}
