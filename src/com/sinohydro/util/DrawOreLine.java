package com.sinohydro.util;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.domain.LengthInfo;
import com.sinohydro.domain.MyPoint;

/**
 * 根据坐标以及品位信息圈定矿石区域
 * 
 * @author MY
 *
 */
public class DrawOreLine {
	private List<CircumcenterCoordinate> returnList = new ArrayList<CircumcenterCoordinate>();
	private List<List<CircumcenterCoordinate>> finalReturnList = new ArrayList<List<CircumcenterCoordinate>>();

	/**
	 * 计算两点距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double length(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	/**
	 * 对list集合按照x坐标进行排序，原理与数组的冒泡排序相同
	 * 
	 * @param list
	 */
	private void taxis(List<CircumcenterCoordinate> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - i - 1; j++) {
				if (list.get(j).getX() > list.get(j + 1).getX()) {
					CircumcenterCoordinate cc = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, cc);
				}
			}
		}
	}

	/**
	 * 通过传入的炮区信息得到炮区内所有矿石区域边界点的集合
	 * 
	 * @param list
	 * @return
	 */
	public List<List<CircumcenterCoordinate>> getAllOreLines(List<CircumcenterCoordinate> list,
			List<CircumcenterCoordinate> givedBorderList) {
		List<CircumcenterCoordinate> pointByMiddle = getPointByMiddle(list);
		List<CircumcenterCoordinate> borderPointList = new ArrayList<CircumcenterCoordinate>();
		List<CircumcenterCoordinate> middlePointList = new ArrayList<CircumcenterCoordinate>();
		if (true) {//givedBorderList == null
			// 将边界点与分隔点分别放入不同的集合
			for (CircumcenterCoordinate circumcenterCoordinate : pointByMiddle) {
				if (circumcenterCoordinate.isFlag()) {
					middlePointList.add(circumcenterCoordinate);
				} else {
					borderPointList.add(circumcenterCoordinate);
				}
			}
		} else {
			// 边界点用给定的坐标信息
			borderPointList = givedBorderList;
			for (CircumcenterCoordinate circumcenterCoordinate : pointByMiddle) {
				if (circumcenterCoordinate.isFlag()) {
					middlePointList.add(circumcenterCoordinate);
				}
			}
		}
		// 得到炮区中所有矿石点的集合
		List<CircumcenterCoordinate> orePoints = new ArrayList<CircumcenterCoordinate>();
		for (CircumcenterCoordinate circumcenterCoordinate : list) {
			if (circumcenterCoordinate.getCu() >= 0.12) {
				orePoints.add(circumcenterCoordinate);
			}
		}
		// 对分隔点进行循环，将所有与边界距离小于3.5m的点筛选出来
		List<CircumcenterCoordinate> middlePointListForLittle = new ArrayList<CircumcenterCoordinate>();
		for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
			CircumcenterCoordinate coordinate = getMinLengthFromBorder(circumcenterCoordinate, borderPointList);
			if (coordinate == null)
				continue;
			double length = length(circumcenterCoordinate.getX(), circumcenterCoordinate.getY(), coordinate.getX(),
					coordinate.getY());
			if (length <= 4) {
				middlePointListForLittle.add(circumcenterCoordinate);
			}
		}
		// middlePointList中没有被循环的点的个数
		int count = 0;
		// 以middlePointListForLittle的点作为第二个点，垂足作为第一个点，在middlePointList中循环寻找没有循环过的最近点
		for (int i = 0; i < middlePointListForLittle.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < middlePointList.size(); j++) {// 在middlePointList中寻找到middlePointListForLittle的点，将标识改为循环过
				if (middlePointList.get(j).getX() == middlePointListForLittle.get(i).getX()
						&& middlePointList.get(j).getY() == middlePointListForLittle.get(i).getY()) {
					if (middlePointList.get(j).getCircle() == 1) {// 判断这个点是否循环过，如果循环过，则此点不用循环。
						flag = true;
					} else {// 此点没有循环过，开始建立新集合，将这个区域的所有矿石点加入到集合中
							// 将此点标识为已经循环过
						middlePointList.get(j).setCircle(1);
						List<CircumcenterCoordinate> oneOreList = new ArrayList<CircumcenterCoordinate>();
						// 垂足坐标放在集合的首位,此点放在第二位
						oneOreList.add(getMinLengthFromBorder(middlePointList.get(j), borderPointList));
						oneOreList.add(middlePointList.get(j));
						LengthInfo temp = getMinLengthPoint(middlePointList, j);
						// 将下一点标识为已循环并加入到矿石区域集合中
						middlePointList.get(temp.getI()).setCircle(1);
						oneOreList.add(middlePointList.get(temp.getI()));
						// 以middlePointList.get(temp.getI())这个点为基础继续向下面判断，直到循环到与边界小于3.5m的点结束，将矿石区域集合加入到要返回的集合中
						int mark = temp.getI();
						count = 0;
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0)
								count++;
						}
						while (count > 0) {// 只要有点没有被循环，就一直循环下去。这个while循环被结束只有一种可能，就是循环里面循环到了距离边界小于3.5m的点
							boolean breakOrNot = false;
							for (int k = 0; k < middlePointList.size(); k++) {
								if (count == 0)
									break;
								// 得到距离最近的点
								LengthInfo t = getMinLengthPoint(middlePointList, mark);
								// 得到点以后，判断距离
								double length = t.getLength();
								if (length > 5) {
									boolean f = true;
									// 判断此点是否为距离边界小于3.5m的点
									for (int l = 0; l < middlePointListForLittle.size(); l++) {
										if ((middlePointList.get(mark).getX() == middlePointListForLittle.get(l).getX()
												&& middlePointList.get(mark).getY() == middlePointListForLittle.get(l)
														.getY())
												|| length > 9) {// 判断length>9是处理距离边界小于3.5m的点生成不完全的情况
											f = false;
											breakOrNot = true;// 用于结束while循环
											// 是距离边界小于3.5m的点，生成垂足，并加入集合。再将整个集合加入到需要返回的集合。结束循环
											CircumcenterCoordinate minLengthFromBorder = getMinLengthFromBorder(
													middlePointList.get(mark), borderPointList);
											oneOreList.add(minLengthFromBorder);
											// 需要将边界上的点加入集合，形成闭合多边形，此处的操作应该在所有的矿石区域集合都形成后才能完成

											finalReturnList.add(oneOreList);
											break;
										}
									}
									if (f) {// 说明没有遇到距离边界小于3.5m的点，还要继续循环
										middlePointList.get(t.getI()).setCircle(1);
										oneOreList.add(middlePointList.get(t.getI()));
										// 改变需要判断的点的位置，继续循环
										mark = t.getI();
										// 重新计算未被循环的点的个数
										count = 0;
										for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
											if (circumcenterCoordinate.getCircle() == 0)
												count++;
										}
									}
								} else {
									// 将下一点标识为已循环并加入到矿石区域集合中
									middlePointList.get(t.getI()).setCircle(1);
									oneOreList.add(middlePointList.get(t.getI()));
									// 改变需要判断的点的位置，继续循环
									mark = t.getI();
									// 重新计算未被循环的点的个数
									count = 0;
									for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
										if (circumcenterCoordinate.getCircle() == 0)
											count++;
									}
									if (count == 0) {
										oneOreList.add(
												getMinLengthFromBorder(middlePointList.get(t.getI()), borderPointList));
										finalReturnList.add(oneOreList);
									}
								}
								if (breakOrNot)// 说明循环到了距离边界小于3.5m的点，跳出for循环
									break;
							}
							if (breakOrNot)// 说明循环到了距离边界小于3.5m的点，强制跳出while循环
								break;
						}

					}
					// 只要进入了大if里面，执行了里面的过程，这个for循环就可以结束，以节省性能
					break;
				}
			}
			if (flag)
				continue;
		}
		// 再处理不需要边界点参与就能形成闭合多边形的情况
		// 重新计算未被循环的点的个数
		count = 0;
		for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
			if (circumcenterCoordinate.getCircle() == 0)
				count++;
		}
		int mark = 0;
		for (int i = 0; i < middlePointList.size(); i++) {
			List<CircumcenterCoordinate> oneOreList = new ArrayList<CircumcenterCoordinate>();// 建立新存放矿石区域点的集合
			if (middlePointList.get(i).getCircle() == 0) {// 没有被循环的点
				middlePointList.get(i).setCircle(1);// 状态改为已经循环过
				oneOreList.add(middlePointList.get(i));
				mark = i;
				while (count > 0) {
					boolean flag = false;
					if (count == 1) {// 最后一个点得不到距离最近的点，直接加入集合
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0) {
								oneOreList.add(circumcenterCoordinate);
								circumcenterCoordinate.setCircle(1);
								oneOreList.add(oneOreList.get(0));// 最后一个点要与第一个点相同才能形成闭合多边形
								finalReturnList.add(oneOreList);
								flag = true;
								break;
							}
						}
					}
					if (flag)
						break;
					// 得到距离最近的点
					LengthInfo t = getMinLengthPoint(middlePointList, mark);
					CircumcenterCoordinate cc = middlePointList.get(t.getI());
					// 判断这个点是否为集合中的第一个点，如果是，结束循环，如果不是，加入到集合中，改变参数，继续循环
					if (cc.getX() == oneOreList.get(0).getX() && cc.getY() == oneOreList.get(0).getY()) {// 说明是集合中第一个点
						// 将集合加入到要返回的集合
						// 为了得到闭合多边形，最后一个点与第一个点要一样
						oneOreList.add(cc);
						finalReturnList.add(oneOreList);
						break;
					} else if (t.getLength() > 6) {
						oneOreList.add(oneOreList.get(0));
						finalReturnList.add(oneOreList);
						break;
					} else {
						// 将下一点标识为已循环并加入到矿石区域集合中
						middlePointList.get(t.getI()).setCircle(1);
						oneOreList.add(middlePointList.get(t.getI()));
						// 改变需要判断的点的位置，继续循环
						mark = t.getI();
						// 重新计算未被循环的点的个数
						count = 0;
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0)
								count++;
						}
					}
				}
			}
		}

		// 在此处处理边界点与矿石区域集合的关系
		/*
		 * 思路： 1.选出所有的垂足点形成新的集合
		 * 2.遍历垂足点集合，对某个垂足点，首先判断哪边是矿石区域，判断方向后一次添加最近的边界点进集合，直到遇到垂足点，如果这个垂足点不是首先判断的垂足
		 * 的所在的集合，则将这个垂足点所在集合的所有点按顺序添加进首个垂足点集合，以这个垂足点集合中最后一个垂足点开始之前的循环，直到遇到首个垂足点所在
		 * 集合的另一个垂足点结束循环
		 */
		// 选出所有的垂足点
		List<CircumcenterCoordinate> minLengthPoint = new ArrayList<CircumcenterCoordinate>();
		for (int i = 0; i < finalReturnList.size(); i++) {
			for (int j = 0; j < finalReturnList.get(i).size(); j++) {
				if (finalReturnList.get(i).get(j).getCircle() == 3) {
					finalReturnList.get(i).get(j).setNumber(i);// 记录此垂足所在集合在finalReturnList中的位置
					minLengthPoint.add(finalReturnList.get(i).get(j));
				}
			}
		}
		// 将垂足点加入到边界点集合中
		borderPointList.addAll(minLengthPoint);
		// 记录borderPointList当前循环的位置，
		int markPoint = 0;
		// 计算边界点集合中未被循环的垂足点的个数
		int countPoint = 0;
		for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
			if (circumcenterCoordinate.getCircle() == 3)
				countPoint++;
		}
		while (countPoint > 0) {
			// 对垂足点进行遍历操作
			for (int i = 0; i < borderPointList.size(); i++) {
				boolean flag = false;
				// 判断是不是垂足点，不是直接跳过.这里不等于3还判断了是否循环过，下面就不需要再判断如果是垂足点是否循环的情况。等于0，说明不是垂足点，等于1说明已经循环过
				if (borderPointList.get(i).getCircle() != 3)
					continue;
				// 判断垂足点是不是集合中的最后一个元素，不是直接跳过
				CircumcenterCoordinate circumcenterCoordinate2 = finalReturnList.get(borderPointList.get(i).getNumber())
						.get(finalReturnList.get(borderPointList.get(i).getNumber()).size() - 1);
				if (circumcenterCoordinate2 != borderPointList.get(i))
					continue;
				// 改垂足点循环状态
				borderPointList.get(i).setCircle(1);
				// 得到与垂足最近的矿石点
				CircumcenterCoordinate minLengthOrePoint = getMinLengthOrePoint(borderPointList.get(i), orePoints);
				// 得到与垂足距离有序的边界点集合
				List<CircumcenterCoordinate> borderList = getBorderList(borderPointList.get(i), borderPointList);
				// 判断方向，接下来要加入集合的边界点与垂足点以及最近的矿石点三点，以垂足点为顶点的角是锐角就满足要求
				CircumcenterCoordinate temp = new CircumcenterCoordinate();
				CircumcenterCoordinate cc = new CircumcenterCoordinate();
				for (int j = 0; j < borderList.size(); j++) {
					boolean b = isSmallAngelOrNotForBorder(minLengthOrePoint, borderPointList.get(i),
							borderList.get(j));
					if (b) {// 满足要求，将点加入到矿石区域集合中
						finalReturnList.get(borderPointList.get(i).getNumber()).add(borderList.get(j));
						// 循环过后状态改掉
						for (int l = 0; l < borderPointList.size(); l++) {
							if (borderPointList.get(l).getX() == borderList.get(j).getX()
									&& borderPointList.get(l).getY() == borderList.get(j).getY()) {
								borderPointList.get(l).setCircle(1);
								markPoint = l;
							}
						}
						temp = borderList.get(j);
						break;
					}
				}
				// 添加第二个点
				// 在borderPointList中寻找下一个最近点
				List<CircumcenterCoordinate> borderPointListCopy = new ArrayList<CircumcenterCoordinate>();
				for (int j = 0; j < borderPointList.size(); j++) {
					borderPointListCopy.add(j, borderPointList.get(j));
				}
				LengthInfo info = new LengthInfo();
				while (true) {
					info = getMinLengthPoint(borderPointListCopy, markPoint);
					cc = borderPointList.get(info.getI());
					// 判断这个最近点，首个垂足点不能在temp与cc中间
					if (!(borderPointList.get(i).getX() >= minNum(temp.getX(), cc.getX())
							&& borderPointList.get(i).getX() <= maxNum(temp.getX(), cc.getX())
							&& borderPointList.get(i).getY() >= minNum(temp.getY(), cc.getY())
							&& borderPointList.get(i).getY() <= maxNum(temp.getY(), cc.getY()))) {
						break;
					} else {
						borderPointListCopy.set(info.getI(), null);
					}
				}
				while (true) {
					// 在borderPointList中寻找下一个最近点
					if (info == null) {
						info = getMinLengthPoint(borderPointList, markPoint);
					}
					cc = borderPointList.get(info.getI());
					// 判断这个最近点是否为垂足，
					if (cc.getCircle() == 3) {
						// 是垂足，需要判断是哪个集合的垂足
						if (cc.getNumber() == borderPointList.get(i).getNumber()) {// 是当前循环垂足的集合的垂足，则循环结束
							// 为了得到闭合的多边形，要将此点加入
							finalReturnList.get(borderPointList.get(i).getNumber()).add(cc);
							// 改变状态
							borderPointList.get(info.getI()).setCircle(1);
							// 重新计算边界点集合中未被循环的垂足点的个数
							countPoint = 0;
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (circumcenterCoordinate.getCircle() == 3)
									countPoint++;
							}
							// 跳出循环
							break;
						} else {// 是其他集合的垂足，则开始新的循环
							// 判断这个垂足是不是所在集合的第一个元素，如果不是，反转此集合
							if (!(finalReturnList.get(cc.getNumber()).get(0) == cc)) {
								Collections.reverse(finalReturnList.get(cc.getNumber()));
							}
							// 将集合中第一个垂足的状态在borderPointList中改为1
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (finalReturnList.get(cc.getNumber()).get(0).getX() == circumcenterCoordinate.getX()
										&& finalReturnList.get(cc.getNumber()).get(0).getY() == circumcenterCoordinate
												.getY()) {
									circumcenterCoordinate.setCircle(1);
								}
							}
							// 重新计算边界点集合中未被循环的垂足点的个数
							countPoint = 0;
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (circumcenterCoordinate.getCircle() == 3)
									countPoint++;
							}
							// 将此集合全部按序加入当前循环垂足所在的集合
							finalReturnList.get(borderPointList.get(i).getNumber())
									.addAll(finalReturnList.get(cc.getNumber()));
							// 将集合最后一个元素的number值重新设置
							finalReturnList.get(borderPointList.get(i).getNumber())
									.get(finalReturnList.get(borderPointList.get(i).getNumber()).size() - 1)
									.setNumber(borderPointList.get(i).getNumber());
							// 在finalReturnList将此集合位置置为null，方便输出数据时不重复
							finalReturnList.set(cc.getNumber(), null);
							flag = true;
							break;
						}
					} else {// 不是垂足
						finalReturnList.get(borderPointList.get(i).getNumber()).add(cc);// 加入集合
						borderPointList.get(info.getI()).setCircle(1);// 改变状态
						markPoint = info.getI();// 重新定位循环位置
					}
					info = null;
				}

				if (flag)
					break;
			}
		}
		if (givedBorderList!=null && borderPointList.size() == givedBorderList.size()
				&& borderPointList.get(1).getX() == givedBorderList.get(1).getX()) {// 说明边界为给定的有序集合
			finalReturnList.add(borderPointList);
		} else {
			finalReturnList.add(getBorderListCopy(borderPointList, list));
		}
		return finalReturnList;
	}

	/**
	 * 得到与垂足点距离有序的边界点集合
	 * 
	 * @param circumcenterCoordinate
	 * @param borderPointList
	 * @return
	 */
	private List<CircumcenterCoordinate> getBorderList(CircumcenterCoordinate circumcenterCoordinate,
			List<CircumcenterCoordinate> borderPointList) {
		List<CircumcenterCoordinate> borderList = new ArrayList<CircumcenterCoordinate>();
		List<LengthInfo> temp = new ArrayList<LengthInfo>();
		for (int i = 0; i < borderPointList.size(); i++) {
			LengthInfo lengthInfo = new LengthInfo();
			lengthInfo.setI(i);
			lengthInfo.setLength(length(circumcenterCoordinate.getX(), circumcenterCoordinate.getY(),
					borderPointList.get(i).getX(), borderPointList.get(i).getY()));
			temp.add(lengthInfo);
		}
		// 排序
		for (int i = 0; i < temp.size() - 1; i++) {
			for (int j = 0; j < temp.size() - i - 1; j++) {
				if (temp.get(j).getLength() > temp.get(j + 1).getLength()) {
					LengthInfo li = temp.get(j);
					temp.set(j, temp.get(j + 1));
					temp.set(j + 1, li);
				}
			}
		}
		// 向borderList里赋值
		for (int i = 0; i < temp.size(); i++) {
			borderList.add(borderPointList.get(temp.get(i).getI()));
		}
		return borderList;
	}

	/**
	 * 得到与垂足点最近的矿石点
	 * 
	 * @param circumcenterCoordinate
	 *            垂足点
	 * @param orePoints
	 *            炮区中矿石点的集合
	 */
	private CircumcenterCoordinate getMinLengthOrePoint(CircumcenterCoordinate circumcenterCoordinate,
			List<CircumcenterCoordinate> orePoints) {
		// 在middlePointList寻找最近点加入到集合中
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < orePoints.size(); k++) {
			LengthInfo lengthInfo = new LengthInfo();
			lengthInfo.setI(k);
			lengthInfo.setLength(length(circumcenterCoordinate.getX(), circumcenterCoordinate.getY(),
					orePoints.get(k).getX(), orePoints.get(k).getY()));
			lengthList.add(lengthInfo);
		}
		// 得到距离最小值的点
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return orePoints.get(temp.getI());
	}

	private LengthInfo getMinLengthPoint(List<CircumcenterCoordinate> middlePointList, int i) {
		// 在middlePointList寻找最近点加入到集合中
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < middlePointList.size(); k++) {
			// 如果循环过或者为空值，直接跳过
			if (middlePointList.get(k) == null || middlePointList.get(k).getCircle() == 1)
				continue;
			else {
				LengthInfo lengthInfo = new LengthInfo();
				lengthInfo.setI(k);
				lengthInfo.setLength(length(middlePointList.get(i).getX(), middlePointList.get(i).getY(),
						middlePointList.get(k).getX(), middlePointList.get(k).getY()));
				lengthList.add(lengthInfo);
			}
		}
		// 得到距离最小值的点
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return temp;
	}

	private LengthInfo getMinLengthPoint1(List<CircumcenterCoordinate> middlePointList, CircumcenterCoordinate cc) {
		// 在middlePointList寻找最近点加入到集合中
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < middlePointList.size(); k++) {
			// 如果循环过或者为空值，直接跳过
			if (middlePointList.get(k) == null || middlePointList.get(k).getCircle() == 1)
				continue;
			else {
				LengthInfo lengthInfo = new LengthInfo();
				lengthInfo.setI(k);
				lengthInfo.setLength(
						length(cc.getX(), cc.getY(), middlePointList.get(k).getX(), middlePointList.get(k).getY()));
				lengthList.add(lengthInfo);
			}
		}
		// 得到距离最小值的点
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return temp;
	}

	/**
	 * 根据中点生成所有的分隔点以及边界点也要生成出来的集合
	 * 
	 * @param list
	 * @param returnList
	 * @return
	 */
	private List<CircumcenterCoordinate> getPointByMiddle(List<CircumcenterCoordinate> list) {
		taxis(list);// 对集合按x坐标进行从小到大排序
		// 对集合中所有孔进行循环，在矿废石之间生成分隔点
		for (int i = 0; i < list.size(); i++) {
			// 对与此点距离小于11的所有点生成分隔点
			for (int j = i + 1; j < list.size(); j++) {
				double l = length(list.get(i).getX(), list.get(i).getY(), list.get(j).getX(), list.get(j).getY());
				if (l < 11) {
					// 判断需不需要生成分隔点，即判断这两点的品位是不是一矿一废
					if ((list.get(i).getCu() >= 0.12 && list.get(j).getCu() < 0.12)
							|| (list.get(i).getCu() < 0.12 && list.get(j).getCu() >= 0.12)) {
						CircumcenterCoordinate coordinate = new CircumcenterCoordinate();
						// 控制double小数点后两位
						DecimalFormat df = new DecimalFormat("0.00");
						double a = Double.parseDouble(df.format((list.get(i).getX() + list.get(j).getX()) / 2));
						double c = Double.parseDouble(df.format((list.get(i).getY() + list.get(j).getY()) / 2));
						coordinate.setX(a);
						coordinate.setY(c);
						returnList.add(coordinate);
					}
				}
			}
		}
		// 将炮区边界点也加入到返回的list集合中
		getBorderPoint(list, returnList);
		return returnList;

	}

	/**
	 * 工具方法，求出一个炮区中最适合做原点的坐标，即这个炮区中最小的x坐标，最小的y坐标当作原点坐标
	 * 
	 * @param list
	 * @return
	 */
	public CircumcenterCoordinate getMinPoint(List<CircumcenterCoordinate> list) {
		CircumcenterCoordinate cc = new CircumcenterCoordinate();
		double temp1 = list.get(0).getX();
		double temp2 = list.get(0).getY();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).getX() < list.get(i).getX()) {
					temp1 = list.get(j).getX();
				}
				if (list.get(j).getY() < list.get(i).getY()) {
					temp2 = list.get(j).getY();
				}
			}
		}
		cc.setX(temp1);
		cc.setY(temp2);
		return cc;
	}

	/**
	 * 得到炮区边界的所有点
	 * 
	 * @param list
	 * @param returnList
	 * @return
	 */
	private void getBorderPoint(List<CircumcenterCoordinate> list, List<CircumcenterCoordinate> returnList) {
		int length = 11;// 根据此距离来判断象限是否有点，一般炮孔间距都小于此值
		double offset = 4;
		// 对集合中所有孔进行循环，判断是否为边界点
		for (int i = 0; i < list.size(); i++) {
			double x = list.get(i).getX();
			double y = list.get(i).getY();
			boolean flag1 = true;// 判断第一象限是否有近点
			boolean flag2 = true;// 判断第二象限是否有近点
			boolean flag3 = true;// 判断第三象限是否有近点
			boolean flag4 = true;// 判断第四象限是否有近点
			List<Point2D.Double> polygon1 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon2 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon3 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon4 = new ArrayList<Point2D.Double>();
			// 第一象限
			Point2D.Double o1 = new Point2D.Double(x, y);
			Point2D.Double o2 = new Point2D.Double(x + length, y);
			Point2D.Double o3 = new Point2D.Double(x + length, y + length);
			Point2D.Double o4 = new Point2D.Double(x, y + length);
			polygon1.add(o1);
			polygon1.add(o2);
			polygon1.add(o3);
			polygon1.add(o4);
			// 第二象限
			Point2D.Double t1 = new Point2D.Double(x, y);
			Point2D.Double t2 = new Point2D.Double(x, y + length);
			Point2D.Double t3 = new Point2D.Double(x - length, y + length);
			Point2D.Double t4 = new Point2D.Double(x - length, y);
			polygon2.add(t1);
			polygon2.add(t2);
			polygon2.add(t3);
			polygon2.add(t4);
			// 第三象限
			Point2D.Double s1 = new Point2D.Double(x, y);
			Point2D.Double s2 = new Point2D.Double(x - length, y);
			Point2D.Double s3 = new Point2D.Double(x - length, y - length);
			Point2D.Double s4 = new Point2D.Double(x, y - length);
			polygon3.add(s1);
			polygon3.add(s2);
			polygon3.add(s3);
			polygon3.add(s4);
			// 第四象限
			Point2D.Double f1 = new Point2D.Double(x, y);
			Point2D.Double f2 = new Point2D.Double(x + length, y);
			Point2D.Double f3 = new Point2D.Double(x + length, y - length);
			Point2D.Double f4 = new Point2D.Double(x, y - length);
			polygon4.add(f1);
			polygon4.add(f2);
			polygon4.add(f3);
			polygon4.add(f4);
			// 判断第一象限
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon1)) {// 只要有一个点处在区域内，就可判断这个象限有近点，直接跳出循环
					flag1 = false;
					break;
				}
			}
			// 判断第二象限
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon2)) {// 只要有一个点处在区域内，就可判断这个象限有近点，直接跳出循环
					flag2 = false;
					break;
				}
			}
			// 判断第三象限
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon3)) {// 只要有一个点处在区域内，就可判断这个象限有近点，直接跳出循环
					flag3 = false;
					break;
				}
			}
			// 判断第四象限
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon4)) {// 只要有一个点处在区域内，就可判断这个象限有近点，直接跳出循环
					flag4 = false;
					break;
				}
			}
			// 根据判断四个象限分别有没有点的结果来确定是否在此点附近生成边界点,判断依据如下
			// 第一象限或第四象限没有近点，则x坐标加3.5，第二象限或第三象限没有近点，则x坐标-3.5
			// 第一，二象限没有近点，y坐标+3.5；第三，四象限没有近点，y坐标-3.5；第二，三象限没有近点，x坐标-3.5；第一，四象限没有近点，x坐标+3.5
			// 第一，三象限没有近点或者第二，四象限没有近点，生成两个边界点x坐标+3.5和x坐标-3.5
			// 如果三个象限都没有近点，则生成四个边界点，x+3.5，x-3.5，y+3.5，y-3.5，这样生成以后，需要再判断去除与分隔d较近的边界点
			if ((flag1 == true && flag2 == false && flag3 == false && flag4 == false)
					|| (flag1 == false && flag2 == false && flag3 == false && flag4 == true)
					|| ((flag1 == true && flag2 == false && flag3 == false && flag4 == true))) {// 第一象限或第四象限没有近点,第一，四象限没有近点
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setX(list.get(i).getX() + offset);
				cc.setY(list.get(i).getY());
				cc.setFlag(false);// 边界点

				returnList.add(cc);
			} else if ((flag1 == false && flag2 == true && flag3 == false && flag4 == false)
					|| (flag1 == false && flag2 == false && flag3 == true && flag4 == false)
					|| (flag1 == false && flag2 == true && flag3 == true && flag4 == false)) {// 第二象限或第三象限没有近点,第二，三象限没有近点
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setX(list.get(i).getX() - offset);
				cc.setY(list.get(i).getY());
				cc.setFlag(false);// 边界点

				returnList.add(cc);
			} else if (flag1 == true && flag2 == true && flag3 == false && flag4 == false) {// 第一，二象限没有近点，y坐标+offset
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setY(list.get(i).getY() + offset);
				cc.setX(list.get(i).getX());
				cc.setFlag(false);// 边界点

				returnList.add(cc);
			} else if (flag1 == false && flag2 == false && flag3 == true && flag4 == true) {// 第三，四象限没有近点，y坐标-offset
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setY(list.get(i).getY() - offset);
				cc.setX(list.get(i).getX());
				cc.setFlag(false);// 边界点

				returnList.add(cc);
			} else if ((flag1 == true && flag2 == false && flag3 == true && flag4 == false)
					|| (flag1 == false && flag2 == true && flag3 == false && flag4 == true)) {// 第一，三象限没有近点或者第二，四象限没有近点
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// 边界点

				c2.setX(list.get(i).getX() - offset);
				c2.setY(list.get(i).getY());
				c2.setFlag(false);// 边界点

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == false && flag2 == true && flag3 == true && flag4 == true) {// 如果只第一象限有近点，则生成两边界点，x-offset，y-offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() - offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// 边界点

				c2.setY(list.get(i).getY() - offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// 边界点

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == false && flag3 == true && flag4 == true) {// 如果只第二象限有近点，则生成辆个边界点，，x+offset，y-offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// 边界点

				c2.setY(list.get(i).getY() - offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// 边界点

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == true && flag3 == false && flag4 == true) {// 如果只第三象限有近点，则生成两个边界点，x+offset，y+offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// 边界点

				c2.setY(list.get(i).getY() + offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// 边界点

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == true && flag3 == true && flag4 == false) {// 如果只第四象限有近点，则生成两个边界点，x-offset，y+offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() - offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// 边界点

				c2.setY(list.get(i).getY() + offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// 边界点

				returnList.add(c1);
				returnList.add(c2);
			}
		}
	}

	/**
	 * 判断点是否在多边形内，如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
	 * 
	 * @param point
	 *            检测点
	 * @param pts
	 *            多边形的顶点
	 * @return 点在多边形内返回true,否则返回false
	 */
	private static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts) {

		int N = pts.size();
		boolean boundOrVertex = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		int intersectCount = 0;// cross points count of x
		double precision = 2e-10; // 浮点类型计算时候与0比较时候的容差
		Point2D.Double p1, p2;// neighbour bound vertices
		Point2D.Double p = point; // 当前点

		p1 = pts.get(0);// left vertex
		for (int i = 1; i <= N; ++i) {// check all rays
			if (p.equals(p1)) {
				return boundOrVertex;// p is an vertex
			}

			p2 = pts.get(i % N);// right vertex
			if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {// ray is outside of our interests
				p1 = p2;
				continue;// next ray left point
			}

			if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {// ray is crossing over by the algorithm
																			// (common part of)
				if (p.y <= Math.max(p1.y, p2.y)) {// x is before of ray
					if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {// overlies on a horizontal ray
						return boundOrVertex;
					}

					if (p1.y == p2.y) {// ray is vertical
						if (p1.y == p.y) {// overlies on a vertical ray
							return boundOrVertex;
						} else {// before ray
							++intersectCount;
						}
					} else {// cross point on the left side
						double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;// cross point of y
						if (Math.abs(p.y - xinters) < precision) {// overlies on a ray
							return boundOrVertex;
						}

						if (p.y < xinters) {// before ray
							++intersectCount;
						}
					}
				}
			} else {// special case when ray is crossing through the vertex
				if (p.x == p2.x && p.y <= p2.y) {// p crossing over p2
					Point2D.Double p3 = pts.get((i + 1) % N); // next vertex
					if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {// p.x lies between p1.x & p3.x
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;// next ray left point
		}

		if (intersectCount % 2 == 0) {// 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}
	}

	/**
	 * 得到某隔分隔点与边界最近的点的坐标,即垂足
	 * 
	 * @param splitPoint
	 *            分隔点坐标
	 * @param borderList
	 *            边界点坐标
	 * @return 最近点坐标对象
	 */
	private CircumcenterCoordinate getMinLengthFromBorder(CircumcenterCoordinate splitPoint,
			List<CircumcenterCoordinate> borderList) {
		CircumcenterCoordinate cc = new CircumcenterCoordinate();
		// 求出与splitPoint的距离，加入到集合中
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int i = 0; i < borderList.size(); i++) {
			LengthInfo lengthInfo = new LengthInfo();
			lengthInfo.setI(i);
			lengthInfo.setLength(
					length(splitPoint.getX(), splitPoint.getY(), borderList.get(i).getX(), borderList.get(i).getY()));
			lengthList.add(lengthInfo);
		}
		// 排序
		for (int i = 0; i < lengthList.size() - 1; i++) {
			for (int j = 0; j < lengthList.size() - i - 1; j++) {
				if (lengthList.get(j).getLength() > lengthList.get(j + 1).getLength()) {
					LengthInfo temp = lengthList.get(j);
					lengthList.set(j, lengthList.get(j + 1));
					lengthList.set(j + 1, temp);
				}
			}
		}
		/*
		 * // 从最小的距离开始选出两个点 boolean flagForLength = false; for (int i = 0; i <
		 * lengthList.size() - 2; i++) { for (int j = i + 1; j < i + 3; j++) {
		 * CircumcenterCoordinate point1 = borderList.get(lengthList.get(i).getI());
		 * CircumcenterCoordinate point2 = borderList.get(lengthList.get(j).getI());
		 * boolean smallAngelOrNot = isSmallAngelOrNot(splitPoint, point1, point2); if
		 * (smallAngelOrNot) { // 满足要求，就生成splitPoint在两个点上的垂足，并返回 cc =
		 * getMinLengthCoordinate(splitPoint, point1, point2); // 生成的垂足必须在边界线上 boolean
		 * ifInLine = ifInLine(cc, borderList); if(ifInLine) {//在边界线上，结束循环 flagForLength
		 * = true; break; } } } if (flagForLength) break; }
		 */
		// 另一种投机的方法：直接选出最小的四个点组合，一般垂足必定在这四个点中的两个组成的直线上
		CircumcenterCoordinate point1 = borderList.get(lengthList.get(0).getI());
		CircumcenterCoordinate point2 = borderList.get(lengthList.get(1).getI());
		CircumcenterCoordinate point3 = borderList.get(lengthList.get(2).getI());
		CircumcenterCoordinate point4 = borderList.get(lengthList.get(3).getI());
		if (isSmallAngelOrNot(splitPoint, point1, point2)) {
			if (length(splitPoint.getX(), splitPoint.getY(), point1.getX(), point1.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point2.getX(), point2.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point1, point2);
				cc.setCircle(3);
			}
		} else if (isSmallAngelOrNot(splitPoint, point1, point3)) {
			if (length(splitPoint.getX(), splitPoint.getY(), point1.getX(), point1.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point3.getX(), point3.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point1, point3);
				cc.setCircle(3);
			}
		} else if (isSmallAngelOrNot(splitPoint, point1, point4)) {
			if (length(splitPoint.getX(), splitPoint.getY(), point1.getX(), point1.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point4.getX(), point4.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point1, point4);
				cc.setCircle(3);
			}
		} else if (isSmallAngelOrNot(splitPoint, point2, point3)) {
			if (length(splitPoint.getX(), splitPoint.getY(), point3.getX(), point3.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point2.getX(), point2.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point2, point3);
				cc.setCircle(3);
			}
		} else if (isSmallAngelOrNot(splitPoint, point2, point4)) {
			if (length(splitPoint.getX(), splitPoint.getY(), point4.getX(), point4.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point2.getX(), point2.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point2, point4);
				cc.setCircle(3);
			}
		} else {
			if (length(splitPoint.getX(), splitPoint.getY(), point3.getX(), point3.getY()) > 7
					&& length(splitPoint.getX(), splitPoint.getY(), point4.getX(), point4.getY()) > 7) {
				cc = null;
			} else {
				cc = getMinLengthCoordinate(splitPoint, point3, point4);
				cc.setCircle(3);
			}
		}
		return cc;
	}

	/**
	 * 判断选出的两点为顶点的角是不是锐角，如果是，说明这两点是满足要求的点
	 * 
	 * @param splitPoint
	 *            分隔点
	 * @param point1
	 *            边界点1
	 * @param point2
	 *            边界点2
	 * @return 如果满足要求，返回true，否则为false
	 */
	private boolean isSmallAngelOrNot(CircumcenterCoordinate splitPoint, CircumcenterCoordinate point1,
			CircumcenterCoordinate point2) {
		// 求出三点组成的三角形三边
		double splitPointL = length(point1.getX(), point1.getY(), point2.getX(), point2.getY());
		double point1L = length(splitPoint.getX(), splitPoint.getY(), point2.getX(), point2.getY());
		double point2L = length(splitPoint.getX(), splitPoint.getY(), point1.getX(), point1.getY());
		// 要保证点splitPoint的垂线在point1与point2形成的线上，而不是延长线上，要保证以point1与point2点形成的角为锐角，即余弦值在(0，1]之间
		// point1为顶点的角的余弦值
		double point1A = (splitPointL * splitPointL + point2L * point2L - point1L * point1L)
				/ (2 * splitPointL * point2L);
		// point2为顶点的角的余弦值
		double point2A = (splitPointL * splitPointL + point1L * point1L - point2L * point2L)
				/ (2 * splitPointL * point1L);
		// 如果point1A与point2A都在(0，1]之间，则返回true，否则为false
		if (point1A >= 0 && point1A < 1 && point2A >= 0 && point2A < 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断边界上矿石方向，以便添加边界点形成闭合多边形
	 * 
	 * @param minLengthOrePoint
	 * @param minLengthPoint
	 * @param oneBorderPoint
	 * @return
	 */
	private boolean isSmallAngelOrNotForBorder(CircumcenterCoordinate minLengthOrePoint,
			CircumcenterCoordinate minLengthPoint, CircumcenterCoordinate oneBorderPoint) {
		// 求出三点组成的三角形三边
		double minLengthOrePointL = length(minLengthPoint.getX(), minLengthPoint.getY(), oneBorderPoint.getX(),
				oneBorderPoint.getY());
		double minLengthPointL = length(minLengthOrePoint.getX(), minLengthOrePoint.getY(), oneBorderPoint.getX(),
				oneBorderPoint.getY());
		double oneBorderPointL = length(minLengthOrePoint.getX(), minLengthOrePoint.getY(), minLengthPoint.getX(),
				minLengthPoint.getY());
		// 垂足点形成的角为锐角，即余弦值在(0，1]之间
		// point1为顶点的角的余弦值
		double minLengthPointA = (minLengthOrePointL * minLengthOrePointL + oneBorderPointL * oneBorderPointL
				- minLengthPointL * minLengthPointL) / (2 * minLengthOrePointL * oneBorderPointL);
		if (minLengthPointA >= 0 && minLengthPointA < 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 方法返回值即为两个数中最大值
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public double maxNum(double num1, double num2) {
		if (num1 > num2) {
			return num1;
		} else {
			return num2;
		}
	}

	/**
	 * 方法返回值即为两个数中最小值
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public double minNum(double num1, double num2) {
		if (num1 < num2) {
			return num1;
		} else {
			return num2;
		}
	}

	/**
	 * 生成封装有垂足坐标的对象
	 * 
	 * @param splitPoint
	 * @param point1
	 * @param point2
	 * @return
	 */
	private CircumcenterCoordinate getMinLengthCoordinate(CircumcenterCoordinate splitPoint,
			CircumcenterCoordinate point1, CircumcenterCoordinate point2) {
		CircumcenterCoordinate cc = new CircumcenterCoordinate();
		double x0 = splitPoint.getX();
		double y0 = splitPoint.getY();
		double A = point2.getY() - point1.getY();
		double B = point1.getX() - point2.getX();
		double C = point1.getY() * (point2.getX() - point1.getX()) - point1.getX() * (point2.getY() - point1.getY());
		cc.setX((B * B * x0 - A * B * y0 - A * C) / (A * A + B * B));
		cc.setY((-A * B * x0 + A * A * y0 - B * C) / (A * A + B * B));
		return cc;
	}

	/**
	 * 获得有序的边界点集合，并添加到矿石区域集合的最后一位
	 * 
	 * @param borderPointList
	 * @return
	 */
	private List<CircumcenterCoordinate> getBorderListCopy(List<CircumcenterCoordinate> borderPointList,
			List<CircumcenterCoordinate> list) {
		List<CircumcenterCoordinate> borderListCopy = new ArrayList<CircumcenterCoordinate>();
		List<CircumcenterCoordinate> borderListCopy2 = new ArrayList<CircumcenterCoordinate>();
		for (int j = 0; j < borderPointList.size(); j++) {
			borderListCopy.add(j, borderPointList.get(j));
			borderListCopy.get(j).setCircle(0);// 表示未循环过
		}
		int mark = 0;
		borderListCopy.get(0).setCircle(1);// 状态改为已经循环过
		borderListCopy2.add(borderListCopy.get(0));
		int count = borderListCopy.size() - 1;
		while (count > 0) {
			boolean flag = false;
			if (count == 1) {// 最后一个点得不到距离最近的点，直接加入集合
				for (CircumcenterCoordinate circumcenterCoordinate : borderListCopy) {
					if (circumcenterCoordinate.getCircle() == 0) {
						if (length(borderListCopy.get(mark).getX(), borderListCopy.get(mark).getY(),
								circumcenterCoordinate.getX(), circumcenterCoordinate.getY()) < 15)
							borderListCopy2.add(circumcenterCoordinate);
						borderListCopy2.add(borderListCopy2.get(0));// 最后一个点要与第一个点相同才能形成闭合多边形
						flag = true;
						break;
					}
				}
			}
			if (flag)
				break;
			LengthInfo t = new LengthInfo();
			boolean ff = true;
			List<CircumcenterCoordinate> temp = new ArrayList<CircumcenterCoordinate>();
			for (int j = 0; j < borderListCopy.size(); j++) {
				if (borderListCopy.get(j).getCircle() != 1)
					temp.add(borderListCopy.get(j));
			}
			MyPoint aa = new MyPoint();
			CircumcenterCoordinate cc2 = borderListCopy.get(mark);
			aa.x = cc2.getX();
			aa.y = cc2.getY();
			while (ff) {
				// 得到距离最近的点
				t = getMinLengthPoint1(temp, cc2);
				CircumcenterCoordinate cc = temp.get(t.getI());
				// 判断距离最近的点是否符合要求
				MyPoint bb = new MyPoint();
				bb.x = cc.getX();
				bb.y = cc.getY();
				for (int j = 0; j < list.size(); j++) {
					MyPoint ccc = new MyPoint();
					ccc.x = list.get(j).getX();
					ccc.y = list.get(j).getY();
					for (int j2 = j + 1; j2 < list.size(); j2++) {
						MyPoint dd = new MyPoint();
						dd.x = list.get(j2).getX();
						dd.y = list.get(j2).getY();
						ff = isIntersection(aa, bb, ccc, dd);
						if (ff) {
							temp.remove(cc);
							break;
						}
					}
					if (ff)
						break;// ff为true，说明线段相交，直接跳出循环
				}
				if (!ff) {
					// 将下一点标识为已循环并加入到矿石区域集合中
					for (int j = 0; j < borderListCopy.size(); j++) {
						if (borderListCopy.get(j).getX() == temp.get(t.getI()).getX()
								&& borderListCopy.get(j).getY() == temp.get(t.getI()).getY()) {
							borderListCopy.get(j).setCircle(1);
							borderListCopy2.add(borderListCopy.get(j));
							System.out.println(
									borderListCopy.get(j).getX() + "," + borderListCopy.get(j).getY() + "," + 0.0);
							// 改变需要判断的点的位置，继续循环
							mark = j;
							break;
						}
					}
					break;
				}
			}
			// 重新计算未被循环的点的个数
			count = 0;
			for (CircumcenterCoordinate circumcenterCoordinate : borderListCopy) {
				if (circumcenterCoordinate.getCircle() == 0)
					count++;
			}
		}
		return borderListCopy2;
	}

	/**
	 * 根据多边形顶点计算多边形的面积
	 * 
	 * @param vertex
	 * @param pointNum
	 * @return
	 */
	public double caculate(MyPoint vertex[], int pointNum) {
		int i = 0;
		float temp = 0;
		for (; i < pointNum - 1; i++) {
			temp += (vertex[i].x - vertex[i + 1].x) * (vertex[i].y + vertex[i + 1].y);
		}
		temp += (vertex[i].x - vertex[0].x) * (vertex[i].y + vertex[0].y);
		return temp / 2;
	}

	/**
	 * 计算闭合多边形的体积（高按15m计算）
	 * 
	 * @param list
	 * @return
	 */
	public String getData(List<CircumcenterCoordinate> list) {
		// 控制double小数点后四位
		DecimalFormat df = new DecimalFormat("0.00");
		MyPoint[] vertex = new MyPoint[list.size()];
		for (int i = 0; i < list.size(); i++) {
			vertex[i] = new MyPoint();
		}
		for (int i = 0; i < vertex.length; i++) {
			vertex[i].x = list.get(i).getX();
			vertex[i].y = list.get(i).getY();
		}
		double caculate = new DrawOreLine().caculate(vertex, list.size()) * 15;
		caculate = Math.abs(caculate);
		return df.format(caculate);
	}

	/**
	 * aa, bb为一条线段两端点 cc, dd为另一条线段的两端点 相交返回true, 不相交返回false
	 * 
	 * @param aa
	 * @param bb
	 * @param cc
	 * @param dd
	 * @return
	 */
	public boolean isIntersection(MyPoint aa, MyPoint bb, MyPoint cc, MyPoint dd) {
		// 如果cc与dd两点距离超过12m，则直接返回false
		if (length(cc.x, cc.y, dd.x, dd.y) > 12) {
			return false;
		}
		if (maxNum(aa.x, bb.x) < minNum(cc.x, dd.x)) {
			return false;
		}
		if (maxNum(aa.y, bb.y) < minNum(cc.y, dd.y)) {
			return false;
		}
		if (maxNum(cc.x, dd.x) < minNum(aa.x, bb.x)) {
			return false;
		}
		if (maxNum(cc.y, dd.y) < minNum(aa.y, bb.y)) {
			return false;
		}
		if (mult(cc, bb, aa) * mult(bb, dd, aa) < 0) {
			return false;
		}
		if (mult(aa, dd, cc) * mult(dd, bb, cc) < 0) {
			return false;
		}
		return true;
	}

	public double mult(MyPoint a, MyPoint b, MyPoint c) {
		return (a.x - c.x) * (b.y - c.y) - (b.x - c.x) * (a.y - c.y);
	}
}
