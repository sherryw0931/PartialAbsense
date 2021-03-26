# Task: Partial Absense
Write a Spring Boot Application to identify all the participants who did not attend full session.

### Requirement:

#### 1. There are 2 input files
   1.1 Input File 1: Java April 2020- Session 10 - Attendee Report.xlsx 
   
   This file contains the attendance report of a particular session.
       
   1.2 Input File 2: ContactsList.xlsx
   
   This file contains the complete Contacts List of a batch.

#### 2. Content
You need to check the total duration of the session from Attendee Report (Java April 2020- Session 10 - Attendee Report.xlsx) and identify all those participants who did not attend the session in full length. There is a Time in Session column in the report which you must compare with Duration (Cell C5) If the difference is more than 30 min, then that participant record from the ContactsList.xlsx will be part of the Output file.

#### For example:
As per our sample attendance report here, the session was for 3 hr & 4 min. So, all those who attended it only for 2.5 hrs must be in the output file.

#### 3. Tech Stack to be used
   3.1 Spring Boot

   3.2 Apache POI

   3.3 Eclipse STS
