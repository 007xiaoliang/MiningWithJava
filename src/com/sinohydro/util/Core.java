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

	// 取当前用户桌面
	static File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
	public static String desktopPath = desktopDir.getAbsolutePath();
	public static String excelPath = desktopPath + File.separator + "计算后样品信息.xls";

	public static Result geologicTest(String filePath, double k) throws IOException {
		List<Ore> list = readExcel(filePath);
		Result r = calculateData(list, k);
		return r;

	}

	/**
	 * 核心代码，对数据进行分析后得到最终品位
	 * 
	 * @param list
	 * @param k
	 * @return
	 */
	private static Result calculateData(List<Ore> list, double k) {
		//对list中小于0.12的数据剔除
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getOrePercent()<0.12) {
				list.remove(i);
			}
		}
		// 控制double小数点后四位
		DecimalFormat df = new DecimalFormat("0.0000");
		// 建立一个数组，用来存放ore对象当中的品位值，数组长度为list集合的长度
		double[] grade = new double[list.size()];
		int num = 0;// 计算循环次数
		double temp = 1.1;// 临时变量,为了第一次进入while循环，这里将初始值设置为大于1的数
		for (int i = 0; i < grade.length; i++) {
			grade[i] = list.get(i).getOrePercent();
		}
		// 根据temp的值是否小于等于1来做无限循环
		while (temp > 1) {
			// 计算出样品中包含特高品位值在内的n个样品的均值,此处n即为list的size，也是数组的长度。
			double sum = dataSum(grade);
			double M = sum / grade.length;
			double M1=M;//将M存储，方便以后进行比较
			double m = 0.0;// 去除本身后的平均值m
			// 对数据进行单个分析，根据参数k判断有没有特高品位值，如果有，求出特高品位值下限
			for (int i = 0; i < grade.length; i++) {
				// 计算去除本身后的平均值m
				m = (sum - grade[i]) / (grade.length - 1);
				// 根据参数k判断是否为特高品位值
				temp = (M / m) / (k + 1);// 如果temp小于等于1，则grade[i]此样品为正常值，不予处理，此处只需要处理不为正常值的样品
				if (temp > 1) {
					// 如果出现了temp大于1的情况，就说明样品中存在特高品位值，此时计算出特高品位值的下限
					double GL = (grade.length * k + 1) * M / (k + 1);
//					GL = Double.parseDouble(df.format(GL));
					// 将大于GL的数据都用GL替换
					for (int j = 0; j < grade.length; j++) {
						if (grade[j] > GL)
							grade[j] = GL;
					}
					num++;
					// 在这里重新计算一次temp，注意此时M的值已经变化
					sum = dataSum(grade);
					M = sum / grade.length;
					if(M==M1) {
						temp=0.9;
						break;
					}
					m = (sum - grade[i]) / (grade.length - 1);
					temp = (M / m) / (k + 1);
					break;// 如果有一个数据是特高品位值，就跳出循环
				}
			}
		}
		// 将list中的样品品位更新。
		for (int i = 0; i < grade.length; i++) {
			list.get(i).setOrePercent( Double.parseDouble(df.format(grade[i])));
		}
		// 返回两个结果，循环次数和最终的样品数值，因此将结果封装在对象中返回。
		Result r = new Result();
		r.setGrade(grade);
		r.setNum(num);
		r.setList(list);
		// 写出到excel文件
		try {
			writeExcel(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 工具方法，用来计算数组中所有元素的总和
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
	 * 读取指定路径的excel文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Ore> readExcel(String filePath) throws IOException {
		// 创建容器，存放读到的ORE对象
		List<Ore> oreList = new ArrayList<Ore>();
		// 得到文件对象
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
		// 得到excel工作簿对象
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 得到excel工作表对象
		HSSFSheet sheet = wb.getSheetAt(0); // 默认得到sheet0工作表
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {// 遍历工作表中所有行。
			// 创建Ore对象，将工作表每行读到的内容存储到对象中
			Ore o = new Ore();
			// 得到Excel工作表的行
			HSSFRow row = sheet.getRow(rowNum);
			// 如果是空行，跳过继续
			if (row == null)
				continue;
			// 建立容器，存放读取到的数据
			List temp = new ArrayList();
			// 得到Excel工作表指定行的单元格
			for (int cellNum = 0; cellNum < 7; cellNum++) {
				HSSFCell cell = row.getCell(cellNum);
				// 判断单元格中数据类型，采用不同的取值方式
				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					temp.add(cell.getStringCellValue());// 读取单元格内容，并放入集合中
				} else {
					temp.add(cell.getNumericCellValue());// 读取单元格内容，并放入集合中
				}
			}
			// 将容器中的数据存放到对象中
			o.setCoordinateX((double) temp.get(0));
			o.setCoordinateY((double) temp.get(1));
			o.setCoordinateZ((double) temp.get(2));
			o.setHoleDepth((double) temp.get(3));
			o.setHoleNumber((String) temp.get(4));
			o.setOrePercent((double) temp.get(5));
			o.setStickiness((String) temp.get(6));
			// 将对象存放到容器中
			oreList.add(o);
		}
		return oreList;
	}

	/**
	 * 将更改后的样品写入新excel文件
	 * 
	 * @param list
	 */
	@SuppressWarnings("resource")
	public static void writeExcel(List<Ore> list) throws IOException {
		String[] titleRow = { "X坐标", "Y坐标", "Z坐标", "孔深", "孔号", "品位", "岩性" };
		Workbook wb = null;
		File f = new File(excelPath);
		// 如果文件存在，防止数据写入出现错误，先删除原有文件
		if (f.exists()) {
			f.delete();
		}
		File file = new File(excelPath);
		Sheet sheet = null;
		// 创建工作文档对象
		wb = new HSSFWorkbook();
		// 创建sheet对象
		sheet = (Sheet) wb.createSheet("sheet1");
		OutputStream outputStream = new FileOutputStream(excelPath);
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
		// 添加表头
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		row.setHeight((short) 540);
		cell.setCellValue("样品详细信息"); // 创建第一行

		CellStyle style = wb.createCellStyle(); // 样式对象
		// 设置单元格的背景颜色为淡蓝色
		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
		style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

		cell.setCellStyle(style); // 样式，居中

		// 单元格合并
		// 四个参数分别是：起始行，起始列，结束行，结束列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

		row = sheet.createRow(1); // 创建第二行
		for (int i = 0; i < titleRow.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleRow[i]);
			cell.setCellStyle(style); // 样式，居中
		}
		row.setHeight((short) 540);

		// 循环写入行数据
		for (int i = 0; i < list.size(); i++) {
			row = (Row) sheet.createRow(i + 2);
			cell.setCellStyle(style); // 样式，居中
			row.setHeight((short) 500);
			row.createCell(0).setCellValue((list.get(i)).getCoordinateX());
			row.createCell(1).setCellValue((list.get(i)).getCoordinateY());
			row.createCell(2).setCellValue((list.get(i)).getCoordinateZ());
			row.createCell(3).setCellValue((list.get(i)).getHoleDepth());
			row.createCell(4).setCellValue((list.get(i)).getHoleNumber());
			row.createCell(5).setCellValue((list.get(i)).getOrePercent());
			row.createCell(6).setCellValue((list.get(i)).getStickiness());
		}

		// 创建文件流
		OutputStream stream = new FileOutputStream(excelPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
	}

}
