package org.example;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PdfToGrey {

    public static void pdfToImage(String pathname, boolean doBinary) throws IOException {
        File pdfFile = new File(pathname);
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            System.out.printf("Success. Document has %d pages.", document.getNumberOfPages());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int page = 0;
            ImageType imageType = doBinary ? ImageType.BINARY : ImageType.GRAY;
            for(PDPage pdPage: document.getPages()) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, imageType);
                page++;
                ImageIOUtil.writeImage(bufferedImage, "src/main/resources/image" + "-" + page + ".jpg", 300);
            }

        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void imageToPdf() throws IOException {
        String[] formats = {"png", "jpg", "jpeg", "gif", "bmp"};
        // Create blank PDDocument as the new PDF shell
        PDDocument document = new PDDocument();
        // Read in the directory of images
        Collection<File> files = FileUtils.listFiles(new File("src/main/resources/"), formats, false);
        // Iterate over each image and create a PDF page from it
        for(File file : files) {
            PDImageXObject imgObj = PDImageXObject.createFromFileByContent(file, document);
            int width = imgObj.getWidth();
            int height = imgObj.getHeight();
            PDPage page = new PDPage(new PDRectangle( width, height));
            document.addPage(page);
            var contentStream = new PDPageContentStream( document, page );
            contentStream.drawImage( imgObj, 0, 0 );
            contentStream.close();
        }
        document.save("src/main/resources/testgray.pdf");
        document.close();
    }
}
