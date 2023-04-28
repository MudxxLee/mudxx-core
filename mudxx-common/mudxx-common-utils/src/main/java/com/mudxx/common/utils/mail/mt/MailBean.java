package com.mudxx.common.utils.mail.mt;

import java.util.Vector;

/**
 * @description 发送邮件实体Bean
 * @author laiwen
 * @date 2019年07月21日 下午3:09:17
 */
@SuppressWarnings("ALL")
public class MailBean {

    /**
     * 发件人邮箱
     */
    private String from;

    /**
     * 收件人邮箱（如果有多个的话以,进行拼接，必须设置）
     */
    private String to;

    /**
     * 抄送人邮箱（如果有多个的话以,进行拼接，如果没有可以不设置）
     */
    private String cc;

    /**
     * 暗送人邮箱（如果有多个的话以,进行拼接，如果没有可以不设置）
     */
    private String bcc;

    /**
     * SMTP主机
     */
    private String host;

    /**
     * 发件人的用户名
     */
    private String username;

    /**
     * 发件人的密码
     */
    private String password;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件正文
     */
    private String content;

    /**
     * 多个附件（元素值其实就是文件的绝对存储路径，比如：E:\qqpic\xxqe.jpg）
     */
    Vector<String> file;

    /**
     * 附件的文件名（该属性暂时用不到，因为我们使用附件原始文件名，即附件文件名我们不做自定义修改）
     */
    private String filename;

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Vector<String> getFile() {
        return file;
    }

    /**
     * description：往可变数组里面添加文件绝对存储路径
     * user laiwen
     * time 2019-07-22 0:48
     * @param fileName 文件绝对存储路径
     */
    public void attachFile(String fileName) {
        if (file == null) {
            file = new Vector<>();
        }
        file.addElement(fileName);
    }

}