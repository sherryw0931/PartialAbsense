package com.marlabs.PartialAbsense.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.marlabs.PartialAbsense.service.PartialAbsenseProcesor;

@RestController
public class testController {
	
	
	@Autowired
	PartialAbsenseProcesor processor;
	
	
	@GetMapping("/getPartialAbsense")
	public void testProcessor() throws Exception {
		processor.getPartialAbsenseFile();
	}
	
	
	

}
