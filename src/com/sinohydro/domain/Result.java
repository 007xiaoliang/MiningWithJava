package com.sinohydro.domain;

import java.util.List;

public class Result {

	private int num;
	private double[] grade;
	private List<Ore> list;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public double[] getGrade() {
		return grade;
	}
	public void setGrade(double[] grade) {
		this.grade = grade;
	}
	public List<Ore> getList() {
		return list;
	}
	public void setList(List<Ore> list) {
		this.list = list;
	}
	
}
