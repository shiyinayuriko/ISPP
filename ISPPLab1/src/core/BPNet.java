package core;

import java.util.List;

public class BPNet {
	private class UnitSet{
		double[] hiddenUnits;
		double[] outputUnits;
	}	
	
	public static TrainingWeight trainingWeight;
	private int inputSize,hiddenSize,outputSize;
	private double stepLength;
	public BPNet(int inputSize,int hiddenSize, int outputSize,double stepLength){
		trainingWeight = new TrainingWeight(inputSize, hiddenSize, outputSize);
		this.inputSize = inputSize;
		this.hiddenSize = hiddenSize;
		this.outputSize = outputSize;
		this.stepLength = stepLength;
	}
	
	public double[] getResult(TrainingItem trainItem){
		return computeUnits(trainItem).outputUnits;
	}
	public static int getMaxIndex(double[] outputUnits){
		int maxR = -1;
		double max=-Double.MIN_VALUE;
		for(int i=0;i<outputUnits.length;i++)
			if(outputUnits[i]>max){
				maxR = i;
				max = outputUnits[i];
			}
		
		return maxR;
	}
	
	public void trainingSet(List<TrainingItem> trainSet){
			for (TrainingItem item:trainSet) {
				UnitSet units = computeUnits(item);
				adjustUnits(item,units);
			}
	}
	private UnitSet computeUnits(TrainingItem trainItem) {
		double[] hiddenUnits = new double[hiddenSize];
		double[] outputUnits = new double[outputSize];
		
		for (int i = 0; i < hiddenSize; i++) {
			double tmpSum = 0;
			for (int j = 0; j < inputSize; j++)
				tmpSum += trainingWeight.firstLayerWeights[j][i]
						* trainItem.input[j];
			hiddenUnits[i] = 1 / (1 + Math.pow(Math.E, -1 * tmpSum));
		}

		for (int i = 0; i < outputSize; i++) {
			double tmpSum = 0;
			for (int j = 0; j < hiddenSize; j++)
				tmpSum += trainingWeight.secondLayerWeights[j][i] * hiddenUnits[j];
			outputUnits[i] = 1 / (1 + Math.pow(Math.E, -1 * tmpSum));
		}
		
		UnitSet units = new UnitSet();
		units.hiddenUnits = hiddenUnits;
		units.outputUnits = outputUnits;
		return units;
	}
	private void adjustUnits(TrainingItem trainItem,UnitSet unit) {
		double[][] deltaWeightP = new double[this.hiddenSize][this.outputSize];

		for (int j = 0; j < hiddenSize; j++)
			for (int i = 0; i < outputSize; i++) {
				deltaWeightP[j][i] = (trainItem.output[i] - unit.outputUnits[i])
						* unit.outputUnits[i] * (1 - unit.outputUnits[i])
						* trainingWeight.secondLayerWeights[j][i];
				
				trainingWeight.secondLayerWeights[j][i] += stepLength
						* (trainItem.output[i] - unit.outputUnits[i]) * unit.outputUnits[i]
						* (1 - unit.outputUnits[i]) * unit.hiddenUnits[j];
			}

		double tmpSum = 0;
		for (int k = 0; k < inputSize; k++)
			for (int j = 0; j < hiddenSize; j++) {
				tmpSum = 0;
				for (int i = 0; i < outputSize; i++) {
					tmpSum += deltaWeightP[j][i];
				}
				trainingWeight.firstLayerWeights[k][j] += stepLength * unit.hiddenUnits[j]
						* (1 - unit.hiddenUnits[j])
						* trainItem.input[k] * tmpSum;
			}
	}

}
