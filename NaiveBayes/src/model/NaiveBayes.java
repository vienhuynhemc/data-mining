package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
	private Instances instances;
	// Map lưu trữ số lần xuất hiện các lớp của thuộc tính được chọn
	private Map<Integer, Integer> countMainAttributes;
	// Danh sách lưu tất cả các object chứa các tham số của các thuộc tính còn lại
	private List<ObjectNaiveBayes> objectNaiveBayes;
	// Biến để xem thử có ông nào bằng 0 hay ko để cộng
	private boolean isHaveZero;

	public NaiveBayes() {
	}

	private void createAndCountMainAttribute(Instances instances) {
		TittleInstances mainTitleInstances = instances.getTitles().get(instances.getClassIndex());
		countMainAttributes = new LinkedHashMap<>();
		for (int number : mainTitleInstances.getAttributesNumber()) {
			countMainAttributes.put(number, 0);
		}
		List<Instance> listInstance = instances.getInstances();
		for (Instance instance : listInstance) {
			countMainAttributes.put(instance.getDataNumbers().get(instances.getClassIndex()),
					countMainAttributes.get(instance.getDataNumbers().get(instances.getClassIndex())) + 1);
		}
	}

	private void createObjectNaiveBayes(Instances instances) {
		objectNaiveBayes = new ArrayList<>();
		List<TittleInstances> listTittleInstances = instances.getTitles();
		TittleInstances tittleInstancesClassIndex = listTittleInstances.get(instances.getClassIndex());
		for (TittleInstances tittleInstances : listTittleInstances) {
			// Phải khác ông được chọn mới tính
			if (tittleInstances.getNameNumber() != listTittleInstances.get(instances.getClassIndex()).getNameNumber()) {
				List<Integer> attributeNumbres = tittleInstances.getAttributesNumber();
				// Duyệt qua các lớp của từng thuộc tính
				for (int number : attributeNumbres) {
					// Với mỗi lớp tương ứng ta kèm đều với mainAttributes
					for (int numberClassIndex : tittleInstancesClassIndex.getAttributesNumber()) {
						int count = countObject(tittleInstances.getNameNumber(), number, numberClassIndex, instances);
						if (count == 0 && !isHaveZero) {
							isHaveZero = true;
							for (ObjectNaiveBayes objectNaiveBayes : this.objectNaiveBayes) {
								objectNaiveBayes.setCount(objectNaiveBayes.getCount() + 1);
								objectNaiveBayes.setCountValueClassIndex(objectNaiveBayes.getCountValueClassIndex()
										+ tittleInstances.getAttributesNumber().size());
							}
						}
						ObjectNaiveBayes objectNaiveBayes = new ObjectNaiveBayes(tittleInstances.getNameNumber(),
								number, count, numberClassIndex, countMainAttributes.get(numberClassIndex));
						if (isHaveZero) {
							objectNaiveBayes.setCount(objectNaiveBayes.getCount() + 1);
							objectNaiveBayes.setCountValueClassIndex(objectNaiveBayes.getCountValueClassIndex()
									+ tittleInstances.getAttributesNumber().size());
						}
						this.objectNaiveBayes.add(objectNaiveBayes);
					}
				}
			}
		}
		// in ra cho người dùng thấy
		printableObjectNaiveBayes(instances);
	}

	private void printableObjectNaiveBayes(Instances instances) {
		printMainAttribute(instances);
		printDetail(instances);
	}

	private void printDetail(Instances instances) {
		List<TittleInstances> listTittleInstances = instances.getTitles();
		TittleInstances tittleInstancesClassIndex = listTittleInstances.get(instances.getClassIndex());
		for (TittleInstances tittleInstances : listTittleInstances) {
			if (tittleInstances.getNameNumber() != listTittleInstances.get(instances.getClassIndex()).getNameNumber()) {
				List<Integer> attributeNumbres = tittleInstances.getAttributesNumber();
				System.out.println(tittleInstances.getName());
				List<Integer> totals = new ArrayList<>();
				for (int i = 0; i < tittleInstancesClassIndex.getAttributesNumber().size(); i++) {
					totals.add(0);
				}
				for (int number : attributeNumbres) {
					List<Integer> list = new ArrayList<>();
					for (int numberClassIndex : tittleInstancesClassIndex.getAttributesNumber()) {
						for (ObjectNaiveBayes objectNaiveBayes : objectNaiveBayes) {
							if (objectNaiveBayes.getNameNumber() == number
									&& objectNaiveBayes.getNameTitle() == tittleInstances.getNameNumber()
									&& objectNaiveBayes.getValueClassIndex() == numberClassIndex) {
								list.add(objectNaiveBayes.getCount());
								totals.set(list.size() - 1, totals.get(list.size() - 1) + objectNaiveBayes.getCount());
								break;
							}
						}
					}
					if (tittleInstances.getStringOfInt(number).length() >= 6) {
						System.out.print("  " + tittleInstances.getStringOfInt(number) + "\t\t");
					} else {
						System.out.print("  " + tittleInstances.getStringOfInt(number) + "\t\t\t");
					}
					for (int value : list) {
						System.out.print(value + "\t");
					}
					System.out.println();
				}
				System.out.print("   [total]\t\t");
				for (int value : totals) {
					System.out.print(value + "\t");
				}
				System.out.println();
				System.out.println();
			}
		}
	}

	private void printMainAttribute(Instances instances) {
		System.out.print("Attribute\t\t");
		int sumCountMainAttributes = 0;
		for (Map.Entry<Integer, Integer> entry : countMainAttributes.entrySet()) {
			sumCountMainAttributes += entry.getValue();
			System.out.print(instances.getNameAttributeByNumber(entry.getKey(),
					instances.getTitles().get(instances.getClassIndex()).getNameNumber()) + "\t");
		}
		System.out.println();
		System.out.print("\t\t\t");
		for (Map.Entry<Integer, Integer> entry : countMainAttributes.entrySet()) {
			double per = ((double) entry.getValue()) / sumCountMainAttributes;
			System.out.print("(" + String.format("%,.3f", per) + ")\t");
		}
		System.out.println();
		System.out.println("=============================");
	}

	public void buildClassifier(Instances instances) {
		// Lấy ông được chọn và tính số lần xuất hiện của các lớp đầu tiên
		createAndCountMainAttribute(instances);
		// Đếm số lần xuất hiện của các lớp của các thuộc tính còn lại
		createObjectNaiveBayes(instances);
		this.instances = instances;
	}

	private int countObject(int nameTittle, int number, int numberClassIndex, Instances instances) {
		int index = 0;
		for (int i = 0; i < instances.getTitles().size(); i++) {
			if (instances.getTitles().get(i).getNameNumber() == nameTittle) {
				index = i;
				break;
			}
		}
		int count = 0;
		for (Instance instance : instances.getInstances()) {
			if (instance.getDataNumbers().get(index) == number
					&& instance.getDataNumbers().get(instances.getClassIndex()) == numberClassIndex) {
				count++;
			}
		}
		return count;
	}

	public String testClassifier(String dataTest) {
		String[] array = dataTest.split(",");
		List<Integer> listTittle = new ArrayList<>();
		List<Integer> maaping = new ArrayList<>();
		List<TittleInstances> listTittleInstances = instances.getTitles();
		TittleInstances tittleInstancesClassIndex = listTittleInstances.get(instances.getClassIndex());
		for (TittleInstances tittleInstances : listTittleInstances) {
			if (tittleInstances.getNameNumber() != listTittleInstances.get(instances.getClassIndex()).getNameNumber()) {
				maaping.add(tittleInstances.getIntOfString(array[maaping.size()]));
				listTittle.add(tittleInstances.getNameNumber());
			}
		}
		int sum = 0;
		for (Map.Entry<Integer, Integer> entry : countMainAttributes.entrySet()) {
			sum += entry.getValue();
		}
		double max = 0;
		int numberSelect = 0;
		for (int number : tittleInstancesClassIndex.getAttributesNumber()) {
			double cal = ((double) countMainAttributes.get(number)) / sum;
			for (int i = 0; i < maaping.size(); i++) {
				for (ObjectNaiveBayes objectNaiveBayes : this.objectNaiveBayes) {
					if (objectNaiveBayes.getNameTitle() == listTittle.get(i)
							&& objectNaiveBayes.getNameNumber() == maaping.get(i)
							&& objectNaiveBayes.getValueClassIndex() == number) {
						cal *= ((double) objectNaiveBayes.getCount()) / objectNaiveBayes.getCountValueClassIndex();
						break;
					}
				}
			}
			if (cal > max) {
				max = cal;
				numberSelect = number;
			}
		}
		return tittleInstancesClassIndex.getStringOfInt(numberSelect);
	}

}
