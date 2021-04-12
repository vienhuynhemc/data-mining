package algothirm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.Dataset;
import model.AssociationRule;
import model.Itemset;

public class Apriori {

	// Khai báo các thuộc tính
	// Dataset
	private Dataset dataset;
	// Minsupport
	private Double minSupport;
	// Minconfidence
	private Double minConfidence;
	// Tập L gồm các itemset phổ biến
	private List<List<Itemset>> L;
	// Danh sách luật kết hợp
	private List<AssociationRule> associationRules;

	// Constructor nhận vào
	// 1. Link database
	// 2. Minsupport (theo %)
	// 3. Minconfidence (theo %)
	public Apriori() {
		// Khởi tạo
		init();
	}

	private void init() {
		// Tập L
		L = new ArrayList<>();
		// Danh sách luật kết hợp
		associationRules = new ArrayList<>();
	}

	public void loadFileText(String linkDatabase) {
		this.dataset = new Dataset(linkDatabase, Dataset.FILE_TEXT);
	}

	public void loadFileArff(String linkDatabase) {
		this.dataset = new Dataset(linkDatabase, Dataset.FILE_ARFF);
	}

	public void run() {
		// In các thông indataset và duyệt lần đầu
		// Vì lần đầu chưa cần áp dụng apriori
		printRunInformation();
		// Duyệt dataset tìm tất cả các itemset phổ biết còn lại
		browseDataset();
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

	private void printAttribute() {
		System.out.println("\nAprior");
		System.out.println("=======");
		System.out.println("Độ hỗ trợ tối thiểu (min support): " + String.format("%,.2f", minSupport * 100) + "%");
		System.out.println(
				"Độ tin cậy tối thiểu (min confidence): " + String.format("%,.2f", minConfidence * 100) + "%\n");
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
		// Duyệt lần dầu
		// Bởi vì lần đầu chỉ là 1-itemset
		// nên chưa áp dụng apriori
		// Ta đã lấy tập các attribute không trùng ở trên nên có thì sử dụng thôi
		System.out.println("=======");
		System.out.println("Giai đoạn 1 - Tìm tất cả các tập phổ biến");
		System.out.println("=======");
		browseDatasetFirst(distinctAttribute);
	}

	private void browseDataset() {
		// Các thuộc tính cần thiết
		// 1.Lần duyệt
		// Ta bắt đầu từ 2
		int k = 2;
		// 2.Tập L kề trước lần duyệt
		// Ta bắt đầu k = 2 thì tập L hiện tại là L1
		List<Itemset> listBefore = L.get(0);
		// Chạy thuật toán
		while (true) {
			// Phát sinh tập phổ biến tiếp theo từ tập phổ biến kề trước
			// Yêu cầu là:
			// 1. Chỉ ghép đc 2 tập phổ biến có k-2 phần tử giống nhau
			// 2. Áp dụng Apriori: Tất cả tập con k-1 phần tử phải nằm trong
			// tập phổ biến kề trc (Các tập con của tập phổ biến đều phổ biến)
			// Bước 1 : Phát sinh tập ứng viên k-item
			// -- Thõa 2 yêu cầu ở trên đã đề ra
			List<Itemset> Cn = getCandidateStepK(listBefore, k - 2);
			// Bước 2 : Có tập Cn rồi ta tính số lần xuất hiện và độ hỗ trợ
			fillSupportAndCountForCn(Cn);
			// -- In Cn ra
			printListItemSet("C" + k, Cn);
			// Bước 2 : Duyệt qua Cn loại các itemset không thõa minsupport tạo thành Ln
			verifyMinSupport(Cn);
			// -- In Ln ra
			printListItemSet("L" + k, Cn);
			// Nếu Ln có size = 0 thì dừng thuật toán
			if (Cn.size() == 0) {
				System.out.println("-----------------Kết thúc giao đoạn 1----------------- ");
				break;
			}
			// Bước 3 : Thêm Ln vào L
			L.add(Cn);
			// --- Kết thúc vòng n --
			// Bây giờ ta tăng k lên
			k++;
			// Gán lại listBefore
			listBefore = Cn;
		}

	}

	private void fillSupportAndCountForCn(List<Itemset> cn) {
		// Duyệt qua dataset
		for (Map.Entry<String, Itemset> entry : dataset.getDataset().entrySet()) {
			// Duyệt qua mảng Cn , cứ nếu itemset nào là con của itemset của entry thì ++
			// lên
			for (Itemset itemset : cn) {
				if (isSubItemset(itemset, entry.getValue())) {
					itemset.setCount(itemset.getCount() + 1);
				}
			}
		}
		// Tính độ hỗ trợ
		for (Itemset itemset : cn) {
			// Làm tròn còn 1 chữ số thập phân
			// Áp dụng cho dataset nhỏ
//			double support = (double) Math.round(((double) itemset.getCount() / dataset.getDataset().size()) * 10) / 10;
			// Không làm tròn áp dụng với mọi dataset
			double support = (double) itemset.getCount() / dataset.getDataset().size();
			itemset.setSupport(support);
		}
	}

	private List<Itemset> getCandidateStepK(List<Itemset> listBefore, int same) {
		// Tạo mảng chứa
		List<Itemset> listCandidate = new ArrayList<>();
		// Cứ ghép 1 ông với tất cả các ông còn lại theo thứ tự tăng dần
		for (int i = 0; i < listBefore.size() - 1; i++) {
			for (int j = i + 1; j < listBefore.size(); j++) {
				Itemset itemsetFirst = listBefore.get(i);
				Itemset itemsetSecond = listBefore.get(j);
				// Kiểm tra thõa điều kiện giống nhau k-2 item
				if (verifySame(itemsetFirst, itemsetSecond, same)) {
					// Nối 2 itemset lại
					Itemset itemCandidate = concatTwoItemsets(itemsetFirst, itemsetSecond);
					// Kiểm tra thử listCandidate có tồn tại itemCandidate này chưa
					if (!isContain(listCandidate, itemCandidate)) {
						// Áp dụng Apriori
						// Vì sao same + 2, vì same ta nhận vào là k - 2
						if (verifyApriori(itemCandidate, listBefore, same + 2)) {
							// add vào
							listCandidate.add(itemCandidate);
						}
					}
				}
			}
		}
		return listCandidate;
	}

	private boolean isContain(List<Itemset> listCandidate, Itemset itemCandidate) {
		for (int i = 0; i < listCandidate.size(); i++) {
			// Dùng lại phương thức isSubitemset để check
			if (isSubItemset(itemCandidate, listCandidate.get(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean verifyApriori(Itemset itemCandidate, List<Itemset> listBefore, int k) {
		// Mỗi k-itemset thì sẽ có k+1 k-1itemset
		// Lợi dụng điều đó ta sẽ duyệt qua listBefore
		// - Nếu có một itemset trong listBefore là con của itemCandidate thì ta ++
		// - Nếu như count = k thì thõa Apriori
		int count = 0;
		for (int i = 0; i < listBefore.size(); i++) {
			if (isSubItemset(listBefore.get(i), itemCandidate)) {
				count++;
			}
			// Thõa rồi thì ko chạy tiếp nữa
			if (count == k) {
				return true;
			}
		}
		return count == k;
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

	private boolean verifySame(Itemset itemsetFirst, Itemset itemsetSecond, int same) {
		int count = 0;
		for (int i = 0; i < itemsetFirst.getListItem().size(); i++) {
			for (int j = 0; j < itemsetSecond.getListItem().size(); j++) {
				if (itemsetFirst.getListItem().get(i) == itemsetSecond.getListItem().get(j)) {
					count++;
					break;
				}
			}
		}
		return count == same;
	}

	private void browseDatasetFirst(List<Integer> distinceAtrribute) {
		// Duyệt qua dataset tạo các 1-itemset
		// 1. Tạo map hỗ trợ
		Map<Integer, Integer> mapHelp = new LinkedHashMap<>();
		for (Integer number : distinceAtrribute) {
			mapHelp.put(number, 0);
		}
		// 2. Duyệt qua dataset rồi đếm số lần xuất hiện của các thuộc tính
		for (Map.Entry<String, Itemset> entry : dataset.getDataset().entrySet()) {
			for (int number : entry.getValue().getListItem()) {
				mapHelp.put(number, mapHelp.get(number) + 1);
			}
		}
		// 3. Tạo mảng C1 chứa các 1-itemset
		List<Itemset> C1 = new ArrayList<>();
		// 4. Tạo các 1-itemset và thêm vào C1
		for (Map.Entry<Integer, Integer> entry : mapHelp.entrySet()) {
			// support
			// Làm tròn còn 1 chữ số thập phân
			// Áp dụng cho dataset nhỏ
//			double support = (double) Math.round((entry.getValue() / (double) dataset.getDataset().size()) * 10) / 10;
			// Không làm tròn áp dụng với mọi dataset
			double support = entry.getValue() / (double) dataset.getDataset().size();
			// các biến của itemset
			List<Integer> list = new ArrayList<>();
			list.add(entry.getKey());
			Itemset itemset = new Itemset(list, entry.getValue(), support);
			// add vào C1
			C1.add(itemset);
		}
		// in C1 ra console
		printListItemSet("C1", C1);
		// Xóa những itemset không đủ độ hỗ trợ
		verifyMinSupport(C1);
		// in L1 ra console
		printListItemSet("L1", C1);
		// Thêm L1 vào L
		L.add(C1);
	}

	private void verifyMinSupport(List<Itemset> itemsets) {
		int count = 0;
		while (count < itemsets.size()) {
			if (itemsets.get(count).getSupport() < minSupport) {
				itemsets.remove(count);
			} else {
				count++;
			}
		}
	}

	private void printListItemSet(String name, List<Itemset> itemsets) {
		System.out.println(name + " có " + itemsets.size() + " giá trị:");
		for (int i = 0; i < itemsets.size(); i++) {
			if (i == itemsets.size() - 1) {
				System.out.print("\t" + itemsets.get(i) + ".\n");
			} else {
				System.out.print("\t" + itemsets.get(i) + ",\n");
			}
		}
		System.out.println("------------------------------------------------------");
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
