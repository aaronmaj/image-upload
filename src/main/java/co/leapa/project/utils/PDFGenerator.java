package co.leapa.project.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFGenerator {

    private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static ByteArrayInputStream generatePDF(byte[] imageBytes) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(1);

            Image image = Image.getInstance(imageBytes);
            image.scalePercent(50);
            table.addCell(image);

            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();


        }catch(DocumentException | IOException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

