package model;
/**
 * �������浥����ĳ���ı����ֵļ�¼
 * @author yuriko
 *
 */
public class ContextWord {
	/**
	 * �õ����ı�
	 */
	public String word;
	/**
	 * ѵ�������������������δ֪����Ϊnull
	 */
	public String answer;
	/**
	 * ���ı��г��ֵ���������0��
	 */
	public int lineId;
	/**
	 * �������ֶζ��ֱ����ʱ�ж�Ϊ������
	 */
	@Override
	public boolean equals(Object obj) {
		ContextWord cw = (ContextWord) obj;
		return this.word.equals(cw.word) &&
				this.answer.equals(cw.answer) &&
				this.lineId == cw.lineId;
	}
	
}
