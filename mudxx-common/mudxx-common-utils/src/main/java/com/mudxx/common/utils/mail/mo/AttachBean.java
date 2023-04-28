package com.mudxx.common.utils.mail.mo;

import java.io.File;

/**
 * @description 附件类
 * @author laiwen
 * @date 2019-07-20 13:11
 */
@SuppressWarnings("ALL")
public class AttachBean {

    /**
     * 唯一标识符
     */
    private String cid;

    /**
     * 文件对象
     */
    private File file;

    /**
     * 文件名称（无后缀）
     */
    private String fileName;

    public AttachBean() {

    }

    public AttachBean(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
