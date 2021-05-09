package model;

import java.util.List;

public class Instance {

	private List<String> datas;
	private List<Integer> dataNumbers;

	public Instance(List<String> datas) {
		this.datas = datas;
	}

	public String toString() {
		String s = "";
		for (String string : datas) {
			s += string + ",";
		}
		s = s.substring(0, s.length() - 1);
		return s;
	}

	public String toStringMapping() {
		String s = "";
		for (int number: dataNumbers) {
			s += number + ",";
		}
		s = s.substring(0, s.length() - 1);
		return s;
	}

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}

	public List<Integer> getDataNumbers() {
		return dataNumbers;
	}

	public void setDataNumbers(List<Integer> dataNumbers) {
		this.dataNumbers = dataNumbers;
	}

}
