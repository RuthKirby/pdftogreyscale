package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello pdfs!");
        try {
            PdfToGrey.pdfToImage("src/main/resources/testcolours.pdf", false);
            PdfToGrey.imageToPdf();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}