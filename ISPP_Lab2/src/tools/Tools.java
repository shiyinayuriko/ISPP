package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.WordData;

/**
 * �����࣬����������Ӧ����
 * @author yuriko
 *
 */
public class Tools {
	/**
	 * ��ȡ����label��¼������Ϊ��̬ʵ��
	 * @return ���п��ܵ�label
	 */
	public static String[] getAllTags(){
		return new String[]{"S","B","I","E"};
	}
	
	/**
	 * Viterbi �㷨�ľ���ʵ��
	 * @param wd ��Ҫ����ĵ��ʣ������˱�Ҫ�����ݱ�
	 * @param O ��Ӧ���ʵ�ĳ�γ��ֵ�O��
	 * @param labels ���п��ܵ�label
	 * @return ��������ߵĽ���飬һ��Ϊ���������������ͬ�򷵻ض��
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
	 * ���ɵ�λ������ΪA��
	 * @param N ��λ�������
	 * @return A����λ����
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
