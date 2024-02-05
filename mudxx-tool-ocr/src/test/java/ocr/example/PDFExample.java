package ocr.example;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.ContentType;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author laiw
 * @date 2024/1/25 15:55
 */
public class PDFExample {

    public static void main(String[] args) throws IOException {
//        System.out.print(getPdfFileText("/Users/laiwen/Documents/temp/temp.pdf"));
//        System.out.print(getPdfFileText1("/Users/laiwen/Documents/temp/temp.pdf"));
//        getPdfFileExtractor("/Users/laiwen/Documents/temp/AAA.pdf");
//        getPdfFileExtractor2("/Users/laiwen/Documents/temp/EU.pdf");
        System.out.println(readPdfByPage("/Users/laiwen/Documents/temp/EU.pdf"));
    }

    public static String getPdfFileText(String fileName) throws IOException {
        try (PDDocument document = PDDocument.load(createMfileByPath(fileName).getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }

    public static void getPdfFileExtractor(String fileName) throws IOException {
        try (PDDocument document = PDDocument.load(createMfileByPath(fileName).getInputStream())) {
            PDPageTree pages = document.getPages();
            //创建StringBuilder实例
            StringBuilder sb = new StringBuilder();
            PDPage pdPage;
            //遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
            for(int i = 0; i< pages.getCount(); i++){
                pdPage = pages.get(i);
                sb.append(IOUtils.toString(pdPage.getContents(), "utf-8"));
            }
            FileWriter writer;
            try {
                //将StringBuilder对象中的文本写入到文本文件
                writer = new FileWriter("/Users/laiwen/Documents/temp/ttt.txt");
                writer.write(sb.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getPdfFileExtractor2(String fileName) throws IOException {
        //创建PdfDocument实例
        PdfDocument doc = new PdfDocument();
        //加载PDF文件
        doc.loadFromStream(createMfileByPath(fileName).getInputStream());

        //创建StringBuilder实例
        StringBuilder sb = new StringBuilder();

        PdfPageBase page;
        //遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
        for(int i= 0;i<doc.getPages().getCount();i++){
            page = doc.getPages().get(i);
            sb.append(page.extractText(true));
        }
        FileWriter writer;
        try {
            //将StringBuilder对象中的文本写入到文本文件
            writer = new FileWriter("/Users/laiwen/Documents/temp/ttt1.txt");
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.close();
    }

    /**
     * 用来读取pdf文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readPdfByPage(String fileName) {
        String result = "";
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            PdfReader reader = new PdfReader(fileName);
            reader.setAppendable(true);
            // 对PDF文件进行解析，获取PDF文档页码
            int size = reader.getNumberOfPages();
            for(int i = 1 ; i < size + 1; ){
                //一页页读取PDF文本
                String pageStr = PdfTextExtractor.getTextFromPage(reader,i);
                result = result + pageStr + "\n" + "PDF解析第"+ (i)+ "页\n";
                i= i+1;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }


    public static String getPdfFileText2(String fileName) throws IOException {
        PdfReader reader = new PdfReader(fileName);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        StringBuffer buff = new StringBuffer();
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i,
                    new SimpleTextExtractionStrategy());
            buff.append(strategy.getResultantText());
        }
        return buff.toString();
    }

    /**
     * 根据文件路径，获取MultipartFile对象
     */
    public static MultipartFile createMfileByPath(String path) {
        MultipartFile mFile = null;
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);

            String fileName = file.getName();
            fileName = fileName.substring((fileName.lastIndexOf("/") + 1));
            mFile = new MockMultipartFile(fileName, fileName, ContentType.OCTET_STREAM.getValue(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFile;
    }

}
