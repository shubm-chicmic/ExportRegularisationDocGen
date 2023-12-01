package com.chicmic;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfTableExtractor {
    public static void main(String[] args) {
        try {
            File file = new File("1 - 30 sep.pdf"); // Replace with your PDF file path

            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            FileInputStream inputstream = new FileInputStream(file);
            ParseContext pcontext = new ParseContext();

            // Parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata, pcontext);

            // Get the contents of the PDF
            String pdfContent = handler.toString();
            int targetLineNumber = 3; // Example line number
            int targetRunIndex = 5; // Example run index within the line
            printWordsWithIndexes(pdfContent);
// Split the content into lines
            String[] lines = pdfContent.split("\\r?\\n");

            if (targetLineNumber <= lines.length) {
                // Get the line at the specified line number
                String targetLine = lines[targetLineNumber - 1]; // Adjust for 0-based indexing

                // Split the line into words
                String[] words = targetLine.split("\\s+"); // Split by whitespace

                if (targetRunIndex <= words.length) {
                    // Get the word at the specified run index
                    String targetWord = words[targetRunIndex - 1]; // Adjust for 0-based indexing

                    // Display the extracted word
                    System.out.println("Word at Line " + targetLineNumber + ", Run " + targetRunIndex + ": " + targetWord);
                } else {
                    System.out.println("Specified run index is out of bounds for the line.");
                }
            } else {
                System.out.println("Specified line number is out of bounds.");
            }
            // Display extracted content
            System.out.println("Extracted content from PDF:");
            System.out.println(pdfContent);



            String firstRow = extractFirstRowFromPdfTable(file);

            if (firstRow != null) {
                System.out.println("First row of the table:");
                System.out.println(firstRow);
            } else {
                System.out.println("No table found or insufficient content for table extraction.");
            }
        } catch (IOException | TikaException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public static void printWordsWithIndexes(String content) {
        // Split the content into lines
        String[] lines = content.split("\\r?\\n");

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            // Split the line into words
            String[] words = lines[lineIndex].split("\\s+");

            for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                // Print each word with its line and run index
                System.out.println("Word: " + words[wordIndex] + ", Line: " + (lineIndex + 1) + ", Run: " + (wordIndex + 1));
            }
        }
    }
    private static String extractFirstRowFromPdfTable(File file) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputStream = new FileInputStream(file);
        ParseContext parseContext = new ParseContext();

        // Parsing the document using PDF parser
        PDFParser pdfParser = new PDFParser();
        pdfParser.parse(inputStream, handler, metadata, parseContext);

        // Get the contents of the PDF
        String pdfContent = handler.toString();

        // Split the content by lines to analyze row-wise
        String[] lines = pdfContent.split("\\r?\\n");

        // Assuming the table has headers and data rows, extract the first row
        if (lines.length > 1) {
            return lines[0]; // Return the first row
        } else {
            return null;
        }
    }
}
