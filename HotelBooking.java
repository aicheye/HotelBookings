// import libraries
import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/*
 Programmer: Raymond Zhang, Sean Yang
 Program name: HotelBooking
 Last Modified: 27/05/2024
 Description: Runs the main loop for the login screen and menu selection
 */

public class HotelBooking
{
    // Constant for quit number
    private static final int QUIT_NUM = -1;

    // Current employee in the system
    private static String employeeID = null;

    // Create Scanner
    private static Scanner sc = new Scanner(System.in);

    /*
     Method Name: dateIntToStr
     Return Type: String - Returns the date in dd/mm/yyyy format
     Parameters: int Days- Number of days given by user
     Description: Returns a date given the number of days from the start of the year
     Date Modified:
     * 22/05/2024
       Sean Liu - Created dateConverter to accommodate printing and localized time

     * 24/05/2024
       Raymond Zhang - Formatted date string to be consistent with the main class

     * 27/05/2024
       Sean Yang - Moved dateConverter from Reservations.java to HotelBooking.java
                   Changed name to dateIntToStr to be consistent with other methods in this class
     */
    public static String dateIntToStr(int days)
    {
        String combined;
        LocalDate startDate = LocalDate.of(2024, 1, 1);//puts in start date

        // Calculate the target date by adding the number of days to the start date
        LocalDate targetDate = startDate.plusDays(days);

        // Extract the day, month, and year from the target date
        int day = targetDate.getDayOfMonth();
        int month = targetDate.getMonthValue();
        int year = targetDate.getYear();//runs methods that get the month, day, year of the given date.

        combined = String.format("%02d/%02d/%02d", day, month, year);//combines the date into a dd/mm/yyyy format
        return combined;
    }

    /*
     Method Name: dateStrToInt
     Return Type: int - The number of days pass since the start date (Jan 1, 2024).
                        Will return -1 if date is Dec 31, 2023 or if an invalid date format was given.
     Parameters: String - A date in dd/MM/yyyy
     Description: Converts a formatted date into a number representing the number of days past since Jan 01, 2024.
     Dates modified:
     * 23/05/2024
       Raymond Zhang - Created and finished method.
    */
    public static int dateStrToInt(String dateStr)
    {
        // Declare variables
        int daysBetween;
        LocalDate targetDate;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Try to convert String to date
        try {
            targetDate = LocalDate.parse(dateStr, dateFormat);

            // Calculate days since start
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, targetDate);
        }
        // Invalid date format entered
        catch (DateTimeParseException e) {
            daysBetween = -1;
        }

