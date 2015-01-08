import java.util.List;


public class TableModel {
	public List<int[]> table;
	public int[] C;
	public int[] D;
	
	public static int countBit(int[] in){
		int tmp = 0;
		for(int i:in){
			if(i!=0) tmp++;
		}
		return tmp;
	}
}
