package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * ��¼����ѵ�������Ϣ
 * @author yuriko
 *
 */
public class WordData {
	/**
	 * ��ǰ����
	 */
	public String word;
	/**
	 * ��ӦPI��
	 */
	public Map<String,Double> prior;
	/**
	 * ��Ӧ����feature�б�
	 */
	public List<Feature> featureList;
	/**
	 * ��ӦB��
	 */
	public HashMap<String, int[]> B;
}

