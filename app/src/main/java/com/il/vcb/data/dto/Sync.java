package com.il.vcb.data.dto;

public interface Sync<K> {
    K getServerId();
    boolean isSync();
}
