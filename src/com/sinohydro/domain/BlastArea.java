package com.sinohydro.domain;

import java.util.Date;

public class BlastArea {
	private String blastName;// �������
	private String holeNo;// �׺�
	private double coordinateX;// x����
	private double coordinateY;// Y����
	private double coordinateZ;// Z����
	private double holeDepth;// ����
	private double azimuth = 0;// ��λ��
	private double inclination = 90;// ���
	private Date date;// ����
	private String sampleName;// ȡ�����
	private double Cu;// Ʒλ
	private String lith;// lith?
	private String clay;// ճ�ԣ���ΪLC��MC,HC
	private Double remark;// remark,�жϼ��׼���
	private double Fe;// Ʒλ
	private double Co;// Ʒλ
	private double NAG;// Ʒλ

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
