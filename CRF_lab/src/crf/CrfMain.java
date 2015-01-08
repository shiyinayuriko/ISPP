package crf;
import java.io.IOException;
import java.util.List;

import tools.ContextLoader;
import model.ContextWord;

/**
 * 程序启动的主类，负责调用训练和识别并检查结果
 * @author yuriko
 *
 */
public class CrfMain {

	/**
	 * 程序启动的主函数
	 * @param args 略
	 * @throws IOException 训练、测试文件发生错误时抛出
	 */
	public static void main(String[] args) throws IOException {
		Crf crf = new Crf();
		long startTraingTime = System.currentTimeMillis();
		crf.train("./train.utf8");
		long endTraingTime = System.currentTimeMillis();
		System.out.println("训练结束!");
		System.out.println("训练时间："+(endTraingTime-startTraingTime)+"ms");
		System.out.println("训练单词："+ContextLoader.getWithAnswer("./train.utf8").size());

		long startClassifyTime = System.currentTimeMillis();
		List<ContextWord> result = crf.classify("./test.utf8");
		long endClassifyTime = System.currentTimeMillis();
		
		System.out.println("测试结束!");
		System.out.println("测试时间："+(endClassifyTime-startClassifyTime)+"ms");
		System.out.println("测试单词："+ContextLoader.getWithAnswer("./train.utf8").size());

		List<ContextWord> correct = ContextLoader.getWithAnswer("./test.utf8");
		int correctNum = 0;
		int wrongNum = 0;
		for(int i = 0;i<correct.size();i++){
			ContextWord wa = result.get(i);
			ContextWord ca = correct.get(i);
			if(!ca.equals(wa)){
				wrongNum++;
			}else{
				correctNum++;
			}
		}
		
		
		System.out.println("正确/错误单词："+correctNum+"/"+wrongNum);
		System.out.printf("正确率：%.2f%%\n",(((double)correctNum)/(correctNum+wrongNum)*100));
 	}

}
