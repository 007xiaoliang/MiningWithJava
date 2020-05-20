package com.sinohydro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sinohydro.domain.Ore;
import com.sinohydro.domain.Result;

public class Core {

	// ȡ��ǰ�û�����
	static File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
	public static String desktopPath = desktopDir.getAbsolutePath();
	public static String excelPath = desktopPath + File.separator + "�������Ʒ��Ϣ.xls";

	public static Result geologicTest(String filePath, double k) throws IOException {
		List<Ore> list = readExcel(filePath);
		Result r = calculateData(list, k);
		return r;

	}

	/**
	 * ���Ĵ��룬�����ݽ��з�����õ�����Ʒλ
	 * 
	 * @param list
	 * @param k
	 * @return
	 */
	private static Result calculateData(List<Ore> list, double k) {
		//��list��С��0.12�������޳�
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getOrePercent()<0.12) {
				list.remove(i);
			}
		}
		// ����doubleС�������λ
		DecimalFormat df = new DecimalFormat("0.0000");
		// ����һ�����飬�������ore�����е�Ʒλֵ�����鳤��Ϊlist���ϵĳ���
		double[] grade = new double[list.size()];
		int num = 0;// ����ѭ������
		double temp = 1.1;// ��ʱ����,Ϊ�˵�һ�ν���whileѭ�������ｫ��ʼֵ����Ϊ����1����
		for (int i = 0; i < grade.length; i++) {
			grade[i] = list.get(i).getOrePercent();
		}
		// ����temp��ֵ�Ƿ�С�ڵ���1��������ѭ��
		while (temp > 1) {
			// �������Ʒ�а����ظ�Ʒλֵ���ڵ�n����Ʒ�ľ�ֵ,�˴�n��Ϊlist��size��Ҳ������ĳ��ȡ�
			double sum = dataSum(grade);
			double M = sum / grade.length;
			double M1=M;//��M�洢�������Ժ���бȽ�
			double m = 0.0;// ȥ��������ƽ��ֵm
			// �����ݽ��е������������ݲ���k�ж���û���ظ�Ʒλֵ������У�����ظ�Ʒλֵ����
			for (int i = 0; i < grade.length; i++) {
				// ����ȥ��������ƽ��ֵm
				m = (sum - grade[i]) / (grade.length - 1);
				// ���ݲ���k�ж��Ƿ�Ϊ�ظ�Ʒλֵ
				temp = (M / m) / (k + 1);// ���tempС�ڵ���1����grade[i]����ƷΪ����ֵ�����账���˴�ֻ��Ҫ����Ϊ����ֵ����Ʒ
				if (temp > 1) {
					// ���������temp����1���������˵����Ʒ�д����ظ�Ʒλֵ����ʱ������ظ�Ʒλֵ������
					double GL = (grade.length * k + 1) * M / (k + 1);
//					GL = Double.parseDouble(df.format(GL));
					// ������GL�����ݶ���GL�滻
					for (int j = 0; j < grade.length; j++) {
						if (grade[j] > GL)
							grade[j] = GL;
					}
					num++;
					// ���������¼���һ��temp��ע���ʱM��ֵ�Ѿ��仯
					sum = dataSum(grade);
					M = sum / grade.length;
					if(M==M1) {
						temp=0.9;
						break;
					}
					m = (sum - grade[i]) / (grade.length - 1);
					temp = (M / m) / (k + 1);
					break;// �����һ���������ظ�Ʒλֵ��������ѭ��
				}
			}
		}
		// ��list�е���ƷƷλ���¡�
		for (int i = 0; i < grade.length; i++) {
			list.get(i).setOrePercent( Double.parseDouble(df.format(grade[i])));
		}
		// �������������ѭ�����������յ���Ʒ��ֵ����˽������װ�ڶ����з��ء�
		Result r = new Result();
		r.setGrade(grade);
		r.setNum(num);
		r.setList(list);
		// д����excel�ļ�
		try {
			writeExcel(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * ���߷�����������������������Ԫ�ص��ܺ�
	 * 
	 * @param grade
	 * @return
	 */
	public static double dataSum(double[] grade) {
		double sum = 0.0;
		for (int i = 0; i < grade.length; i++) {
			sum = sum + grade[i];
		}
		return sum;
	}

	/**
	 * ��ȡָ��·����excel�ļ�
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Ore> readExcel(String filePath) throws IOException {
		// ������������Ŷ�����ORE����
		List<Ore> oreList = new ArrayList<Ore>();
		// �õ��ļ�����
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
		// �õ�excel����������
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// �õ�excel���������
		HSSFSheet sheet = wb.getSheetAt(0); // Ĭ�ϵõ�sheet0������
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {// �����������������С�
			// ����Ore���󣬽�������ÿ�ж��������ݴ洢��������
			Ore o = new Ore();
			// �õ�Excel���������
			HSSFRow row = sheet.getRow(rowNum);
			// ����ǿ��У���������
			if (row == null)
				continue;
			// ������������Ŷ�ȡ��������
			List temp = new ArrayList();
			// �õ�Excel������ָ���еĵ�Ԫ��
			for (int cellNum = 0; cellNum < 7; cellNum++) {
				HSSFCell cell = row.getCell(cellNum);
				// �жϵ�Ԫ�����������ͣ����ò�ͬ��ȡֵ��ʽ
				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					temp.add(cell.getStringCellValue());// ��ȡ��Ԫ�����ݣ������뼯����
				} else {
					temp.add(cell.getNumericCellValue());// ��ȡ��Ԫ�����ݣ������뼯����
				}
			}
			// �������е����ݴ�ŵ�������
			o.setCoordinateX((double) temp.get(0));
			o.setCoordinateY((double) temp.get(1));
			o.setCoordinateZ((double) temp.get(2));
			o.setHoleDepth((double) temp.get(3));
			o.setHoleNumber((String) temp.get(4));
			o.setOrePercent((double) temp.get(5));
			o.setStickiness((String) temp.get(6));
			// �������ŵ�������
			oreList.add(o);
		}
		return oreList;
	}

	/**
	 * �����ĺ����Ʒд����excel�ļ�
	 * 
	 * @param list
	 */
	@SuppressWarnings("resource")
	public static void writeExcel(List<Ore> list) throws IOException {
		String[] titleRow = { "X����", "Y����", "Z����", "����", "�׺�", "Ʒλ", "����" };
		Workbook wb = null;
		File f = new File(excelPath);
		// ����ļ����ڣ���ֹ����д����ִ�����ɾ��ԭ���ļ�
		if (f.exists()) {
			f.delete();
		}
		File file = new File(excelPath);
		Sheet sheet = null;
		// ���������ĵ�����
		wb = new HSSFWorkbook();
		// ����sheet����
		sheet = (Sheet) wb.createSheet("sheet1");
		OutputStream outputStream = new FileOutputStream(excelPath);
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
		// ��ӱ�ͷ
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		row.setHeight((short) 540);
		cell.setCellValue("��Ʒ��ϸ��Ϣ"); // ������һ��

		CellStyle style = wb.createCellStyle(); // ��ʽ����
		// ���õ�Ԫ��ı�����ɫΪ����ɫ
		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// ��ֱ
		style.setAlignment(CellStyle.ALIGN_CENTER);// ˮƽ
		style.setWrapText(true);// ָ������Ԫ��������ʾ����ʱ�Զ�����

		cell.setCellStyle(style); // ��ʽ������

		// ��Ԫ��ϲ�
		// �ĸ������ֱ��ǣ���ʼ�У���ʼ�У������У�������
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

		row = sheet.createRow(1); // �����ڶ���
		for (int i = 0; i < titleRow.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleRow[i]);
			cell.setCellStyle(style); // ��ʽ������
		}
		row.setHeight((short) 540);

		// ѭ��д��������
		for (int i = 0; i < list.size(); i++) {
			row = (Row) sheet.createRow(i + 2);
			cell.setCellStyle(style); // ��ʽ������
			row.setHeight((short) 500);
			row.createCell(0).setCellValue((list.get(i)).getCoordinateX());
			row.createCell(1).setCellValue((list.get(i)).getCoordinateY());
			row.createCell(2).setCellValue((list.get(i)).getCoordinateZ());
			row.createCell(3).setCellValue((list.get(i)).getHoleDepth());
			row.createCell(4).setCellValue((list.get(i)).getHoleNumber());
			row.createCell(5).setCellValue((list.get(i)).getOrePercent());
			row.createCell(6).setCellValue((list.get(i)).getStickiness());
		}

		// �����ļ���
		OutputStream stream = new FileOutputStream(excelPath);
		// д������
		wb.write(stream);
		// �ر��ļ���
		stream.close();
	}

}
