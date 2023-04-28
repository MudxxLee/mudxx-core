package com.mudxx.common.utils.excel;

import com.mudxx.common.utils.empty.EmptyUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: Excel导入工具类
 * @author laiwen
 * @date 2021-10-09 17:31:42
 */
@SuppressWarnings("ALL")
public class ExcelImportUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelImportUtil.class);

    /**
     * description: 获取Excel工作表
     * @author laiwen
     * @date 2021-09-28 11:03:18
     * @param file Excel文件
     * @param sheetName 工作表名称
     * @return 返回Excel工作表
     */
    public static Sheet getExcelSheet(MultipartFile file, String sheetName) {
        // 定义Excel工作簿对象
        Workbook workbook = null;
        // 获取Excel文件名
        String fileName = file.getOriginalFilename();
        if (EmptyUtil.isEmpty(fileName)) {
            throw new ExcelException("Excel文件名不存在！");
        }
        log.info("【Excel文件名】：{}", fileName);
        // 实例化Excel对象
        if (fileName.endsWith("xls")) {
            try {
                // 2003版本
                workbook = new HSSFWorkbook(file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (fileName.endsWith("xlsx")) {
            try {
                // 2007版本
                workbook = new XSSFWorkbook(file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 文件不是Excel文件
            throw new ExcelException("文件不是Excel文件！");
        }
        if (EmptyUtil.isEmpty(workbook)) {
            throw new ExcelException("Excel工作簿不存在！");
        }
        // 获取工作表
        Sheet sheet = workbook.getSheet(sheetName);
        if (EmptyUtil.isEmpty(sheet)) {
            throw new ExcelException("Excel工作表不存在！");
        }
        int rows = sheet.getLastRowNum();
        log.info("【工作表数据行总数】：{}", rows);
        if (rows == 0) {
            // 数据为空，请填写数据
            throw new ExcelException("数据为空");
        }
        return sheet;
    }

    /**
     * description: 获取单元格数据
     * @author laiwen
     * @date 2021-09-27 16:08:03
     * @param cell 单元格对象
     * @return 返回单元格数据
     */
    public static String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    // 数字
                    value = cell.getNumericCellValue() + "";
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            // 日期格式化
                            value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        // 解析cell时候，数字类型默认是double类型的，这里我们进行四舍五入取两位小数
                        value = new DecimalFormat("0.00").format(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    // 字符串
                    value = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    // Boolean类型
                    value = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    // 空值
                    value = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    // 错误类型
                    value = "非法字符";
                    break;
                default:
                    value = "未知类型";
                    break;
            }

        }
        return value.trim();
    }

}
