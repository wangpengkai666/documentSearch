package com.example.documentseach.common.util;

import java.util.Objects;

/**
 * @author wangpengkai
 */
public class Triple<T, V, U> {
    private T v1;
    private V v2;
    private U v3;

    public Triple(){}

    public Triple(T v1, V v2, U v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public T v1() {
        return v1;
    }

    public Triple<T, V, U> setV1(T v1) {
        this.v1 = v1;
        return this;
    }

    public V v2() {
        return v2;
    }

    public Triple<T, V, U> setV2(V v2) {
        this.v2 = v2;
        return this;
    }

    public U v3() {
        return v3;
    }

    public Triple<T, V, U> setV3(U v3) {
        this.v3 = v3;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;

        if (!Objects.equals(v1, triple.v1)) {return false;}
        if (!Objects.equals(v2, triple.v2)) {return false;}
        return Objects.equals(v3, triple.v3);
    }

    @Override
    public int hashCode() {
        int result = (v1 != null) ? v1.hashCode() : 0;
        result = 63 * result + 31 * (v2 != null ? v2.hashCode() : 0) + (v2 != null ? v2.hashCode() : 0);
        return result;
    }
}