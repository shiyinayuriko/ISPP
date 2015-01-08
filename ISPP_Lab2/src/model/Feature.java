package model;

import java.util.Arrays;
/**
 * ���ʵ�ĳ����������Ӧ�ı��еĵ���feature��Ϣ
 * @author yuriko
 *
 */
public class Feature {
	/**
	 * Feature��Ӧ�ĵ�ģ����Ϣ�������ж�Ӧ�������λ��
	 */
	public int[] template; 
	/**
	 * Feature��Ӧģ��Ķ������֣�����template[i]��Ӧvalue[i]
	 */
	public String[] value;
	@Override
	/**
	 * ����������ֱ����ʱ��������
	 */
	public boolean equals(Object obj) {
		Feature feature = (Feature) obj;
		return 
				Arrays.equals(this.template, feature.template) &&
				Arrays.equals(this.value, feature.value) ;
	}
	
	@Override
	/**
	 * ������
	 */
	public String toString() {
		String ret = "[";
		for(int i=0;i<template.length;i++){
			ret += template[i]+":" + value[i]+" ";
		}
		return ret+"]";
	}
}
