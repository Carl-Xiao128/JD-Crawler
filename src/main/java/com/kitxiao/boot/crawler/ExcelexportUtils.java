package com.kitxiao.boot.crawler;

import java.awt.FontMetrics;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @项目名称：wupao-job
 * @类名称：ExcelUtils
 * @类描述：excel文件生成和下载
 * @创建人：王炎
 * @创建时间：2016年4月19日 下午4:18:26 
 * @version：
 */
public class ExcelexportUtils {
	/**
	 * 将数据写入到Excel文件
	 * @param filePath 文件路径
	 * @param sheetName 工作表名称
	 * @param title 工作表标题栏
	 * @param data 工作表数据
	 * @throws FileNotFoundException 文件不存在异常
	 * @throws IOException IO异常
	 */
	public void writeToFile(String filePath, String[] sheetName,
			List<? extends Object[]> title,
			List<? extends List<? extends Object[]>> data)
			throws FileNotFoundException, IOException {
		// 创建并获取工作簿对象
		Workbook wb = getWorkBook(sheetName, title, data);
		// 写入到文件
		FileOutputStream out = new FileOutputStream(filePath);
		wb.write(out);
		out.close();
	}

	/**
	 * @author 王炎
	 * 创建工作簿对象<br>
	 * <font color="red">工作表名称，工作表标题，工作表数据最好能够对应起来</font><br>
	 * 比如三个不同或相同的工作表名称，三组不同或相同的工作表标题，三组不同或相同的工作表数据<br>
	 * <b>
	 * 注意：<br>
	 * 需要为每个工作表指定<font color="red">工作表名称，工作表标题，工作表数据</font><br>
	 * 如果工作表的数目大于工作表数据的集合，那么首先会根据顺序一一创建对应的工作表名称和数据集合，然后创建的工作表里面是没有数据的<br>
	 * 如果工作表的数目小于工作表数据的集合，那么多余的数据将不会写入工作表中
	 * </b>
	 * @param sheetName 工作表名称的数组
	 * @param title 每个工作表名称的数组集合
	 * @param data 每个工作表数据的集合的集合
	 * @return Workbook工作簿
	 * @throws FileNotFoundException 文件不存在异常
	 * @throws IOException IO异常
	 */
	public Workbook getWorkBook(String[] sheetName,
			List<? extends Object[]> title,
			List<? extends List<? extends Object[]>> data)
			throws FileNotFoundException, IOException {
		// 创建工作簿，支持2007及以后的文档格式
		Workbook wb = new XSSFWorkbook();
		// 创建一个工作表sheet
		Sheet sheet = null;
		// 申明行
		Row row = null;
		// 申明单元格
		Cell cell = null;
		// 单元格样式
		CellStyle titleStyle = wb.createCellStyle();
		CellStyle cellStyle = wb.createCellStyle();
		// 字体样式
		Font font = wb.createFont();
		// 粗体
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(font);
		// 水平居中
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 水平居中
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 标题数据
		Object[] title_temp = null;
		// 行数据
		Object[] rowData = null;
		// 工作表数据
		List<? extends Object[]> sheetData = null;
		// 遍历sheet
		for (int sheetNumber = 0; sheetNumber < sheetName.length; sheetNumber++) {
			// 创建工作表
			sheet = wb.createSheet();
			// 工作表缩小50%
			// sheet.setZoom(3,2);
			// 设置默认列宽
			sheet.setDefaultColumnWidth(11);
			// 列宽注意乘256
			// sheet.setColumnWidth(columnIndex, excel.width() * 256);
			// 设置工作表名称
			wb.setSheetName(sheetNumber, sheetName[sheetNumber]);
			// 设置标题
			title_temp = title.get(sheetNumber);
			row = sheet.createRow(0);
			// 写入标题
			for (int i = 0; i < title_temp.length; i++) {
				if (i == 0) {
					// // 列宽注意乘256
					sheet.setColumnWidth((short) i, (short) 20 * 256);
					// row.setHeight((short)height);
				} else if (i == 2) {
					sheet.setColumnWidth((short) i, (short) 21 * 256);
				} else if (i == 3) {
					sheet.setColumnWidth((short) i, (short) 18 * 256);
				} else if (i == 4) {
					sheet.setColumnWidth((short) i, (short) 36 * 256);
				}
				cell = row.createCell(i);
				cell.setCellStyle(titleStyle);
				cell.setCellValue(title_temp[i].toString());
			}
			try {
				sheetData = data.get(sheetNumber);
			} catch (Exception e) {
				continue;
			}
			// 写入行数据
			for (int rowNumber = 0; rowNumber < sheetData.size(); rowNumber++) {
				// 如果没有标题栏，起始行就是0，如果有标题栏，行号就应该为1
				row = sheet.createRow(title_temp == null ? rowNumber
						: (rowNumber + 1));
				rowData = sheetData.get(rowNumber);
				for (int columnNumber = 0; columnNumber < rowData.length; columnNumber++) {
					cell = row.createCell(columnNumber);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(rowData[columnNumber].toString());
				}
			}
		}
		return wb;
	}

