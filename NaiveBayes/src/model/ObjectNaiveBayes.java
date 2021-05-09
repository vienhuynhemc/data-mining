package model;

public class ObjectNaiveBayes {

	private int nameTitle;
	private int nameNumber;
	private int count;
	private int valueClassIndex;
	private int countValueClassIndex;

	public ObjectNaiveBayes(int nameTitle, int nameNumber, int count, int valueClassIndex, int countValueClassIndex) {
		super();
		this.nameTitle = nameTitle;
		this.nameNumber = nameNumber;
		this.count = count;
		this.valueClassIndex = valueClassIndex;
		this.countValueClassIndex = countValueClassIndex;
	}

	public int getNameTitle() {
		return nameTitle;
	}

	public void setNameTitle(int nameTitle) {
		this.nameTitle = nameTitle;
	}

	public int getNameNumber() {
		return nameNumber;
	}

	public void setNameNumber(int nameNumber) {
		this.nameNumber = nameNumber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getValueClassIndex() {
		return valueClassIndex;
	}

	public void setValueClassIndex(int valueClassIndex) {
		this.valueClassIndex = valueClassIndex;
	}

	public int getCountValueClassIndex() {
		return countValueClassIndex;
	}

	public void setCountValueClassIndex(int countValueClassIndex) {
		this.countValueClassIndex = countValueClassIndex;
	}

}
