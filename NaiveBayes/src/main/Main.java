package main;

import model.DataSource;
import model.Instances;
import model.NaiveBayes;

public class Main {

	public static void main(String[] args) {
		// Lấy dữ liệu
		DataSource dataSource = new DataSource("data/weather.nominal.arff");
		Instances instances = dataSource.getDataSet();
		System.out.println("DATA gốc-----------------------------------");
		System.out.println(instances);
		System.out.println("-------------------------------------------");
		System.out.println("DATA mapping-------------------------------");
		System.out.println(instances.toStringMapping());
		System.out.println("-------------------------------------------");
		// Set thuộc tính cần xác định
		instances.setClassIndex(instances.numAttribute() - 1);
		// Tạo model NaiveBayes
		NaiveBayes naiveBayes = new NaiveBayes();
		naiveBayes.buildClassifier(instances);
		System.out.println("Test với từng dòng ------------------------------------------------------------");
		System.out.println("sunny,mild,normal,TRUE ==> " + naiveBayes.testClassifier("sunny,mild,normal,TRUE"));
		System.out.println("sunny,hot,high,FALSE ==> " + naiveBayes.testClassifier("sunny,hot,high,FALSE"));
		System.out.println("sunny,mild,normal,TRUE ==> " + naiveBayes.testClassifier("overcast,hot,normal,FALSE"));
		System.out.println("rainy,mild,high,TRUE ==> " + naiveBayes.testClassifier("rainy,mild,high,TRUE"));
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("Test với dataset---------------------------------------------------------------");
		naiveBayes.testClassifier(instances);
		System.out.println("-----------------------------------------------------------------------------");
	}

}
