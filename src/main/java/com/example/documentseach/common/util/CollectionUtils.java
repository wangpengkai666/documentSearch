package com.example.documentseach.common.util;

import java.util.List;
import java.util.Set;

/**
 * @author wangpengkai
 */
public class CollectionUtils {

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isEmpty(Set<?> set) {
        return set==null||set.isEmpty();
    }
}
