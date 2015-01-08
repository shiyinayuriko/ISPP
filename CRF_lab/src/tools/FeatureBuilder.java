package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ContextWord;
import model.Feature;
import model.WordData;
/**
 * 对于目标文件快速生成feature和O表的生成工具
 * @author yuriko
 *
 */
public class FeatureBuilder {
	/**
	 * 模板文件信息，本次lab中暂时使用静态数据
	 */
	int[][] templates = {
			{-2},
			{-1},
			{0},
			{1},
			{2},
			{-2,-1},
			{-1,0},
			{-1,0},
			{-1,1},
			{0,1},
			{1,2},
	};
	/**
	 * 文本全文列表
	 */
	private List<ContextWord> context;
	/**
	 * 构造函数，需要生成feature的全文本列表
	 * @param context
	 */
	public FeatureBuilder(List<ContextWord> context){
		this.context = context;
	}
	
	/**
	 * 获取对应文本的所有Feature
	 * @param word 目标单词的某次出现记录
	 * @return 对应文本的所有Feature
	 */
	public List<Feature> getFerture(ContextWord word){
		List<Feature> features = new ArrayList<Feature>();
		int line = word.lineId;
l1:		for(int[] template:templates){
			String[] value = new String[template.length];
			for(int i=0;i<template.length;i++){
				if(line + template[i]<0||line + template[i]>=context.size()) continue l1;
				value[i] = context.get(line + template[i]).word;
			}
			Feature feature = new Feature();
			feature.template = template;
			feature.value = value;
			features.add(feature);
		}
		return features;
	}
	
	/**
	 * O表缓存对象
	 */
	Map<ContextWord, List<Integer>> OBuffer = new HashMap<ContextWord, List<Integer>>();
	/**
	 * 获取对应单词的O表信息，如果有缓存数据则提取，反之则重新生成
	 * @param cw 对应单词的某次出现
	 * @param wd 对应单词的学习记录
	 * @return O表，所有命中的feature在WordData.featureList的标号
	 */
	public List<Integer> getO(ContextWord cw,WordData wd){
		List<Integer> O = OBuffer.get(cw);
		if(O==null){
			//CalcO
			List<Feature> features = this.getFerture(cw);
			O = new ArrayList<Integer>();
			for(Feature feature:features){
				int indexF = wd.featureList.indexOf(feature);
				if(indexF!=-1) O.add(indexF);
			}
			OBuffer.put(cw, O);
		}
		return O;
	}
}
