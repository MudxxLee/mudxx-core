package com.mudxx.common.utils.mail.mo;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明 --- 需要设置：发件人邮箱、收件人邮箱、抄送(可选)、暗送(可选)、主题、内容，以及附件(可选)
 * 在创建了Mail对象之后
 * 可以调用它的setSubject()、setContent()，设置主题和正文
 * 也可以调用setFrom()和addToAddress()，设置发件人和添加收件人。
 * 也可以调用addAttach()添加附件
 * 创建AttachBean：new AttachBean(new File("..."), "fileName");
 * @description 邮件类
 * @author laiwen
 * @date 2019-07-20 13:11
 */
@SuppressWarnings("ALL")
public class Mail {

    /**
     * 发件人邮箱
     */
    private String from;

    /**
     * 收件人邮箱（可以有多个）
     */
    private StringBuilder toAddress = new StringBuilder();

    /**
     * 抄送邮箱（可以有多个）
     */
    private StringBuilder ccAddress = new StringBuilder();

    /**
     * 暗送邮箱（可以有多个）
     */
    private StringBuilder bccAddress = new StringBuilder();

    /**
     * 主题
     */
    private String subject;

    /**
     * 正文
     */
    private String content;

    /**
     * 附件列表
     */
    private List<AttachBean> attachList = new ArrayList<>();

    public Mail() {}

    public Mail(String from, String to) {
        this(from, to, null, null);
    }

    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.toAddress.append(to);
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getToAddress() {
        return toAddress.toString();
    }
    public void addToAddress(String to) {
        if (toAddress.length() > 0) {
            toAddress.append(",");
        }
        toAddress.append(to);
    }

    public String getCcAddress() {
        return ccAddress.toString();
    }
    public void addCcAddress(String cc) {
        if(ccAddress.length() > 0) {
            ccAddress.append(",");
        }
        ccAddress.append(cc);
    }

    public String getBccAddress() {
        return bccAddress.toString();
    }
    public void addBccAddress(String bcc) {
        if(bccAddress.length() > 0) {
            bccAddress.append(",");
        }
        bccAddress.append(bcc);
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachBean> getAttachList() {
        return attachList;
    }
    public void addAttach(AttachBean attachBean) {
        attachList.add(attachBean);
    }

}
