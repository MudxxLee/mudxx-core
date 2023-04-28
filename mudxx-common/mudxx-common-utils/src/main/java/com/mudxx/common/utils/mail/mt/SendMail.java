package com.mudxx.common.utils.mail.mt;

import com.mudxx.common.utils.empty.EmptyUtil;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 说明：该工具类比较完美，支持发送带有附件的邮件，并且支持发送多人、抄送多人、暗送多人，推荐使用该工具类
 * @description 发送邮件工具类
 * @author laiwen
 * @date 2019年07月21日 下午3:09:17
 */
@SuppressWarnings("ALL")
public class SendMail {

    /**
     * description：对中文字符进行UTF-8编码
     * user laiwen
     * time 2019-07-21 19:00
     * @param text 待编码的字符串
     * @return 返回经过UTF-8编码的字符串
     */
    public String toChinese(String text) {
        try {
            text = MimeUtility.encodeText(new String(text.getBytes(), "UTF-8"), "UTF-8", "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * description：发送邮件
     * user laiwen
     * time 2019-07-21 18:59
     * @param mb 发送邮件实体Bean对象
     * @return 如果发送邮件成功返回true，否则返回false
     */
    public boolean sendMail(MailBean mb) {
        //SMTP主机
        String host = mb.getHost();

        //发件人的用户名
        final String username = mb.getUsername();

        //发件人的密码
        final String password = mb.getPassword();

        //发件人
        String from = mb.getFrom();

        //收件人
        String to = mb.getTo();

        //抄送人
        String cc = mb.getCc();

        //暗送人
        String bcc = mb.getBcc();

        //邮件主题
        String subject = mb.getSubject();

        //邮件正文
        String content = mb.getContent();

        //附件的文件名
        String fileName;
        //多个附件
        Vector<String> file = mb.getFile();

        Properties props = System.getProperties();
        //设置SMTP的主机
        props.put("mail.smtp.host", host);
        //需要经过验证
        props.put("mail.smtp.auth", "true");

        //创建验证器，获取session对象
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            //创建邮件对象
            MimeMessage msg = new MimeMessage(session);

            //设置发件人
            msg.setFrom(new InternetAddress(from));

            //设置收件人
            String[] tos = to.split(",");
            InternetAddress[] toAddress = new InternetAddress[tos.length];
            for (int i = 0; i < tos.length; i++) {
                toAddress[i] = new InternetAddress(tos[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toAddress);

            //设置抄送人
            if (EmptyUtil.isNotEmpty(cc)) {
                String[] ccs = cc.split(",");
                InternetAddress[] ccAddress = new InternetAddress[ccs.length];
                for (int i = 0; i < ccs.length; i++) {
                    ccAddress[i] = new InternetAddress(ccs[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddress);
            }

            //设置暗送人
            if (EmptyUtil.isNotEmpty(bcc)) {
                String[] bccs = bcc.split(",");
                InternetAddress[] bccAddress = new InternetAddress[bccs.length];
                for (int i = 0; i < bccs.length; i++) {
                    bccAddress[i] = new InternetAddress(bccs[i]);
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddress);
            }

            //设置主题
            msg.setSubject(toChinese(subject));

            Multipart mp = new MimeMultipart();

            //设置正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setText(content);
            mp.addBodyPart(mbpContent);

            //往邮件中添加附件
            if (file != null) {
                Enumeration<String> efile = file.elements();
                while (efile.hasMoreElements()) {
                    MimeBodyPart mbpFile = new MimeBodyPart();
                    fileName = efile.nextElement();
                    FileDataSource fds = new FileDataSource(fileName);
                    mbpFile.setDataHandler(new DataHandler(fds));
                    mbpFile.setFileName(toChinese(fds.getName()));
                    mp.addBodyPart(mbpFile);
                }
            }

            //设置邮件内容
            msg.setContent(mp);
            //设置发送时间
            msg.setSentDate(new Date());
            //发邮件
            Transport.send(msg);
        } catch (MessagingException me) {
            me.printStackTrace();
            return false;
        }
        return true;
    }

}