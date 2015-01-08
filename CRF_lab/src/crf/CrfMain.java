package crf;
import java.io.IOException;
import java.util.List;

import tools.ContextLoader;
import model.ContextWord;

/**
 * �������������࣬�������ѵ����ʶ�𲢼����
 * @author yuriko
 *
 */
public class CrfMain {

	/**
	 * ����������������
	 * @param args ��
	 * @throws IOException ѵ���������ļ���������ʱ�׳�
	 */
	public static void main(String[] args) throws IOException {
		Crf crf = new Crf();
		long startTraingTime = System.currentTimeMillis();
		crf.train("./train.utf8");
		long endTraingTime = System.currentTimeMillis();
		System.out.println("ѵ������!");
		System.out.println("ѵ��ʱ�䣺"+(endTraingTime-startTraingTime)+"ms");
		System.out.println("ѵ�����ʣ�"+ContextLoader.getWithAnswer("./train.utf8").size());

		long startClassifyTime = System.currentTimeMillis();
		List<ContextWord> result = crf.classify("./test.utf8");
		long endClassifyTime = System.currentTimeMillis();
		
		System.out.println("���Խ���!");
		System.out.println("����ʱ�䣺"+(endClassifyTime-startClassifyTime)+"ms");
		System.out.println("���Ե��ʣ�"+ContextLoader.getWithAnswer("./train.utf8").size());

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
		
		
		System.out.println("��ȷ/���󵥴ʣ�"+correctNum+"/"+wrongNum);
		System.out.printf("��ȷ�ʣ�%.2f%%\n",(((double)correctNum)/(correctNum+wrongNum)*100));
 	}

}
