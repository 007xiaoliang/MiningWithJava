package com.sinohydro.domain;

/**
 * 三点所确定外心交点坐标对象
 * 
 * @author MY
 *
 */
public class CircumcenterCoordinate {

	private double x;
	private double y;
	private double z = 0.0;// 默认高程为0
	private boolean flag = true;// 标记为什么分隔点，默认为非边界点，如果是false则为生成的边界点
	private double Cu;
	private int circle = 0;// 判断此点是否循环过，主要用于生成矿石闭合多边形时使用，为0时没有循环过，循环过后值为1，如果为3 ，则说明此点为垂足点
	private int number=-1;//记录垂足点所在集合在返回的集合中的位置

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getCu() {
		return Cu;
	}

	public void setCu(double cu) {
		Cu = cu;
	}

	public int getCircle() {
		return circle;
	}

	public void setCircle(int circle) {
		this.circle = circle;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	

}
