package com.chicmic;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PdfTableExtractor {
    public static void extractTable() {
        try (PDDocument document = PDDocument.load(new File("1 - 30 sep.pdf"))) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            int tableCount = 0;

            while (pi.hasNext()) {
                Page page = pi.next();
                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    tableCount++;
                    if (tableCount == 2) { // Print content of the second table
                        List<List<RectangularTextContainer>> rows = table.getRows();
                        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                            List<RectangularTextContainer> cells = rows.get(rowIndex);
                            for (int colIndex = 0; colIndex < cells.size(); colIndex++) {
                                RectangularTextContainer content = cells.get(colIndex);
                                String text = content.getText().replace("\r", "");

                                // Print row and column indices along with cell content
                                System.out.println("\u001B[32m Row: " + rowIndex + ", Column: " + colIndex + " - " + text + "\u001B[0m");
                            }
                        }
                        break; // Stop processing after printing the second table
                    }
                }
                if (tableCount >= 2) {
                    break; // Stop iterating over pages after the second table
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    public static void main(String[] args) {
//        try {
//            String filePath = "1 - 30 sep.pdf";
//            File file = new File(filePath); // Replace with your PDF file path
//            extractTable();
//            System.out.println("\u001B[33m Cell Value = " + getCellValueFromTable(filePath, 1, 2, 1) + "\u001B[0m");
//            BodyContentHandler handler = new BodyContentHandler();
//            Metadata metadata = new Metadata();
//            FileInputStream inputstream = new FileInputStream(file);
//            ParseContext pcontext = new ParseContext();
//
//            // Parsing the document using PDF parser
//            PDFParser pdfparser = new PDFParser();
//            pdfparser.parse(inputstream, handler, metadata, pcontext);
//
//            // Get the contents of the PDF
//            String pdfContent = handler.toString();
//            int targetLineNumber = 3; // Example line number
//            int targetRunIndex = 5; // Example run index within the line
////            printWordsWithIndexes(pdfContent);
//// Split the content into lines
//            String[] lines = pdfContent.split("\\r?\\n");
//
//            if (targetLineNumber <= lines.length) {
//                // Get the line at the specified line number
//                String targetLine = lines[targetLineNumber - 1]; // Adjust for 0-based indexing
//
//                // Split the line into words
//                String[] words = targetLine.split("\\s+"); // Split by whitespace
//
//                if (targetRunIndex <= words.length) {
//                    // Get the word at the specified run index
//                    String targetWord = words[targetRunIndex - 1]; // Adjust for 0-based indexing
//
//                    // Display the extracted word
//                    System.out.println("Word at Line " + targetLineNumber + ", Run " + targetRunIndex + ": " + targetWord);
//                } else {
//                    System.out.println("Specified run index is out of bounds for the line.");
//                }
//            } else {
//                System.out.println("Specified line number is out of bounds.");
//            }
//            // Display extracted content
//            System.out.println("Extracted content from PDF:");
//            System.out.println(pdfContent);
//
//
//
//            String firstRow = extractFirstRowFromPdfTable(file);
//
//            if (firstRow != null) {
//                System.out.println("First row of the table:");
//                System.out.println(firstRow);
//            } else {
//                System.out.println("No table found or insufficient content for table extraction.");
//            }
//        } catch (IOException | TikaException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            throw new RuntimeException(e);
//        }
//    }
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

    public static String getCellValueFromTable(String filePath, int tableIndex, int targetRowIndex, int targetColIndex) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            while (pi.hasNext()) {
                Page page = pi.next();
                List<Table> tables = sea.extract(page);
                if (tableIndex >= tables.size()) {
                    return null;
                }
                Table table = tables.get(tableIndex);
                List<List<RectangularTextContainer>> rows = table.getRows();

                // Check if the target indices are within the table's bounds
                if (targetRowIndex >= 0 && targetRowIndex < rows.size()) {
                    List<RectangularTextContainer> cells = rows.get(targetRowIndex);

                    if (targetColIndex >= 0 && targetColIndex < cells.size()) {
                        RectangularTextContainer cell = cells.get(targetColIndex);
                        return cell.getText().replace("\r", "");
                    } else {
                        return "Invalid column index for the specified row.";
                    }
                } else {
                    return "Invalid row index.";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Table not found in the document.";
    }

    public static void printWordsWithIndices(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);

                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");

                int paraIndex = 0;
                for (String line : lines) {
                    String[] words = line.split("\\s+");
                    int runIndex = 0;

                    for (String word : words) {
                        if (!word.isEmpty()) {
                            System.out.println("Page: " + (i + 1) + ", Paragraph: " + paraIndex + ", Run: " + runIndex + " - " + word);
                        }
                        runIndex++;
                    }
                    paraIndex++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}