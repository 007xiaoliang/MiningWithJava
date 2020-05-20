package com.sinohydro.domain;

public class Ore {
	private double coordinateX;// x坐标
	private double coordinateY;// Y坐标
	private double coordinateZ;// Z坐标
	private double holeDepth;// 孔深
	private String holeNumber;// 孔号
	private double orePercent;// 品位
	private String stickiness;// 粘性，分为LC，MC,HC

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
