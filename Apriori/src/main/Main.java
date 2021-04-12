package main;

import algothirm.Apriori;

public class Main {

	public static void main(String[] args) {
		Apriori apriori = new Apriori();
		/********** TEXT FILE **********/
//		apriori.loadFileText("data/dataset_1.txt");
//		apriori.setMinSupport(0.25);
//		apriori.setMinConfidence(0.5);
//		apriori.loadFileText("data/dataset_2.txt");
//		apriori.setMinSupport(0.3);
//		apriori.setMinConfidence(0.5);
//		apriori.loadFileText("data/dataset_3.txt");
//		apriori.setMinSupport((double) 2 / 9);
//		apriori.setMinConfidence(0.5);
//		apriori.loadFileText("data/dataset_4.txt");
//		apriori.setMinSupport(0.3);
//		apriori.setMinConfidence(0.5);
//		apriori.loadFileText("data/dataset_5.txt");
//		apriori.setMinSupport(0.4);
//		apriori.setMinConfidence(0.5);
//		apriori.loadFileText("data/dataset_6.txt");
//		apriori.setMinSupport(0.6);
//		apriori.setMinConfidence(0.8);
//		apriori.loadFileText("data/dataset_7.txt");
//		apriori.setMinSupport(0.7);
//		apriori.setMinConfidence(0.85);
//		apriori.loadFileText("data/dataset_8.txt");
//		apriori.setMinSupport(0.5);
//		apriori.setMinConfidence(1d);
		/************** ARFF FILE ***********/
//		apriori.loadFileArff("data/dataset_1.arff");
//		apriori.setMinSupport(0.3);
//		apriori.setMinConfidence(0.7);
		apriori.loadFileArff("data/supermarket.arff");
		apriori.setMinSupport(0.3);
		apriori.setMinConfidence(0.7);
		apriori.run();
	}

}
