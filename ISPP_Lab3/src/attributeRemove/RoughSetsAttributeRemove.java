package attributeRemove;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RoughSetsAttributeRemove {
	public int[] C;
	public int[] D;
	public List<int[]> V;

	public int M = 100;
	public int N = 10;

	public double alpha = 15;
	public double beta = 2;
	public double k0 = 0.9;
	public double Pc = 0.8;
	public double Pm = 0.03;
	public int re = 20;

	
	public RoughSetsAttributeRemove(){
		V = new ArrayList<int[]>();
	}
	
	
	
	public int[] startInherit(){
		//
		List<GroupMember> group = generateGroups();
		GroupMember maxtmp = null;
		int counter = 0;

		while(true){
			// calc fitness
			double max = -1;
			double sum = 0;
			GroupMember maxMember = null;
			HashMap<GroupMember, Double> distribution = new HashMap<GroupMember, Double>();

			for(GroupMember member:group){
				double f = member.fitness;
				
				if(max < f){
					max = f;
					maxMember = member.clone();
				}
				
				sum += f;
				
				if(!distribution.containsKey(member)){
					distribution.put(member, 0.0);
				}
				distribution.put(member, distribution.get(member)+member.fitness);
			}

//			System.out.println(maxMember);
			
			if(maxMember.equals(maxtmp)){
				counter++;
				if(counter==re) return maxtmp.filter;
			}else{
				counter = 0;
				maxtmp = maxMember;
			}
			
			for(int i=0;i<M; i++){
				double ran = Math.random() * sum;
				for(Entry<GroupMember, Double> entry: distribution.entrySet()){
					GroupMember key = entry.getKey();
					double value = entry.getValue();
					ran -= value;

					if(ran <= 0){
						group.set(i, key.clone());
						break;
					}
				}
			}
			
			// crossover
			int cupper = (int) (Pc  * M) / 2;
			for(int i = 0; i < cupper; i++){
				int[] l1 = group.get(i).filter;
				int[] l2 = group.get(M - i - 1).filter;
				
				for(int j = 0; j < N; j++){
					if(Math.random() <= 0.5){
						int t = l1[j];
						l1[j] = l2[j];
						l2[j] = t;
					}
				}
				group.set(i, new GroupMember(l1,this));
				group.set(M - i - 1, new GroupMember(l2,this));
			}
			
			// mutation
			for(int i = 0; i < M; i++) {
				if(Math.random() < Pm){
					int[] l = group.get(i).filter;
					for(int j = 0; j < N; j++){
						if(Math.random() <= 0.5){
							l[j] = l[j]==0?1:0;
							group.set(i, new GroupMember(l,this));
						}
					}
				}
			}
		}
	}
	
	private List<GroupMember> generateGroups(){
		Set<Integer> cores = new HashSet<Integer>();
		for(int c=0;c<C.length;c++) {
			//TODO compare
			if(C[c]!=0 && Sig(C, c, D) != 0) {
				cores.add(c);
			}
		}
		
		List<GroupMember> group = new ArrayList<GroupMember>();
		for(int i = 0; i < M; i++){
			int[] filter = new int[N];

			for(int j=0;j<N;j++){
				if(cores.contains(j)){
					filter[j] = 1;
				}else{				
					double tmp = Math.random();
					if (tmp < 0.5) {
						filter[j] = 1;
					} else {
						filter[j] = 0;
					}
				}
			}
			group.add(new GroupMember(filter,this));
		}
		return group;
	}
	
	public List<Set<Integer>> partition(int[] filter){
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		Map<String, Set<Integer>> buffer = new HashMap<String, Set<Integer>>();
		for(int index=0; index < V.size() ;index++){
			int[] v = V.get(index);
			String key = "";
			for(int i = 0;i<filter.length;i++){
				if(filter[i]!=0) key += (v[i]+"%");
			}
			//TODO check
			if(!buffer.containsKey(key)){
				Set<Integer> tmp = new HashSet<Integer>();
				buffer.put(key, tmp);
				result.add(tmp);
			}
			buffer.get(key).add(index);
		}
		return result;
	}
	
	public double K( int[] filterC, int[] filterD){
		List<Set<Integer>> setC = partition(filterC);
		List<Set<Integer>> setD = partition(filterD);
		double sum = 0;
		for(Set<Integer> s : setD){
			Set<Integer> p = posSet(setC, s);
			sum += p.size();
		}
		return sum / (double)this.V.size();
	}
	
	public double Sig(int[] filterC, int c, int[] filterD){
		double k1 = K(filterC, filterD);
		
		int[] fileterC2 = Arrays.copyOf(filterC, filterC.length);
		fileterC2[c] = 0;
		double k2 = K(fileterC2, filterD);
		
		return k1 - k2;
	}
	
	public double F(GroupMember x){
		double result = 1 - x.card() / (double)x.filter.length;
		result *= beta;
		double k = K(x.filter, D);
		double tmp = Math.exp(alpha * (k0 - k)) + 1;
		result = result / tmp;
		return result;
	}
	
	public static Set<Integer> posSet(List<Set<Integer>> sets, Set<Integer> X){
		Set<Integer> result = new HashSet<Integer>();

		for(int i:X){
			//TODO NOT Tested
			l1: for (Set<Integer> s : sets) {
				if(!s.contains(i)) continue;
				for(int j:s){
					if(!X.contains(j)) continue l1;
				}
				//TODO test
				result.addAll(s);
			}
		}
		return result;
	}
	
	public static Set<Integer> union(Set<Integer> s1, Set<Integer> s2){
		Set<Integer> tmp = new HashSet<Integer>();
		tmp.addAll(s1);
		tmp.addAll(s2);
		return tmp;
	}
	public static Set<Integer> intersect(Set<Integer> s1, Set<Integer> s2){
		Set<Integer> tmp = new HashSet<Integer>();
		tmp.addAll(s1);
		tmp.retainAll(s2);
		return tmp;
	}
	
	
}
