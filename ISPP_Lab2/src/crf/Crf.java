package crf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tools.ContextLoader;
import tools.FeatureBuilder;
import tools.Tools;
import model.ContextWord;
import model.Feature;
import model.WordData;

/**
 * Crf����Ҫʵ���࣬������ѵ����ʶ��ȷ��������ұ���ѵ�����������ɽ��г���ʹ�á�֧�ֶ��ѵ����
 * @author yuriko
 *
 */
public class Crf {
	/**
	 * ��¼��ѵ�����
	 */
	private Map<String, WordData> map = new HashMap<String, WordData>();
	/**
	 * ���п��ܵı�ǩ������������Ϊ��̬���
	 */
	private String[] labels = Tools.getAllTags();

	/**
	 * ����ѵ���ļ���ͨ��crf�㷨����ѵ��������ᱣ���ڱ�����
	 * @param file ��ѵ���ļ�·��
	 * @throws IOException �ļ���д��������ʱ�׳�
	 */
	public void train(String file) throws IOException{
		List<ContextWord> totalContext = ContextLoader.getWithAnswer(file);
		Map<String, List<ContextWord>> words = ContextLoader.mergeWords(totalContext);

		FeatureBuilder featureBuilder = new FeatureBuilder(totalContext);
		
		System.out.println("total:"+words.size());
		int tmpCounter = 1;
		for(Entry<String,List<ContextWord>> entry : words.entrySet()){
			List<ContextWord> contexts = entry.getValue();
			String word = entry.getKey();
			//prior,featureList,B
			WordData wd = pretreatment(word,contexts,featureBuilder);
			//TODO if not exist
			if(!map.containsKey(word)) map.put(word, wd);
			else wd = map.get(word);
			
			//startTraining
			int step = 0;
			while(step++<500){
				trainCycle(word, contexts, featureBuilder);
			}
			System.out.println("end:"+word+"("+ (tmpCounter++) +"/"+words.size()+")");
		}
		
	}
	/**
	 * ��������ļ���ͨ�����е�ѵ������Բ����ļ�����ʶ��
	 * @param file �������ļ�·��
	 * @return ���Խ����ÿ������Ϊһ��ContextWord����ContextWord.answer��
	 * @throws IOException �ļ���д��������ʱ�׳�
	 */
	public List<ContextWord> classify(String file) throws IOException{
		List<ContextWord> totalContext = ContextLoader.getWithoutAnswer(file);
		FeatureBuilder fb = new FeatureBuilder(totalContext);

		for(ContextWord cw : totalContext){
			WordData wd = map.get(cw.word);
			if(wd==null) {
				cw.answer = null;
				continue;
			}
			List<String> result = Tools.viterbi(wd,fb.getO(cw, wd),labels);//crfTools.Viterbi(N, M, B, PI, O, senses);
			cw.answer = result.get(0);
		}
		return totalContext;
	}
	/**
	 * �ڶ���ѵ���ļ�������õ���δ��ѵ��������������ʼ����ӦWordData����
	 * @param word ��Ӧ�ĵ���
	 * @param contexts ѵ���ļ�ȫ��
	 * @param featureBuilder ��Ӧȫ�����ɵ�FeatureBuilder�����������ɶ�Ӧ��OBuffer
	 * @return ���ɵ�WordData����
	 */
	private WordData pretreatment(String word, List<ContextWord> contexts, FeatureBuilder featureBuilder){
		WordData wd = new WordData();
		wd.word = word;
		
		//Calc the prior
		wd.prior = new HashMap<String, Double>();
		int sum = contexts.size();
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for(ContextWord cw:contexts){
			if(counter.containsKey(cw.answer)){
				counter.put(cw.answer, counter.get(cw.answer)+1);
			}else{
				counter.put(cw.answer,1);
			}
		}
		for(Entry<String, Integer> counterEntry:counter.entrySet()){
			String answer = counterEntry.getKey();
			int count = counterEntry.getValue();
			wd.prior.put(answer, (double)count/(double)sum);
		}
		
		//calc the featureList
		wd.featureList = new ArrayList<Feature>();
		for(ContextWord cw:contexts){
			List<Feature> features = featureBuilder.getFerture(cw);
			for(Feature feature :features){
				if(!wd.featureList.contains(feature)) wd.featureList.add(feature);
			}
		}
		
		//initial B
		wd.B = new HashMap<String, int[]>();
		for(String tag:labels){
			int m = wd.featureList.size();
			int[] Brow = new int[m];
			for(int i=0;i<m;i++){
				Brow[i] = 0;
			}
			wd.B.put(tag, Brow);
		}
		return wd;
	}

	/**
	 * ��ĳ�����ʵ����г��ֽ���һ��ѵ��
	 * @param word ��ѵ������
	 * @param contexts �õ��ʳ��ֵ����г��ּ�¼
	 * @param featureBuilder ��������feature��O���FeatureBuilder����
	 */
	private void trainCycle(String word,List<ContextWord> contexts, FeatureBuilder featureBuilder){
		WordData wd = map.get(word);
		for(ContextWord cw:contexts){
			//TODO
			List<Integer> O = featureBuilder.getO(cw, wd);
			while(true){
				List<String> result = Tools.viterbi(wd,O,labels);//crfTools.Viterbi(N, M, B, PI, O, senses);
				if(result.size() == 1 && result.contains(cw.answer)){
					break;
				}else{
					for(String str:result){
						if(!str.equals(cw.answer)){
							int[] BRow = wd.B.get(str);
							for(Integer index : O){
								BRow[index] = BRow[index] - 1;
							}
						}
						
						int[] BRow = wd.B.get(cw.answer);
						for(Integer index : O){
							BRow[index] = BRow[index] + 1;
						}
						//TODO wd.B.put(str, BRow);
					}
				}
			}
		}
		
		
	}
}
