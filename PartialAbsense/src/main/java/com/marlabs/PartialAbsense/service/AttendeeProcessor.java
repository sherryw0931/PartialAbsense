package com.marlabs.PartialAbsense.service;

import java.util.Set;

import com.marlabs.PartialAbsense.model.People;


public interface AttendeeProcessor {

	public Set<People> getAttendedPeople() throws Exception;

	
}
