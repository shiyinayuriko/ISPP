package model;

import java.util.Arrays;
/**
 * 单词的某处出现在相应文本中的单条feature信息
 * @author yuriko
 *
 */
public class Feature {
	/**
	 * Feature对应的的模板信息，数组中对应的是相对位置
	 */
	public int[] template; 
	/**
	 * Feature对应模板的对于文字，对于template[i]对应value[i]
	 */
	public String[] value;
	@Override
	/**
	 * 当两个数组分别相等时，返回真
	 */
	public boolean equals(Object obj) {
		Feature feature = (Feature) obj;
		return 
				Arrays.equals(this.template, feature.template) &&
				Arrays.equals(this.value, feature.value) ;
	}
	
	@Override
	/**
	 * 测试用
	 */
	public String toString() {
		String ret = "[";
		for(int i=0;i<template.length;i++){
			ret += template[i]+":" + value[i]+" ";
		}
		return ret+"]";
	}
}
