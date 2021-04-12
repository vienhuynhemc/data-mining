package model;

import java.util.List;

public class AssociationRule {

	// Khai báo các thuộc tính cần thiết
	private Itemset itemsetBefore;
	private Itemset itemsetAfter;
	private List<String> itemsetBeforeAfterUnEncode;
	private List<String> itemsetAfterAfterUnEncode;
	private double confidence;

	public AssociationRule(Itemset itemsetBefore, Itemset itemsetAfter, List<String> itemsetBeforeAfterUnEncode,
			List<String> itemsetAfterAfterUnEncode, double confidence) {
		this.itemsetBefore = itemsetBefore;
		this.itemsetAfter = itemsetAfter;
		this.itemsetBeforeAfterUnEncode = itemsetBeforeAfterUnEncode;
		this.itemsetAfterAfterUnEncode = itemsetAfterAfterUnEncode;
		this.confidence = confidence;
	}

	public String toString() {
		String s = "[";
		for (int i = 0; i < itemsetBefore.getListItem().size(); i++) {
			if (i == itemsetBefore.getListItem().size() - 1) {
				s += itemsetBefore.getListItem().get(i) + " | Count: " + itemsetBefore.getCount() + "]";
			} else {
				s += itemsetBefore.getListItem().get(i) + ", ";
			}
		}
		s += " ==> [";
		for (int i = 0; i < itemsetAfter.getListItem().size(); i++) {
			if (i == itemsetAfter.getListItem().size() - 1) {
				s += itemsetAfter.getListItem().get(i) + " | Count: " + itemsetAfter.getCount() + "]";
			} else {
				s += itemsetAfter.getListItem().get(i) + ", ";
			}
		}
		s += " (Confidence: " + String.format("%,.5f", confidence) + ")";
		return s;
	}

	public String toStringUnEncode() {
		String s = "[";
		for (int i = 0; i < itemsetBeforeAfterUnEncode.size(); i++) {
			if (i == itemsetBeforeAfterUnEncode.size() - 1) {
				s += itemsetBeforeAfterUnEncode.get(i) + " | Count: " + itemsetBefore.getCount() + "]";
			} else {
				s += itemsetBeforeAfterUnEncode.get(i) + ", ";
			}
		}
		s += " ==> [";
		for (int i = 0; i < itemsetAfterAfterUnEncode.size(); i++) {
			if (i == itemsetAfterAfterUnEncode.size() - 1) {
				s += itemsetAfterAfterUnEncode.get(i) + " | Count: " + itemsetAfter.getCount() + "]";
			} else {
				s += itemsetAfterAfterUnEncode.get(i) + ", ";
			}
		}
		s += " (Confidence: " + String.format("%,.5f", confidence) + ")";
		return s;
	}

	// getter and setter
	public Itemset getItemsetBefore() {
		return itemsetBefore;
	}

	public void setItemsetBefore(Itemset itemsetBefore) {
		this.itemsetBefore = itemsetBefore;
	}

	public Itemset getItemsetAfter() {
		return itemsetAfter;
	}

	public void setItemsetAfter(Itemset itemsetAfter) {
		this.itemsetAfter = itemsetAfter;
	}

	public List<String> getItemsetBeforeAfterUnEncode() {
		return itemsetBeforeAfterUnEncode;
	}

	public void setItemsetBeforeAfterUnEncode(List<String> itemsetBeforeAfterUnEncode) {
		this.itemsetBeforeAfterUnEncode = itemsetBeforeAfterUnEncode;
	}

	public List<String> getItemsetAfterAfterUnEncode() {
		return itemsetAfterAfterUnEncode;
	}

	public void setItemsetAfterAfterUnEncode(List<String> itemsetAfterAfterUnEncode) {
		this.itemsetAfterAfterUnEncode = itemsetAfterAfterUnEncode;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

}
