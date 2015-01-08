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
 * Crf的主要实现类，包含了训练和识别等方法，并且保存训练结果，保存可进行长期使用。支持多次训练。
 * @author yuriko
 *
 */
public class Crf {
	/**
	 * 记录的训练结果
	 */
	private Map<String, WordData> map = new HashMap<String, WordData>();
	/**
	 * 所有可能的标签，本次试验中为静态结果
	 */
	private String[] labels = Tools.getAllTags();

	/**
	 * 读入训练文件，通过crf算法进行训练，结果会保存在本类中
	 * @param file 待训练文件路径
	 * @throws IOException 文件读写发生错误时抛出
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
	 * 读入测试文件，通过已有的训练结果对测试文件进行识别
	 * @param file 待测试文件路径
	 * @return 测试结果，每个单词为一个ContextWord对象，ContextWord.answer中
	 * @throws IOException 文件读写发生错误时抛出
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
	 * 在读入训练文件后，如果该单词未曾训练过，建立并初始化对应WordData对象
	 * @param word 对应的单词
	 * @param contexts 训练文件全文
	 * @param featureBuilder 对应全文生成的FeatureBuilder对象，用以生成对应的OBuffer
	 * @return 生成的WordData对象
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
	 * 对某个单词的所有出现进行一次训练
	 * @param word 待训练单词
	 * @param contexts 该单词出现的所有出现记录
	 * @param featureBuilder 用来生成feature和O表的FeatureBuilder对象
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
