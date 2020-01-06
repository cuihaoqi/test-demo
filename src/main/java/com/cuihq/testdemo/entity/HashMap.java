package com.cuihq.testdemo.entity;

import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

public class HashMap<K,V> implements Map<K,V>{
	private int size;
	private K k;
	private V v;
	private int cap;
	private double loadFactor;
	private double throld;
	private Node[] table;

	public HashMap() {
		this(16,0.75);
	}

	public HashMap(int cap, double loadFactor) {
		this.cap = tableSizeFor(cap);
		this.loadFactor = loadFactor;
		table = new Node[cap];
	}

	//初始化数组大小必为2的幂次方
	static final int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}

	//散列算法
	private int getIndex(K k){
		int hashCode = k.hashCode();
		int index = hashCode & (cap - 1);
		return Math.abs(index);
	}

	@Override
	public void put(K k, V v) {
		//根据key，获取在map中Node数组的下标
		//获取到数组对应元素
		//如果没有元素，则直接添加
		//如果有元素，则判断k是否一致，一致替换，不一致形成链表
		int index = getIndex(k);
		int hashCode = k.hashCode();
		Node node = table[index];
		if(node == null){
			table[index] = new Node(hashCode,k,v,null);
		}else{
			if(node.getHash() == hashCode){
				node.setV(v);
			}else{
				//hash碰撞
				table[index] = new Node(hashCode,k,v,node);
			}
		}
		++size;
	}

	@Override
	public V get(K k) {
		//获取下标
		//取出对应数组下标元素，如果元素的hashCode与元素相同，取出，如果不同，拿到next对比
		int index = getIndex(k);
		int hashCode = k.hashCode();
		Node<K,V> node = table[index];
		getNodeV(hashCode, node);
		return this.v;
	}

	private void getNodeV(int hashCode, Node<K, V> node) {
		if(node == null){
			return;
		}

		int nodeHash = node.getHash();
		if (hashCode != nodeHash) {
			node = node.getNext();
			getNodeV(hashCode, node);
		}else{
			this.v =  node.getV();
		}
	}

	@Override
	public int size() {
		return size;
	}
}
