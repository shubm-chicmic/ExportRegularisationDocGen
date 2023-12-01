import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfExtractor {
    public static void getRowAndColIndexForTable(String filePath, int tableIndex) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            int tableCount = 0;

            while (pi.hasNext()) {
                Page page = pi.next();
                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    tableCount++;
                    if (tableCount == tableIndex) { // Print content of the tableIndex
                        List<List<RectangularTextContainer>> rows = table.getRows();
                        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                            List<RectangularTextContainer> cells = rows.get(rowIndex);
                            for (int colIndex = 0; colIndex < cells.size(); colIndex++) {
                                RectangularTextContainer content = cells.get(colIndex);
                                String text = content.getText().replace("\r", " ");

                                // Print row and column indices along with cell content
                                System.out.println("Row: " + rowIndex + ", Column: " + colIndex + " - " + text);
                            }
                        }
                        break; // Stop processing after printing the tableIndex
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
    public static String getCellValueForTable(String filePath, int tableIndex, int rowIndex, int colIndex) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            int tableCount = 0;

            while (pi.hasNext()) {
                Page page = pi.next();
                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    tableCount++;
                    if (tableCount == 2) { // Second table found
                        List<List<RectangularTextContainer>> rows = table.getRows();

                        // Check if the target indices are within the table's bounds
                        if (rowIndex >= 0 && rowIndex < rows.size()) {
                            List<RectangularTextContainer> cells = rows.get(rowIndex);

                            if (colIndex >= 0 && colIndex < cells.size()) {
                                RectangularTextContainer cell = cells.get(colIndex);
                                return cell.getText().replace("\r", " ");
                            } else {
                                return "Invalid column index for the specified row.";
                            }
                        } else {
                            return "Invalid row index.";
                        }
                    }
                }
                if (tableCount >= 2) {
                    break; // Stop iterating over pages after the second table
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Table not found in the document.";
    }
}
