package model;

import java.util.ArrayList;
import java.util.List;

public class Instances {

	private String name;
	private List<Instance> instances;
	private List<TittleInstances> titles;
	private int classIndex;

	public Instances(String name, List<Instance> instances, List<TittleInstances> titles) {
		this.name = name;
		this.instances = instances;
		this.titles = titles;
		classIndex = 0;
		// mapping dữ liệu từ string -> number để chạy
		mapping();
	}

	public String getNameAttributeByNumber(int number, int nameNumber) {
		String resultString = "";
		for (TittleInstances tittleInstances : titles) {
			if (tittleInstances.getNameNumber() == nameNumber) {
				resultString = tittleInstances.getStringOfInt(number);
				break;
			}
		}
		return resultString;
	}

	private void mapping() {
		int name = 1;
		// tittle and atrribute
		for (TittleInstances tittleInstances : titles) {
			tittleInstances.setNameNumber(name);
			name++;
			int nameAttributes = 1;
			List<Integer> attributeNumbers = new ArrayList<>();
			for (int i = 0; i < tittleInstances.getAttributes().size(); i++) {
				attributeNumbers.add(nameAttributes);
				nameAttributes++;
			}
			tittleInstances.setAttributesNumber(attributeNumbers);
		}
		// instance
		for (Instance instance : instances) {
			List<String> attributes = instance.getDatas();
			List<Integer> dataNumbres = new ArrayList<>();
			for (int i = 0; i < attributes.size(); i++) {
				dataNumbres.add(titles.get(i).getIntOfString(attributes.get(i)));
			}
			instance.setDataNumbers(dataNumbres);
		}
	}

	public int numAttribute() {
		return titles.size();
	}

	public String toString() {
		String s = name + "\n";
		for (TittleInstances tittleInstances : titles) {
			s += tittleInstances.toString() + "\n";
		}
		for (Instance instance : instances) {
			s += instance.toString() + "\n";
		}
		return s;
	}

	public String toStringMapping() {
		String s = name + "\n";
		for (TittleInstances tittleInstances : titles) {
			s += tittleInstances.toStringMapping() + "\n";
		}
		for (Instance instance : instances) {
			s += instance.toStringMapping() + "\n";
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public List<TittleInstances> getTitles() {
		return titles;
	}

	public void setTitles(List<TittleInstances> titles) {
		this.titles = titles;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

}
