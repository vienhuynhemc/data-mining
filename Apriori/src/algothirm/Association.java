package algothirm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.Dataset;
import model.AssociationRule;
import model.Itemset;

public class Association {

	// Khai báo các thuộc tính
	// Dataset
	private Dataset dataset;
	// Minsupport
	private Double minSupport;
	// Minconfidence
	private Double minConfidence;

	public Association() {
	}

	public void loadFileText(String linkDatabase) {
		this.dataset = new Dataset(linkDatabase, Dataset.FILE_TEXT);
	}

	public void loadFileArff(String linkDatabase) {
		this.dataset = new Dataset(linkDatabase, Dataset.FILE_ARFF);
	}

	public List<List<Itemset>> getFrequentItemsets(InterfaceAlgorithm interfaceAlgorithm) {
		return interfaceAlgorithm.execute(dataset, minSupport);
	}

	public List<AssociationRule> getRules(List<List<Itemset>> L) {
		List<AssociationRule> associationRules = new ArrayList<>();
		System.out.println("\n=======");
		System.out.println("Giai đoạn 2 - Phát sinh luật kết hợp");
		System.out.println("=======");
		// Ta sẽ phát sinh luật bằng cách cứ duyệt từ Ln -> Lmax-1 với n bắt dầu từ 1
		// Vòng for 2 từ Ln+1 tới hết rồi phát sinh từng luật cụ thể
		for (int i = 0; i < L.size() - 1; i++) {
			List<Itemset> Ln = L.get(i);
			int count = 0;
			for (int j = i + 1; j < L.size(); j++) {
				List<Itemset> LnPlus1 = L.get(j);
				for (int k = 0; k < Ln.size(); k++) {
					for (int l = 0; l < LnPlus1.size(); l++) {
						Itemset itemsetBefore = Ln.get(k);
						Itemset itemsetAfter = LnPlus1.get(l);
						// Kiểm tra thử ông trc có phải là con của ông sau hay không
						if (isSubItemset(itemsetBefore, itemsetAfter)) {
							// ==> Có thể phát sinh luật từ cặp này
							// Ghép chúng lại thành 1 itemset cho dễ đếm confidence
							Itemset unify = concatTwoItemsets(itemsetBefore, itemsetAfter);
							// Số lần xuất hiện của before
							int countBefore = itemsetBefore.getCount();
							// Số lần xuất hiện của unify
							int countUnify = getCountOneItemset(unify);
							// Tính độ tinh cậy
							double confidence = (double) countUnify / countBefore;
							// Kiểm tra và thêm vào danh sách luật
							if (confidence >= minConfidence) {
								// Giải mã 2 itemset
								// Lấy itemsetAfterDistinct = cách lấy itemsetAfter - itemsetBefore
								Itemset itemsetAfterDistinct = subItemset(itemsetAfter, itemsetBefore);
								itemsetAfterDistinct.setCount(countUnify);
								List<String> itemsetBeforeAfterUnEnCode = unEncode(itemsetBefore);
								List<String> itemsetAfterAfterUnEncode = unEncode(itemsetAfterDistinct);
								associationRules.add(new AssociationRule(itemsetBefore, itemsetAfterDistinct,
										itemsetBeforeAfterUnEnCode, itemsetAfterAfterUnEncode, confidence));
								count++;
							}
						}
					}
				}
			}
			System.out.println("Với " + (i + 1) + "-itemset phát sinh được " + count + " luật");
		}
		System.out.println("-----------------Kết thúc giao đoạn 2-----------------\n");
		return associationRules;
	}

	public void run(InterfaceAlgorithm interfaceAlgorithm) {
		// In thông tin dataset
		printRunInformation();
		// Chạy 2 bước của thuật toán
		List<List<Itemset>> L = getFrequentItemsets(interfaceAlgorithm);
		List<AssociationRule> associationRules = getRules(L);
		// Kết quả
		System.out.println("=======");
		System.out.println("Kết quả chưa được giải mã");
		System.out.println("=======");
		for (AssociationRule associationRule : associationRules) {
			System.out.println(associationRule.toString());
		}
		System.out.println("\n=======");
		System.out.println("Kết quả đã được giải mã");
		System.out.println("=======");
		for (AssociationRule associationRule : associationRules) {
			System.out.println(associationRule.toStringUnEncode());
		}
		System.out.println("=======");
		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("******************************************");
	}

