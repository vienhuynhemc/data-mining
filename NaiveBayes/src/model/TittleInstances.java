package model;

import java.util.List;

public class TittleInstances {

	private String name;
	private List<String> attributes;
	private int nameNumber;
	private List<Integer> attributesNumber;

	public TittleInstances(String name, List<String> attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public int getIntOfString(String attribute) {
		int result = 0;
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).equals(attribute)) {
				result = attributesNumber.get(i);
				break;
			}
		}
		return result;
	}

	public String getStringOfInt(int number) {
		String result = "";
		for (int i = 0; i < attributes.size(); i++) {
			if (attributesNumber.get(i) == number) {
				result = attributes.get(i);
				break;
			}
		}
		return result;
	}

	public String toString() {
		String s = name + " {";
		for (String string : attributes) {
			s += string + ", ";
		}
		s = s.substring(0, s.length() - 2);
		s += "}";
		return s;
	}

	public String toStringMapping() {
		String s = nameNumber + " {";
		for (int number : attributesNumber) {
			s += number + ", ";
		}
		s = s.substring(0, s.length() - 2);
		s += "}";
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public int getNameNumber() {
		return nameNumber;
	}

	public void setNameNumber(int nameNumber) {
		this.nameNumber = nameNumber;
	}

	public List<Integer> getAttributesNumber() {
		return attributesNumber;
	}

	public void setAttributesNumber(List<Integer> attributesNumber) {
		this.attributesNumber = attributesNumber;
	}

}
