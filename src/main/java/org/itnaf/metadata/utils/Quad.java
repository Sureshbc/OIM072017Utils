package org.itnaf.metadata.utils;

public class Quad {
	private String attributeList = "";
	private String rule = "";
	private String transformation = "";
	private String target = "";

	public Quad() {
	}

	public Quad(String attributeList, String rule, String transformation, String target, boolean singleValue) {
		super();
		this.attributeList = attributeList;
		this.rule = rule;
		this.transformation = transformation;
		this.target = target;
	}

	

	public String getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(String attributeList) {
		this.attributeList = attributeList;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getTransformation() {
		return transformation;
	}

	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "Quad [attributeList=" + attributeList + ", rule=" + rule + ", transformation=" + transformation
				+ ", target=" + target + "]";
	}
}
