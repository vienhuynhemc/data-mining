package aprioriUseAPIweka;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AprioriUseAPIweka {

	public static void main(String[] args) throws Exception {
		// Load file arff
		DataSource dataSource = new DataSource("data/dataset_ban_hang.arff");
		// Lấy dataset từ dataSource
		Instances datasets = dataSource.getDataSet();
		// Tạo model Apriori
		Apriori aprioriModel = new Apriori();
		// Độ hỗ trợ
		aprioriModel.setLowerBoundMinSupport(0.4);
		// Độ tin cây
		aprioriModel.setMinMetric(0.5);
		// run
		aprioriModel.buildAssociations(datasets);
		// in ra màn hình
		System.out.println(aprioriModel);
	}

}
