package main;

import algothirm.Apriori;

public class Main {

	public static void main(String[] args) {
		Apriori apriori1 = new Apriori("data/dataset_1.txt", 0.25, 0.5);
		Apriori apriori2 = new Apriori("data/dataset_2.txt", 0.3, 0.5);
		Apriori apriori3 = new Apriori("data/dataset_3.txt", (double) 2 / 9, 0.5);
		Apriori apriori4 = new Apriori("data/dataset_4.txt", 0.3, 0.5);
		Apriori apriori5 = new Apriori("data/dataset_5.txt", 0.4, 0.5);
		Apriori apriori6 = new Apriori("data/dataset_6.txt", 0.6, 0.8);
		Apriori apriori7 = new Apriori("data/dataset_7.txt", 0.7, 0.85);
		Apriori apriori8 = new Apriori("data/dataset_8.txt", 0.5, 1);
//		apriori1.run();
		apriori2.run();
//		apriori3.run();
//		apriori4.run();
//		apriori5.run();
//		apriori6.run();
//		apriori7.run();
//		apriori8.run();
	}

}
