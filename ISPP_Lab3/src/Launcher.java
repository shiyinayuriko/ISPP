import java.util.List;

import attributeRemove.RoughSetsAttributeRemove;
import attributeValueRemove.AttributeValueRemove;

public class Launcher {
	public static void main(String[] args){
		TableModel tableModel = TableReader.getTable();
		
		
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
		return null;
	}
}
