package com.marlabs.PartialAbsense.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marlabs.PartialAbsense.model.People;

@Service
public class PartialAbsenseProcesorImpl implements PartialAbsenseProcesor {

	private final String fullContactInfo = "./datafiles/ContactsList.xlsx";
	private XSSFWorkbook outfile;
	private Set<People> fullyAttended;
	
	@Autowired
	FielLoader fileLoader;
	
	@Autowired
	AttendeeProcessor attendeeProcessor;
	
	@Override
	public void getPartialAbsenseFile() throws Exception {
		XSSFSheet fullContacts = fileLoader.loadFile(fullContactInfo);
		fullyAttended = attendeeProcessor.getAttendedPeople();
		outfile = new XSSFWorkbook();
		XSSFSheet outsheet = outfile.createSheet();
		int[] nameIdxs = getNameIdxes(fullContacts); 
		copyRowsToNewFile(fullContacts, outsheet, nameIdxs);
		// write file as xlsx file
		try {
            FileOutputStream out = new FileOutputStream(new File("./datafiles/PartialAbsenteesList.xlsx"));
            outfile.write(out);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	
	private int[] getNameIdxes(XSSFSheet sheet) {
		int[] idxes = new int[3];
		int obtained = 0;
		outer:
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {
			XSSFRow row = sheet.getRow(r);
			Iterator cellIter = row.cellIterator();
			while (cellIter.hasNext()) {
				XSSFCell cell = (XSSFCell) cellIter.next();
				if (cell.getStringCellValue().equals("First Name")) { 
					idxes[0] = cell.getRowIndex();
					idxes[1] = cell.getColumnIndex(); 
					obtained += 1;
				}
				if (cell.getStringCellValue().equals("Last Name")) { 
					idxes[2] = cell.getColumnIndex(); 
					obtained += 1;
				}
				if (obtained == 2) { 
					break outer; 
				}
			}
		}
		return idxes;
	}

	
	
	private void copyRowsToNewFile(XSSFSheet sourcesheet, XSSFSheet outsheet, int[] nameIdxs) {
		int rId = nameIdxs[0], fnId = nameIdxs[1], lnId = nameIdxs[2];
		int outRowNum = 0;
		for (int r = rId; r <= sourcesheet.getLastRowNum(); r++) {
			XSSFRow sourcerow = sourcesheet.getRow(r);
			if (r == rId) { // title row; directly write into new file
				XSSFRow outrow = outsheet.createRow(outRowNum);
				copyRow(sourcerow, outrow);
				outRowNum += 1;
				continue;
			}
			String fn = sourcerow.getCell(fnId).getStringCellValue();
			String ln = sourcerow.getCell(lnId).getStringCellValue();
			People people = new People(fn, ln);
			if (!fullyAttended.contains(people)) {
				XSSFRow outrow = outsheet.createRow(outRowNum);
				copyRow(sourcerow, outrow);
				outRowNum += 1;
			}
		}
	}
	
	
	
	
	
	private void copyRow(XSSFRow sourcerow, XSSFRow outrow) {
		for (int i = 0; i <= sourcerow.getLastCellNum(); i++) {
			XSSFCell sourcecell = sourcerow.getCell(i);
			XSSFCell outcell = outrow.createCell(i);
			if (sourcecell == null) continue;
			
			outcell.setCellType(sourcecell.getCellTypeEnum());
			switch (sourcecell.getCellTypeEnum()) {
			case STRING:
				outcell.setCellValue(sourcecell.getStringCellValue());
				break;
			case NUMERIC:
				outcell.setCellValue(sourcecell.getNumericCellValue());
				break;
			}
		}
	}
	
	
	
}
