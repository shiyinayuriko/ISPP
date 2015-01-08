import java.io.FileNotFoundException;
import java.util.List;
import core.BPNet;
import core.TrainingItem;
import core.TrainingItemBuilder;
import core.TrainingItemPaser;


public class BPFileReader implements TrainingItemPaser{
	public static final int INPUT_SIZE = 13;
	public static final int OUTPUT_SIZE = 30;
	
	public static final int HIDDEN_SIZE = 500;
	
	public static void main(String[] args) throws FileNotFoundException{
		TrainingItemBuilder builder = new TrainingItemBuilder(new BPFileReader());
		
		BPNet bp = new BPNet(INPUT_SIZE,HIDDEN_SIZE,OUTPUT_SIZE, 0.326);
		List<TrainingItem> trainSet1 = builder.loadTrainingSet("./lab1_train_1.txt","./lab1_output.txt");
		List<TrainingItem> trainSet2 = builder.loadTrainingSet("./lab1_train_2.txt","./lab1_output.txt");
		List<TrainingItem> trainSet3 = builder.loadTrainingSet("./lab1_train_3.txt","./lab1_output.txt");
		List<TrainingItem> trainSet4 = builder.loadTrainingSet("./lab1_train_4.txt","./lab1_output.txt");
		for(int i=0;i<2000;i++){
			if(i%100==0) System.out.println(i);
			bp.trainingSet(trainSet1);
			bp.trainingSet(trainSet2);
			bp.trainingSet(trainSet3);
			bp.trainingSet(trainSet4);

		}
		
		List<TrainingItem> testSet = builder.loadTrainingSet("./lab1_test.txt","./lab1_output.txt");
		int counter=0;
		int all=0;
		for(int i=0;i<testSet.size();i++){
			TrainingItem trainItem = testSet.get(i);
			int result = BPNet.getMaxIndex(bp.getResult(trainItem));
			System.out.printf("%d:%d %d\n",i, BPNet.getMaxIndex(trainItem.output), result);
			if(BPNet.getMaxIndex(trainItem.output) == result) counter++;
			all++;
		}
		
		System.out.println(counter + " " + all);
	}

	@Override
	public double[] parseInput(String str) {
		String[] ss = str.split("\t");
		double[] ret = new double[ss.length];
		for(int i=0;i<ss.length;i++){
			ret[i] = Double.parseDouble(ss[i]);
		}
		if(ss.length != INPUT_SIZE) System.out.println("输入异常");
		return ret;
	}

	@Override
	public double[] parseOutput(String str) {
		String[] ss = str.split("\t");
		double[] ret = new double[ss.length];
		for(int i=0;i<ss.length;i++){
			ret[i] = Double.parseDouble(ss[i]);
		}
		if(ss.length != OUTPUT_SIZE) System.out.println("输入异常");
		return ret;
	}
}
