import java.util.Arrays;
import java.util.List;

import attributeRemove.RoughSetsAttributeRemove;
import attributeValueRemove.AttributeValueRemove;

public class Launcher {
	public static void main(String[] args){
		TableModel tableModel = TableReader.getTable("./train.txt");
		
		
		RoughSetsAttributeRemove rs = new RoughSetsAttributeRemove();
		rs.V.addAll(tableModel.table);
		rs.C  =  tableModel.C;
		rs.D  =  tableModel.D;
		rs.N = rs.D.length-TableModel.countBit(rs.D);
		int[] ret = rs.startInherit();
		
		System.out.print("revieve:");printLine(ret);
		
		TableModel newTableModel = TableReader.getNewTabel(tableModel, ret);
		
		AttributeValueRemove rvs = new AttributeValueRemove();
		rvs.setTable(newTableModel.table);
		rvs.C = newTableModel.C;
		rvs.D = newTableModel.D;

		List<int[]> rtable = rvs.startRemoveValue();
		printTable(rtable);	
		
		TableModel testModel = TableReader.getNewTabel(TableReader.getTable("./train.txt"), ret);

		int counter = 0;
		for(int[] testLine:testModel.table){
			int[] a = makeDecision(testLine,testModel.C,testModel.D,rtable);
			if(a==null || testLine[4]!=a[4]){
				System.out.println("error");
//				printLine(testLine);
//				printLine(a);
				counter ++;
			}
			System.out.println(counter);
		}
		
	}
	
	static void printTable(List<int[]> table){
		for(int[] line: table){
			printLine(line);
		}
	}
	static void printLine(int[] line){
		String str = "";
		for(int i=0;i<line.length;i++)
			str+=line[i];
		System.out.println(str);
	}
	
	static int[] makeDecision(int[] condition,int[] conditionFilter, int[] decisionFilter, List<int[]> rtable){
		for(int[] line:rtable){
			if(!isConditionEquals(condition, line, conditionFilter)) continue;

			int[] result = Arrays.copyOf(condition, condition.length);
			for(int i=0;i<condition.length;i++){
				if(decisionFilter[i]!=0) result[i] = line[i];
			}
			return result;
		}
		return null;
	}
	
	private static boolean isConditionEquals(int[] condition,int[] condition2,int[] conditionFilter){
		for(int i=0;i<condition.length;i++){
			if(conditionFilter[i]!=0 && condition2[i]!=0 && condition[i]!=condition2[i]) return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
}
