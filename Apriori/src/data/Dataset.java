package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Itemset;

public class Dataset {

	public static final int FILE_TEXT = 0;
	public static final int FILE_ARFF = 1;

	// Khai báo các thuộc tính
	// Có một danh sách các cặp <Tên> - <List item>
	private Map<String, Itemset> dataset;
	// Một danh sách ánh xạ tên -> số cho dễ làm việc ^^
	private Map<Integer, String> nameMapping;
	// Tên của 2 cột <Tên> và <List item>
	private String numericalOrder;
	private String nameListItem;

	// Constructor nhận vào link database
	// Nạp các dữ liệu vào các biết
	public Dataset(String linkDatabase, int type) {
		// Khởi tạo
		init();
		// Nạp dữ liệu
		switch (type) {
		case FILE_TEXT:
			loadDataTxt(linkDatabase);
			break;
		case FILE_ARFF:
			loadArrFile(linkDatabase);
			break;
		}
	}

	private void loadNameArffFile(String readLine) {
		String[] array = readLine.split(" ");
		numericalOrder = "Tên quan hệ: ";
		nameListItem = array[1];
	}

	// To String
	public String toString() {
		String s = numericalOrder + "\t" + nameListItem + "\n";
		for (Map.Entry<String, Itemset> entry : dataset.entrySet()) {
			s += entry.getKey() + "\t" + entry.getValue().toStringWithNoCount() + "\n";
		}
		return s;
	}

	// To String mapping
	public String toStringMapping() {
		String s = "{\n";
		for (Map.Entry<Integer, String> entry : nameMapping.entrySet()) {
			s += entry.getValue() + " được gán tên là: " + entry.getKey() + ",\n";
		}
		s = s.substring(0, s.length() - 2);
		s += ".\n}\n";
		return s;
	}

	private void init() {
		dataset = new LinkedHashMap<>();
		nameMapping = new LinkedHashMap<>();
	}

	// load arff file
	private void loadArrFile(String linkDatabase) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(linkDatabase)));
			loadNameArffFile(bufferedReader.readLine());
			String line = null;
			boolean nextIsData = false;
			List<String> nameIndex = new ArrayList<>();
			while (true) {
				line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				// Nếu chưa tới đoạn lấy data thì lấy thuộc tính
				if (!nextIsData) {
					String[] array_1 = line.split(" ");
					if (array_1[0].equals("@data")) {
						nextIsData = true;
						// sau khi lấy thuộc tính xong ta mapping vào nameMapping
						mappingArff(nameIndex);
					} else {
						nameIndex.add(array_1[1].substring(1, array_1[1].length() - 1));
					}
				} else {
					String[] array = line.split(",");
					List<Integer> listNameMapping = new ArrayList<>();
					for (int i = 0; i < array.length; i++) {
						if (array[i].equals("t")) {
							listNameMapping.add(i+1);
						}
					}
					Itemset itemset = new Itemset(listNameMapping);
					dataset.put(dataset.size() + 1 + "", itemset);
				}
			}
		} catch (IOException e) {
			System.out.println("Không tìm thấy dữ liệu");
			e.printStackTrace();
		}
	}

	public void mappingArff(List<String> nameIndex) {
		for (String name : nameIndex) {
			nameMapping.put(nameMapping.size() + 1, name);
		}
	}

	private void loadDataTxt(String linkDatabase) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(linkDatabase)));
			// Nạp tên dataset
			loadNameDataset(bufferedReader.readLine());
			String line = null;
			while (true) {
				line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				String[] arrayCharGroup = line.split("\t");
				// Tên
				String nameLine = arrayCharGroup[0];
				// Itemset
				// Cắt 2 dấu " "
				String dataLine = arrayCharGroup[1].substring(1, arrayCharGroup[1].length() - 1);
				// Xem thử item nào chưa ánh xạ thì ánh xạ
				// Xong rồi trả về itemset chỉ toàn số not chữ
				Itemset itemset = handleData(dataLine);
				// Thêm vào dataset
				dataset.put(nameLine, itemset);
			}
		} catch (IOException e) {
			System.out.println("Không tìm thấy dữ liệu");
			e.printStackTrace();
		}
	}

	private Itemset handleData(String dataLine) {
		List<Integer> listNumber = new ArrayList<>();
		String[] arrayCharString = dataLine.split(",");
		for (String string : arrayCharString) {
			if (!isExistsInMapping(string)) {
				nameMapping.put(nameMapping.size() + 1, string);
			}
			listNumber.add(getNumberByStringFromNameMapping(string));
		}
		return new Itemset(listNumber);
	}

	public int getNumberByStringFromNameMapping(String string) {
		int result = 0;
		for (Map.Entry<Integer, String> entry : nameMapping.entrySet()) {
			if (entry.getValue().equals(string)) {
				result = entry.getKey();
				break;
			}
		}
		return result;
	}

	private boolean isExistsInMapping(String string) {
		return nameMapping.values().contains(string);
	}

	private void loadNameDataset(String firstLine) {
		String[] arrayCharFirstLine = firstLine.split("\t");
		numericalOrder = arrayCharFirstLine[0];
		nameListItem = arrayCharFirstLine[1];
	}

	// Phương thức lấy số thuộc tính của dataset
	public List<Integer> getDistinctAttribute() {
		List<Integer> list = new ArrayList<Integer>();
		for (Itemset itemset : dataset.values()) {
			for (int number : itemset.getListItem()) {
				if (!list.contains(number)) {
					list.add(number);
				}
			}
		}
		return list;
	}

	// Getter and setter
	public Map<String, Itemset> getDataset() {
		return dataset;
	}

	public void setDataset(Map<String, Itemset> dataset) {
		this.dataset = dataset;
	}

	public String getNumericalOrder() {
		return numericalOrder;
	}

	public void setNumericalOrder(String numericalOrder) {
		this.numericalOrder = numericalOrder;
	}

	public String getNameListItem() {
		return nameListItem;
	}

	public void setNameListItem(String nameListItem) {
		this.nameListItem = nameListItem;
	}

	public Map<Integer, String> getNameMapping() {
		return nameMapping;
	}

	public void setNameMapping(Map<Integer, String> nameMapping) {
		this.nameMapping = nameMapping;
	}

}
