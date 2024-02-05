package ocr.example;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.FileWriter;
import java.io.IOException;

public class ExtractTable {
    public static void main(String[] args)throws IOException {
        //加载PDF文档
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromStream(PDFExample.createMfileByPath("/Users/laiwen/Documents/temp/AAA.pdf").getInputStream());

        //创建StringBuilder类的实例
        StringBuilder builder = new StringBuilder();

        //抽取表格
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
        PdfTable[] tableLists ;
        for (int page = 0; page < pdf.getPages().getCount(); page++) {
            tableLists = extractor.extractTable(page);
            if (tableLists != null && tableLists.length > 0) {
                for (PdfTable table : tableLists)
                {
                    int row = table.getRowCount();
                    int column = table.getColumnCount();
                    for (int i = 0; i < row; i++)
                    {
                        for (int j = 0; j < column; j++)
                        {
                            String text = table.getText(i, j);
                            builder.append(text+"\t");
                        }
                        builder.append("\r\n");
                    }
                }
            }
        }

        //将提取的表格内容写入txt文档
        FileWriter fileWriter = new FileWriter("/Users/laiwen/Documents/temp/ttt_t.txt");
        fileWriter.write(builder.toString());
        fileWriter.flush();
        fileWriter.close();
    }

}
