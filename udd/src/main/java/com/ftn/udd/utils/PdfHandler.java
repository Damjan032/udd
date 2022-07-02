package com.ftn.udd.utils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;

import com.ftn.udd.elasticsearch.model.IndexingUnit;

public class PdfHandler extends DocumentHandler {

	@Override
	public IndexingUnit getIndexingUnit(File fileCV) {
		IndexingUnit retVal = new IndexingUnit();
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(fileCV, "r"));
			parser.parse();
			String text = getText(parser);
			retVal.getCv().setContent(text);

		} catch (IOException e) {
			System.out.println("Greska pri konvertovanju dokumenta u pdf");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(parser.getPDDocument());
			return text;
		} catch (IOException e) {
			System.out.println("Greska pri konvertovanju dokumenta u pdf");
		}
		return null;
	}
	
	public String getText(PDFParser parser) {
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(parser.getPDDocument());
			return text;
		} catch (IOException e) {
			System.out.println("Greska pri konvertovanju dokumenta u pdf");
		}
		return null;
	}
}