	private void printRunInformation() {
		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("******************************************");
		System.out.println("=== Chạy thông tin ===\n");
		System.out.println("Dataset: \n" + dataset.toString());
		System.out.println("Mapping: \n" + dataset.toStringMapping());
		System.out.println("Số dòng dữ liệu: " + dataset.getDataset().size() + "\n");
		// Lấy các thuộc tính riêng biệt từ dataset
		List<Integer> distinctAttribute = dataset.getDistinctAttribute();
		System.out.println("Số thuộc tính: " + distinctAttribute.size());
		System.out.println(distinctAttribute.toString());
		System.out.println("----------------------------------------");
		// các tham số
		printAttribute();
	}

	private void printAttribute() {
		System.out.println("\nAprior");
		System.out.println("=======");
		System.out.println("Độ hỗ trợ tối thiểu (min support): " + String.format("%,.2f", minSupport * 100) + "%");
		System.out.println(
				"Độ tin cậy tối thiểu (min confidence): " + String.format("%,.2f", minConfidence * 100) + "%\n");
	}

	private boolean isSubItemset(Itemset itemset, Itemset itemCandidate) {
		for (int i = 0; i < itemset.getListItem().size(); i++) {
			int count = 0;
			for (int j = 0; j < itemCandidate.getListItem().size(); j++) {
				if (itemset.getListItem().get(i) == itemCandidate.getListItem().get(j)) {
					count++;
					break;
				}
			}
			if (count == 0) {
				return false;
			}
		}
		return true;
	}

	private Itemset concatTwoItemsets(Itemset itemsetFirst, Itemset itemsetSecond) {
		List<Integer> listItem = new ArrayList<>();
		int count = 0;
		boolean isFirst = false;
		int duyet1 = itemsetFirst.getListItem().size();
		if (itemsetSecond.getListItem().size() < duyet1) {
			duyet1 = itemsetSecond.getListItem().size();
			isFirst = true;
		}
		for (int i = 0; i < duyet1; i++) {
			// Mục đích ở đây là xem thử con nào nhỏ hơn add vô trước
			// Cho mảng tạo theo thế tăng dần khỏi sắp xếp lại tốn time
			int min;
			int max;
			if (itemsetFirst.getListItem().get(i) > itemsetSecond.getListItem().get(i)) {
				min = itemsetSecond.getListItem().get(i);
				max = itemsetFirst.getListItem().get(i);
			} else {
				max = itemsetSecond.getListItem().get(i);
				min = itemsetFirst.getListItem().get(i);
			}
			if (!listItem.contains(min)) {
				listItem.add(min);
			}
			if (!listItem.contains(max)) {
				listItem.add(max);
			}
			count++;
		}
		for (int i = count; i < (isFirst ? itemsetFirst.getListItem().size()
				: itemsetSecond.getListItem().size()); i++) {
			int number = isFirst ? itemsetFirst.getListItem().get(i) : itemsetSecond.getListItem().get(i);
			if (!listItem.contains(number)) {
				listItem.add(number);
			}
		}
		return new Itemset(listItem);
	}

	private int getCountOneItemset(Itemset unify) {
		int count = 0;
		for (Map.Entry<String, Itemset> entry : dataset.getDataset().entrySet()) {
			// Dùng lại hàm isSubItem
			if (isSubItemset(unify, entry.getValue())) {
				count++;
			}
		}
		return count;
	}

	private Itemset subItemset(Itemset itemsetAfter, Itemset itemsetBefore) {
		List<Integer> listNewItem = new ArrayList<>();
		for (int i = 0; i < itemsetAfter.getListItem().size(); i++) {
			boolean isHave = false;
			for (int j = 0; j < itemsetBefore.getListItem().size(); j++) {
				if (itemsetBefore.getListItem().get(j) == itemsetAfter.getListItem().get(i)) {
					isHave = true;
					break;
				}
			}
			if (!isHave) {
				listNewItem.add(itemsetAfter.getListItem().get(i));
			}
		}
		return new Itemset(listNewItem);
	}

	private List<String> unEncode(Itemset itemset) {
		List<String> result = new ArrayList<>();
		for (Integer number : itemset.getListItem()) {
			result.add(dataset.getNameMapping().get(number));
		}
		return result;
	}

	// getter and setter
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Double getMinSupport() {
		return minSupport;
	}

	public void setMinSupport(Double minSupport) {
		this.minSupport = minSupport;
	}

	public Double getMinConfidence() {
		return minConfidence;
	}

	public void setMinConfidence(Double minConfidence) {
		this.minConfidence = minConfidence;
	}

}
