package com.framgia.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.framgia.users.bean.BorrowedDetailInfo;
import com.framgia.users.bean.BorrowedInfo;

public class CsvFileWriter {
	// Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	public final static String HEADER_CSV_BORROWED = "Borrowed id, Borrowed code, Username, Fullname, Email, Phone number, Gender, Date intend borrowed, Date intend payment, Date borrowed, "
			+ "Date payment, Status of borrowed, Status of book rent, Book code, Book name, Price, Page number, Category, Publisher";

	@SuppressWarnings("resource")
	public static void writeBorrowedCsv(String fileName, List<BorrowedInfo> borrowedInfo) throws FileNotFoundException {
		Writer fileWriter = null;
		CSVPrinter csvFilePrinter = null;

		// Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormatHeard = CSVFormat.newFormat(',').withHeader(HEADER_CSV_BORROWED)
				.withRecordSeparator(NEW_LINE_SEPARATOR);

		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try {
			// initialize FileWriter object
			fileWriter = new OutputStreamWriter(new FileOutputStream(fileName));

			// Create CSV file header
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormatHeard);

			// initialize CSVPrinter object
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			// Write a new student object list to the CSV file
			for (BorrowedInfo item : borrowedInfo) {
				for (BorrowedDetailInfo borrowedDetail : item.getBorrowedDetail()) {
					List<String> borrowedDataRecord = new ArrayList<String>();

					// Add data
					borrowedDataRecord.add(String.valueOf(item.getBorrowedId()));
					borrowedDataRecord.add(item.getBorrowedCode());

					borrowedDataRecord.add(item.getUserInfo().getUserName());
					borrowedDataRecord.add(item.getUserInfo().getName());
					borrowedDataRecord.add(item.getUserInfo().getEmail());
					borrowedDataRecord.add(item.getUserInfo().getPhone());
					borrowedDataRecord.add(item.getUserInfo().getSex());

					borrowedDataRecord.add(item.getdIntendBorrowed());
					borrowedDataRecord.add(item.getdIntendArrived());
					borrowedDataRecord.add(item.getDateBorrrowed());
					borrowedDataRecord.add(item.getDateArrived());
					borrowedDataRecord.add(item.getStatus());

					borrowedDataRecord.add(borrowedDetail.getStatus());
					borrowedDataRecord.add(borrowedDetail.getBookInfo().getBookCode());
					borrowedDataRecord.add(borrowedDetail.getBookInfo().getName());
					borrowedDataRecord.add(Helper.formatCurrency(borrowedDetail.getBookInfo().getPrice()));
					borrowedDataRecord.add(String.valueOf(borrowedDetail.getBookInfo().getNumberPage()));
					borrowedDataRecord.add(borrowedDetail.getBookInfo().getCategoriesName());
					borrowedDataRecord.add(borrowedDetail.getBookInfo().getPublishersName());

					csvFilePrinter.printRecord(borrowedDataRecord);
				}
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();

			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}
		}
	}
}
