package com.chicmic;

public class MainRunner {

	public static final boolean autoDeleteFolder = true;
	public static final String FINWNumber = "003FINW232480063"; // Inward remittance Reference Number
	public static final String FILE_NAME = "For Bank (September).xlsx";
	public static final String rootDirectory = System.getProperty("user.dir");
	public static final String invoiceDirectoriesPath = rootDirectory + "/invoices";
	public static final String documentName = "L1 Request letter for Submission of Export doc.docx";
    public static final String fourPointDeclarationDocumentName = "FOUR POINT DECLARATION.docx";
	public static final String fourPointDeclarationDocumentPath = rootDirectory + "/" + fourPointDeclarationDocumentName;

	public static final String exportRegularisationDocumentName = "EXPORT REGULARISATION TEMPLATE/EXPORT REGULARISATION FORMAT.doc";

	public static void main(String[] args) {
//		FolderHeirarchyAndFourPoint.main(args);
		PdfReadAndDocUpdate.main(args);
	}


}