        return daysBetween;
    }

    /*
     Method Name: getEmployeeID
     Return Type: String - The ID of the employee currently logged in to the system
     Parameters: N/A
     Description: Accessor method for ID of the currently logged in employee
     Dates modified:
     * 17/05/2024
       Raymond Zhang - Created and finished method.

     * 21/05/2024
       Raymond Zhang - Changed the return type to String.

     * 25/05/2024
       Sean Yang - Renamed method to getCurrentEmployee
    */
    public static String getCurrentEmployee() {
        return employeeID;
    }

    /*
     Method Name: getRoomInput
     Return Type: int - A valid room number inputted by the user
     Parameters: int date - The date that the user wants to choose a room from
     Description: Continuously receives input from user until an available room number is entered
     Dates modified:
     * 17/05/2024
       Raymond Zhang - Created and finished method.
                       Yet to be tested.

     * 21/05/2024
       Raymond Zhang - Replaced method calls to Reservation.java with print statements for testing.
       Fixed infinite loop in input validation by reading line outside try-catch block
       Changed error messages to stand out more.
       Removed local declaration of Scanner.

     * 24/05/2024
       Raymond Zhang - Changed method call to checkAvailability to align with new parameters.
       Change quit number to -1

     * 25/05/2024
       Raymond Zhang - Fixed room validation by checking for values less than 100 instead of 0.
    */
    public static int getRoomInput(int date)
    {
        // Declare variables
        int room = -1;
        boolean validRoom = false;
        String line;

        // Get a valid room number from user
        do
        {
            // Display available rooms on given date
            if (Reservations.listAvailableRooms(date))
            {
                // Receive input for room number
                System.out.print("Enter the room number (-1 to cancel): ");
                line = sc.nextLine();
                try {
                    room = Integer.parseInt(line);

                    // User cancels reservation
                    if(room == QUIT_NUM) {
                        validRoom = true;
                    }
                    // Check if the room is invalid
                    else if(room <= 100)
                    {
                        System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                    }

                    // Check if room is available
                    else
                    {
                        validRoom = Reservations.checkAvailability(room, date);
                        if(!validRoom)
                        {
                            System.out.printf("**ERROR: Room %d is not available on day %d.**%n%n", room, date);
                        }
                    }
                }
                // User inputted non-numerical characters
                catch (NumberFormatException e)
                {
                    System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                }

            }
            // No rooms available, break out of loop
            else
            {
                room = QUIT_NUM;
                validRoom = true;
            }

        } while(!validRoom);

        // Return the room number
        return room;
    }

    /*
     Method Name: getDateInput
     Return Type: int - A valid date inputted by the user
     Parameters: N/A
     Description: Continuously receives input from user until a valid (non-negative) date is entered
     Dates modified:
     * 17/05/2024
       Raymond Zhang - Created and finished method.
                       Has yet to be tested.

     * 21/05/2024
       Raymond Zhang - Fixed infinite loop in input validation by reading line outside try-catch block
       Changed error message to stand out more.
       Removed local declaration of Scanner.

     * 23/05/2024
       Raymond Zhang - Changed date to string input.

     * 24/05/2024
       Raymond Zhang - Added an option to quit.

     * 25/05/2024
       Raymond Zhang - Added quit messages
     */
    public static int getDateInput()
    {
        // Declare variable
        int date;
        String dateStr;
        boolean validDate = false;

        // Get the date from user
        do
        {
            System.out.print("Enter the date (-1 to cancel): ");
            dateStr = sc.nextLine();

            // User chose to quit
            if (dateStr.equals(String.valueOf(QUIT_NUM)))
            {
                System.out.println("Operation aborted.");
                date = QUIT_NUM;
                validDate = true;
            }
            // Try to convert date String to numerical value
            else
            {
                date = dateStrToInt(dateStr);
                // Check if the date is invalid
                if(date < 0)
                {
                    System.out.println("**ERROR: Date must be 01/01/2024 or later (DD/MM/YYYY).**\n");
                }
                else
                {
                    validDate = true;
                }
            }

        } while(!validDate);

        // Return the date
        return date;
    }

    /*
     Method Name: getReservation
     Return Type: int[] - A reservation made on a room by the customer on a date ([date, room])
     Parameters: String firstName, String lastName - The name of the customer
     Description: Lists all reservations made by the user and allows the user to select the date
                  and room of one of them.
                  Error will be thrown if the customer has no reservations in the file.
                  The user may choose to abort the cancellation by setting the room to -1.
     Dates modified:
     * 17/05/2024
       Raymond Zhang - Created and finished method.
                       Yet to be tested.

     * 21/05/2024
       Raymond Zhang - Replaced method calls to Reservation.java with print statements for testing.
       Renamed customRooms to customerRooms. Added IOException handling.
       Fixed infinite loop in input validation by reading line outside try-catch block
       Changed error messages to stand out more.
       Removed local declaration of Scanner.

     * 22/05/2024
       Raymond Zhang - Swapped room and date to match with Query method call

     * 23/05/2024
       Raymond Zhang - Changed date to string during input and output.

     * 24/05/2024
       Raymond Zhang - Added an option to quit, made print formatting a bit neater.
       Changed quit value to -1

     * 25/05/2024
      Raymond Zhang - Added a quit message
     */
    public static int[] getReservation(String firstName, String lastName) {
        // Declare variables
        Map<Integer, List<Integer>> customerReservations = new HashMap<Integer, List<Integer>>();
        List<Integer> customerDates = new ArrayList<Integer>();
        int date = -1, room;
        boolean validDate = false, validRoom = false;
        String line;

        // Get customer reservations
        try
        {
            customerReservations = Query.getReservationsCustomer(firstName, lastName); // Reservations made by the customer
        }
        catch (IOException e)
        {
            System.out.println(e + " Problem reading file.");
        }

        // Customer has no reservations in file
        if(customerReservations.isEmpty())
        {
            System.out.printf("**ERROR: %s %s has no reservations in file.**%n%n", firstName, lastName);
            room = QUIT_NUM; // Quit the current operation
        }

        else
        {
            // Get the room from user
            do
            {
                Reservations.listReservations(firstName, lastName);

                System.out.print("Enter the room number (-1 to quit): ");
                line = sc.nextLine();
                try
                {
                    room = Integer.parseInt(line);
                }
                // User inputted non-numerical characters
                catch (NumberFormatException e)
                {
                    room = QUIT_NUM-1;
                }

                // User aborts operation
                if (room == QUIT_NUM)
                {
                    System.out.println("Operation aborted.");
                    validRoom = true;
                    validDate = true; // Skip next loop
                }

                // Check if the room is invalid
                else if (room < 0)
                {
                    System.out.println("**ERROR: Room must be an non-negative integer value.**\n");
                }

                // Check if customer has reservations on that date
                else if (customerReservations.containsKey(room))
                {
                    customerDates = customerReservations.get(room);
                    validRoom = true;
                }

                else
                {
                    System.out.printf("**ERROR: Customer has no reservations for room %d.**%n%n", room);
                }

            } while (!validRoom);

            // Get a valid date number from user
            while (!validDate)
            {
                // Display reserved dates for given room
                for(Integer day : customerDates)
                {
                    System.out.println("  " +  dateIntToStr(day));
                }

                // Receive input for room number
                System.out.print("Enter a reserved date (-1 to quit): ");
                line = sc.nextLine();

                // User chose to abort
                if (line.equals(String.valueOf(QUIT_NUM)))
                {
                    System.out.println("Operation aborted.");
                    room = QUIT_NUM;
                    date = QUIT_NUM;
                    validDate = true;
                }

                else
                {
                    // Convert date to int
                    date = dateStrToInt(line);

                    // Check if the date was valid
                    if (date < 0)
                    {
                        System.out.println("**ERROR: Date must be 01/01/2024 or later (DD/MM/YYYY).**\n");
                    }

                    else
                    {
                        // Check if the date was reserved
                        validDate = customerDates.contains(date);
                        if (!validDate)
                        {
                            System.out.printf("**ERROR: %s %s has not reserved room %d on day %s.**%n%n", firstName, lastName, room, dateIntToStr(date));
                        }
                    }
                }
            }
        }

        // Return the reservation
        return new int[]{room, date};
    }

    /*
     Method Name: login
     Return Type: void
     Parameters: N/A
     Description: Prompts the user to enter their employee ID, then checks if
                  the entered ID is valid and in the system. If so, then the user
                  must enter their corresponding PIN until they get it correct.
                  Alternatively, the user may enter -1 to return to the login screen.
                  Once the user is successfully logged in, the according menu will
                  be displayed depending on if the user is an admin or not.
     Dates Modified:
     * 16/05/2024
       Raymond Zhang - Created method. Implemented user input for employee
       ID and PIN, with input validation loops. Method has yet to be tested,
       UI may need improvement.

     * 17/05/2024
       Raymond Zhang - Reformatted input validation to be consistent with other methods.
       Closed the Scanner. Testing is still not done.

     * 21/05/2024
       Raymond Zhang - Added IOException handling. Changed ID and PIN data type to String.
       Fixed infinite loop in input validation by reading line outside try-catch block
       Changed error messages to stand out more.
       Removed local declaration of Scanner.
       Added newlines to messages to look less cluttered in the console.
       Change the default String value to be null

     * 24/05/2024
       Raymond Zhang - Fixed bug where entering an ID that is valid but isn't in file crashes program.
       Comparison to null should have used ==, not .equals()
       Changed quit value to -1.
       Fixed NullPointerException from assigning null value to queryPin[1] by assigning "" instead
       Sean Yang - Added constants for ID & pin length and welcome message upon login

     * 25/05/2024
       Raymond Zhang - Changed call to the display menu to align with changes made to displayMenu().
     */
    public static void login()
    {
        // Declare variables
        final String ID_LENGTH = "6";
        final String PIN_LENGTH = "4";
        String pin;
        String[] queryPin = {null, null};
        boolean validID = false, validPIN = false;

        // Welcome message
        System.out.println("\nWelcome to the Hotel Booking System!");

        // Run until valid employee ID was found
        do
        {
            // Get employee ID
            System.out.print("Enter employee ID: ");

            employeeID = sc.nextLine();

            // Check if ID is valid (six integers)
            if(employeeID.matches( "^[0-9]{" + ID_LENGTH + "}$"))
            {
                // Check if the employee is in the system
                try
                {
                    queryPin = Query.getEmployee(employeeID);
                }
                catch (IOException e)
                {
                    System.out.println(e + " Problem reading file.");
                }

                if (queryPin[0] == null)
                {
                    System.out.println("**ERROR: ID was not found in system.**\n");
                }

                else
                {
                    validID = true;
                }
            }

            else
            {
                System.out.println("**ERROR: ID must be a six-digit integer.**\n");
            }

        } while(!validID);

        // Run until valid PIN entered or user exits
        do
        {
            // Get PIN
            System.out.print("Enter PIN (-1 to return to login screen): ");
            pin = sc.nextLine();

            // User quit; return to login screen
            if (pin.equals(String.valueOf(QUIT_NUM)))
            {
                queryPin[1] = ""; // Mark as invalid, skip menu display
                validPIN = true;
            }
            // Inputted PIN matches with employee PIN
            else if (pin.equals(queryPin[0])) {
                validPIN = true;
            }
            // PIN was invalid
            else if(!pin.matches("^[0-9]{" + PIN_LENGTH + "}$")) {
                System.out.println("**ERROR: PIN must be a four-digit integer.**\n");
            }
            // PIN does not match
            else {
                System.out.println("**ERROR: PIN does not match.**\n");
            }

        } while (!validPIN);

        // output welcome message
        if (queryPin[1].equals("0")) System.out.println("\nWelcome, " + queryPin[2] + " " + queryPin[3] + "!");

        // Employee menu
        if (queryPin[1].equals("0"))
            displayMenu(false);

        // Admin menu
        else if (queryPin[1].equals("1"))
            displayMenu(true);

        // Reset current employee ID
        employeeID = null;
    }

    /*
     Method Name: displayMenu
     Return Type: void
     Parameters: N/A
     Description: Displays the main menu for regular employees.
     Dates Modified:
     * 16/05/2024
       Raymond Zhang - Created method.
                       Created menu list and implemented selection
       functionality. Options for 1, 2, 3, 8, and invalid cases have been implemented
       but not tested. The UI may need improvement.

     * 17/05/2024
       Raymond Zhang - Finished all cases except 6. Testing has yet to be done.

     * 21/05/2024
       Raymond Zhang - Replaced method calls to Reservation.java, Update.java and Write.java with print statements for testing.
       Added IOException handling to case 7. Changed the PIN datatype to String.
       Fixed infinite loop in input validation by reading line outside try-catch block
       Changed error messages to stand out more.
       Removed local declaration of Scanner.
       Fixed issue with formatted printing in case 7 trying to print a String as an int.
       Added error message for invalid PINs in case 7
       Reset looping variable in case 7

     * 22/05/2024
       Raymond Zhang - Finished all cases. Swapped date and room in case 5 and 6.
       Added a confirmation to continue (by pressing the enter key) to allow for changes to be read.

     * 23/05/2024
       Raymond Zhang - Changed date output to be string

     * 24/05/2024
       Raymond Zhang - Changed method call to checkAvailability to align with new parameters.
       Replaced print statements with finished method calls.
       Added option to quit when entering date
       Fixed issue with room and date being mixed up when canceling a reservation
       Added quit messages

     * 25/05/2024
       Raymond Zhang - Added cases for the admin menu.
       Changed method name to displayMenu and added parameter for employee/admin.
       Refactored variable declaration for clarity
       Fix bug in reservation change where room and date values were not updated

     * 27/05/2024
       Sean Yang - Replaced calls to Update with calls to Reservations
     */
    public static void displayMenu(boolean isAdmin)
    {
        // Declare variables
        final String ID_LENGTH = "6";
        final String PIN_LENGTH = "4";
        int choiceMenu; // Choice for the main menu
        int choiceChange; // Choice for changing reservations menu
        int date, room = -1; // Date and room
        int newDate, newRoom; // New date and room for changing reservations
        int[] res; // Reservation (room, date) for cancelling and changing reservations
        boolean running = true; // Main menu looping condition
        boolean validPIN = false; // Looping condition for entering PIN for changing PIN and adding employee
        boolean changed = false; // Looping condition for changing reservations
        boolean validRoom = false; // Looping condition for getting room num for adding/deleting rooms
        boolean validID = false; // Looping condition for entering ID for adding employee
        String line; // Gets input
        String firstName, lastName; // Customer name for listing/cancelling/changing reservations
        String pin, oldPIN, newPIN; // PIN for changing PIN and adding new employee
        String firstNew, lastNew; // Customer new name for changing reservation name
        String newID; // ID for adding/deleting employee
        String checkID; // Will successfully get a PIN if the ID exists, null otherwise
        String newIsAdmin; // Choice for employee or admin (y/n)
        List<Integer> hotelRooms; // All hotel rooms for adding/deleting rooms

        while (running)
        {
            // Print menu
            System.out.println("\nWhat would you like to do?");
            System.out.println();
            System.out.println("1. List available rooms on a date.");
            System.out.println("2. List reservations on a date.");
            System.out.println("3. List reservations under a name.");
            System.out.println("4. Make a reservation.");
            System.out.println("5. Cancel a reservation.");
            System.out.println("6. Change a reservation.");
            System.out.println("7. Change PIN.");

            // Additional admin options
            if(isAdmin)
            {
                System.out.println("8. Add a hotel room.");
                System.out.println("9. Delete a hotel room.");
                System.out.println("10. Add an employee.");
                System.out.println("11. Delete an employee.");
                System.out.println("12. Logout.");
            }
            else
            {
                System.out.println("8. Logout.");
            }

            System.out.println();
            System.out.print("Enter your selection: ");

            // Get menu choice from user
            line = sc.nextLine();
            try
            {
                choiceMenu = Integer.parseInt(line);
            }
            // User inputted non-numerical characters
            catch (NumberFormatException e)
            {
                choiceMenu = QUIT_NUM;
            }

            // Run actions depending on choice
            switch (choiceMenu)
            {
                // List available rooms on given date
                case 1:
                    // Receive user input for the date
                    date = getDateInput();

                    // List rooms if user did not quit
                    if (date != QUIT_NUM)
                    {
                        Reservations.listAvailableRooms(date);
                    }

                    // Let user know that they quit
                    else
                    {
                        System.out.println("Operation aborted.");
                    }

                    break;

                // List reservations on a given date
                case 2:
                    // Get the date to find available rooms
                    date = getDateInput();

                    // List reservations if user did not quit
                    if(date != QUIT_NUM)
                    {
                        Reservations.listReservations(date);
                    }

                    // Let user know that they quit
                    else {
                        System.out.println("Operation aborted.");
                    }

                    break;

                // List reservations under a name
                case 3:
                    // Get first name
                    System.out.print("Enter the first name: ");
                    firstName = sc.nextLine();

                    // Get last name
                    System.out.print("Enter the last name: ");
                    lastName = sc.nextLine();

                    // List reservations
                    Reservations.listReservations(firstName, lastName);

                    break;

                // Make a reservation
                case 4:
                    // Get first name
                    System.out.print("Enter the first name: ");
                    firstName = sc.nextLine();

                    // Get last name
                    System.out.print("Enter the last name: ");
                    lastName = sc.nextLine();

                    // Get reservation date
                    date = getDateInput();

                    // Get reservation room if user did not quit
                    if (date != QUIT_NUM)
                    {
                        room = getRoomInput(date);

                        // Make reservation if user did not quit
                        if (room != QUIT_NUM)
                        {
                            Reservations.reserveCreate(firstName, lastName, room, date);
                            System.out.printf("Created reservation for %s %s for room %d on day %s.%n", firstName, lastName, room, dateIntToStr(date));
                        }
                    }

                    // Let user know if they chose to quit
                    if (date == QUIT_NUM || room == QUIT_NUM)
                    {
                        System.out.println("Reservation aborted.");
                    }
                    break;

                // Cancel a reservation
                case 5:
                    // Get first name
                    System.out.print("Enter the first name: ");
                    firstName = sc.nextLine();

                    // Get last name
                    System.out.print("Enter the last name: ");
                    lastName = sc.nextLine();

                    // Get the reservation of choice
                    res = getReservation(firstName, lastName);
                    room = res[0];
                    date = res[1];

                    // User did not choose to abort
                    if (room != QUIT_NUM)
                    {
                        Reservations.reserveCancel(firstName, lastName, room, date);
                    }

                    break;

                // Change a reservation
                case 6:
                    // ? Should probably list all reservations

                    // Get first name
                    System.out.print("Enter the first name: ");
                    firstName = sc.nextLine();

                    // Get last name
                    System.out.print("Enter the last name: ");
                    lastName = sc.nextLine();

                    // Get the reservation of choice
                    res = getReservation(firstName, lastName);
                    room = res[0];
                    date = res[1];

                    // User did not choose to abort
                    if (res[0] != QUIT_NUM)
                    {
                        // Loop until valid choice is made
                        do
                        {
                            // Print change options
                            System.out.println("\nWhat would you like to change?");
                            System.out.println();
                            System.out.println("1. Name");
                            System.out.println("2. Date");
                            System.out.println("3. Room");
                            System.out.println("4. Go back to menu");
                            System.out.println();
                            System.out.print("Enter your selection: ");

                            // Get choice from user
                            line = sc.nextLine();
                            try
                            {
                                choiceChange = Integer.parseInt(line);
                            }
                            // User inputted non-numerical characters
                            catch (NumberFormatException e)
                            {
                                choiceChange = QUIT_NUM;
                            }

                            // Run actions depending on choice
                            switch (choiceChange) {
                                // Change name
                                case 1:
                                    // Get new first name
                                    System.out.print("Enter the new first name: ");
                                    firstNew = sc.nextLine();

                                    // Get new last name
                                    System.out.print("Enter the new last name: ");
                                    lastNew = sc.nextLine();

                                    // Change name of reservation
                                    Reservations.reserveChange(firstName, lastName, room, date, firstNew, lastNew);

                                    break;

                                // Change date
                                case 2:

                                    do {
                                        // Get the new date
                                        newDate = getDateInput();

                                        // Let user know that they quit
                                        if(newDate == QUIT_NUM) {
                                            System.out.println("Date change aborted.");
                                            changed = true;
                                        }
                                        // Check if room is available on new date
                                        else if(Reservations.checkAvailability(room, newDate)) {
                                            Reservations.reserveChange(firstName, lastName, false, room, date, newDate);
                                            changed = true;
                                        } else {
                                            System.out.printf("**ERROR: Room %d is unavailable on day %s.**%n%n", room, dateIntToStr(newDate));
                                        }
                                    } while(!changed);

                                    // Reset loop condition
                                    changed = false;

                                    break;

                                // Change room
                                case 3:
                                    do
                                    {
                                        // Get new room
                                        System.out.print("Enter the new room number (-1 to cancel): ");
                                        line = sc.nextLine();

                                        try
                                        {
                                            newRoom = Integer.parseInt(line);

                                            // Let user know they aborted
                                            if (newRoom == QUIT_NUM)
                                            {
                                                System.out.println("Room change aborted.");
                                                changed = true;
                                            }
                                            // Check if new room is available
                                            else if (Reservations.checkAvailability(newRoom, date))
                                            {
                                                Reservations.reserveChange(firstName, lastName, true, date, room, newRoom);
                                                changed = true;
                                            }
                                            else
                                            {
                                                System.out.printf("**ERROR: Room %d is unavailable.**%n%n", newRoom);
                                            }
                                        }
                                        // User inputted non-numerical characters
                                        catch (NumberFormatException e) {
                                            System.out.println("**ERROR: Room number must be an integer.**\n");
                                        }

                                    } while (!changed);

                                    // Reset loop condition
                                    changed = false;

                                    break;

                                // Cancel change
                                case 4:
                                    System.out.println("Returning to main menu...");

                                    break;

                                // Invalid choice made
                                default:
                                    System.out.println("**ERROR: Choice must be an integer from 1 to 4.**");
                                    choiceChange = QUIT_NUM;

                                    break;
                            }
                        } while(choiceChange == QUIT_NUM);
                    }

                    break;

                // Change PIN
                case 7:

                    try
                    {
                        // Employee can only change PIN if they know their old PIN
                        // Get employee's previous PIN for reference
                        oldPIN = Query.getEmployee(employeeID)[0];

                        do
                        {
                            // Get old PIN
                            System.out.print("Enter your old PIN (-1 to cancel): ");
                            pin = sc.nextLine();

                            // PIN matches
                            if(pin.equals(oldPIN))
                            {
                                do
                                {
                                    // Get new PIN
                                    System.out.print("Enter the new PIN (-1 to cancel): ");
                                    newPIN = sc.nextLine();

                                    // New PIN is valid; change PIN and break
                                    if (newPIN.matches("^[0-9]{" + PIN_LENGTH + "}$"))
                                    {
                                        Write.edtPin(employeeID, newPIN);
                                        validPIN = true;
                                    }

                                    // User chooses to quit; break out of loop
                                    else if (newPIN.equals(String.valueOf(QUIT_NUM)))
                                    {
                                        System.out.println("PIN change aborted.");
                                        validPIN = true;
                                    }

                                    // PIN was invalid
                                    else
                                    {
                                        System.out.println("**ERROR: New PIN must be a four-digit integer.**\n");
                                    }

                                } while (!validPIN);
                            }
                            // User chooses to quit; break out of loop
                            else if (pin.equals(String.valueOf(QUIT_NUM)))
                            {
                                System.out.println("PIN change aborted.");
                                validPIN = true;
                            }

                            // Invalid PIN was entered
                            else if (!pin.matches("^[0-9]{" + PIN_LENGTH+ "}$"))
                            {
                                System.out.println("**ERROR: New PIN must be a four-digit integer.**\n");
                            }

                            // PIN does not match
                            else
                            {
                                System.out.println("**ERROR: PIN does not match.**\n");
                            }

                        } while (!validPIN);
                    }

                    catch (IOException e) {
                        System.out.println(e + " Problem reading file.");
                    }

                    // Reset validPIN for next loop
                    validPIN = false;

                    break;

                // Logout or admin options
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    // Admin options
                    if (isAdmin)
                    {
                        switch (choiceMenu)
                        {
                            // Add hotel room
                            case 8:
                                // Try to get all rooms
                                try
                                {
                                    hotelRooms = Query.getAllRooms();

                                    do
                                    {
                                        // Get room from user
                                        System.out.print("Enter a room to add (-1 to cancel): ");
                                        line = sc.nextLine();

                                        // Get room value
                                        try
                                        {
                                            room = Integer.parseInt(line);

                                            // User aborted
                                            if (room == QUIT_NUM)
                                            {
                                                System.out.println("Room addition aborted.");
                                                validRoom = true;
                                            }

                                            // The room is invalid
                                            else if (room <= 100)
                                            {
                                                System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                                            }

                                            // Room already exists
                                            else if (hotelRooms.contains(room))
                                            {
                                                System.out.printf("**ERROR: Room %d already exists.**%n%n", room);
                                            }

                                            // Valid Room entered
                                            else
                                            {
                                                Write.addRoom(room);
                                                System.out.printf("Room %d was successfully added to the hotel.%n", room);
                                                validRoom = true;
                                            }
                                        }

                                        // User inputted non-numerical characters
                                        catch (NumberFormatException e)
                                        {
                                            System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                                        }

                                    } while (!validRoom);
                                }
                                catch (IOException e)
                                {
                                    System.out.println(e +  " Problem reading file.");
                                }

                                    // Reset looping condition
                                    validRoom = false;

                                break;

                            // Delete hotel room
                            case 9:
                                // Try to get all rooms
                                try
                                {
                                    hotelRooms = Query.getAllRooms();

                                    do
                                    {
                                        // Get room from user
                                        System.out.print("Enter a room to delete (-1 to cancel): ");
                                        line = sc.nextLine();

                                        // Get room value
                                        try
                                        {
                                            room = Integer.parseInt(line);

                                            // User aborted
                                            if (room == QUIT_NUM)
                                            {
                                                System.out.println("Room deletion aborted.");
                                                validRoom = true;
                                            }
                                            // The room is invalid
                                            else if (room <= 100)
                                            {
                                                System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                                            }
                                            // Room does not exist
                                            else if (!hotelRooms.contains(room))
                                            {
                                                System.out.printf("**ERROR: Room %d does not exist.**%n%n", room);
                                            }
                                            // Valid Room entered
                                            else
                                            {
                                                Write.delRoom(room);
                                                System.out.printf("Room %d was removed from the hotel.%n", room);
                                                validRoom = true;
                                            }
                                        }
                                        // User inputted non-numerical characters
                                        catch (NumberFormatException e)
                                        {
                                            System.out.println("**ERROR: Room number must be an integer greater than or equal to 100.**\n");
                                        }

                                    } while (!validRoom);
                                }
                                catch (IOException e)
                                {
                                    System.out.println(e +  " Problem reading file.");
                                }

                                // Reset looping condition
                                validRoom = false;

                                break;

                            // Add employee
                            case 10:
                                // Get employee ID
                                do
                                {
                                    System.out.print("Enter the new employee's id (-1 to quit): ");
                                    newID = sc.nextLine();

                                    // Check if ID is valid (six integers)
                                    if (newID.matches( "^[0-9]{" + ID_LENGTH + "}$"))
                                    {
                                        // Check if the employee is in the system
                                        try
                                        {
                                            checkID = Query.getEmployee(newID)[0];

                                            // Employee is already in the system, cannot add as new employee
                                            if (checkID != null)
                                            {
                                                System.out.println("**ERROR: ID is already in system.**\n");
                                            }
                                            else
                                            {
                                                validID = true;
                                            }
                                        }
                                        catch (IOException e)
                                        {
                                            System.out.println(e + " Problem reading file.");
                                        }

                                    }

                                    // User aborted operation
                                    else if (newID.equals(String.valueOf(QUIT_NUM)))
                                    {
                                        System.out.println("Employee addition aborted.");
                                        validID = true;
                                    }

                                    // ID was invalid
                                    else
                                    {
                                        System.out.println("**ERROR: ID must be a six-digit integer.**\n");
                                    }

                                } while (!validID);

                                // Get new name
                                System.out.print("Enter the new employee's first name: ");
                                firstName = sc.nextLine();
                                System.out.print("Enter the new employee's last name: ");
                                lastName = sc.nextLine();

                                // Get employee PIN
                                do
                                {
                                    System.out.print("Enter the new employee's PIN (-1 to cancel): ");
                                    newPIN = sc.nextLine();

                                    // New PIN is valid; change PIN and break
                                    if (newPIN.matches("^[0-9]{" + PIN_LENGTH + "}$"))
                                    {
                                        validPIN = true;
                                    }

                                    // User chooses to quit; break out of loop
                                    else if(newPIN.equals(String.valueOf(QUIT_NUM)))
                                    {
                                        System.out.println("Employee addition aborted.");
                                        validPIN = true;
                                    }

                                    // PIN was invalid
                                    else
                                    {
                                        System.out.println("**ERROR: New PIN must be a four-digit integer.**\n");
                                    }

                                } while (!validPIN);

                                // Determine if the new employee is admin or not
                                do
                                {
                                    // Get choice
                                    System.out.print("Is the new employee an admin (y/n, -1 to cancel)? ");
                                    newIsAdmin = sc.nextLine().toLowerCase(); // convert to lower for case insensitivity

                                    switch (newIsAdmin)
                                    {
                                        // Add admin and quit
                                        case "yes":
                                        case "y":
                                            try
                                            {
                                                Write.addEmployee(newID, firstName, lastName, newPIN, "1");
                                                System.out.printf("Successfully added %s %s with id %s and PIN %s as admin.%n", firstName, lastName, newID, newPIN);
                                                newIsAdmin = String.valueOf(QUIT_NUM);

                                                break;
                                            }
                                            catch (IOException e)
                                            {
                                                System.out.println(e + " Problem reading file.");
                                            }
                                        // Add employee and quit
                                        case "no":
                                        case "n":
                                            try
                                            {
                                                Write.addEmployee(newID, firstName, lastName, newPIN, "0");
                                                System.out.printf("Successfully added %s %s with id %s and PIN %s as employee.%n", firstName, lastName, newID, newPIN);
                                                newIsAdmin = String.valueOf(QUIT_NUM);
                                            }
                                            catch( IOException e)
                                            {
                                                System.out.println(e + " Problem reading file.");
                                            }
                                            break;

                                        // Quit
                                        case "-1":
                                            System.out.println("Employee addition aborted.");
                                            break;

                                        // Invalid choice made
                                        default:
                                            System.out.println("**ERROR: Please enter y, n, or -1.**\n");
                                            break;
                                    }
                                } while (!newIsAdmin.equals(String.valueOf(QUIT_NUM)));

                                // Reset looping conditions
                                validID = false;
                                validPIN = false;

                                break;

                            // Delete employee
                            case 11:
                                // Run until valid employee ID was found
                                do
                                {
                                    // Get employee ID
                                    System.out.print("Enter employee ID (-1 to quit): ");

                                    newID = sc.nextLine();

                                    // Check if ID is valid (six integers)
                                    if (newID.matches("^[0-9]{" + ID_LENGTH + "}$")) {
                                        // Check if the employee is in the system
                                        try
                                        {
                                            checkID = Query.getEmployee(newID)[0];
                                            // Employee not in the system
                                            if (checkID == null)
                                            {
                                                System.out.println("**ERROR: ID was not found in system.**\n");
                                            }
                                            // Employee in system; delete employee
                                            else
                                            {
                                                try
                                                {
                                                    Write.delEmployee(newID);
                                                    System.out.printf("Removed employee %s from the system.%n", newID);
                                                }
                                                catch (IOException e)
                                                {
                                                    System.out.println(e + " Problem reading file.");
                                                }
                                                validID = true;
                                            }
                                        }
                                        catch (IOException e)
                                        {
                                            System.out.println(e + " Problem reading file.");
                                        }

                                    }

                                    // User aborted operation
                                    else if (newID.equals(String.valueOf(QUIT_NUM)))
                                    {
                                        System.out.println("Employee deletion aborted.");
                                        validID = true;
                                    }

                                    // ID was invalid
                                    else
                                    {
                                        System.out.println("**ERROR: ID must be a six-digit integer.**\n");
                                    }

                                } while (!validID);

                                // Reset looping condition
                                validID = false;

                                break;

                            // Admin logout
                            case 12:
                                System.out.println("Heading to login screen...");
                                running = false;
                                break;
                        }
                    }

                    // Regular employee logs out
                    else if (choiceMenu == 8)
                    {
                        System.out.println("Heading to login screen...");
                        running = false;
                    }
                    // Regular employee made invalid choice
                    else
                    {
                        System.out.println("**ERROR: Choice must be an integer from 1 to 8.**\n");
                    }

                    break;

                // Invalid choice
                default:
                    System.out.printf("**ERROR: Choice must be an integer from 1 to %d.**\n", (isAdmin?12:8));
                    break;
            }
            System.out.println("(Press Enter to continue)");
            sc.nextLine();
        }
    }

    /*
     Dates Modified:
     * 16/05/2024
     * Raymond Zhang - Created method.
                       Implemented the main loop.
    */
    public static void main(String[] args)
    {
        // Declare variables
        boolean running = true;

        // Program should keep running until forced termination
        // Always returns to home (login) screen
        // noinspection ConstantValue, LoopConditionNotUpdatedInsideLoop
        while (running)
        {
            login();
        }
    }
}