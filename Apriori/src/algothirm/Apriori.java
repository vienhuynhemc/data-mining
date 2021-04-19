package algothirm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.Dataset;
import model.Itemset;

public class Apriori implements InterfaceAlgorithm {

	// Tập L gồm các itemset phổ biến
	private List<List<Itemset>> L;

	// Constructor nhận vào
	// 1. Link database
	// 2. Minsupport (theo %)
	// 3. Minconfidence (theo %)

	public Apriori() {
	}

	private void browseDataset(Dataset dataset, double minSupport) {
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
			fillSupportAndCountForCn(Cn, dataset);
			// -- In Cn ra
			printListItemSet("C" + k, Cn);
			// Bước 2 : Duyệt qua Cn loại các itemset không thõa minsupport tạo thành Ln
			verifyMinSupport(Cn, minSupport);
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

	private void fillSupportAndCountForCn(List<Itemset> cn, Dataset dataset) {
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

	private void browseDatasetFirst(Dataset dataset, double minSupport) {
		List<Integer> distinceAtrribute = dataset.getDistinctAttribute();
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
		verifyMinSupport(C1, minSupport);
		// in L1 ra console
		printListItemSet("L1", C1);
		// Thêm L1 vào L
		L.add(C1);
	}

	private void verifyMinSupport(List<Itemset> itemsets, double minSupport) {
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

	@Override
	public List<List<Itemset>> execute(Dataset dataset, double minSupport) {
		L = new ArrayList<>();
		// Duyệt lần dầu
		// Bởi vì lần đầu chỉ là 1-itemset
		// nên chưa áp dụng apriori
		// Ta đã lấy tập các attribute không trùng ở trên nên có thì sử dụng thôi
		System.out.println("=======");
		System.out.println("Giai đoạn 1 - Tìm tất cả các tập phổ biến");
		System.out.println("=======");
		browseDatasetFirst(dataset, minSupport);
		// Duyệt dataset tìm tất cả các itemset phổ biết còn lại
		browseDataset(dataset, minSupport);
		return L;
	}

}
