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
 * 文本读入处理工具
 * @author yuriko
 *
 */
public class ContextLoader {
	@SuppressWarnings("resource")
	/**
	 * 获取文本，包含对应正确答案
	 * @param file 待读取文本
	 * @return 文本内容列表，对应顺序为文本中出现顺序
	 * @throws IOException 文件读写发生错误时抛出
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
	 * 获取文本，不包含对应正确答案
	 * @param file 待读取文本
	 * @return 文本内容列表，对应顺序为文本中出现顺序
	 * @throws IOException 文件读写发生错误时抛出
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
	 * 合并文本中相同文字出现，以方便训练时使用
	 * @param contexts 文本内容列表
	 * @return 单词-对应所有出现的键值对表
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
