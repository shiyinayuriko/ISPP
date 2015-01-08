package model;
/**
 * 用来保存单词在某个文本出现的记录
 * @author yuriko
 *
 */
public class ContextWord {
	/**
	 * 该单词文本
	 */
	public String word;
	/**
	 * 训练结果，如果结果不明或未知，则为null
	 */
	public String answer;
	/**
	 * 在文本中出现的行数，从0起。
	 */
	public int lineId;
	/**
	 * 当三个字段都分别相等时判定为返回真
	 */
	@Override
	public boolean equals(Object obj) {
		ContextWord cw = (ContextWord) obj;
		return this.word.equals(cw.word) &&
				this.answer.equals(cw.answer) &&
				this.lineId == cw.lineId;
	}
	
}
