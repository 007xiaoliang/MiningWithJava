package com.sinohydro.domain;

import java.util.Date;

public class BlastArea {
	private String blastName;// 炮区编号
	private String holeNo;// 孔号
	private double coordinateX;// x坐标
	private double coordinateY;// Y坐标
	private double coordinateZ;// Z坐标
	private double holeDepth;// 孔深
	private double azimuth = 0;// 方位角
	private double inclination = 90;// 倾角
	private Date date;// 日期
	private String sampleName;// 取样编号
	private double Cu;// 品位
	private String lith;// lith?
	private String clay;// 粘性，分为LC，MC,HC
	private Double remark;// remark,判断几米见矿
	private double Fe;// 品位
	private double Co;// 品位
	private double NAG;// 品位

	public String getBlastName() {
		return blastName;
	}

	public void setBlastName(String blastName) {
		this.blastName = blastName;
	}

	public String getHoleNo() {
		return holeNo;
	}

	public void setHoleNo(String holeNo) {
		this.holeNo = holeNo;
	}

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

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public double getInclination() {
		return inclination;
	}

	public void setInclination(double inclination) {
		this.inclination = inclination;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public double getCu() {
		return Cu;
	}

	public void setCu(double cu) {
		Cu = cu;
	}

	public String getLith() {
		return lith;
	}

	public void setLith(String lith) {
		this.lith = lith;
	}

	public String getClay() {
		return clay;
	}

	public void setClay(String clay) {
		this.clay = clay;
	}

	public Double getRemark() {
		return remark;
	}

	public void setRemark(Double remark) {
		this.remark = remark;
	}

	public double getFe() {
		return Fe;
	}

	public void setFe(double fe) {
		Fe = fe;
	}

	public double getCo() {
		return Co;
	}

	public void setCo(double co) {
		Co = co;
	}

	public double getNAG() {
		return NAG;
	}

	public void setNAG(double nAG) {
		NAG = nAG;
	}

	@Override
	public String toString() {
		return (holeNo+":"+sampleName+":"+Cu+":"+coordinateX);
	}

	
}
