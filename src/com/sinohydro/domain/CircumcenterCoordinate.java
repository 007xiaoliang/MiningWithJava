package com.sinohydro.domain;

/**
 * ������ȷ�����Ľ����������
 * 
 * @author MY
 *
 */
public class CircumcenterCoordinate {

	private double x;
	private double y;
	private double z = 0.0;// Ĭ�ϸ߳�Ϊ0
	private boolean flag = true;// ���Ϊʲô�ָ��㣬Ĭ��Ϊ�Ǳ߽�㣬�����false��Ϊ���ɵı߽��
	private double Cu;
	private int circle = 0;// �жϴ˵��Ƿ�ѭ��������Ҫ�������ɿ�ʯ�պ϶����ʱʹ�ã�Ϊ0ʱû��ѭ������ѭ������ֵΪ1�����Ϊ3 ����˵���˵�Ϊ�����
	private int number=-1;//��¼��������ڼ����ڷ��صļ����е�λ��

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
