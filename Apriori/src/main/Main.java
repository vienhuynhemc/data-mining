package main;

import algothirm.Apriori;
import algothirm.Association;

public class Main {

	public static void main(String[] args) {
		Association association = new Association();
		Apriori apriori = new Apriori();
		/********** TEXT FILE **********/
//		association.loadFileText("data/dataset_1.txt");
//		association.setMinSupport(0.25);
//		association.setMinConfidence(0.5);
//		association.run(apriori);
//		association.loadFileText("data/dataset_2.txt");
//		association.setMinSupport(0.3);
//		association.setMinConfidence(0.5);
//		association.run(apriori);
//		association.loadFileText("data/dataset_3.txt");
//		association.setMinSupport((double) 2 / 9);
//		association.setMinConfidence(0.5);
//		association.run(apriori);
//		association.loadFileText("data/dataset_4.txt");
//		association.setMinSupport(0.3);
//		association.setMinConfidence(0.5);
//		association.run(apriori);
//		association.loadFileText("data/dataset_5.txt");
//		association.setMinSupport(0.4);
//		association.setMinConfidence(0.5);
//		association.run(apriori);
//		association.loadFileText("data/dataset_6.txt");
//		association.setMinSupport(0.6);
//		association.setMinConfidence(0.8);
//		association.run(apriori);
//		association.loadFileText("data/dataset_7.txt");
//		association.setMinSupport(0.7);
//		association.setMinConfidence(0.85);
//		association.run(apriori);
//		association.loadFileText("data/dataset_8.txt");
//		association.setMinSupport(0.5);
//		association.setMinConfidence(1d);
//		association.run(apriori);
		/************** ARFF FILE ***********/
//		association.loadFileArff("data/dataset_1.arff");
//		association.setMinSupport(0.3);
//		association.setMinConfidence(0.7);
//		association.run(apriori);
		association.loadFileArff("data/supermarket.arff");
		association.setMinSupport(0.3);
		association.setMinConfidence(0.7);
		association.run(apriori);
	}

}
