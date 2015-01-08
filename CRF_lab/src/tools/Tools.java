package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.WordData;

/**
 * 工具类，辅助计算相应数据
 * @author yuriko
 *
 */
public class Tools {
	/**
	 * 获取所有label记录，这里为静态实现
	 * @return 所有可能的label
	 */
	public static String[] getAllTags(){
		return new String[]{"S","B","I","E"};
	}
	
	/**
	 * Viterbi 算法的具体实现
	 * @param wd 需要计算的单词，包含了必要的数据表
	 * @param O 对应单词的某次出现的O表
	 * @param labels 所有可能的label
	 * @return 可能性最高的结果组，一般为单个，如果概率相同则返回多个
	 */
	public static List<String> viterbi(WordData wd, List<Integer> O, String[] labels){
		int N = labels.length;
		Map<String, Double> PI = wd.prior;
		Map<String, int[]> B = wd.B;
		
		//generate A matrix
		double[][] A = getUnitMatrix(N);

		int T = O.size();
		
		ArrayList<String> q = new ArrayList<String>();
			
		if(T == 0){
			double max = 0;
			for(int i = 0; i < N; i++){
				if(PI.get(labels[i]) > max){
					max = PI.get(labels[i]);
				}
			}
			
			for(int i = 0; i < N; i++){
				if(PI.get(labels[i]) == max){
					q.add(labels[i]);
				}
			}
			return q;
		}
		
		double delta[][] = new double[T][N];
		double psi[][] = new double[T][N];
		
		/*
		 * Initialization
		 */
		
		for(int i = 0; i < N; i++){
			String sense = labels[i];
			Double tmp;
			if(PI.get(sense)==null) tmp=0.0; else tmp = PI.get(sense);
			delta[0][i] = tmp * B.get(sense)[O.get(0)];
			psi[0][i] = 0;
		}
		
		/*
		 * Recursion
		 */
		
 		for(int t = 1; t < T; t++){
			for(int j = 0; j < N; j++)
			{
				String sense = labels[j];
				double maxval = 0;
				double maxvalind = 0;
				
				for(int i = 0; i < N; i++){
					double val = delta[t-1][i] * A[i][j];
					if(val > maxval){
						maxval = val;
						maxvalind = i;
					}
				}
				
				int tmpo = O.get(t);
				int[] tmpb = B.get(sense);
				int tmpi = tmpb[tmpo];
				
				delta[t][j] = maxval * tmpi;
				psi[t][j] = maxvalind;
			}
		}
		
		/*
		 * Termination
		 */
		
		double pprob = 0;
		
		for(int i = 0; i < N; i++){
			if(delta[T - 1][i] > pprob){
				pprob = delta[T - 1][i];
			}
		}
		
		
		for(int i = 0; i < N; i++)
		{
			if(delta[T - 1][i] == pprob)
			{
				q.add(labels[i]);
				break;
			}
		}
		return q;
	}
	/**
	 * 生成单位矩阵作为A表
	 * @param N 单位矩阵阶数
	 * @return A表，单位矩阵
	 */
	private static double[][] getUnitMatrix(int N){
		double[][] A = new double[N][N];
		for(int i = 0; i < N; i++)
		{
			for(int j = 0; j < N; j++)
			{
				if(i == j)
				{
					A[i][j] = 1;
				}
				else
				{
					A[i][j] = 0;
				}
			}
		}
		return A;
	}
}
