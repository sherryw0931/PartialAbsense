package com.marlabs.PartialAbsense.service;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class FielLoader {

	
	public XSSFSheet loadFile(String fileName) throws Exception{
		FileInputStream fileInputSteram = new FileInputStream(fileName);
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputSteram);
		XSSFSheet sheet = workbook.getSheetAt(0);
		return sheet;
	}
}
