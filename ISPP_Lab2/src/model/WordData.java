package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 记录单词训练结果信息
 * @author yuriko
 *
 */
public class WordData {
	/**
	 * 当前单词
	 */
	public String word;
	/**
	 * 对应PI表
	 */
	public Map<String,Double> prior;
	/**
	 * 对应所有feature列表
	 */
	public List<Feature> featureList;
	/**
	 * 对应B表
	 */
	public HashMap<String, int[]> B;
}

