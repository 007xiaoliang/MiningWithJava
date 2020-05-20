package com.sinohydro.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sinohydro.domain.BlastArea;
import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.domain.Ore;

public class ReadAndWriteExcel {
	private static List<BlastArea> list = new ArrayList<BlastArea>();

	/**
	 * POI读取 编录表
	 * 
	 * @param realPath
	 */
	public void poiExcel1(String realPath) {
		try {
			File fileDes = new File(realPath);
			InputStream str = new FileInputStream(fileDes);
			XSSFWorkbook xwb = new XSSFWorkbook(str); // 利用poi读取excel文件流
			XSSFSheet st = xwb.getSheetAt(0); // 读取sheet的第一个工作表
			int rows = st.getLastRowNum();// 总行数
			int cols;// 总列数
			for (int i = 1; i < rows; i++) {// 从第二行开始读取数据
				BlastArea blastArea = new BlastArea();
				// 建立容器，存放读取到的数据
				List<String> temp = new ArrayList<String>();
				XSSFRow row = st.getRow(i);// 读取某一行数据
				if (row != null) {
					// 获取行中所有列数据
					cols = row.getLastCellNum();
					for (int j = 0; j < cols; j++) {
						XSSFCell cell = row.getCell(j);
						// 判断单元格的数据类型
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_NUMERIC: // 数字
							temp.add(cell.getNumericCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_STRING: // 字符串
							temp.add(cell.getStringCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
							temp.add(cell.getBooleanCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_BLANK: // 空值
							temp.add("");
							break;
						}
					}
					blastArea.setHoleNo(temp.get(0));
					blastArea.setSampleName(temp.get(1));
					blastArea.setRemark(Double.valueOf(temp.get(2)) / Double.valueOf(temp.get(3)));
					blastArea.setClay(temp.get(4));
					list.add(blastArea);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * POI读取 化验表
	 * 
	 * @param realPath
	 * @param fileName
	 */
	public void poiExcel2(String realPath) {
		try {
			File fileDes = new File(realPath);
			InputStream str = new FileInputStream(fileDes);
			XSSFWorkbook xwb = new XSSFWorkbook(str); // 利用poi读取excel文件流
			XSSFSheet st = xwb.getSheetAt(0); // 读取sheet的第一个工作表
			int rows = st.getLastRowNum();// 总行数
			int cols;// 总列数
			for (int i = 0; i < rows; i++) {
				// 建立容器，存放读取到的数据
				List<String> temp = new ArrayList<String>();
				XSSFRow row = st.getRow(i);// 读取某一行数据
				if (row != null && row.getCell(0) != null
						&& row.getCell(0).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {// 只读取第一列是数字的行
					// 获取行中所有列数据
					cols = row.getLastCellNum();
					for (int j = 1; j < cols; j++) {
						XSSFCell cell = row.getCell(j);
						// System.out.println(cell);
						if (cell != null) {
							// 判断单元格的数据类型
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_NUMERIC: // 数字
								temp.add(cell.getNumericCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_STRING: // 字符串
								temp.add(cell.getStringCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
								temp.add(cell.getBooleanCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_BLANK: // 空值
								temp.add("");
								break;
							}
						}
					}
					// 根据取样编号把化验表中的数据与编录表的数据一一对应
					for (BlastArea blastArea : list) {
						if (blastArea.getSampleName().equals(temp.get(0))) {
							blastArea.setCu(Double.parseDouble(temp.get(1)));
							if (temp.get(2).equals("-")) {
								blastArea.setFe(0.0);
							} else {
								blastArea.setFe(Double.parseDouble(temp.get(2)));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取坐标信息
	 * 
	 * @param realPath
	 * @param fileName
	 */
	public void poiExcel3(String realPath) {
		File csv = new File(realPath); // CSV文件路径
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = "";
		String everyLine = "";
		try {
			while ((line = br.readLine()) != null) // 读取到的内容给line变量
			{
				everyLine = line;
				String[] split = everyLine.split(",");
				for (BlastArea blastArea : list) {
					if (blastArea.getHoleNo().equals(split[4] + ".0")) {
						blastArea.setCoordinateX(Double.parseDouble(split[0]));
						blastArea.setCoordinateY(Double.parseDouble(split[1]));
						blastArea.setCoordinateZ(Double.parseDouble(split[2]));
						blastArea.setHoleDepth(Double.parseDouble(split[3]));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据文件路径返回输入到数据库的所有炮孔信息集合
	 * 
	 * @param path1
	 *            编录表路径
	 * @param path2
	 *            化验表路径
	 * @param path3
	 *            坐标表路径
	 * @return
	 */
	public List<BlastArea> dataInput(String path1, String path2, String path3) {
		ReadAndWriteExcel re = new ReadAndWriteExcel();
		re.poiExcel1(path1);
		re.poiExcel2(path2);
		re.poiExcel3(path3);
		return list;

	}

	public void writeOutput(List<List<CircumcenterCoordinate>> allOreLines, String blastName) throws IOException {
		String[] titleRow = { "X坐标", "Y坐标", "Z坐标" };
		String excelPath = Core.desktopPath + File.separator + blastName + ".xls";
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
		cell.setCellValue(blastName + "炮区圈矿坐标"); // 创建第一行

		CellStyle style = wb.createCellStyle(); // 样式对象

		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
		style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

		// 单元格合并
		// 四个参数分别是：起始行，起始列，结束行，结束列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		int temp = 1;// 记录当前操作的行
		for (int i = 0; i < allOreLines.size(); i++) {
			if (allOreLines.get(i) != null) {
				if (i == allOreLines.size() - 1) {// 最后一个是边界线集合
					row = sheet.createRow(temp);
					cell = row.createCell(0);
					cell.setCellValue("炮区边界线"); // 创建第一行
					// sheet.addMergedRegion(new CellRangeAddress(temp, 0, temp, 2));
					temp += 1;
					row = sheet.createRow(temp);
					for (int j = 0; j < titleRow.length; j++) {
						row.createCell(j).setCellValue(titleRow[j]);
					}
				} else {
					row = sheet.createRow(temp); // 创建第二行
					for (int j = 0; j < titleRow.length; j++) {
						row.createCell(j).setCellValue(titleRow[j]);
					}
				}
				temp += 1;
				// 循环写入行数据
				for (int j = 0; j < allOreLines.get(i).size(); j++) {
					row = sheet.createRow(temp);
					row.createCell(0).setCellValue(allOreLines.get(i).get(j).getX());
					row.createCell(1).setCellValue(allOreLines.get(i).get(j).getY());
					row.createCell(2).setCellValue(allOreLines.get(i).get(j).getZ());
					temp += 1;
				}
				temp += 1;
			}
		}
		// 创建文件流
		OutputStream stream = new FileOutputStream(excelPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
	}

	/**
	 * 读取给定的边界信息
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public List<CircumcenterCoordinate> readExcel(String filePath) throws IOException {
		// 创建容器，存放读到的ORE对象
		List<CircumcenterCoordinate> givedBorderList = new ArrayList<CircumcenterCoordinate>();
		// 得到文件对象
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
		// 得到excel工作簿对象
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 得到excel工作表对象
		HSSFSheet sheet = wb.getSheetAt(0); // 默认得到sheet0工作表
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {// 遍历工作表中所有行。
			CircumcenterCoordinate cc = new CircumcenterCoordinate();
			// 得到Excel工作表的行
			HSSFRow row = sheet.getRow(rowNum);
			// 如果是空行，跳过继续
			if (row == null)
				continue;
			// 建立容器，存放读取到的数据
			List<Double> temp = new ArrayList<Double>();
			// 得到Excel工作表指定行的单元格
			for (int cellNum = 0; cellNum < 3; cellNum++) {
				HSSFCell cell = row.getCell(cellNum);
				temp.add(cell.getNumericCellValue());// 读取单元格内容，并放入集合中
			}
			// 将容器中的数据存放到对象中
			cc.setX((double) temp.get(0));
			cc.setY((double) temp.get(1));
			cc.setZ((double) temp.get(2));
			cc.setFlag(false);
			// 将对象存放到容器中
			givedBorderList.add(cc);
		}
		return givedBorderList;
	}
}
