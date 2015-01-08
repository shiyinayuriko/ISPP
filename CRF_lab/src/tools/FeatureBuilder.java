package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ContextWord;
import model.Feature;
import model.WordData;
/**
 * ����Ŀ���ļ���������feature��O������ɹ���
 * @author yuriko
 *
 */
public class FeatureBuilder {
	/**
	 * ģ���ļ���Ϣ������lab����ʱʹ�þ�̬����
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
	 * �ı�ȫ���б�
	 */
	private List<ContextWord> context;
	/**
	 * ���캯������Ҫ����feature��ȫ�ı��б�
	 * @param context
	 */
	public FeatureBuilder(List<ContextWord> context){
		this.context = context;
	}
	
	/**
	 * ��ȡ��Ӧ�ı�������Feature
	 * @param word Ŀ�굥�ʵ�ĳ�γ��ּ�¼
	 * @return ��Ӧ�ı�������Feature
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
	 * O�������
	 */
	Map<ContextWord, List<Integer>> OBuffer = new HashMap<ContextWord, List<Integer>>();
	/**
	 * ��ȡ��Ӧ���ʵ�O����Ϣ������л�����������ȡ����֮����������
	 * @param cw ��Ӧ���ʵ�ĳ�γ���
	 * @param wd ��Ӧ���ʵ�ѧϰ��¼
	 * @return O���������е�feature��WordData.featureList�ı��
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
