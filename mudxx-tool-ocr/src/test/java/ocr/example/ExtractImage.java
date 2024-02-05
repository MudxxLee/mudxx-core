package ocr.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExtractImage {
    public static void main(String[] args) throws IOException {
        //加载PDF文档

        PDDocument document = PDDocument.load(PDFExample.createMfileByPath("/Users/laiwen/Documents/temp/temp2.pdf").getInputStream());

        PDPage page = document.getPage(0);
        List<PDImageXObject> images = new ArrayList<>();
        page.getResources().getXObjectNames().forEach(name -> {
            try {
                PDXObject xobject = page.getResources().getXObject(name);
                if (xobject instanceof PDImageXObject) {
                    images.add((PDImageXObject) xobject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        int i = 0;
        for (PDImageXObject image : images) {
            ImageIO.write(image.getImage(), "png", new File("/Users/laiwen/Documents/temp/" + i + ".png"));
            i++;
        }
        document.close();
    }

    public static void ImageReader() {
        File imageFile = new File("path/to/image.jpg");
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imageFile));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = baos.toByteArray();
            System.out.println("Image size: " + imageBytes.length + " bytes");

            bis.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
