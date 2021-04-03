package model;

import java.util.List;

public class Itemset {

	// Khai báo các thuộc tính
	// Có một list các tên item đã được đánh dấu sang số
	private List<Integer> listItem;
	// Số lần xuất hiện
	private int count;
	// Độ hỗ trợ
	private double support;

	// Itemset constructor nhận vào list tên item không trùng nhau
	public Itemset(List<Integer> listItem) {
		this.listItem = listItem;
	}

	// Itemset constructor nhận vào
	// 1. k-itemset
	// 2. số lần xuất hiện
	// 3. Độ hỗ trợ
	public Itemset(List<Integer> listItem, int count, double support) {
		this.listItem = listItem;
		this.count = count;
		this.support = support;
	}

	// Tostring không có số đếm
	public String toStringWithNoCount() {
		String s = "";
		for (int number : listItem) {
			s += number + ", ";
		}
		s = s.substring(0, s.length() - 2);
		return s;
	}

	// Tostring theo dạng itemset riêng biệt
	public String toString() {
		return "{ Data: " + listItem.toString() + ", Count: [" + count + "], Support: [" +  String.format("%,.2f", support) + "] }";
	}

	// Getter and setter
	public List<Integer> getListItem() {
		return listItem;
	}

	public void setListItem(List<Integer> listItem) {
		this.listItem = listItem;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSupport() {
		return support;
	}

	public void setSupport(double support) {
		this.support = support;
	}

}
