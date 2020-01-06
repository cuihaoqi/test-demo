package com.cuihq.testdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class Node<K,V> {
	private int hash;
	private K k;
	private V v;
	private Node next;



	@Override
	public String toString() {
		return "Node{" +
				"k=" + k +
				", v=" + v +
				'}';
	}
}