	/**
	 * @author 肖龙祥
	 * 创建工作簿对象<br>
	 * <font color="red">工作表名称，工作表标题，工作表数据最好能够对应起来</font><br>
	 * 比如三个不同或相同的工作表名称，三组不同或相同的工作表标题，三组不同或相同的工作表数据<br>
	 * <b>
	 * 注意：<br>
	 * 需要为每个工作表指定<font color="red">工作表名称，工作表标题，工作表数据</font><br>
	 * 如果工作表的数目大于工作表数据的集合，那么首先会根据顺序一一创建对应的工作表名称和数据集合，然后创建的工作表里面是没有数据的<br>
	 * 如果工作表的数目小于工作表数据的集合，那么多余的数据将不会写入工作表中
	 * </b>
	 * @param sheetName 工作表名称的数组
	 * @param title 每个工作表名称的数组集合
	 * @param data 每个工作表数据的集合的集合
	 * @return Workbook工作簿
	 * @throws FileNotFoundException 文件不存在异常
	 * @throws IOException IO异常
	 */
	public Workbook getAutoWorkBook(String[] sheetName,
			List<? extends Object[]> title,
			List<? extends List<? extends Object[]>> data)
			throws FileNotFoundException, IOException {
		// 创建工作簿，支持2007及以后的文档格式
		Workbook wb = new XSSFWorkbook();
		// 创建一个工作表sheet
		Sheet sheet = null;
		// 申明行
		Row row = null;
		// 申明单元格
		Cell cell = null;
		// 单元格样式
		CellStyle titleStyle = wb.createCellStyle();
		CellStyle cellStyle = wb.createCellStyle();
		// 字体样式
		Font font = wb.createFont();
		// 粗体
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(font);
		// 水平居中
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 水平居中
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 标题数据
		Object[] title_temp = null;
		// 行数据
		Object[] rowData = null;
		// 工作表数据
		List<? extends Object[]> sheetData = null;
		// 遍历sheet
		for (int sheetNumber = 0; sheetNumber < sheetName.length; sheetNumber++) {
			// 创建工作表
			sheet = wb.createSheet();
			// 工作表缩小50%
			// sheet.setZoom(3,2);
			// 设置默认列宽
			sheet.setDefaultColumnWidth(20);
			// 列宽注意乘256
			sheet.setColumnWidth(1, 50 * 512);
			// 设置工作表名称
			wb.setSheetName(sheetNumber, sheetName[sheetNumber]);
			// 设置标题
			title_temp = title.get(sheetNumber);
			row = sheet.createRow(0);
			// 写入标题
			for (int i = 0; i < title_temp.length; i++) {
				java.awt.Font f = new java.awt.Font("Calibri",
						java.awt.Font.BOLD, 12);
				@SuppressWarnings("restriction")
				FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
				int wid = fm.stringWidth((String) title_temp[i]);
				//sheet.setColumnWidth((short) i, (short) wid * 256);
				cell = row.createCell(i);
				cell.setCellStyle(titleStyle);
				cell.setCellValue(title_temp[i].toString());
			}
			
			try {
				sheetData = data.get(sheetNumber);
			} catch (Exception e) {
				continue;
			}
			// 写入行数据
			for (int rowNumber = 0; rowNumber < sheetData.size(); rowNumber++) {
				// 如果没有标题栏，起始行就是0，如果有标题栏，行号就应该为1
				row = sheet.createRow(title_temp == null ? rowNumber
						: (rowNumber + 1));
				rowData = sheetData.get(rowNumber);
				for (int columnNumber = 0; columnNumber < rowData.length; columnNumber++) {
					cell = row.createCell(columnNumber);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(rowData[columnNumber].toString());
				}
			}
		}
		return wb;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		String[] title1 = { "第一列", "第二", "第三列sdgwghasdf肖阿萨德" };
		List<String[]> titles = new ArrayList<String[]>();
		titles.add(title1);
		String[] data1 = { "i", "j", "k" };
		String[] data2 = { "m", "n", "o" };
		String[] data3 = { "x", "y", "z" };
		List<String[]> data = new ArrayList<String[]>();
		data.add(data1);
		data.add(data2);
		data.add(data3);
		List<List<String[]>> data_ = new ArrayList<List<String[]>>();
		data_.add(data);
		String[] sheetName = { "第一张表" };
		new ExcelexportUtils().writeToFile("D:\\xx.xlsx", sheetName, titles, data_);
	}
}