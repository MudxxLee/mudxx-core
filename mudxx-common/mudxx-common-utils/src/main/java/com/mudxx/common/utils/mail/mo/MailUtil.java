package com.mudxx.common.utils.mail.mo;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;

/**
 * 说明：该工具类不是很完美，因此虽然支持发送带有附件的邮件，但是仍然不是很推荐使用该工具类
 * @description 发送邮件工具类
 * @author laiwen
 * @date 2019-07-20 13:11
 */
@SuppressWarnings("ALL")
public class MailUtil {

    /**
     * description：创建Session
     * user laiwen
     * time 2019-07-20 16:26
     * @param host 服务器主机名（比如smtp.163.com）
     * @param username 发件人用户名
     * @param password 发件人密码
     * @return 返回Session
     */
    public static Session createSession(String host, final String username, final String password) {
        Properties prop = new Properties();
        //指定主机
        prop.setProperty("mail.host", host);

        //指定验证为true
        prop.setProperty("mail.smtp.auth", "true");

        //创建验证器
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // 获取session对象
        return Session.getInstance(prop, auth);
    }

    /**
     * description：发送指定的邮件
     * user laiwen
     * time 2019-07-20 16:29
     * @param session Session信息
     * @param mail mail对象
     * @throws Exception 异常信息
     */
    public static void send(Session session, final Mail mail) throws Exception {
        //创建邮件对象
        MimeMessage msg = new MimeMessage(session);

        //设置发件人
        msg.setFrom(new InternetAddress(mail.getFrom()));

        //设置收件人
        msg.addRecipients(Message.RecipientType.TO, mail.getToAddress());

        //设置抄送
        String cc = mail.getCcAddress();
        if (!cc.isEmpty()) {
            msg.addRecipients(Message.RecipientType.CC, cc);
        }

        //设置暗送
        String bcc = mail.getBccAddress();
        if (!bcc.isEmpty()) {
            msg.addRecipients(Message.RecipientType.BCC, bcc);
        }

        //设置主题
        msg.setSubject(mail.getSubject());

        //创建部件集对象
        MimeMultipart parts = new MimeMultipart();
        //创建一个部件
        MimeBodyPart part = new MimeBodyPart();
        //设置邮件文本内容
        part.setContent(mail.getContent(), "text/html;charset=utf-8");
        //把部件添加到部件集中
        parts.addBodyPart(part);

        //添加附件
        //获取所有附件
        List<AttachBean> attachBeanList = mail.getAttachList();
        if (attachBeanList != null) {
            for (AttachBean attach : attachBeanList) {
                //创建一个部件
                MimeBodyPart attachPart = new MimeBodyPart();
                //设置附件文件
                attachPart.attachFile(attach.getFile());
                //设置附件文件名
                attachPart.setFileName(MimeUtility.encodeText(attach.getFileName()));
                //设置内容ID
                String cid = attach.getCid();
                if(cid != null) {
                    attachPart.setContentID(cid);
                }
                //把部件添加到部件集中
                parts.addBodyPart(attachPart);
            }
        }

        //给邮件设置内容
        msg.setContent(parts);
        //发邮件
        Transport.send(msg);
    }

}
