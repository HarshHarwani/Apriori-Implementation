In order to run the program just run the main file AprioriMain.java
The program will ask for support and confidence values through the console. The user will have to enter the values else the program will consider default values - Support = 50 and Confidence = 70.
The association-rule-test-data.txt file is in the current project directory (the code folder) and the path is hard coded in the main file. This file has to be in the same folder where the src folder is.
The template queries are present in the main file (AprioriMain.java) you may modify the queries by changing them in the main file.

The queries are entered in the following format:

Template 1 example: "HEAD HAS (1) of (Gene1_UP,Gene10_DOWN)"
Template 2 example: "SizeOf(RULE) >= 2"
Template 3 example: "BODY HAS (ANY) of (Gene1_UP) OR SizeOf(HEAD) >= 2"
        
The program will print the following details:

Sample Size.
Frequent item sets and their respective support counts.
Total number od frequent item sets.
Total number of assocaiation rules for frequent item sets
Total Time taken for the program to run
All the query results - both the number of results and the actual results (associations) themselves.