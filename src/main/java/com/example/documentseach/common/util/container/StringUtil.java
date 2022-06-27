package com.example.documentseach.common.util.container;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Iterator;

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
    public static String join(Iterable<?> iterable, String separator) {
        return iterable == null ? null : join(iterable.iterator(), separator);
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                String result = ObjectUtils.toString(first);
                return result;
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while(iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }
}
