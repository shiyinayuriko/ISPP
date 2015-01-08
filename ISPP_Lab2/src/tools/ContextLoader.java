package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ContextWord;
/**
 * �ı����봦����
 * @author yuriko
 *
 */
public class ContextLoader {
	@SuppressWarnings("resource")
	/**
	 * ��ȡ�ı���������Ӧ��ȷ��
	 * @param file ����ȡ�ı�
	 * @return �ı������б���Ӧ˳��Ϊ�ı��г���˳��
	 * @throws IOException �ļ���д��������ʱ�׳�
	 */
	public static List<ContextWord> getWithAnswer(String file) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
		String line;
		int lineNum = 0;
		
		List<ContextWord> text = new ArrayList<ContextWord>(); 
		while((line = br.readLine())!= null){
			if(line.trim().equals("")) continue;
			String[] rows = line.split(" ");
			
			ContextWord word = new ContextWord();
			word.lineId = lineNum++;
			word.word = rows[0];
			word.answer = rows[1];
			text.add(word);
		}
		return text;
	}
	
	/**
	 * ��ȡ�ı�����������Ӧ��ȷ��
	 * @param file ����ȡ�ı�
	 * @return �ı������б���Ӧ˳��Ϊ�ı��г���˳��
	 * @throws IOException �ļ���д��������ʱ�׳�
	 */
	@SuppressWarnings("resource")
	public static List<ContextWord> getWithoutAnswer(String file) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
		String line;
		int lineNum = 0;
		
		List<ContextWord> text = new ArrayList<ContextWord>(); 
		while((line = br.readLine())!= null){
			if(line.trim().equals("")) continue;
			String[] rows = line.split(" ");
			
			ContextWord word = new ContextWord();
			word.lineId = lineNum++;
			word.word = rows[0];
			word.answer = null;
			text.add(word);
		}
		return text;
	}
	
	/**
	 * �ϲ��ı�����ͬ���ֳ��֣��Է���ѵ��ʱʹ��
	 * @param contexts �ı������б�
	 * @return ����-��Ӧ���г��ֵļ�ֵ�Ա�
	 */
	public static Map<String,List<ContextWord>> mergeWords(List<ContextWord> contexts){
		Map<String,List<ContextWord>> map = new HashMap<String, List<ContextWord>>();
		
		for(ContextWord word:contexts){
			if(!map.containsKey(word.word)) map.put(word.word, new ArrayList<ContextWord>());
			map.get(word.word).add(word);
		}
		return map;
	}
}
