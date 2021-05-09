package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSource {

	private String path;
	private Instances dataSet;

	public DataSource(String path) {
		this.path = path;
		loadData();
	}

	private void loadData() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.path)));
			String lineString = null;
			lineString = bufferedReader.readLine();
			// Tên dataset
			String nameInstances = lineString.split(" ")[1];
			// Danh sách các tiêu đề của dataset
			List<TittleInstances> listTitleInstances = new ArrayList<>();
			// Danh sách các instance
			List<Instance> instances = new ArrayList<>();
			boolean nextIsData = false;
			while (true) {
				lineString = bufferedReader.readLine();
				if (lineString == null) {
					break;
				}
				String[] arrayString = lineString.split(" ");
				if (arrayString.length > 0) {
					if (!nextIsData) {
						// Tittle
						if (arrayString[0].equals("@attribute")) {
							String nameTitleString = arrayString[1];
							List<String> attributes = getAttributes(arrayString);
							TittleInstances tittleInstances = new TittleInstances(nameTitleString, attributes);
							listTitleInstances.add(tittleInstances);
						}
						// Instance
						else if (arrayString[0].equals("@data")) {
							nextIsData = true;
						}
					} else {
						Instance instanceData = new Instance(Arrays.asList(lineString.split(",")));
						instances.add(instanceData);
					}
				}
			}
			bufferedReader.close();
			// Tạo dataSet
			this.dataSet = new Instances(nameInstances, instances, listTitleInstances);
		} catch (IOException e) {
			System.out.println("File không tồn tại");
			e.printStackTrace();
		}
	}

	private List<String> getAttributes(String[] dataArray) {
		String data = "";
		for (int i = 2; i < dataArray.length; i++) {
			data += dataArray[i];
		}
		data = data.substring(1, data.length() - 1);
		String[] datas = data.split(",");
		List<String> result = Arrays.asList(datas);
		return result;
	}

	public Instances getDataSet() {
		return dataSet;
	}

}
