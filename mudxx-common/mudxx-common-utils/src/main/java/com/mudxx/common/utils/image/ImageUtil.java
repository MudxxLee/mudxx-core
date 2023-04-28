package com.mudxx.common.utils.image;


import com.mudxx.common.utils.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @description 图片工具类
 * @author laiwen
 * @date 2019-04-25 09:53
 */
@SuppressWarnings("ALL")
public class ImageUtil {

    /**
     * description：对图片进行缩放（图片尺寸变小，存储也会跟着变小）
     * user laiwen
     * time 2019-04-25 09:55
     * @param src 源文件绝对路径（全路径）（缩放前）
     * @param to 目的文件绝对路径（全路径）（缩放后）（如果文件不存在会进行创建，包括文件夹）
     * @param newWidth 指定图片新的宽度（缩放宽度）
     * @param newHeight 指定图片新的高度（缩放高度）
     * @return 如果缩放成功返回true，否则返回false
     */
    public static boolean resize(String src, String to, int newWidth, int newHeight) {
        try {
            //源文件
            File srcFile = new File(src);

            //创建目标文件方法一
            //FileUtils.openOutputStream方法的作用如下解释
            //如果目标文件不存在会进行创建，包括文件夹
            //该方法的作用就是用来创建文件以及文件夹
            //如果没有该方法，文件没有可以创建，文件夹没有就无法创建
            //也就是说如果没有该方法，当目录不存在时会抛找不到文件异常
            /*File toFile = new File(to);
            FileUtils.openOutputStream(toFile);*/

            //创建目标文件方法二（如果目标文件不存在会进行创建，包括文件夹）
            File toFile = FileUtil.createFile(to);

            BufferedImage img = ImageIO.read(srcFile);
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage destImg = new BufferedImage(newWidth, newHeight, img.getType());
            Graphics2D g = destImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
            g.dispose();
            ImageIO.write(destImg, "jpg", toFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
