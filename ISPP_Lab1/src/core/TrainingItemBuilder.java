package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrainingItemBuilder {
	private TrainingItemPaser tp;
	public TrainingItemBuilder(TrainingItemPaser tp){
		this.tp = tp;
	}
	
	public List<TrainingItem> loadTrainingSet(String infile,String outfile) throws FileNotFoundException{
		Scanner isc = new Scanner(new File(infile));
		Scanner osc = new Scanner(new File(outfile));
		
		List<TrainingItem> trainSet = new ArrayList<TrainingItem>();
		while(isc.hasNext()){
			String inline = isc.nextLine();
			String outline = osc.nextLine();
			
			TrainingItem ti = getItem(inline,outline);
			trainSet.add(ti);
		}
		return trainSet;
	}
	
	public List<TrainingItem> loadTestSet(String infile) throws FileNotFoundException{
		Scanner isc = new Scanner(new File(infile));
		
		List<TrainingItem> trainSet = new ArrayList<TrainingItem>();
		while(isc.hasNext()){
			String inline = isc.nextLine();
			
			TrainingItem ti = getItem(inline);
			trainSet.add(ti);
		}
		return trainSet;
	}
	
	
	private TrainingItem getItem(String inputLine){
		TrainingItem ret = new TrainingItem();
		ret.input = tp.parseInput(inputLine);
		return ret;
	}
	private TrainingItem getItem(String inputLine,String outputLine){
		TrainingItem ret = getItem(inputLine);
		ret.output = tp.parseOutput(outputLine);
		return ret;
	}
}
