package com.chicmic;

import com.chicmic.pdfOperations.DocUpdateWithPdfData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfReadAndDocUpdate {
    public static void main(String[] args) {
        String filePath = "1 - 30 sep.pdf";
        PdfTableExtractor.extractTable();
        PdfTableExtractor.getCellValueFromTable(filePath, 1,2, 1);
        PdfTableExtractor.printWordsWithIndices(filePath);
//        DocUpdateWithPdfData docUpdateWithPdfData = new DocUpdateWithPdfData();
////        docUpdateWithPdfData.documentUpdate();
////        readPDF("sample1.pdf");
//        float startX =  220.87439f; // Start X position of the word
//        float startY =  62.411133f; // Start Y position of the word
//        float endX = 269.28534f; // End X position of the word
//        float endY = 729.58887f; // End Y position of the word
//
//
//        List<String> desiredWord = getWordsInPositionRange("sample1.pdf", startX, startY, endX, endY);
//
//        if (!desiredWord.isEmpty()) {
//            System.out.println("Word retrieved: " + desiredWord.get(0));
//        } else {
//            System.out.println("No word found in the specified position range.");
//        }
//    }
//    public static List<String> getWordsInPositionRange(String filePath, float startX, float startY, float endX, float endY) {
//        List<String> wordsInRange = new ArrayList<>();
//
//        try (PDDocument document = PDDocument.load(new File(filePath))) {
//            PDFTextStripper stripper = new PDFTextStripper() {
//                @Override
//                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
//                    super.writeString(text, textPositions);
//
//                    StringBuilder word = new StringBuilder();
//                    float currentX = 0f;
//                    float currentY = 0f;
//
//                    for (TextPosition textPosition : textPositions) {
//                        currentX = textPosition.getX();
//                        currentY = textPosition.getY();
//
//                        // Check if the current position falls within the specified range
//                        if (currentX >= startX && currentX <= endX && currentY >= startY && currentY <= endY) {
//                            String currentWord = textPosition.getUnicode();
//                            word.append(currentWord);
//                        } else {
//                            // If the current word ends, add it to the list
//                            if (word.length() > 0) {
//                                wordsInRange.add(word.toString());
//                                word.setLength(0); // Clear the word buffer for the next word
//                            }
//                        }
//                    }
//                }
//            };
//
//            // Set configurations for text extraction (optional)
//            stripper.setSortByPosition(true); // Process text based on its position
//
//            // Extract text from the PDF
//            stripper.getText(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return wordsInRange;
    }

    // Helper method to check if a position falls within the specified range
    private static boolean isInRange(float x, float y, float startX, float startY, float endX, float endY) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    public static void readPDF(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void startDocument(PDDocument document) throws IOException {
                    super.startDocument(document);
                    System.out.println("PDF Document Information:");
                    System.out.println("Number of Pages: " + document.getNumberOfPages());
                    System.out.println("---------------------------");
                }

                @Override
                protected void startPage(PDPage page) throws IOException {
                    super.startPage(page);
                    int pageNumber = getCurrentPageNo();
                    System.out.println("Page " + pageNumber + ":\n---------------------------");

                }

                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    super.writeString(text, textPositions);
                    System.out.println(text); // Print text content of each page

                    StringBuilder wordWithPosition = new StringBuilder();
                    float startX = 0f;
                    float startY = 0f;
                    float endX = 0f;
                    float endY = 0f;

                    for (TextPosition textPosition : textPositions) {
                        String currentWord = textPosition.getUnicode();

                        // If there's a space, consider it as a word separator
                        if (currentWord.equals(" ")) {
                            if (wordWithPosition.length() > 0) {
                                wordWithPosition.append("Position Range: (")
                                        .append(startX).append(", ").append(startY).append(") to (")
                                        .append(endX).append(", ").append(endY).append(")\n");
                                System.out.println("\u001B[32m" + wordWithPosition.toString() + "\u001B[0m");

                                wordWithPosition.setLength(0); // Clear the word buffer for the next word
                            }
                        } else {
                            if (wordWithPosition.length() == 0) {
                                startX = textPosition.getX(); // Set start X position
                                startY = textPosition.getY(); // Set start Y position
                            }
                            wordWithPosition.append(currentWord); // Append letters to form a word
                            endX = textPosition.getEndX(); // Update end X position
                            endY = textPosition.getEndY(); // Update end Y position
                        }
                    }

                    // Print the last word encountered in the loop (if any)
                    if (wordWithPosition.length() > 0) {
                        wordWithPosition.append(" Position Range: (")
                                .append(startX).append(", ").append(startY).append(") to (")
                                .append(endX).append(", ").append(endY).append(")\n");
                        System.out.println("\u001B[32m" + wordWithPosition.toString() + "\u001B[0m");
                    }
                }


                @Override
                protected void endPage(PDPage page) throws IOException {
                    super.endPage(page);
                    System.out.println("\n---------------------------\n"); // Separator between pages
                }

                @Override
                protected void endDocument(PDDocument document) throws IOException {
                    super.endDocument(document);
                    System.out.println("End of Document");
                }
            };

            // Set configurations for text extraction (optional)
            stripper.setSortByPosition(true); // Process text based on its position

            // Extract text from the PDF
            stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Process the extracted table data
    private static void processTable(List<List<String>> table) {
        for (List<String> row : table) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
