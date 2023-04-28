package com.mudxx.common.utils.excel;

/**
 * @description Excel导入导出中会出现各种各样的问题，比如：数据源为空、有重复行等，ExcelException异常类，用来处理这些问题。
 * @author laiwen
 * @date 2018年7月6日 上午11:31:12
 */
@SuppressWarnings("ALL")
public class ExcelException extends RuntimeException {

    private static final long serialVersionUID = -8592866006237445797L;

    public ExcelException() {

    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

}
