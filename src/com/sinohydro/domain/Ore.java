package com.sinohydro.domain;

public class Ore {
	private double coordinateX;// x����
	private double coordinateY;// Y����
	private double coordinateZ;// Z����
	private double holeDepth;// ����
	private String holeNumber;// �׺�
	private double orePercent;// Ʒλ
	private String stickiness;// ճ�ԣ���ΪLC��MC,HC

	public double getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(double coordinateX) {
		this.coordinateX = coordinateX;
	}

	public double getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(double coordinateY) {
		this.coordinateY = coordinateY;
	}

	public double getCoordinateZ() {
		return coordinateZ;
	}

	public void setCoordinateZ(double coordinateZ) {
		this.coordinateZ = coordinateZ;
	}

	public double getHoleDepth() {
		return holeDepth;
	}

	public void setHoleDepth(double holeDepth) {
		this.holeDepth = holeDepth;
	}

	public String getHoleNumber() {
		return holeNumber;
	}

	public void setHoleNumber(String holeNumber) {
		this.holeNumber = holeNumber;
	}

	public double getOrePercent() {
		return orePercent;
	}

	public void setOrePercent(double orePercent) {
		this.orePercent = orePercent;
	}

	public String getStickiness() {
		return stickiness;
	}

	public void setStickiness(String stickiness) {
		this.stickiness = stickiness;
	}

}
