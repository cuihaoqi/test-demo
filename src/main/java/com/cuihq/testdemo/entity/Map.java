package com.cuihq.testdemo.entity;

public interface Map<K,V> {
	void put(K k, V v);
	V get(K k);
	int size();
}
