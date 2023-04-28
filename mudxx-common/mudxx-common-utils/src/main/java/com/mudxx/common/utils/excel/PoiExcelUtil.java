package com.mudxx.common.utils.excel;

import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.file.FileUtil;
import com.mudxx.common.utils.string.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description java用POI创建和导入导出2003和2007版本Excel
 * @author laiwen
 * @date 2018年7月6日 上午11:35:45
 */
@SuppressWarnings("ALL")
public class PoiExcelUtil {

	private static final Logger log = LoggerFactory.getLogger(PoiExcelUtil.class);

	/**
	 * @description 创建Excel2003
	 * @param fileName Excel工作簿名称（全路径）
	 * @param sheetName sheet工作表名称
	 */
	private static void createExcel2003(String fileName, String sheetName) {
		// 创建一个Excel2003文档对象
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 创建一个工作表对象（工作表默认名称为Sheet0）
		// HSSFSheet sheet = workbook.createSheet();

		// 创建一个工作表对象（工作表名称自定义为firstSheet）
		workbook.createSheet(sheetName);

		// 执行创建excel2003
		writeOut(workbook, fileName, true, true);
	}

	/**
	 * @description 导出Excel2003
	 * @param fileName 如果是本地磁盘导出：工作簿名称（Excel文件全路径）；如果是浏览器下载：文件名即可，比如aaa.xls
	 * @param sheetName 工作表名称
	 * @param request 当request为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param response 当response为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param list 待导出的数据集合
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系（需要传递LinkedHashMap）
	 * @param theme 主题
	 * @param isAutoColSize true：数据列自动设置列宽，false：数据列不自动设置列宽
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @throws Exception 异常信息
	 */
	private static <T> void exportExcel2003(String fileName, String sheetName,
											HttpServletRequest request, HttpServletResponse response, List<T> list,
											Map<String,String> fieldMap, String theme,
											boolean isAutoColSize, boolean isSetBgColor) throws Exception {
		// 定义存放英文字段名和中文字段名的数组（表头）
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];
		// 填充数组（表头）
		int count = 0;
		for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
			// key英文，字段英文名称
			enFields[count] = entry.getKey();
			// value中文，字段中文名称
			cnFields[count] = entry.getValue();
			count++;
		}

		Workbook workbook;
		if (isAutoColSize) {
			// 方式一：强调：可以使用autoColumnSizeOK(sheet, count);//自动设置列宽，导出excel用时稍微多一点
			// 创建一个Excel2003文档对象
			workbook = new HSSFWorkbook();
		} else {
			// 方式二：强调：不可以使用autoColumnSizeOK(sheet, count);//自动设置列宽，导出excel用时稍微少一点
			// 内存中允许100条数据，以免内存溢出，其余的写入硬盘
			workbook = new SXSSFWorkbook(100);
		}
		// 创建一个工作表对象（工作表名称自定义）
		Sheet sheet = workbook.createSheet(sheetName);

		Map<String, Object> styleMap = getStyle(true);
		// 将数据库数据导出到Excel中
		list2Excel(theme, sheet, count, workbook, styleMap, cnFields, enFields, list, fieldMap.size(), isAutoColSize, isSetBgColor);

		if (response == null) {
			// 导出到本地磁盘
			writeOut(workbook, fileName, false, true);
		} else {
			// 浏览器下载
			browserDownload(request, response, fileName, workbook);
		}
	}

	/**
	 * @description 读取Excel2003（导入Excel2003）
	 * @param file 待读文件
	 * @param sheetName sheet工作表名称
	 * @param sheetIndex sheet工作表索引，索引从0开始
	 * @return 返回list集合
	 * @throws Exception 异常信息
	 */
	private static List<List<String>> readExcel2003(File file, String sheetName, Integer sheetIndex) throws Exception {
		// 创建一个Excel2003文档对象
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

		// 获取工作表
		Sheet sheet = accessSheet(workbook, sheetName, sheetIndex);

		log.info("开始读取【office Excel 2003】数据内容...");
		// 获取工作表中的单元格数据（有效数据，即数据库数据）;
		return cellValue2List(sheet);
	}

	/**
	 * @description 创建Excel2007
	 * @param fileName Excel工作簿名称（全路径）
	 * @param sheetName sheet工作表名称
	 */
	private static void createExcel2007(String fileName, String sheetName) {
		// 创建一个Excel2007文档对象
		XSSFWorkbook workbook = new XSSFWorkbook();

		// 创建一个工作表对象，工作表默认名称为Sheet0
		// XSSFSheet sheet = workbook.createSheet();

		// 创建一个工作表对象，工作表名称自定义为Sheet_one
		workbook.createSheet(sheetName);

		// 执行创建excel2007
		writeOut(workbook, fileName, true, false);
	}

	/**
	 * @description 导出Excel2007
	 * @param fileName 如果是本地磁盘导出：工作簿名称（Excel文件全路径）；如果是浏览器下载：文件名即可，比如aaa.xls
	 * @param sheetName 工作表名称
	 * @param request 当request为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param response 当response为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param list 待导出的数据集合
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系（需要传递LinkedHashMap）
	 * @param theme 主题
	 * @param isAutoColSize true：数据列自动设置列宽，false：数据列不自动设置列宽
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @throws Exception 异常信息
	 */
	private static <T> void exportExcel2007(String fileName, String sheetName,
											HttpServletRequest request, HttpServletResponse response, List<T> list,
											Map<String,String> fieldMap, String theme,
											boolean isAutoColSize, boolean isSetBgColor) throws Exception {
		// 定义存放英文字段名和中文字段名的数组（表头）
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];
		// 填充数组（表头）
		int count = 0;
		for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
			// key英文，字段英文名称
			enFields[count] = entry.getKey();
			// value中文，字段中文名称
			cnFields[count] = entry.getValue();
			count++;
		}

		Workbook workbook;
		if (isAutoColSize) {
			// 方式一：强调：可以使用autoColumnSizeOK(sheet, count);//自动设置列宽，导出excel用时稍微多一点
			// 创建一个Excel2007文档对象
			workbook = new XSSFWorkbook();
		} else {
			// 方式二：强调：不可以使用autoColumnSizeOK(sheet, count);//自动设置列宽，导出excel用时稍微少一点
			// 内存中允许100条数据，以免内存溢出，其余的写入硬盘
			workbook = new SXSSFWorkbook(100);
		}
		// 创建一个工作表对象（工作表名称自定义）
		Sheet sheet = workbook.createSheet(sheetName);

		Map<String, Object> styleMap = getStyle(false);

		// 将数据库数据导出到Excel中
		list2Excel(theme, sheet, count, workbook, styleMap, cnFields, enFields, list, fieldMap.size(), isAutoColSize, isSetBgColor);

		if (response == null) {
			// 导出到本地磁盘
			writeOut(workbook, fileName, false, false);
		} else {
			// 浏览器下载
			browserDownload(request, response, fileName, workbook);
		}
	}

	/**
	 * @description 读取Excel2007（导入Excel2007）
	 * @param file 待读文件
	 * @param sheetName sheet工作表名称
	 * @param sheetIndex sheet工作表索引，索引从0开始
	 * @return 返回list集合
	 * @throws Exception 异常信息
	 */
	private static List<List<String>> readExcel2007(File file, String sheetName, Integer sheetIndex) throws Exception {
		// 创建一个Excel2007文档对象
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

		// 获取工作表
		Sheet sheet = accessSheet(workbook, sheetName, sheetIndex);

		log.info("开始读取【office Excel 2007】数据内容...");
		// 获取工作表中的单元格数据（有效数据，即数据库数据）;
		return cellValue2List(sheet);
	}

	/**
	 * @description 创建Excel2003和Excel2007
	 * @param fileName 工作簿名称（Excel文件全路径）
	 * @param sheetName sheet工作表名称
	 * @throws Exception 异常信息
	 */
	public static void createExcel(String fileName, String sheetName) throws Exception {
		// 获取文件后缀名
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			// Excel2003
			createExcel2003(fileName, sheetName);
		} else if ("xlsx".equals(extension)) {
			// Excel2007
			createExcel2007(fileName, sheetName);
		} else {
			// 既不是Excel2003也不是Excel2007
			throw new IOException("不支持的文件类型！");
		}
	}

	/**
	 * 说明：该方法是通用的excel导出方法！
	 * @description 本地磁盘导出（或者浏览器下载）Excel2003和Excel2007
	 * @param fileName 如果是本地磁盘导出：工作簿名称（Excel文件全路径）；如果是浏览器下载：文件名即可，比如aaa.xls
	 * @param sheetName sheet工作表名称
	 * @param request 当request为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param response 当response为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param list 待导出的数据集合
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系（需要传递LinkedHashMap）
	 * @param theme 主题
	 * @param isAutoColSize true：数据列自动设置列宽，false：数据列不自动设置列宽
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @param <T> 泛型
	 * @throws Exception 异常信息
	 */
	public static <T> void exportExcel(String fileName, String sheetName, HttpServletRequest request,
									   HttpServletResponse response, List<T> list,
									   Map<String,String> fieldMap, String theme,
									   boolean isAutoColSize, boolean isSetBgColor) throws Exception {
		// 获取文件后缀名
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			// Excel2003
			exportExcel2003(fileName, sheetName, request, response, list, fieldMap, theme, isAutoColSize, isSetBgColor);
		} else if ("xlsx".equals(extension)) {
			// Excel2007
			exportExcel2007(fileName, sheetName, request, response, list, fieldMap, theme, isAutoColSize, isSetBgColor);
		} else {
			// 既不是Excel2003也不是Excel2007
			throw new IOException("不支持的文件类型！");
		}
	}

	/**
	 * 注意：该方法是根据业务定制，具体问题具体分析。
	 * @description 本地磁盘导出（或者浏览器下载）Excel2003和Excel2007
	 * @param module 列表名称，比如跟进合伙人、已加盟合伙人、无效合伙人等等。
	 * @param request 当request为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param response 当response为null的时候是本地磁盘导出，不为null的时候是浏览器下载
	 * @param list 待导出的数据集合
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系（需要传递LinkedHashMap）
	 * @param isExcel_2007 true：是Excel_2007，false：是Excel_2003
	 * @param isAutoColSize true：数据列自动设置列宽，false：数据列不自动设置列宽
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @param <T> 泛型
	 * @throws Exception 异常信息
	 */
	public static <T> void exportExcelForSpecial(String module, HttpServletRequest request,
												 HttpServletResponse response, List<T> list,
												 Map<String,String> fieldMap, boolean isExcel_2007,
												 boolean isAutoColSize, boolean isSetBgColor) throws Exception {
		String exportDate = CalendarUtil.convertDateTime2Zh(CalendarUtil.getNowDate(), "点", "分", "秒");
		String fileName;
		if (isExcel_2007) {
			fileName = module + "(" + exportDate + ").xlsx";
		} else {
			fileName = module + "(" + exportDate + ").xls";
		}
		String sheetName = module + "列表";
		String excelNameTitle = module + "(" + exportDate + "导出)";
		exportExcel(fileName, sheetName, request, response, list, fieldMap, excelNameTitle, isAutoColSize, isSetBgColor);
	}

	/**
	 * @description 读取Excel2003和Excel2007（导入Excel）
	 * @param file 待读文件
	 * @param sheetName sheet工作表名称
	 * @param sheetIndex sheet工作表索引，索引从0开始
	 * @return 返回list集合
	 * @throws Exception 异常信息
	 */
	public static List<List<String>> readExcel(File file, String sheetName, Integer sheetIndex) throws Exception {
		// getName()获取文件名（含有后缀），不含路径
		String fileName = file.getName();
		// 获取文件后缀名
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			// Excel2003
			return readExcel2003(file, sheetName, sheetIndex);
		} else if ("xlsx".equals(extension)) {
			// Excel2007
			return readExcel2007(file, sheetName, sheetIndex);
		} else {
			// 既不是Excel2003也不是Excel2007
			throw new IOException("不支持的文件类型！");
		}
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 将从数据库获取的数据导出到Excel里面，具体需求具体调整，这个方法不是唯一情况！
	 * @param theme 工作表主题
	 * @param sheet 工作表对象
	 * @param count 展示的字段个数
	 * @param workbook Excel文档对象
	 * @param styleMap 样式的map集合
	 * @param cnFields 字段中文名称
	 * @param enFields 字段英文名称
	 * @param list 待导出数据集合
	 * @param <T> 集合里的对象
	 * @param size 列数
	 * @param isAutoColSize true：数据列自动设置列宽，false：数据列不自动设置列宽
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @throws Exception 异常信息
	 */
	private static <T> void list2Excel(String theme, Sheet sheet, int count, Workbook workbook,
									   Map<String, Object> styleMap, String[] cnFields,
									   String[] enFields, List<T> list, Integer size,
									   boolean isAutoColSize, boolean isSetBgColor) throws Exception {
		if (theme == null) {
			// 工作表没有主题
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			Row row1 = sheet.createRow(0);
			listData2Excel(workbook, sheet, count, row1, 1, styleMap, cnFields, enFields, list, isSetBgColor);
		} else {
			// 工作表有主题
			// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			CellRangeAddress cra = new CellRangeAddress(0, 0, 0, size - 1);
			sheet.addMergedRegion(cra);
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			Row row1 = sheet.createRow(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			Cell cell1 = row1.createCell(0);
			// 设置单元格内容
			cell1.setCellValue(theme);
			// 定义单元格样式对象并初始化（主题行）（0表示不是数据行）
			CellStyle cellStyle = getSettingCellStyle(workbook, styleMap, sheet, cra, true, false, 0);
			cell1.setCellStyle(cellStyle);
			// 在sheet里创建第二行
			Row row2 = sheet.createRow(1);
			listData2Excel(workbook, sheet, count, row2, 2, styleMap, cnFields, enFields, list, isSetBgColor);
		}
		if (isAutoColSize) {
			// 数据列自动设置列宽
			// 自动设置列宽
			autoColumnSizeOK(sheet, count);
		}
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 将从数据库获取的数据导出到Excel里面
	 * @param workbook Excel文档对象
	 * @param sheet 工作表对象
	 * @param count 展示的字段个数
	 * @param rowN row1第一行，row2第二行
	 * @param num num为1的时候第二行，num为2的时候第三行
	 * @param styleMap 样式的map集合
	 * @param cnFields 字段中文名称
	 * @param enFields 字段英文名称
	 * @param list 待导出数据集合
	 * @param isSetBgColor true：数据行设置间隔背景色，false：数据行不设置间隔背景色（无背景色）
	 * @param <T> 集合里的对象
	 * @throws Exception 异常信息
	 */
	private static <T> void listData2Excel(Workbook workbook, Sheet sheet, int count,
										   Row rowN, int num, Map<String, Object> styleMap,
										   String[] cnFields, String[] enFields, List<T> list, boolean isSetBgColor) throws Exception {
		// 定义单元格样式对象并初始化（列名行）（0表示不是数据行）
		CellStyle cellStyle1 = getSettingCellStyle(workbook, styleMap, null, null, false, true, 0);
		CellStyle cellStyle2 = null;
		if (!isSetBgColor) {
			// 数据行不设置间隔背景色（无背景色）
			// 定义单元格样式对象并初始化（数据行）（-1表示不设置背景色）
			cellStyle2 = getSettingCellStyle(workbook, styleMap, null, null, false, false, -1);
		}
		// 定义单元格对象
		Cell cell;
		for (int m = 0; m < count; m++) {
			cell = rowN.createCell(m);
			// 第n行设置为字段中文名称，即列名（从第一格开始）
			cell.setCellValue(cnFields[m]);
			cell.setCellStyle(cellStyle1);
		}
		// 行
		Row row;
		// 对象（可能是pojo也可能是Map集合）
		Object object;
		for (int n = 0; n < list.size(); n++) {
			// 从第num+1行开始给单元格设置内容
			row = sheet.createRow(n + num);
			if (isSetBgColor) {
				// 数据行设置间隔背景色
				cellStyle2 = getSettingCellStyle(workbook, styleMap, null, null, false, false, row.getRowNum());
			}
			object = list.get(n);
			for (int j = 0; j < count; j++) {
				// 从每行的第一个单元格开始给单元格设置内容，因为key和value是对应的
				set2CellValue(row, j, enFields, object, cellStyle2);
			}
			// 必须配合SXSSFWorkbook使用，否则会报类型转换异常
			/*if (n % 100 == 0) {
				((SXSSFSheet)sheet).flushRows();
            }*/
		}
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 自动设置列宽（已过时）
	 * @param sheet 工作表对象
	 * @param column 总列数
	 */
	@SuppressWarnings("unused")
	private static void autoColumnSize(Sheet sheet, int column) {
		for (int i = 0; i < column; i++) {
			// 可以解决中文问题，但我不怎么会用
			// sheet.setColumnWidth(i,value.toString().length() * 512);

			// 自动设置列宽，不过对中文不适用
			// sheet.autoSizeColumn(i,true);

			// 设置自动列宽
			setAutoColumnSize(sheet, i);
		}
	}

	/**
	 * 强调：该方法不建议使用，因为在导出大批数据的时候会抛数组角标越界异常
	 * 问题主要出现在SheetUtil.getColumnWidth(sheet, column, true);方法上面。
	 * （导出数据库list数据到Excel）
	 * @description 设置自动列宽（半成品）
	 * @param sheet 工作表对象
	 * @param column 列号
	 */
	private static void setAutoColumnSize(Sheet sheet, int column) {
		// 这里的SheetUtil不是我自己写的工具类，而是第三方提供的工具类，需要import
		// 这里的true表示对合并单元格也同样适用
		double width = SheetUtil.getColumnWidth(sheet, column, true);
		// width * 512这里乘以512而不是256是因为要考虑到中文，乘以256适合字母但不适合中文，乘以512会通用
		// sheet.setColumnWidth(column, (int) (width * 512));
		// 选择第三行是要确保该行数据是数据库数据（具体选择第几行根据实际情况调整）
		Row row = sheet.getRow(2);
		Cell cell = row.getCell(column);
		String content = cell.getStringCellValue();
		if (StringUtil.isChinese(content)) {
			// 含有中文
			// 这里的2.5可以根据实际情况进行调整
			sheet.setColumnWidth(column, (int) (width * 256 * 2.5));
		} else {
			// 不含有中文
			// 这里的2可以根据实际情况进行调整
			sheet.setColumnWidth(column, (int) (width * 256 * 2));
		}
	}

	/**
	 * @description 自动设置列宽，该方法完美解决了数据量过大而导致的数组角标越界的问题。（最终方法）
	 * @param sheet 工作表对象
	 * @param col 总列数
	 */
	private static void autoColumnSizeOK(Sheet sheet, int col) {
		// 列宽，默认为8
		Integer columnWidth;
		// 让列宽随着导出的列长自动适应
		for (int colNum = 0; colNum < col; colNum++) {
			// 列宽是以一个字符的1/256的宽度作为一个单位
			// 获取第colNum列当前字符长度
			columnWidth = sheet.getColumnWidth(colNum) / 256;
			// 这里rowNum = 1;即从第2行开始循环是根据具体情况来设置
			// int num = sheet.getLastRowNum();这里的getLastRowNum指的是excel的最大行索引，从0开始，不是行数，而是行数减一。
			// 定义当前行
			Row currentRow;
			// 当前单元格
			Cell currentCell;
			// 列宽，中间变量
			Integer length;
			for (int rowNum = 1; rowNum < sheet.getLastRowNum() + 1; rowNum++) {
				if (sheet.getRow(rowNum) == null) {
					// 当前行不存在就创建
					currentRow = sheet.createRow(rowNum);
				} else {
					// 当前行已存在就获取
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(colNum) != null) {
					currentCell = currentRow.getCell(colNum);
					if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
						// 单元格内容字符长度
						length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							// 将columnWidth设置成该列单元格中内容字符长度最大值
							columnWidth = length;
						}
					}
				}
			}
			if (colNum == 0) {
				// 设置第一列列宽
				// 根据实际情况自行调整
				sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
			} else {
				// 设置其他列列宽
				// 根据实际情况自行调整
				sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
			}
		}
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 给单元格赋值
	 * @param row 行对象
	 * @param j 列号
	 * @param enFields 字段英文名称
	 * @param object 泛型T
	 * @param cellStyle 单元格样式对象
	 * @throws Exception 异常信息
	 */
	private static void set2CellValue(Row row, int j, String[] enFields,
									  Object object, CellStyle cellStyle) throws Exception {
		Cell cell = row.createCell(j);
		Object objValue;
		if (object instanceof Map) {
			// 如果集合包裹类型是Map
			Map<?, ?> map = (Map<?, ?>) object;
			objValue = map.get(enFields[j]);
		} else {
			// 如果集合包裹类型是pojo
			objValue = getFieldValueByNameSequence(enFields[j], object);
		}
		String fieldValue = getFieldValue(objValue);
		if (StringUtils.isBlank(fieldValue)) {
			fieldValue = "/";
		}
		cell.setCellValue(fieldValue);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 根据属性对象获取属性的值
	 * @param objValue 属性对象
	 * @return 返回属性的值
	 */
	private static String getFieldValue(Object objValue) {
		String fieldValue;
		if (objValue instanceof Date) {
			// 日期时间
			fieldValue = CalendarUtil.dateToString((Date)objValue,"yyyy-MM-dd HH:mm:ss");
		} else {
			fieldValue = objValue == null ? "" : objValue.toString();
		}
		return fieldValue;
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 根据带路径或不带路径的属性名获取属性值，
	 * 			即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
	 * @param fieldNameSequence 带路径的属性名或简单属性名
	 * @param o 对象
	 * @return 属性值
	 * @throws Exception 异常信息
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {
		Object value;
		// 将fieldNameSequence根据"."进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, o);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], o);
			// 说明：其中indexOf(".")获取的是.首次出现的位置
			String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 根据字段名获取字段值
	 * @param fieldName 字段名
	 * @param o 对象
	 * @return 返回字段值
	 * @throws Exception 异常信息
	 */
	private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
		// 写法一：
		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			// 如果取得的Field是private的,那么就要调用setAccessible(true),否则会报IllegalAccessException
			field.setAccessible(true);
			// 获取字段值
			return field.get(o);
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}
		// return null;//这行代码不能写，因为不会执行到本行代码，Unreachable statement

		//写法二：
		/*Object value;
		Field field = getFieldByName(fieldName, o.getClass());
		if(field != null){
			field.setAccessible(true);
			value = field.get(o);
		}else{
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}
		return value;*/
	}

	/**
	 * （导出数据库list数据到Excel）
	 * @description 根据字段名获取字段
	 * @param fieldName 字段名
	 * @param clazz 包含该字段的类
	 * @return 返回字段
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();
		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null  &&  superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}
		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * （读取Excel然后导入list到数据库）
	 * @description 获取工作表
	 * @param workbook 工作簿对象
	 * @param sheetName sheet工作表名称
	 * @param sheetIndex sheet工作表索引，索引从0开始
	 * @return 返回工作表对象
	 */
	private static Sheet accessSheet(Workbook workbook, String sheetName, Integer sheetIndex) {
		Sheet sheet = null;
		if (sheetName != null) {
			// 根据工作表的名字获取工作簿中的某个工作表
			sheet = workbook.getSheet(sheetName);
		} else if (sheetIndex != null) {
			// 根据索引获取工作簿中的某个工作表，索引从0开始
			sheet = workbook.getSheetAt(sheetIndex);
		}
		if (sheet == null) {
			// 如果工作表对象为空，则默认获取索引为0的工作表即第一个工作表
			sheet = workbook.getSheetAt(0);
		}
		if (sheet == null) {
			throw new ExcelException("获取工作表失败！");
		}
		return sheet;
	}

	/**
	 * （读取Excel然后导入list到数据库）
	 * @description 获取工作表中的单元格数据（有效数据，即数据库数据）
	 * 		 一行是一个list集合，多行也是一个list集合，即List<List<Object>>
	 * @param sheet 工作表对象
	 * @return 返回list集合
	 */
	private static List<List<String>> cellValue2List(Sheet sheet) {
		// 创建链表式list集合，便于插入操作
		List<List<String>> list = new LinkedList<>();
		// 行
		Row row;
		// 链表式list集合
		List<String> linked;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			// 循环遍历工作表中的行对象
			row = sheet.getRow(i);
			if (row == null) {
				// 如果该行不存在（该行可能被删掉了，行号不连续）
				// 跳出本次循环，继续下个循环
				continue;
			}
			// 创建链表式list集合，快捷插入
			linked = new LinkedList<>();
			// 单元格
			Cell cell;
			// 单元格内容
			String value;
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				// 循环遍历行中的单元格
				cell = row.getCell(j);
				if (cell == null) {
					// 如果单元格为空，则单元格内容直接返回""
					value = "";
				} else {
					// 获取单元格内容（可能是字符串类型，也可能是其他数据类型比如boolean）
					value = getCellValue(cell);
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}

	/**
	 * （读取Excel然后导入list到数据库）
	 * @description 获取单元格内容（可能是字符串类型，也可能是其他数据类型比如boolean）
	 * @param cell 单元格对象
	 * @return 返回单元格内容
	 */
	private static String getCellValue(Cell cell) {
		String value;
		// 数字格式化，取整数部分
		DecimalFormat df = new DecimalFormat("0");
		// 日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 数字格式化，取整数部分以及四舍五入保留2位小数
		DecimalFormat nf = new DecimalFormat("0.00");
		switch (cell.getCellType()) {
			// 仅支持 int，short，char，byte
			// 字符串类型
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			// 数字或者日期类型
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = getValueByType(df, sdf, nf, cell.getCellStyle()
						.getDataFormatString(), cell.getNumericCellValue());
				break;
			// boolean类型
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			// blank类型，比如null，"  "，""
			case HSSFCell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				value = cell.toString();
		}
		return value;
	}

	/**
	 * （读取Excel然后导入list到数据库）
	 * @description 当单元格的值的类型为数字或者日期的时候获取单元格的值
	 * @param df 数字格式化
	 * @param sdf 日期格式化
	 * @param nf 数字格式化
	 * @param type 单元格格式类型（字符串形式）
	 * @param cellValue 单元格内容
	 * @return 返回单元格的值
	 */
	private static String getValueByType(DecimalFormat df, SimpleDateFormat sdf,
										 DecimalFormat nf, String type, double cellValue) {
		String value;
		// 说明：如果该列数据是整数，但是又不属于字符串类型，那么我们必须手动设置该列的单元格格式为@
		if ("@".equals(type)) {
			// 单元格格式为自定义的@，不可控，需要手动设置
			// 取整数部分
			value = df.format(cellValue);
		} else if ("General".equals(type)) {
			// 单元格格式为常规格式
			// 取整数部分以及四舍五入保留2位小数
			value = nf.format(cellValue);
		} else {
			// 单元格格式为日期格式，比如2017-5-3 10:21，对应的自定义格式为 yyyy-m-d h:mm;@
			// 格式化日期
			value = sdf.format(HSSFDateUtil.getJavaDate(cellValue));
		}
		return value;
	}

	/**
	 * 说明：如果文件夹存在同名文件那么旧文件将会被新文件所覆盖，
	 * 关于fileName，如果没有路径，只有文件名，那么文件生成目录为项目所在目录，
	 * 比如D:\workspace\project\ideaProject\demo，
	 * 我们也可以自定义文件生成目录，不过要写清楚文件所在路径，即文件的全路径。
	 * @description 将Excel文档对象写入文件输出流，即生成excel文档
	 * @param workbook excel文档对象
	 * @param fileName excel文件名
	 * @param isCreate 是否是创建 true：是创建；false：是导出
	 * @param isExcel_2003 true：是Excel_2003，false：是Excel_2007
	 */
	private static void writeOut(Workbook workbook, String fileName, boolean isCreate, boolean isExcel_2003) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			// 将Excel文档对象写入文件输出流
			workbook.write(out);
			// 把流里的缓冲数据输出（因为close在关闭流之前会执行该操作，故该步骤可省略）
			out.flush();
			// 关闭文件输出流
			out.close();
			if (isCreate) {
				if (isExcel_2003) {
					log.info("【创建成功！】： office Excel 2003！");
				} else {
					log.info("【创建成功！】： office Excel 2007！");
				}
			} else {
				if (isExcel_2003) {
					log.info("【导出到本地磁盘成功！】： office Excel 2003！");
				} else {
					log.info("【导出到本地磁盘成功！】： office Excel 2007！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("【文件创建（导出）异常！】");
		}
	}

	/**
	 * @description 浏览器下载
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param fileName 文件名，比如sss.xls，或者sss.xlsx
	 * @param workbook excel工作簿对象
	 */
	private static void browserDownload(HttpServletRequest request,
										HttpServletResponse response, String fileName, Workbook workbook) {
		// response.setContentType("application/vnd.ms-excel");
		// response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/octet-stream");
		fileName = FileUtil.handleFileName(request, fileName);
		response.setHeader("Content-disposition","attachment;filename=\"" + fileName + "\"");
		try {
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
			if ("xls".equals(FileUtil.getFileNameExtension(fileName))) {
				log.info("【浏览器下载成功！】： office Excel 2003！");
			} else if ("xlsx".equals(FileUtil.getFileNameExtension(fileName))) {
				log.info("【浏览器下载成功！】： office Excel 2007！");
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("【浏览器下载失败！】");
		}
	}

	/**
	 * @description 获取样式，比如单元格样式以及字体样式
	 * @param isExcel_2003 true：是Excel_2003，false：是Excel_2007
	 * @return 返回样式的map集合
	 */
	private static Map<String, Object> getStyle(boolean isExcel_2003) {
		Map<String, Object> map = new HashMap<>();
		if (isExcel_2003) {
			// 居中
			map.put("alignCenter", HSSFCellStyle.ALIGN_CENTER);
			// 设置背景色（列名行）
			map.put("fillForegroundColor", IndexedColors.PALE_BLUE.getIndex());
			// 设置背景色（数据奇偶行）
			map.put("fillForegroundColorOdd", IndexedColors.LEMON_CHIFFON.getIndex());
			// 设置背景色（数据奇偶行）
			map.put("fillForegroundColorEven", IndexedColors.LIGHT_GREEN.getIndex());
			// 填充背景色
			map.put("fillPattern", HSSFCellStyle.SOLID_FOREGROUND);
			// 设置左边框
			map.put("leftBorder", HSSFCellStyle.BORDER_THIN);
			// 设置右边框
			map.put("rightBorder", HSSFCellStyle.BORDER_THIN);
			// 设置上边框
			map.put("topBorder", HSSFCellStyle.BORDER_THIN);
			// 设置下边框
			map.put("bottomBorder", HSSFCellStyle.BORDER_THIN);
		} else {
			// 居中
			map.put("alignCenter", XSSFCellStyle.ALIGN_CENTER);
			// 设置背景色（列名行）
			map.put("fillForegroundColor", IndexedColors.PALE_BLUE.getIndex());
			// 设置背景色（数据奇偶行）
			map.put("fillForegroundColorOdd", IndexedColors.LEMON_CHIFFON.getIndex());
			// 设置背景色（数据奇偶行）
			map.put("fillForegroundColorEven", IndexedColors.LIGHT_GREEN.getIndex());
			// 填充背景色
			map.put("fillPattern", XSSFCellStyle.SOLID_FOREGROUND);
			// 设置左边框
			map.put("leftBorder", XSSFCellStyle.BORDER_THIN);
			// 设置右边框
			map.put("rightBorder", XSSFCellStyle.BORDER_THIN);
			// 设置上边框
			map.put("topBorder", XSSFCellStyle.BORDER_THIN);
			// 设置下边框
			map.put("bottomBorder", XSSFCellStyle.BORDER_THIN);
		}
		return map;
	}

	/**
	 * @description 获取给单元格设置的样式
	 * @param workbook excel工作簿对象
	 * @param styleMap 样式的map集合
	 * @param sheet 工作表对象
	 * @param cra 合并单元格对象
	 * @param isTitle 是否是标题 true：是标题；false：不是标题
	 * @param isColName 是否是列名 true：是列名；false：不是列名
	 * @param rowNum 行数，用来间隔设置数据行背景色（如果不是数据行赋值为0即可）（赋值为-1表示数据行不设置背景色）
	 * @return 返回给单元格设置的样式
	 */
	private static CellStyle getSettingCellStyle(Workbook workbook, Map<String, Object> styleMap,
												 Sheet sheet, CellRangeAddress cra,
												 boolean isTitle, boolean isColName, int rowNum) {
		// 创建单元格样式对象
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体对象
		Font font = workbook.createFont();
		if (isTitle) {
			// 如果是标题，即主题
			// 使用RegionUtil类为合并后的单元格添加边框
			// 设置下边框
			RegionUtil.setBorderBottom(1, cra, sheet, workbook);
			// 设置左边框
			RegionUtil.setBorderLeft(1, cra, sheet, workbook);
			// 设置右边框
			RegionUtil.setBorderRight(1, cra, sheet, workbook);
			// 设置上边框
			RegionUtil.setBorderTop(1, cra, sheet, workbook);
			// 居中
			cellStyle.setAlignment((Short) styleMap.get("alignCenter"));
		} else {
			if (isColName) {
				// 如果是列名
				// 设置背景色
				cellStyle.setFillForegroundColor((Short) styleMap.get("fillForegroundColor"));
				// 填充背景色
				cellStyle.setFillPattern((Short) styleMap.get("fillPattern"));
			} else if (!Objects.equals(rowNum, -1)) {
				// 如果是数据行（行数赋值不为-1表示数据行需要设置背景色）
				if (rowNum % 2 == 1) {
					// 设置背景色（土黄）
					cellStyle.setFillForegroundColor((Short) styleMap.get("fillForegroundColorOdd"));
					// 填充背景色
					cellStyle.setFillPattern((Short) styleMap.get("fillPattern"));
				} else {
					// 设置背景色（军绿）
					cellStyle.setFillForegroundColor((Short) styleMap.get("fillForegroundColorEven"));
					// 填充背景色
					cellStyle.setFillPattern((Short) styleMap.get("fillPattern"));
				}
			}
			// 居中
			cellStyle.setAlignment((Short) styleMap.get("alignCenter"));
			// 设置下边框
			cellStyle.setBorderBottom((Short) styleMap.get("bottomBorder"));
			// 设置上边框
			cellStyle.setBorderTop((Short) styleMap.get("topBorder"));
			// 设置左边框
			cellStyle.setBorderLeft((Short) styleMap.get("leftBorder"));
			// 设置右边框
			cellStyle.setBorderRight((Short) styleMap.get("rightBorder"));
		}
		cellStyle.setFont(font);
		return cellStyle;
	}

}
