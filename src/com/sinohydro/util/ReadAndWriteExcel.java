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
	 * POI��ȡ ��¼��
	 * 
	 * @param realPath
	 */
	public void poiExcel1(String realPath) {
		try {
			File fileDes = new File(realPath);
			InputStream str = new FileInputStream(fileDes);
			XSSFWorkbook xwb = new XSSFWorkbook(str); // ����poi��ȡexcel�ļ���
			XSSFSheet st = xwb.getSheetAt(0); // ��ȡsheet�ĵ�һ��������
			int rows = st.getLastRowNum();// ������
			int cols;// ������
			for (int i = 1; i < rows; i++) {// �ӵڶ��п�ʼ��ȡ����
				BlastArea blastArea = new BlastArea();
				// ������������Ŷ�ȡ��������
				List<String> temp = new ArrayList<String>();
				XSSFRow row = st.getRow(i);// ��ȡĳһ������
				if (row != null) {
					// ��ȡ��������������
					cols = row.getLastCellNum();
					for (int j = 0; j < cols; j++) {
						XSSFCell cell = row.getCell(j);
						// �жϵ�Ԫ�����������
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_NUMERIC: // ����
							temp.add(cell.getNumericCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_STRING: // �ַ���
							temp.add(cell.getStringCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
							temp.add(cell.getBooleanCellValue() + "");
							break;
						case XSSFCell.CELL_TYPE_BLANK: // ��ֵ
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
	 * POI��ȡ �����
	 * 
	 * @param realPath
	 * @param fileName
	 */
	public void poiExcel2(String realPath) {
		try {
			File fileDes = new File(realPath);
			InputStream str = new FileInputStream(fileDes);
			XSSFWorkbook xwb = new XSSFWorkbook(str); // ����poi��ȡexcel�ļ���
			XSSFSheet st = xwb.getSheetAt(0); // ��ȡsheet�ĵ�һ��������
			int rows = st.getLastRowNum();// ������
			int cols;// ������
			for (int i = 0; i < rows; i++) {
				// ������������Ŷ�ȡ��������
				List<String> temp = new ArrayList<String>();
				XSSFRow row = st.getRow(i);// ��ȡĳһ������
				if (row != null && row.getCell(0) != null
						&& row.getCell(0).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {// ֻ��ȡ��һ�������ֵ���
					// ��ȡ��������������
					cols = row.getLastCellNum();
					for (int j = 1; j < cols; j++) {
						XSSFCell cell = row.getCell(j);
						// System.out.println(cell);
						if (cell != null) {
							// �жϵ�Ԫ�����������
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_NUMERIC: // ����
								temp.add(cell.getNumericCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_STRING: // �ַ���
								temp.add(cell.getStringCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
								temp.add(cell.getBooleanCellValue() + "");
								break;
							case XSSFCell.CELL_TYPE_BLANK: // ��ֵ
								temp.add("");
								break;
							}
						}
					}
					// ����ȡ����Űѻ�����е��������¼�������һһ��Ӧ
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
	 * ��ȡ������Ϣ
	 * 
	 * @param realPath
	 * @param fileName
	 */
	public void poiExcel3(String realPath) {
		File csv = new File(realPath); // CSV�ļ�·��
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = "";
		String everyLine = "";
		try {
			while ((line = br.readLine()) != null) // ��ȡ�������ݸ�line����
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
	 * �����ļ�·���������뵽���ݿ�������ڿ���Ϣ����
	 * 
	 * @param path1
	 *            ��¼��·��
	 * @param path2
	 *            �����·��
	 * @param path3
	 *            �����·��
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
		String[] titleRow = { "X����", "Y����", "Z����" };
		String excelPath = Core.desktopPath + File.separator + blastName + ".xls";
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
		cell.setCellValue(blastName + "����Ȧ������"); // ������һ��

		CellStyle style = wb.createCellStyle(); // ��ʽ����

		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// ��ֱ
		style.setAlignment(CellStyle.ALIGN_CENTER);// ˮƽ
		style.setWrapText(true);// ָ������Ԫ��������ʾ����ʱ�Զ�����

		// ��Ԫ��ϲ�
		// �ĸ������ֱ��ǣ���ʼ�У���ʼ�У������У�������
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		int temp = 1;// ��¼��ǰ��������
		for (int i = 0; i < allOreLines.size(); i++) {
			if (allOreLines.get(i) != null) {
				if (i == allOreLines.size() - 1) {// ���һ���Ǳ߽��߼���
					row = sheet.createRow(temp);
					cell = row.createCell(0);
					cell.setCellValue("�����߽���"); // ������һ��
					// sheet.addMergedRegion(new CellRangeAddress(temp, 0, temp, 2));
					temp += 1;
					row = sheet.createRow(temp);
					for (int j = 0; j < titleRow.length; j++) {
						row.createCell(j).setCellValue(titleRow[j]);
					}
				} else {
					row = sheet.createRow(temp); // �����ڶ���
					for (int j = 0; j < titleRow.length; j++) {
						row.createCell(j).setCellValue(titleRow[j]);
					}
				}
				temp += 1;
				// ѭ��д��������
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
		// �����ļ���
		OutputStream stream = new FileOutputStream(excelPath);
		// д������
		wb.write(stream);
		// �ر��ļ���
		stream.close();
	}

	/**
	 * ��ȡ�����ı߽���Ϣ
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public List<CircumcenterCoordinate> readExcel(String filePath) throws IOException {
		// ������������Ŷ�����ORE����
		List<CircumcenterCoordinate> givedBorderList = new ArrayList<CircumcenterCoordinate>();
		// �õ��ļ�����
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
		// �õ�excel����������
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// �õ�excel���������
		HSSFSheet sheet = wb.getSheetAt(0); // Ĭ�ϵõ�sheet0������
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {// �����������������С�
			CircumcenterCoordinate cc = new CircumcenterCoordinate();
			// �õ�Excel���������
			HSSFRow row = sheet.getRow(rowNum);
			// ����ǿ��У���������
			if (row == null)
				continue;
			// ������������Ŷ�ȡ��������
			List<Double> temp = new ArrayList<Double>();
			// �õ�Excel������ָ���еĵ�Ԫ��
			for (int cellNum = 0; cellNum < 3; cellNum++) {
				HSSFCell cell = row.getCell(cellNum);
				temp.add(cell.getNumericCellValue());// ��ȡ��Ԫ�����ݣ������뼯����
			}
			// �������е����ݴ�ŵ�������
			cc.setX((double) temp.get(0));
			cc.setY((double) temp.get(1));
			cc.setZ((double) temp.get(2));
			cc.setFlag(false);
			// �������ŵ�������
			givedBorderList.add(cc);
		}
		return givedBorderList;
	}
}
