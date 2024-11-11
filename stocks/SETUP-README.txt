# SETUP-README

## Running the Program from the JAR File

To run the program from the JAR file, use the terminal to navigate to the folder in which the file
is located. Then, use one of the following commands:

### Text-Based Interface
To open the program with the text-based interface, use the command:
java -jar ./Assignment4.jar -text

The welcome message should appear along with the menu options.

### Graphical User Interface
To open the program with the graphical user interface, use the command:
java -jar ./Assignment4.jar

This is also what will happen if you simply double-click on the JAR file.

### Invalid Command-Line Arguments
If any other command-line arguments are provided, the program will display
an error message and quit.

## Program Features

- The program supports all stocks available through the API.
- The program supports all dates for which stock information is available up to the current day,
excluding stocks from today.
- Every time the program is called, it checks if there is an existing file
for the stock of interest.
 It also checks if the file contains yesterday's date.
  - If the file contains yesterday's date, the program retrieves stock information from the file.
  - If the file does not contain yesterday's date, the program makes a call to the API
  and updates the file.
  - If no file exists for the stock, the program uses the API to create a new file.