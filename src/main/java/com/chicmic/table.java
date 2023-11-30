package com.chicmic;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfTableExtractor {
    public static void main(String[] args) {
        try {
            File file = new File("path/to/your/pdf/file.pdf"); // Replace with your PDF file path

            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            FileInputStream inputstream = new FileInputStream(file);
            ParseContext pcontext = new ParseContext();

            // Parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata, pcontext);

            // Get the contents of the PDF
            String pdfContent = handler.toString();

            // Display extracted content
            System.out.println("Extracted content from PDF:");
            System.out.println(pdfContent);
        } catch (IOException | TikaException e) {
            e.printStackTrace();
        }
    }
}
