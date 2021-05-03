package com.marlabs.PartialAbsense.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marlabs.PartialAbsense.model.People;


@Service
public class AttendeeProcessorImpl implements AttendeeProcessor {
	
	private final String attendeeInfo = "./datafiles/Java April 2020- Session 10 - Attendee Report.xlsx";
		
	
	@Autowired
	StringToTimeAsMinutesConverter timeConverter;
	
	@Autowired
	FielLoader fileLoader;

	@Override
	public Set<People> getAttendedPeople() throws Exception {
		XSSFSheet attenedInfo = fileLoader.loadFile(attendeeInfo);
		int[] durationAxis = getDurationLocation(attenedInfo);
		XSSFCell durationCell = attenedInfo.getRow(durationAxis[0]+1).getCell(durationAxis[1]);
		int duration = timeConverter.getTimeAsMinutes(durationCell.getStringCellValue());
		return addPeopleMeetRequirement(attenedInfo, durationAxis[0]+2, duration);
	}
	
	
		
	private int[] getDurationLocation(XSSFSheet sheet) {
		int[] axis = new int[2];
		Iterator iterator = sheet.iterator();
		
		rowIterator:
		while (iterator.hasNext()) {
			XSSFRow row = (XSSFRow) iterator.next();
			Iterator cellIter = row.cellIterator();
			while(cellIter.hasNext()) {
				XSSFCell cell = (XSSFCell) cellIter.next();
				if (cell.getStringCellValue().equals("Duration")) {
					axis[0] = cell.getRowIndex();
					axis[1] = cell.getColumnIndex();
					break rowIterator;
				}
			}
		}
		return axis;
	}
	
	
	
	
	private Set<People> addPeopleMeetRequirement(XSSFSheet sheet, int startRowIdx, int duration){
		Set<People> added = new HashSet<>();
		int[] idxes = getFullnameAndTimeInSessionIdx(sheet, startRowIdx);
		int rId = idxes[0], fnId = idxes[1], lnId = idxes[2], timeId = idxes[3];

		for (int rowIdx = rId+1; rowIdx <= sheet.getLastRowNum(); rowIdx++ ) {
			XSSFRow row = sheet.getRow(rowIdx);
			int timeInSession = timeConverter.getTimeAsMinutes(row.getCell(timeId).getStringCellValue());
			if (timeInSession >= duration - 30) {
				String firstName = row.getCell(fnId).getStringCellValue();
				String lastName = row.getCell(lnId).getStringCellValue();
				added.add(new People(firstName, lastName));
			}
		}		
		return added;
	}
	
	
	
	
	private int[] getFullnameAndTimeInSessionIdx(XSSFSheet sheet, int startRowIdx) {
		int[] returnedArr = new int[4];
		int infoCount = 0;
		
		outer:
		for (int rowIdx = startRowIdx; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
			XSSFRow row = sheet.getRow(rowIdx);
			Iterator cellIter = row.cellIterator();
			while(cellIter.hasNext()) {
			XSSFCell cell = (XSSFCell) cellIter.next();
				if (cell.getStringCellValue().equals("First Name")){ 
					returnedArr[0] = cell.getRowIndex();
					returnedArr[1] = cell.getColumnIndex();
					infoCount += 1;
					// System.out.println("---------- FirstName Found");	
				}
				if (cell.getStringCellValue().equals("Last Name")){ 
					returnedArr[2] = cell.getColumnIndex();
					infoCount += 1;
					// System.out.println("---------- Last Name Found");
				}
				if (cell.getStringCellValue().equals("Time in Session")){ 
					returnedArr[3] = cell.getColumnIndex();
					infoCount += 1;
					// System.out.println("---------- Time In Session Found");
				}
			}
			if (infoCount == 3) {
				break outer;
			}
		}
		
		return returnedArr;
	}

	
}
