package attributeRemove;
import java.util.Arrays;


public class GroupMember {
	public int[] filter;
	public double fitness;
	
	
	public GroupMember(int[] filter,RoughSetsAttributeRemove rs) {
		this.filter = Arrays.copyOf(filter, filter.length);
//		this.filter[filter.length-1] = 0;
		this.fitness = rs.F(this);
	}
	private GroupMember(int[] filter,double fitness) {
		this.filter = Arrays.copyOf(filter, filter.length);
		this.fitness = fitness;
	}
	public int card(){
		int ret = 0;
		for(int i:filter){
			if(i!=0) ret++;
		}
		return ret;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i=0;i<filter.length;i++)
			str+=filter[i];
		return str+":"+fitness;
	}

	@Override
	public int hashCode() {
		String str = "";
		for(int i=0;i<filter.length;i++)
			str+=filter[i];
		return Integer.parseInt(str);
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null) return false;
		GroupMember k = (GroupMember) arg0;
		//TODO 
//		if(k.fitness!=this.fitness) return false;
		return Arrays.equals(k.filter,this.filter);
	}
	
	@Override
	protected GroupMember clone() {
		return new GroupMember(this.filter, this.fitness);
	}
}
