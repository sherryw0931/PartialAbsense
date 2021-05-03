package com.marlabs.PartialAbsense.service;

import org.springframework.stereotype.Service;

@Service
public class StringToTimeAsMinutesConverter {
	
	public int getTimeAsMinutes(String time) {
		String[] arr = time.trim().split(" ");
		int idx = 1, minutes = 0;
		while (idx < arr.length) {
			if (arr[idx].equals("hours")) {
				minutes += Integer.parseInt(arr[idx-1]) * 60;
			}
			if (arr[idx].equals("minutes")) {
				minutes += Integer.parseInt(arr[idx-1]);
			}
			idx += 2;
		}
		
		return minutes;
	}

}
