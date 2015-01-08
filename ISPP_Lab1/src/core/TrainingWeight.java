package core;

public class TrainingWeight {

	public TrainingWeight(int inputSize,int hiddenSize, int outputSize){
		firstLayerWeights = new double[inputSize][hiddenSize];
		secondLayerWeights = new double[hiddenSize][outputSize];
		randomize(firstLayerWeights);
		randomize(secondLayerWeights);
	}
	double[][] firstLayerWeights;
	double[][] secondLayerWeights;
	
	
	private void randomize(double[][] data) {
		for(int i = 0;i < data.length;i++)
			for(int j = 0; j < data[0].length; j++){
				data[i][j] = (Math.random() - 0.5) *2;
			}
	}
}
