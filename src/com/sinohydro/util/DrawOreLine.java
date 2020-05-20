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
 * ���������Լ�Ʒλ��ϢȦ����ʯ����
 * 
 * @author MY
 *
 */
public class DrawOreLine {
	private List<CircumcenterCoordinate> returnList = new ArrayList<CircumcenterCoordinate>();
	private List<List<CircumcenterCoordinate>> finalReturnList = new ArrayList<List<CircumcenterCoordinate>>();

	/**
	 * �����������
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
	 * ��list���ϰ���x�����������ԭ���������ð��������ͬ
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
	 * ͨ�������������Ϣ�õ����������п�ʯ����߽��ļ���
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
			// ���߽����ָ���ֱ���벻ͬ�ļ���
			for (CircumcenterCoordinate circumcenterCoordinate : pointByMiddle) {
				if (circumcenterCoordinate.isFlag()) {
					middlePointList.add(circumcenterCoordinate);
				} else {
					borderPointList.add(circumcenterCoordinate);
				}
			}
		} else {
			// �߽���ø�����������Ϣ
			borderPointList = givedBorderList;
			for (CircumcenterCoordinate circumcenterCoordinate : pointByMiddle) {
				if (circumcenterCoordinate.isFlag()) {
					middlePointList.add(circumcenterCoordinate);
				}
			}
		}
		// �õ����������п�ʯ��ļ���
		List<CircumcenterCoordinate> orePoints = new ArrayList<CircumcenterCoordinate>();
		for (CircumcenterCoordinate circumcenterCoordinate : list) {
			if (circumcenterCoordinate.getCu() >= 0.12) {
				orePoints.add(circumcenterCoordinate);
			}
		}
		// �Էָ������ѭ������������߽����С��3.5m�ĵ�ɸѡ����
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
		// middlePointList��û�б�ѭ���ĵ�ĸ���
		int count = 0;
		// ��middlePointListForLittle�ĵ���Ϊ�ڶ����㣬������Ϊ��һ���㣬��middlePointList��ѭ��Ѱ��û��ѭ�����������
		for (int i = 0; i < middlePointListForLittle.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < middlePointList.size(); j++) {// ��middlePointList��Ѱ�ҵ�middlePointListForLittle�ĵ㣬����ʶ��Ϊѭ����
				if (middlePointList.get(j).getX() == middlePointListForLittle.get(i).getX()
						&& middlePointList.get(j).getY() == middlePointListForLittle.get(i).getY()) {
					if (middlePointList.get(j).getCircle() == 1) {// �ж�������Ƿ�ѭ���������ѭ��������˵㲻��ѭ����
						flag = true;
					} else {// �˵�û��ѭ��������ʼ�����¼��ϣ��������������п�ʯ����뵽������
							// ���˵��ʶΪ�Ѿ�ѭ����
						middlePointList.get(j).setCircle(1);
						List<CircumcenterCoordinate> oneOreList = new ArrayList<CircumcenterCoordinate>();
						// ����������ڼ��ϵ���λ,�˵���ڵڶ�λ
						oneOreList.add(getMinLengthFromBorder(middlePointList.get(j), borderPointList));
						oneOreList.add(middlePointList.get(j));
						LengthInfo temp = getMinLengthPoint(middlePointList, j);
						// ����һ���ʶΪ��ѭ�������뵽��ʯ���򼯺���
						middlePointList.get(temp.getI()).setCircle(1);
						oneOreList.add(middlePointList.get(temp.getI()));
						// ��middlePointList.get(temp.getI())�����Ϊ���������������жϣ�ֱ��ѭ������߽�С��3.5m�ĵ����������ʯ���򼯺ϼ��뵽Ҫ���صļ�����
						int mark = temp.getI();
						count = 0;
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0)
								count++;
						}
						while (count > 0) {// ֻҪ�е�û�б�ѭ������һֱѭ����ȥ�����whileѭ��������ֻ��һ�ֿ��ܣ�����ѭ������ѭ�����˾���߽�С��3.5m�ĵ�
							boolean breakOrNot = false;
							for (int k = 0; k < middlePointList.size(); k++) {
								if (count == 0)
									break;
								// �õ���������ĵ�
								LengthInfo t = getMinLengthPoint(middlePointList, mark);
								// �õ����Ժ��жϾ���
								double length = t.getLength();
								if (length > 5) {
									boolean f = true;
									// �жϴ˵��Ƿ�Ϊ����߽�С��3.5m�ĵ�
									for (int l = 0; l < middlePointListForLittle.size(); l++) {
										if ((middlePointList.get(mark).getX() == middlePointListForLittle.get(l).getX()
												&& middlePointList.get(mark).getY() == middlePointListForLittle.get(l)
														.getY())
												|| length > 9) {// �ж�length>9�Ǵ������߽�С��3.5m�ĵ����ɲ���ȫ�����
											f = false;
											breakOrNot = true;// ���ڽ���whileѭ��
											// �Ǿ���߽�С��3.5m�ĵ㣬���ɴ��㣬�����뼯�ϡ��ٽ��������ϼ��뵽��Ҫ���صļ��ϡ�����ѭ��
											CircumcenterCoordinate minLengthFromBorder = getMinLengthFromBorder(
													middlePointList.get(mark), borderPointList);
											oneOreList.add(minLengthFromBorder);
											// ��Ҫ���߽��ϵĵ���뼯�ϣ��γɱպ϶���Σ��˴��Ĳ���Ӧ�������еĿ�ʯ���򼯺϶��γɺ�������

											finalReturnList.add(oneOreList);
											break;
										}
									}
									if (f) {// ˵��û����������߽�С��3.5m�ĵ㣬��Ҫ����ѭ��
										middlePointList.get(t.getI()).setCircle(1);
										oneOreList.add(middlePointList.get(t.getI()));
										// �ı���Ҫ�жϵĵ��λ�ã�����ѭ��
										mark = t.getI();
										// ���¼���δ��ѭ���ĵ�ĸ���
										count = 0;
										for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
											if (circumcenterCoordinate.getCircle() == 0)
												count++;
										}
									}
								} else {
									// ����һ���ʶΪ��ѭ�������뵽��ʯ���򼯺���
									middlePointList.get(t.getI()).setCircle(1);
									oneOreList.add(middlePointList.get(t.getI()));
									// �ı���Ҫ�жϵĵ��λ�ã�����ѭ��
									mark = t.getI();
									// ���¼���δ��ѭ���ĵ�ĸ���
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
								if (breakOrNot)// ˵��ѭ�����˾���߽�С��3.5m�ĵ㣬����forѭ��
									break;
							}
							if (breakOrNot)// ˵��ѭ�����˾���߽�С��3.5m�ĵ㣬ǿ������whileѭ��
								break;
						}

					}
					// ֻҪ�����˴�if���棬ִ��������Ĺ��̣����forѭ���Ϳ��Խ������Խ�ʡ����
					break;
				}
			}
			if (flag)
				continue;
		}
		// �ٴ�����Ҫ�߽���������γɱպ϶���ε����
		// ���¼���δ��ѭ���ĵ�ĸ���
		count = 0;
		for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
			if (circumcenterCoordinate.getCircle() == 0)
				count++;
		}
		int mark = 0;
		for (int i = 0; i < middlePointList.size(); i++) {
			List<CircumcenterCoordinate> oneOreList = new ArrayList<CircumcenterCoordinate>();// �����´�ſ�ʯ�����ļ���
			if (middlePointList.get(i).getCircle() == 0) {// û�б�ѭ���ĵ�
				middlePointList.get(i).setCircle(1);// ״̬��Ϊ�Ѿ�ѭ����
				oneOreList.add(middlePointList.get(i));
				mark = i;
				while (count > 0) {
					boolean flag = false;
					if (count == 1) {// ���һ����ò�����������ĵ㣬ֱ�Ӽ��뼯��
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0) {
								oneOreList.add(circumcenterCoordinate);
								circumcenterCoordinate.setCircle(1);
								oneOreList.add(oneOreList.get(0));// ���һ����Ҫ���һ������ͬ�����γɱպ϶����
								finalReturnList.add(oneOreList);
								flag = true;
								break;
							}
						}
					}
					if (flag)
						break;
					// �õ���������ĵ�
					LengthInfo t = getMinLengthPoint(middlePointList, mark);
					CircumcenterCoordinate cc = middlePointList.get(t.getI());
					// �ж�������Ƿ�Ϊ�����еĵ�һ���㣬����ǣ�����ѭ����������ǣ����뵽�����У��ı����������ѭ��
					if (cc.getX() == oneOreList.get(0).getX() && cc.getY() == oneOreList.get(0).getY()) {// ˵���Ǽ����е�һ����
						// �����ϼ��뵽Ҫ���صļ���
						// Ϊ�˵õ��պ϶���Σ����һ�������һ����Ҫһ��
						oneOreList.add(cc);
						finalReturnList.add(oneOreList);
						break;
					} else if (t.getLength() > 6) {
						oneOreList.add(oneOreList.get(0));
						finalReturnList.add(oneOreList);
						break;
					} else {
						// ����һ���ʶΪ��ѭ�������뵽��ʯ���򼯺���
						middlePointList.get(t.getI()).setCircle(1);
						oneOreList.add(middlePointList.get(t.getI()));
						// �ı���Ҫ�жϵĵ��λ�ã�����ѭ��
						mark = t.getI();
						// ���¼���δ��ѭ���ĵ�ĸ���
						count = 0;
						for (CircumcenterCoordinate circumcenterCoordinate : middlePointList) {
							if (circumcenterCoordinate.getCircle() == 0)
								count++;
						}
					}
				}
			}
		}

		// �ڴ˴�����߽�����ʯ���򼯺ϵĹ�ϵ
		/*
		 * ˼·�� 1.ѡ�����еĴ�����γ��µļ���
		 * 2.��������㼯�ϣ���ĳ������㣬�����ж��ı��ǿ�ʯ�����жϷ����һ���������ı߽������ϣ�ֱ����������㣬����������㲻�������жϵĴ���
		 * �����ڵļ��ϣ��������������ڼ��ϵ����е㰴˳����ӽ��׸�����㼯�ϣ����������㼯�������һ������㿪ʼ֮ǰ��ѭ����ֱ�������׸����������
		 * ���ϵ���һ����������ѭ��
		 */
		// ѡ�����еĴ����
		List<CircumcenterCoordinate> minLengthPoint = new ArrayList<CircumcenterCoordinate>();
		for (int i = 0; i < finalReturnList.size(); i++) {
			for (int j = 0; j < finalReturnList.get(i).size(); j++) {
				if (finalReturnList.get(i).get(j).getCircle() == 3) {
					finalReturnList.get(i).get(j).setNumber(i);// ��¼�˴������ڼ�����finalReturnList�е�λ��
					minLengthPoint.add(finalReturnList.get(i).get(j));
				}
			}
		}
		// ���������뵽�߽�㼯����
		borderPointList.addAll(minLengthPoint);
		// ��¼borderPointList��ǰѭ����λ�ã�
		int markPoint = 0;
		// ����߽�㼯����δ��ѭ���Ĵ����ĸ���
		int countPoint = 0;
		for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
			if (circumcenterCoordinate.getCircle() == 3)
				countPoint++;
		}
		while (countPoint > 0) {
			// �Դ������б�������
			for (int i = 0; i < borderPointList.size(); i++) {
				boolean flag = false;
				// �ж��ǲ��Ǵ���㣬����ֱ������.���ﲻ����3���ж����Ƿ�ѭ����������Ͳ���Ҫ���ж�����Ǵ�����Ƿ�ѭ�������������0��˵�����Ǵ���㣬����1˵���Ѿ�ѭ����
				if (borderPointList.get(i).getCircle() != 3)
					continue;
				// �жϴ�����ǲ��Ǽ����е����һ��Ԫ�أ�����ֱ������
				CircumcenterCoordinate circumcenterCoordinate2 = finalReturnList.get(borderPointList.get(i).getNumber())
						.get(finalReturnList.get(borderPointList.get(i).getNumber()).size() - 1);
				if (circumcenterCoordinate2 != borderPointList.get(i))
					continue;
				// �Ĵ����ѭ��״̬
				borderPointList.get(i).setCircle(1);
				// �õ��봹������Ŀ�ʯ��
				CircumcenterCoordinate minLengthOrePoint = getMinLengthOrePoint(borderPointList.get(i), orePoints);
				// �õ��봹���������ı߽�㼯��
				List<CircumcenterCoordinate> borderList = getBorderList(borderPointList.get(i), borderPointList);
				// �жϷ��򣬽�����Ҫ���뼯�ϵı߽���봹����Լ�����Ŀ�ʯ�����㣬�Դ����Ϊ����Ľ�����Ǿ�����Ҫ��
				CircumcenterCoordinate temp = new CircumcenterCoordinate();
				CircumcenterCoordinate cc = new CircumcenterCoordinate();
				for (int j = 0; j < borderList.size(); j++) {
					boolean b = isSmallAngelOrNotForBorder(minLengthOrePoint, borderPointList.get(i),
							borderList.get(j));
					if (b) {// ����Ҫ�󣬽�����뵽��ʯ���򼯺���
						finalReturnList.get(borderPointList.get(i).getNumber()).add(borderList.get(j));
						// ѭ������״̬�ĵ�
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
				// ��ӵڶ�����
				// ��borderPointList��Ѱ����һ�������
				List<CircumcenterCoordinate> borderPointListCopy = new ArrayList<CircumcenterCoordinate>();
				for (int j = 0; j < borderPointList.size(); j++) {
					borderPointListCopy.add(j, borderPointList.get(j));
				}
				LengthInfo info = new LengthInfo();
				while (true) {
					info = getMinLengthPoint(borderPointListCopy, markPoint);
					cc = borderPointList.get(info.getI());
					// �ж��������㣬�׸�����㲻����temp��cc�м�
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
					// ��borderPointList��Ѱ����һ�������
					if (info == null) {
						info = getMinLengthPoint(borderPointList, markPoint);
					}
					cc = borderPointList.get(info.getI());
					// �ж����������Ƿ�Ϊ���㣬
					if (cc.getCircle() == 3) {
						// �Ǵ��㣬��Ҫ�ж����ĸ����ϵĴ���
						if (cc.getNumber() == borderPointList.get(i).getNumber()) {// �ǵ�ǰѭ������ļ��ϵĴ��㣬��ѭ������
							// Ϊ�˵õ��պϵĶ���Σ�Ҫ���˵����
							finalReturnList.get(borderPointList.get(i).getNumber()).add(cc);
							// �ı�״̬
							borderPointList.get(info.getI()).setCircle(1);
							// ���¼���߽�㼯����δ��ѭ���Ĵ����ĸ���
							countPoint = 0;
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (circumcenterCoordinate.getCircle() == 3)
									countPoint++;
							}
							// ����ѭ��
							break;
						} else {// ���������ϵĴ��㣬��ʼ�µ�ѭ��
							// �ж���������ǲ������ڼ��ϵĵ�һ��Ԫ�أ�������ǣ���ת�˼���
							if (!(finalReturnList.get(cc.getNumber()).get(0) == cc)) {
								Collections.reverse(finalReturnList.get(cc.getNumber()));
							}
							// �������е�һ�������״̬��borderPointList�и�Ϊ1
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (finalReturnList.get(cc.getNumber()).get(0).getX() == circumcenterCoordinate.getX()
										&& finalReturnList.get(cc.getNumber()).get(0).getY() == circumcenterCoordinate
												.getY()) {
									circumcenterCoordinate.setCircle(1);
								}
							}
							// ���¼���߽�㼯����δ��ѭ���Ĵ����ĸ���
							countPoint = 0;
							for (CircumcenterCoordinate circumcenterCoordinate : borderPointList) {
								if (circumcenterCoordinate.getCircle() == 3)
									countPoint++;
							}
							// ���˼���ȫ��������뵱ǰѭ���������ڵļ���
							finalReturnList.get(borderPointList.get(i).getNumber())
									.addAll(finalReturnList.get(cc.getNumber()));
							// ���������һ��Ԫ�ص�numberֵ��������
							finalReturnList.get(borderPointList.get(i).getNumber())
									.get(finalReturnList.get(borderPointList.get(i).getNumber()).size() - 1)
									.setNumber(borderPointList.get(i).getNumber());
							// ��finalReturnList���˼���λ����Ϊnull�������������ʱ���ظ�
							finalReturnList.set(cc.getNumber(), null);
							flag = true;
							break;
						}
					} else {// ���Ǵ���
						finalReturnList.get(borderPointList.get(i).getNumber()).add(cc);// ���뼯��
						borderPointList.get(info.getI()).setCircle(1);// �ı�״̬
						markPoint = info.getI();// ���¶�λѭ��λ��
					}
					info = null;
				}

				if (flag)
					break;
			}
		}
		if (givedBorderList!=null && borderPointList.size() == givedBorderList.size()
				&& borderPointList.get(1).getX() == givedBorderList.get(1).getX()) {// ˵���߽�Ϊ���������򼯺�
			finalReturnList.add(borderPointList);
		} else {
			finalReturnList.add(getBorderListCopy(borderPointList, list));
		}
		return finalReturnList;
	}

	/**
	 * �õ��봹����������ı߽�㼯��
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
		// ����
		for (int i = 0; i < temp.size() - 1; i++) {
			for (int j = 0; j < temp.size() - i - 1; j++) {
				if (temp.get(j).getLength() > temp.get(j + 1).getLength()) {
					LengthInfo li = temp.get(j);
					temp.set(j, temp.get(j + 1));
					temp.set(j + 1, li);
				}
			}
		}
		// ��borderList�︳ֵ
		for (int i = 0; i < temp.size(); i++) {
			borderList.add(borderPointList.get(temp.get(i).getI()));
		}
		return borderList;
	}

	/**
	 * �õ��봹�������Ŀ�ʯ��
	 * 
	 * @param circumcenterCoordinate
	 *            �����
	 * @param orePoints
	 *            �����п�ʯ��ļ���
	 */
	private CircumcenterCoordinate getMinLengthOrePoint(CircumcenterCoordinate circumcenterCoordinate,
			List<CircumcenterCoordinate> orePoints) {
		// ��middlePointListѰ���������뵽������
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < orePoints.size(); k++) {
			LengthInfo lengthInfo = new LengthInfo();
			lengthInfo.setI(k);
			lengthInfo.setLength(length(circumcenterCoordinate.getX(), circumcenterCoordinate.getY(),
					orePoints.get(k).getX(), orePoints.get(k).getY()));
			lengthList.add(lengthInfo);
		}
		// �õ�������Сֵ�ĵ�
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return orePoints.get(temp.getI());
	}

	private LengthInfo getMinLengthPoint(List<CircumcenterCoordinate> middlePointList, int i) {
		// ��middlePointListѰ���������뵽������
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < middlePointList.size(); k++) {
			// ���ѭ��������Ϊ��ֵ��ֱ������
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
		// �õ�������Сֵ�ĵ�
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return temp;
	}

	private LengthInfo getMinLengthPoint1(List<CircumcenterCoordinate> middlePointList, CircumcenterCoordinate cc) {
		// ��middlePointListѰ���������뵽������
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int k = 0; k < middlePointList.size(); k++) {
			// ���ѭ��������Ϊ��ֵ��ֱ������
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
		// �õ�������Сֵ�ĵ�
		LengthInfo temp = lengthList.get(0);
		for (int k = 0; k < lengthList.size() - 1; k++) {
			if (temp.getLength() > lengthList.get(k + 1).getLength()) {
				temp = lengthList.get(k + 1);
			}
		}
		return temp;
	}

	/**
	 * �����е��������еķָ����Լ��߽��ҲҪ���ɳ����ļ���
	 * 
	 * @param list
	 * @param returnList
	 * @return
	 */
	private List<CircumcenterCoordinate> getPointByMiddle(List<CircumcenterCoordinate> list) {
		taxis(list);// �Լ��ϰ�x������д�С��������
		// �Լ��������п׽���ѭ�����ڿ��ʯ֮�����ɷָ���
		for (int i = 0; i < list.size(); i++) {
			// ����˵����С��11�����е����ɷָ���
			for (int j = i + 1; j < list.size(); j++) {
				double l = length(list.get(i).getX(), list.get(i).getY(), list.get(j).getX(), list.get(j).getY());
				if (l < 11) {
					// �ж��費��Ҫ���ɷָ��㣬���ж��������Ʒλ�ǲ���һ��һ��
					if ((list.get(i).getCu() >= 0.12 && list.get(j).getCu() < 0.12)
							|| (list.get(i).getCu() < 0.12 && list.get(j).getCu() >= 0.12)) {
						CircumcenterCoordinate coordinate = new CircumcenterCoordinate();
						// ����doubleС�������λ
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
		// �������߽��Ҳ���뵽���ص�list������
		getBorderPoint(list, returnList);
		return returnList;

	}

	/**
	 * ���߷��������һ�����������ʺ���ԭ������꣬�������������С��x���꣬��С��y���굱��ԭ������
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
	 * �õ������߽�����е�
	 * 
	 * @param list
	 * @param returnList
	 * @return
	 */
	private void getBorderPoint(List<CircumcenterCoordinate> list, List<CircumcenterCoordinate> returnList) {
		int length = 11;// ���ݴ˾������ж������Ƿ��е㣬һ���ڿ׼�඼С�ڴ�ֵ
		double offset = 4;
		// �Լ��������п׽���ѭ�����ж��Ƿ�Ϊ�߽��
		for (int i = 0; i < list.size(); i++) {
			double x = list.get(i).getX();
			double y = list.get(i).getY();
			boolean flag1 = true;// �жϵ�һ�����Ƿ��н���
			boolean flag2 = true;// �жϵڶ������Ƿ��н���
			boolean flag3 = true;// �жϵ��������Ƿ��н���
			boolean flag4 = true;// �жϵ��������Ƿ��н���
			List<Point2D.Double> polygon1 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon2 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon3 = new ArrayList<Point2D.Double>();
			List<Point2D.Double> polygon4 = new ArrayList<Point2D.Double>();
			// ��һ����
			Point2D.Double o1 = new Point2D.Double(x, y);
			Point2D.Double o2 = new Point2D.Double(x + length, y);
			Point2D.Double o3 = new Point2D.Double(x + length, y + length);
			Point2D.Double o4 = new Point2D.Double(x, y + length);
			polygon1.add(o1);
			polygon1.add(o2);
			polygon1.add(o3);
			polygon1.add(o4);
			// �ڶ�����
			Point2D.Double t1 = new Point2D.Double(x, y);
			Point2D.Double t2 = new Point2D.Double(x, y + length);
			Point2D.Double t3 = new Point2D.Double(x - length, y + length);
			Point2D.Double t4 = new Point2D.Double(x - length, y);
			polygon2.add(t1);
			polygon2.add(t2);
			polygon2.add(t3);
			polygon2.add(t4);
			// ��������
			Point2D.Double s1 = new Point2D.Double(x, y);
			Point2D.Double s2 = new Point2D.Double(x - length, y);
			Point2D.Double s3 = new Point2D.Double(x - length, y - length);
			Point2D.Double s4 = new Point2D.Double(x, y - length);
			polygon3.add(s1);
			polygon3.add(s2);
			polygon3.add(s3);
			polygon3.add(s4);
			// ��������
			Point2D.Double f1 = new Point2D.Double(x, y);
			Point2D.Double f2 = new Point2D.Double(x + length, y);
			Point2D.Double f3 = new Point2D.Double(x + length, y - length);
			Point2D.Double f4 = new Point2D.Double(x, y - length);
			polygon4.add(f1);
			polygon4.add(f2);
			polygon4.add(f3);
			polygon4.add(f4);
			// �жϵ�һ����
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon1)) {// ֻҪ��һ���㴦�������ڣ��Ϳ��ж���������н��㣬ֱ������ѭ��
					flag1 = false;
					break;
				}
			}
			// �жϵڶ�����
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon2)) {// ֻҪ��һ���㴦�������ڣ��Ϳ��ж���������н��㣬ֱ������ѭ��
					flag2 = false;
					break;
				}
			}
			// �жϵ�������
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon3)) {// ֻҪ��һ���㴦�������ڣ��Ϳ��ж���������н��㣬ֱ������ѭ��
					flag3 = false;
					break;
				}
			}
			// �жϵ�������
			for (int j = 0; j < list.size(); j++) {
				if (j == i)
					continue;
				Point2D.Double point = new Point2D.Double(list.get(j).getX(), list.get(j).getY());
				if (IsPtInPoly(point, polygon4)) {// ֻҪ��һ���㴦�������ڣ��Ϳ��ж���������н��㣬ֱ������ѭ��
					flag4 = false;
					break;
				}
			}
			// �����ж��ĸ����޷ֱ���û�е�Ľ����ȷ���Ƿ��ڴ˵㸽�����ɱ߽��,�ж���������
			// ��һ���޻��������û�н��㣬��x�����3.5���ڶ����޻��������û�н��㣬��x����-3.5
			// ��һ��������û�н��㣬y����+3.5��������������û�н��㣬y����-3.5���ڶ���������û�н��㣬x����-3.5����һ��������û�н��㣬x����+3.5
			// ��һ��������û�н�����ߵڶ���������û�н��㣬���������߽��x����+3.5��x����-3.5
			// ����������޶�û�н��㣬�������ĸ��߽�㣬x+3.5��x-3.5��y+3.5��y-3.5�����������Ժ���Ҫ���ж�ȥ����ָ�d�Ͻ��ı߽��
			if ((flag1 == true && flag2 == false && flag3 == false && flag4 == false)
					|| (flag1 == false && flag2 == false && flag3 == false && flag4 == true)
					|| ((flag1 == true && flag2 == false && flag3 == false && flag4 == true))) {// ��һ���޻��������û�н���,��һ��������û�н���
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setX(list.get(i).getX() + offset);
				cc.setY(list.get(i).getY());
				cc.setFlag(false);// �߽��

				returnList.add(cc);
			} else if ((flag1 == false && flag2 == true && flag3 == false && flag4 == false)
					|| (flag1 == false && flag2 == false && flag3 == true && flag4 == false)
					|| (flag1 == false && flag2 == true && flag3 == true && flag4 == false)) {// �ڶ����޻��������û�н���,�ڶ���������û�н���
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setX(list.get(i).getX() - offset);
				cc.setY(list.get(i).getY());
				cc.setFlag(false);// �߽��

				returnList.add(cc);
			} else if (flag1 == true && flag2 == true && flag3 == false && flag4 == false) {// ��һ��������û�н��㣬y����+offset
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setY(list.get(i).getY() + offset);
				cc.setX(list.get(i).getX());
				cc.setFlag(false);// �߽��

				returnList.add(cc);
			} else if (flag1 == false && flag2 == false && flag3 == true && flag4 == true) {// ������������û�н��㣬y����-offset
				CircumcenterCoordinate cc = new CircumcenterCoordinate();

				cc.setY(list.get(i).getY() - offset);
				cc.setX(list.get(i).getX());
				cc.setFlag(false);// �߽��

				returnList.add(cc);
			} else if ((flag1 == true && flag2 == false && flag3 == true && flag4 == false)
					|| (flag1 == false && flag2 == true && flag3 == false && flag4 == true)) {// ��һ��������û�н�����ߵڶ���������û�н���
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// �߽��

				c2.setX(list.get(i).getX() - offset);
				c2.setY(list.get(i).getY());
				c2.setFlag(false);// �߽��

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == false && flag2 == true && flag3 == true && flag4 == true) {// ���ֻ��һ�����н��㣬���������߽�㣬x-offset��y-offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() - offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// �߽��

				c2.setY(list.get(i).getY() - offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// �߽��

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == false && flag3 == true && flag4 == true) {// ���ֻ�ڶ������н��㣬�����������߽�㣬��x+offset��y-offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// �߽��

				c2.setY(list.get(i).getY() - offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// �߽��

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == true && flag3 == false && flag4 == true) {// ���ֻ���������н��㣬�����������߽�㣬x+offset��y+offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() + offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// �߽��

				c2.setY(list.get(i).getY() + offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// �߽��

				returnList.add(c1);
				returnList.add(c2);
			} else if (flag1 == true && flag2 == true && flag3 == true && flag4 == false) {// ���ֻ���������н��㣬�����������߽�㣬x-offset��y+offset
				CircumcenterCoordinate c1 = new CircumcenterCoordinate();
				CircumcenterCoordinate c2 = new CircumcenterCoordinate();

				c1.setX(list.get(i).getX() - offset);
				c1.setY(list.get(i).getY());
				c1.setFlag(false);// �߽��

				c2.setY(list.get(i).getY() + offset);
				c2.setX(list.get(i).getX());
				c2.setFlag(false);// �߽��

				returnList.add(c1);
				returnList.add(c2);
			}
		}
	}

	/**
	 * �жϵ��Ƿ��ڶ�����ڣ������λ�ڶ���εĶ������ϣ�Ҳ�������ڶ�����ڣ�ֱ�ӷ���true
	 * 
	 * @param point
	 *            ����
	 * @param pts
	 *            ����εĶ���
	 * @return ���ڶ�����ڷ���true,���򷵻�false
	 */
	private static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts) {

		int N = pts.size();
		boolean boundOrVertex = true; // �����λ�ڶ���εĶ������ϣ�Ҳ�������ڶ�����ڣ�ֱ�ӷ���true
		int intersectCount = 0;// cross points count of x
		double precision = 2e-10; // �������ͼ���ʱ����0�Ƚ�ʱ����ݲ�
		Point2D.Double p1, p2;// neighbour bound vertices
		Point2D.Double p = point; // ��ǰ��

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

		if (intersectCount % 2 == 0) {// ż���ڶ������
			return false;
		} else { // �����ڶ������
			return true;
		}
	}

	/**
	 * �õ�ĳ���ָ�����߽�����ĵ������,������
	 * 
	 * @param splitPoint
	 *            �ָ�������
	 * @param borderList
	 *            �߽������
	 * @return ������������
	 */
	private CircumcenterCoordinate getMinLengthFromBorder(CircumcenterCoordinate splitPoint,
			List<CircumcenterCoordinate> borderList) {
		CircumcenterCoordinate cc = new CircumcenterCoordinate();
		// �����splitPoint�ľ��룬���뵽������
		List<LengthInfo> lengthList = new ArrayList<LengthInfo>();
		for (int i = 0; i < borderList.size(); i++) {
			LengthInfo lengthInfo = new LengthInfo();
			lengthInfo.setI(i);
			lengthInfo.setLength(
					length(splitPoint.getX(), splitPoint.getY(), borderList.get(i).getX(), borderList.get(i).getY()));
			lengthList.add(lengthInfo);
		}
		// ����
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
		 * // ����С�ľ��뿪ʼѡ�������� boolean flagForLength = false; for (int i = 0; i <
		 * lengthList.size() - 2; i++) { for (int j = i + 1; j < i + 3; j++) {
		 * CircumcenterCoordinate point1 = borderList.get(lengthList.get(i).getI());
		 * CircumcenterCoordinate point2 = borderList.get(lengthList.get(j).getI());
		 * boolean smallAngelOrNot = isSmallAngelOrNot(splitPoint, point1, point2); if
		 * (smallAngelOrNot) { // ����Ҫ�󣬾�����splitPoint���������ϵĴ��㣬������ cc =
		 * getMinLengthCoordinate(splitPoint, point1, point2); // ���ɵĴ�������ڱ߽����� boolean
		 * ifInLine = ifInLine(cc, borderList); if(ifInLine) {//�ڱ߽����ϣ�����ѭ�� flagForLength
		 * = true; break; } } } if (flagForLength) break; }
		 */
		// ��һ��Ͷ���ķ�����ֱ��ѡ����С���ĸ�����ϣ�һ�㴹��ض������ĸ����е�������ɵ�ֱ����
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
	 * �ж�ѡ��������Ϊ����Ľ��ǲ�����ǣ�����ǣ�˵��������������Ҫ��ĵ�
	 * 
	 * @param splitPoint
	 *            �ָ���
	 * @param point1
	 *            �߽��1
	 * @param point2
	 *            �߽��2
	 * @return �������Ҫ�󣬷���true������Ϊfalse
	 */
	private boolean isSmallAngelOrNot(CircumcenterCoordinate splitPoint, CircumcenterCoordinate point1,
			CircumcenterCoordinate point2) {
		// ���������ɵ�����������
		double splitPointL = length(point1.getX(), point1.getY(), point2.getX(), point2.getY());
		double point1L = length(splitPoint.getX(), splitPoint.getY(), point2.getX(), point2.getY());
		double point2L = length(splitPoint.getX(), splitPoint.getY(), point1.getX(), point1.getY());
		// Ҫ��֤��splitPoint�Ĵ�����point1��point2�γɵ����ϣ��������ӳ����ϣ�Ҫ��֤��point1��point2���γɵĽ�Ϊ��ǣ�������ֵ��(0��1]֮��
		// point1Ϊ����Ľǵ�����ֵ
		double point1A = (splitPointL * splitPointL + point2L * point2L - point1L * point1L)
				/ (2 * splitPointL * point2L);
		// point2Ϊ����Ľǵ�����ֵ
		double point2A = (splitPointL * splitPointL + point1L * point1L - point2L * point2L)
				/ (2 * splitPointL * point1L);
		// ���point1A��point2A����(0��1]֮�䣬�򷵻�true������Ϊfalse
		if (point1A >= 0 && point1A < 1 && point2A >= 0 && point2A < 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �жϱ߽��Ͽ�ʯ�����Ա���ӱ߽���γɱպ϶����
	 * 
	 * @param minLengthOrePoint
	 * @param minLengthPoint
	 * @param oneBorderPoint
	 * @return
	 */
	private boolean isSmallAngelOrNotForBorder(CircumcenterCoordinate minLengthOrePoint,
			CircumcenterCoordinate minLengthPoint, CircumcenterCoordinate oneBorderPoint) {
		// ���������ɵ�����������
		double minLengthOrePointL = length(minLengthPoint.getX(), minLengthPoint.getY(), oneBorderPoint.getX(),
				oneBorderPoint.getY());
		double minLengthPointL = length(minLengthOrePoint.getX(), minLengthOrePoint.getY(), oneBorderPoint.getX(),
				oneBorderPoint.getY());
		double oneBorderPointL = length(minLengthOrePoint.getX(), minLengthOrePoint.getY(), minLengthPoint.getX(),
				minLengthPoint.getY());
		// ������γɵĽ�Ϊ��ǣ�������ֵ��(0��1]֮��
		// point1Ϊ����Ľǵ�����ֵ
		double minLengthPointA = (minLengthOrePointL * minLengthOrePointL + oneBorderPointL * oneBorderPointL
				- minLengthPointL * minLengthPointL) / (2 * minLengthOrePointL * oneBorderPointL);
		if (minLengthPointA >= 0 && minLengthPointA < 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��������ֵ��Ϊ�����������ֵ
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
	 * ��������ֵ��Ϊ����������Сֵ
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
	 * ���ɷ�װ�д�������Ķ���
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
	 * �������ı߽�㼯�ϣ�����ӵ���ʯ���򼯺ϵ����һλ
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
			borderListCopy.get(j).setCircle(0);// ��ʾδѭ����
		}
		int mark = 0;
		borderListCopy.get(0).setCircle(1);// ״̬��Ϊ�Ѿ�ѭ����
		borderListCopy2.add(borderListCopy.get(0));
		int count = borderListCopy.size() - 1;
		while (count > 0) {
			boolean flag = false;
			if (count == 1) {// ���һ����ò�����������ĵ㣬ֱ�Ӽ��뼯��
				for (CircumcenterCoordinate circumcenterCoordinate : borderListCopy) {
					if (circumcenterCoordinate.getCircle() == 0) {
						if (length(borderListCopy.get(mark).getX(), borderListCopy.get(mark).getY(),
								circumcenterCoordinate.getX(), circumcenterCoordinate.getY()) < 15)
							borderListCopy2.add(circumcenterCoordinate);
						borderListCopy2.add(borderListCopy2.get(0));// ���һ����Ҫ���һ������ͬ�����γɱպ϶����
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
				// �õ���������ĵ�
				t = getMinLengthPoint1(temp, cc2);
				CircumcenterCoordinate cc = temp.get(t.getI());
				// �жϾ�������ĵ��Ƿ����Ҫ��
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
						break;// ffΪtrue��˵���߶��ཻ��ֱ������ѭ��
				}
				if (!ff) {
					// ����һ���ʶΪ��ѭ�������뵽��ʯ���򼯺���
					for (int j = 0; j < borderListCopy.size(); j++) {
						if (borderListCopy.get(j).getX() == temp.get(t.getI()).getX()
								&& borderListCopy.get(j).getY() == temp.get(t.getI()).getY()) {
							borderListCopy.get(j).setCircle(1);
							borderListCopy2.add(borderListCopy.get(j));
							System.out.println(
									borderListCopy.get(j).getX() + "," + borderListCopy.get(j).getY() + "," + 0.0);
							// �ı���Ҫ�жϵĵ��λ�ã�����ѭ��
							mark = j;
							break;
						}
					}
					break;
				}
			}
			// ���¼���δ��ѭ���ĵ�ĸ���
			count = 0;
			for (CircumcenterCoordinate circumcenterCoordinate : borderListCopy) {
				if (circumcenterCoordinate.getCircle() == 0)
					count++;
			}
		}
		return borderListCopy2;
	}

	/**
	 * ���ݶ���ζ���������ε����
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
	 * ����պ϶���ε�������߰�15m���㣩
	 * 
	 * @param list
	 * @return
	 */
	public String getData(List<CircumcenterCoordinate> list) {
		// ����doubleС�������λ
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
	 * aa, bbΪһ���߶����˵� cc, ddΪ��һ���߶ε����˵� �ཻ����true, ���ཻ����false
	 * 
	 * @param aa
	 * @param bb
	 * @param cc
	 * @param dd
	 * @return
	 */
	public boolean isIntersection(MyPoint aa, MyPoint bb, MyPoint cc, MyPoint dd) {
		// ���cc��dd������볬��12m����ֱ�ӷ���false
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
