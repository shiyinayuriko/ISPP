import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;



public class TableReader {
	public static TableModel getTable(String file){
		TableModel tableModel = new TableModel();
		
		List<int[]> table = new ArrayList<int[]>();
		tableModel.table= table;
		tableModel.C  =  new int[]{1,1,1,1,1,0};
		tableModel.D  =  new int[]{0,0,0,0,0,1};
		
		try {
			Scanner sc = new Scanner(new File(file));
			while(sc.hasNext()){
				String[] s = sc.nextLine().split("\t");
				int[] tmp = new int[6];
				tmp[0] = Integer.parseInt(s[0]);
				tmp[1] = Integer.parseInt(s[1]);
				tmp[2] = Integer.parseInt(s[2]);
				tmp[3] = Integer.parseInt(s[3]);
				tmp[4] = Integer.parseInt(s[4]);
				tmp[5] = Integer.parseInt(s[5]);
				table.add(tmp);
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tableModel;
	}
	
	
	public static TableModel getNewTabel(TableModel tableModel,int[] filter){
		TableModel ret = new TableModel();
		
		List<int[]> newTable = new ArrayList<int[]>();
		int cl = TableModel.countBit(filter);
		int dl = TableModel.countBit(tableModel.D);
		int N = cl + dl;
		
		for(int[] line:tableModel.table){
			int[] newLine = new int[N];
			int index = 0;
			for(int i=0;i<filter.length;i++){
				if(filter[i]!=0){
					newLine[index++]=line[i];
				}
			}
			for(int i=0;i<tableModel.D.length;i++){
				if(tableModel.D[i]!=0){
					newLine[index++]=line[i];
				}
			}
			newTable.add(newLine);
		}
		ret.table = newTable;
		ret.C = new int[N];
		for(int i=0;i<cl;i++){
			ret.C[i] =1;
		}
		ret.D = new int[N];
		for(int i=cl;i<N;i++){
			ret.D[i] =1;
		}
		return ret;
	}
	
	public static TableModel removeConflict(TableModel tableModel){
		List<int[]> table = tableModel.table;
		Map<String, Map<String,LineTmp>> lineStack = new HashMap<String, Map<String,LineTmp>>();
		for(int[] line:table){
			String conditionStr = getLineString(line, tableModel.C);
			if(!lineStack.containsKey(conditionStr)) 
				lineStack.put(conditionStr, new HashMap<String, LineTmp>());
			Map<String, LineTmp> lineMap = lineStack.get(conditionStr);
			
			String deciosionStr = getLineString(line, tableModel.D);
			if(!lineMap.containsKey(deciosionStr))
				lineMap.put(deciosionStr, new LineTmp(line));
			lineMap.get(deciosionStr).counter++;
		}
		
		List<int[]> newTable = new ArrayList<int[]>();
		for(Entry<String, Map<String,LineTmp>> e:lineStack.entrySet()){
			Map<String, LineTmp> lines = e.getValue();
			int maxcounter = 0;
			int[] maxLine = null;
			for(Entry<String, LineTmp> e2:lines.entrySet()){
				if(e2.getValue().counter>maxcounter) 
					maxLine = e2.getValue().line;
			}
			newTable.add(Arrays.copyOf(maxLine, maxLine.length));
		}
		TableModel newtableModel = new TableModel();
		newtableModel.C = Arrays.copyOf(tableModel.C, tableModel.C.length);
		newtableModel.D = Arrays.copyOf(tableModel.D, tableModel.D.length);
		newtableModel.table = newTable;
		return newtableModel;
	}
	private static class LineTmp{
		private int[] line;
		private int counter = 0;
		private LineTmp(int[] line){
			this.line=line;
		}
	}
	private static String getLineString(int[] line,int[] fileter){
		String ret = "";
		for(int i=0;i<line.length;i++){
			if(fileter[i]!=0) ret +="%"+line[i];
		}
		return ret;
	}
}

//table.add(new int[]{1,1,1,1,1,1,1,1,2,2});
//table.add(new int[]{1,1,2,1,1,2,1,2,2,2});
//table.add(new int[]{1,1,2,1,1,1,1,2,2,2});
//table.add(new int[]{1,2,1,1,1,1,1,2,3,1});
//table.add(new int[]{1,1,2,1,1,2,2,2,2,2});
//table.add(new int[]{1,1,2,2,1,2,2,1,1,3});
//table.add(new int[]{1,1,2,1,1,2,1,2,1,3});
//table.add(new int[]{2,2,2,2,2,1,3,2,3,1});
//table.add(new int[]{1,2,2,2,2,1,3,2,2,2});
//table.add(new int[]{1,2,2,2,2,1,2,1,2,2});
//table.add(new int[]{2,2,2,1,2,1,3,2,3,1});
//table.add(new int[]{2,2,2,1,2,2,2,2,2,1});
//table.add(new int[]{1,2,2,2,1,2,2,2,2,2});
//table.add(new int[]{2,2,1,1,2,1,1,2,2,1});
//table.add(new int[]{2,2,2,2,2,2,3,2,2,1});
//table.add(new int[]{1,2,1,1,1,2,1,2,2,2});
//table.add(new int[]{1,1,2,1,1,2,1,1,2,2});
//table.add(new int[]{1,2,2,1,1,2,1,1,2,2});
//table.add(new int[]{2,2,2,1,2,1,2,2,2,1});
//table.add(new int[]{1,2,2,1,2,1,2,2,2,1});
//table.add(new int[]{1,2,2,2,2,1,2,2,2,2});