package com.il.lexicon.data.dto;

public interface Sync<K> {
    K getServerId();
    boolean isSync();
}
