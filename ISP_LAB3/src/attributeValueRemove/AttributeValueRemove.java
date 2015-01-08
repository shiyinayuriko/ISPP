package attributeValueRemove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributeValueRemove {
	
	public List<int[]> table;
	public int[] C;
	public int[] D;
	
	public List<int[]> startRemoveValue(){
		lc:while(true){
			for(int[] line:table){
				if(isConflict(line, new HashSet())){
					if(Math.random()>0.5) table.remove(line);
					continue lc;
				}
			}
			break;
		}
		
		
		List<int[]> newTable = new ArrayList<int[]>();
		for(int[] line:table){
			List<Set<Integer>> stack =new ArrayList<Set<Integer>>();
			for(int i=0;i<line.length;i++){
				if(D[i]==1) continue;
				HashSet<Integer> tmp = new HashSet<Integer>();
				tmp.add(i);
				if(!isConflict(line,tmp)) stack.add(tmp);
			}
			
			boolean switcher = true;
			while(switcher){
				switcher = false;
				Set<Set<Integer>> adder = new HashSet<Set<Integer>>();
				List<Set<Integer>> remover = new ArrayList<Set<Integer>>();
				for(int i=0;i<stack.size()-1;i++){
					for(int j=i+1;j<stack.size();j++){
						Set<Integer> set1 = stack.get(i);
						Set<Integer> set2 = stack.get(j);
						Set<Integer> setU = union(set1, set2) ;
						
						if(!isConflict(line,setU)){
							adder.add(setU);
							remover.add(set1);
							remover.add(set2);
							switcher = true;
						}
					}
				}
				
				stack.removeAll(remover);
				stack.addAll(adder);
			}

			
			if(stack.size()==0){
				int[] tmp = Arrays.copyOf(line, line.length);
				newTable.add(tmp);
			}else{
				for(Set<Integer> s:stack){
					int[] tmp = Arrays.copyOf(line, line.length);
					for(int i:s){
						tmp[i] = 0;
					}
					newTable.add(tmp);
				}
			}
		}
		removeRepeat(newTable);
		return newTable;
	}
	


	public void setTable(List<int[]> table){
		this.table = new ArrayList<int[]>();
		for(int[] line: table){
			this.table.add(Arrays.copyOf(line, line.length));
		}
		removeRepeat(this.table);
	}
	
	private boolean isConflict(int[] line,Set<Integer> ignores){
		for(int[] line2:table){
			if(!isLineConditionEqual(line,line2,ignores)) continue;
			if(!isLineDecisionEqual(line,line2)) return true;
		}
		return false;
	}
	
	private boolean isLineConditionEqual(int[] line1,int[] line2,Set<Integer> ignores){
		for(int i=0;i<line1.length;i++){
			if(!ignores.contains(i) && line1[i]!=line2[i] && C[i]!=0 ) return false;
		}
		return true;
	}
	
	private boolean isLineDecisionEqual(int[] line1, int[] line2) {
		for(int i=0;i<line1.length;i++){
			if(line1[i]!=line2[i] && D[i]!=0 ) return false;
		}
		return true;
	}
	
	public static void removeRepeat(List<int[]> newTable){
		l1:while(true){
			for(int[] line1:newTable){
				for(int[] line2:newTable){
					if(line1!=line2 && arrayEquals(line1, line2)){
						newTable.remove(line2);
						continue l1;
					}
				}
			}
			break;
		}
	}
	
	private static boolean arrayEquals(int[] line1,int[] line2){
		if(line1.length!=line2.length) return false;
		for(int i=0;i<line1.length;i++){
			if(line1[i]!=line2[i] && line1[i]!= 0) return false;
		}
		return true;
	}

	public static Set<Integer> union(Set<Integer> s1, Set<Integer> s2){
		Set<Integer> tmp = new HashSet<Integer>();
		tmp.addAll(s1);
		tmp.addAll(s2);
		return tmp;
	}
}
