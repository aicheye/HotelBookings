import java.util.*; // Import libraries
import java.io.*;

// TODO: Testing

/*
 Programmer: Raymond Zhang
 Program name: HotelBooking
 Last Modified: 17/05/2024
 Description: Runs the main loop for the login screen and menu selection

 Known Errors:
    * Reservations class not yet implemented
    * Update class not yet implemented
    * Write.edtPin not yet implemented
*/

public class HotelBooking {

    // Constant for quit number
    private static final int QUIT_NUM = 0;

    // Current employee in the system
    private static String employeeID = "";

    // Create Scanner
    private static Scanner sc = new Scanner(System.in);

    /*
     Method Name: getEmployeeID
     Return Type: String - The ID of the employee currently logged in to the system
     Parameters: N/A
     Description: Accessor method for ID of the currently logged in employee
     Dates modified:
     * 17/05/2024
     * Raymond Zhang - Created and finished method.

     * 21/05/2024
     * Raymond Zhang - Changed data type to String.
    */
    public static String getEmployeeID() {
        return employeeID;
    }

    /*
     Method Name: getRoomInput
     Return Type: int - A valid room number inputted by the user
     Parameters: int date - The date that the user wants to choose a room from
     Description: Continuously receives input from user until an available room number is entered
     Dates modified:
     * 17/05/2024
     * Raymond Zhang - Created and finished method. Has yet to be tested.

     * 21/05/2024
     * Raymond Zhang - Replaced method calls to Reservation.java with print statements for testing.
                       Fixed infinite loop by reading line outside of try-catch block.
                       Changed error messages to stand out more.
                       Removed local declaration of Scanner.
    */
    public static int getRoomInput(int date) {
        // Declare variables
        int room = -1;
        boolean validRoom = false;
        String line;

        // Get a valid room number from user
        do {
            // Display available rooms on given date
            // ! Reservations.listAvailableRooms(date);
            System.out.printf("Listing available rooms for day %d.%n", date);

            // Receive input for room number
            System.out.print("Enter the room number (0 to cancel): ");
            line = sc.nextLine();
            try {
                room = Integer.parseInt(line);

                // User cancels reservation
                if(room == QUIT_NUM) {
                    validRoom = true;
                }
                // Check if room is invalid
                else {
                    validRoom = true; // !Reservations.checkAvailability(date, room);
                    if(!validRoom) {
                        System.out.printf("**ERROR: Room %d is not available on day %d.%n%n**", room, date);
                    }
                }
            }
            // User inputted non-numerical characters
            catch (NumberFormatException e) {
                System.out.println("**ERROR: Room number must be an integer.**\n");
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
     * Raymond Zhang - Created and finished method. Has yet to be tested.

     * 21/05/2024
     * Raymond Zhang - Fixed infinite loop by reading line outside of try-catch block.
                       Changed error message to stand out more.
                       Removed local declaration of Scanner.
    */
    public static int getDateInput() {
        // Declare variable
        int date = -1;
        String line;

        // Get the date from user
        do {
            System.out.print("Enter the date: ");
            line = sc.nextLine();
            try {
                date = Integer.parseInt(line);
            }
            // User inputted non-numerical characters
            catch (NumberFormatException e) {
                date = -1;
            }

            // Check if date is invalid
            if(date < 0) {
                System.out.println("**ERROR: Date must be an non-negative integer value.\n**");
            }

        } while(date < 0);

        // Return the date
        return date;
    }

    /*
     Method Name: getReservation
     Return Type: int[] - A reservation made on a room by the customer on a date ([date, room])
     Parameters: String firstName, String lastName - The name of the customer
     Description: Lists all reservations made by the user and allows user to select the date
                  and room of one of them. Error will be thrown if customer has no reservations
                  in file. User may choose to abort the cancellation by setting the room to 0.
     Dates modified:
     * 17/05/2024
     * Raymond Zhang - Created and finished method. Has yet to be tested.

     * 21/05/2024
     * Raymond Zhang - Replaced method calls to Reservation.java with print statements for testing.
                       Renamed customRooms to customerRooms. Added IOException handling.
                       Fixed infinite loop by reading line outside of try-catch block.
                       Changed error messages to stand out more.
                       Removed local declaration of Scanner.
    */
    public static int[] getReservation(String firstName, String lastName) {
        // Declare variables
        Map<Integer, List<Integer>> customerReservations = new HashMap<Integer, List<Integer>>();
        List<Integer> customerRooms;
        int date = -1, room = -1;
        boolean validDate = false, validRoom = false;
        String line;

        // Get customer reservations
        try {
            customerReservations = Query.customerQuery(firstName, lastName); // Reservations made by the customer
        } catch (IOException e) {
            System.out.println(e + " Problem reading file.");
        }

        // Customer has no reservations in file
        if(customerReservations.isEmpty()) {
            System.out.printf("**ERROR: %s %s has no reservations in file.**%n%n", firstName, lastName);
            room = QUIT_NUM; // Quit current operation
        }
        else {
            // Get the date from user
            do {
                // ! Reservations.listReservations(firstName, lastName);
                System.out.printf("Listing reservations for %s %s.%n", firstName, lastName);

                System.out.print("Enter the date: ");
                line = sc.nextLine();
                try {
                    date = Integer.parseInt(line);
                }
                // User inputted non-numerical characters
                catch (NumberFormatException e) {
                    date = -1;
                }

                // Check if date is invalid
                if(date < 0) {
                    System.out.println("**ERROR: Date must be an non-negative integer value.**\n");
                }
                // Check if customer has reservations on that date
                else if(!customerReservations.containsKey(date)) {
                    System.out.printf("**ERROR: Customer has no reservations on day %d.**%n%n", date);
                } else {
                    customerRooms = customerReservations.get(date);
                    validDate = true;
                }

            } while(!validDate);

            // Get a valid room number from user
            do {
                // Display available rooms on given date
                // ! Reservations.listAvailableRooms(firstName, lastName, date);
                System.out.printf("Listing available rooms for %s %s on %d.%n", firstName, lastName, date);

                // Receive input for room number
                System.out.print("Enter the room number (0 to cancel): ");
                line = sc.nextLine();
                try {
                    room = Integer.parseInt(line);

                    // User aborts cancellation
                    if(room == QUIT_NUM) {
                        validRoom = true;
                    }
                    // Check if room is invalid
                    else {
                        validRoom = true; // ! customerRooms.contains(room);
                        if(!validRoom) {
                            System.out.printf("**ERROR: %s %s has not reserved room %d.**%n%n", firstName, lastName, room);
                        }
                    }
                }
                // User inputted non-numerical characters
                catch (NumberFormatException e) {
                    System.out.println("**ERROR: Room number must be an integer.**\n");
                }

            } while(!validRoom);
        }

        // Return the reservation
        return new int[]{date, room};
    }

    /*
     Method Name: login
     Return Type: void
     Parameters: N/A
     Description: Prompts the user to enter their employee ID, then checks if
                  the entered ID is valid and in the system. If so, then the user
                  must enter their corresponding PIN until they get it correct.
                  Alternatively, the user may enter 0 to return to the login screen.
                  Once the user is successfully logged in, the according  menu will
                  be displayed depending on if the user is an admin or not.
     Dates Modified:
     * 16/05/2024
     * Raymond Zhang - Created method. Implemented user input for employee
       ID and PIN, with input validation loops. Method has yet to be tested,
       UI may need improvement.

     * 17/05/2024
     * Raymond Zhang - Reformatted input validation to be consistent with other methods.
       Closed the Scanner. Testing still not done.

     * 21/05/2024
     * Raymond Zhang - Added IOException handling. Changed ID and PIN data type to String.
                       Fixed infinite loop by reading line outside of try-catch block.
                       Changed error messages to stand out more.
                       Removed local declaration of Scanner.
                       Added newlines to messages to look less cluttered in console
    */
    public static void login() {
        // Declare variables
        String pin = "";
        String[] queryPin = {"", ""};
        boolean validID = false, validPIN = false;

        // Welcome message
        // ? Could be stylized
        System.out.println("\nWelcome to the Hotel Booking System!");

        // Run until valid employee ID was found
        do {
            // Get employee ID
            System.out.print("Enter employee ID: ");

            employeeID = sc.nextLine();

            // Check if ID is valid (six integers)
            // ? Create constant for ID length
            if(employeeID.matches( "^[0-9]{6}$")) {
                // Check if employee is in system
                try {
                    queryPin = Query.employeePinQuery(employeeID);
                }
                catch (IOException e) {
                    System.out.println(e + " Problem reading file.");
                }

                if(queryPin[0].equals("")) {
                    System.out.println("**ERROR: ID was not found in system.**\n");
                } else {
                    validID = true;
                }
            }
            else {
                System.out.println("**ERROR: ID must be a six-digit integer.**\n");
            }

        } while(!validID);

        // Run until valid PIN entered or user exits
        do {
            // Get PIN
            System.out.print("Enter PIN (0 to return to login screen): ");
            pin = sc.nextLine();

            // User quit; return to login screen
            if(pin.equals(String.valueOf(QUIT_NUM))) {
                queryPin[1] = ""; // Mark as invalid, skip menu display
                validPIN = true;
            }
            // Inputted PIN matches with employee PIN
            else if (pin.equals(queryPin[0])) {
                validPIN = true;
            }
            // PIN was invalid
            // ? Create constant for PIN length
            else if(!pin.matches("^[0-9]{4}$")) {
                System.out.println("**ERROR: PIN must be a four-digit integer.**\n");
            }
            // PIN does not match
            else {
                System.out.println("**ERROR: PIN does not match.**\n");
            }

        } while(!validPIN);

        // Display menu according to employee type
        switch(queryPin[1]) {
            // Employee menu
            case "0":
                defaultMenu();
                break;
            // Admin menu
            case "1":
                adminMenu();
                break;
        }

        // Reset current employee ID
        employeeID = "";
    }

    /*
     Method Name: defaultMenu
     Return Type: void
     Parameters: N/A
     Description: Displays the main menu for regular employees.
     Dates Modified:
     * 16/05/2024
     * Raymond Zhang - Created method. Created menu list and implemented selection
       functionality. Options for 1, 2, 3, 8, and invalid cases have been implemented
       but not tested. UI may need improvement.

     * 17/05/2024
     * Raymond Zhang - Finished all cases except 6. Testing has yet to be done.

     * 21/05/2024
     * Raymond Zhang - Replaced method calls to Reservation.java, Update.java and Write.java with print statements for testing.
                       Added IOException handling to case 7. Changed PIN data type to String.
                       Fixed infinite loop by reading line outside of try-catch block.
                       Changed error messages to stand out more.
                       Removed local declaration of Scanner.
                       Fixed issue with formatted printing in case 7 trying to print a String as an int.
                       Added error message for invalid PINs in case 7
                       Reset looping variable in case 7
    */
    public static void defaultMenu() {
        // Declare variables
        int choice = -1, date = -1, room = -1;
        int[] res; // reservation: [date, room]
        boolean running = true, validPIN = false;
        String line, firstName, lastName, pin, oldPIN, newPIN;

        while(running) {
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
            System.out.println("8. Logout.");
            System.out.println();
            System.out.print("Enter your selection: ");

            // Get menu choice from user
            line = sc.nextLine();
            try {
                choice = Integer.parseInt(line);
            }
            // User inputted non-numerical characters
            catch (NumberFormatException e) {
                choice = -1;
            }

            // Run actions depending on choice
            switch(choice) {
                // List available rooms on given date
                case 1:
                    // Receive user input for the date
                    date = getDateInput();

                    // List rooms
                    // ! Reservations.listAvailableRooms(date);
                    System.out.printf("Listing available rooms for day %d.%n", date);
                    break;

                // List reservations on a given date
                case 2:
                    // Get the date to find available rooms
                    date = getDateInput();

                    // List reservations
                    // ! Reservations.listReservations(date);
                    System.out.printf("Listing reservations for day %d.%n", date);
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
                    // ! Reservations.listReservations(firstName, lastName);
                    System.out.printf("Listing reservations for %s %s.%n", firstName, lastName);
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

                    // Get reservation room
                    room = getRoomInput(date);

                    // Make reservation if user did not exit
                    if(room != QUIT_NUM){
                        // ! Update.reserveCreate(firstName, lastName, room, date);
                        System.out.printf("Created reservation for %s %s for room %d on day %d.%n", firstName, lastName, room, date);
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

                    // User did not choose to abort
                    if(res[1] != QUIT_NUM) {
                        // ! Update.reserveCancel(firstName, lastName, res[0], res[1]);
                        System.out.printf("Cancelled reservation for %s %s for room %d on day %d.%n", firstName, lastName, res[1], res[0]);
                    }

                    break;

                // TODO: Change a reservation
                case 6:

                    break;

                // Change PIN
                case 7:

                    try {
                        // Employee can only change PIN if they know their old PIN
                        // Get employee's previous PIN for reference
                        oldPIN = Query.employeePinQuery(employeeID)[0];

                        do {
                            // Get old PIN
                            System.out.print("Enter your old PIN (0 to cancel): ");
                            pin = sc.nextLine();

                            // PIN matches
                            if(pin.equals(oldPIN)) {
                                do {
                                    // Get new PIN
                                    System.out.print("Enter the new PIN (0 to cancel): ");
                                    newPIN = sc.nextLine();

                                    // New PIN is valid; change PIN and break
                                    // ? Create constant for PIN length
                                    if(newPIN.matches("^[0-9]{4}$")) {
                                        // ! Write.edtPin(employeeID, newPIN);
                                        System.out.printf("Changed %s's pin to %s.%n%n", employeeID, newPIN);
                                        validPIN = true;
                                    }
                                    // User chooses to quit; break out of loop
                                    else if(newPIN.equals(String.valueOf(QUIT_NUM))) {
                                        validPIN = true;
                                    }
                                    // PIN was invalid
                                    else {
                                        System.out.println("**ERROR: New PIN must be a four-digit integer.**\n");
                                    }

                                } while (!validPIN);
                            }
                            // User chooses to quit; break out of loop
                            else if(pin.equals(String.valueOf(QUIT_NUM))) {
                                validPIN = true;
                            }
                            // Invalid PIN was entered
                            else if(!pin.matches("^[0-9]{4}$")) {
                                System.out.println("**ERROR: New PIN must be a four-digit integer.**\n");
                            }
                            // PIN does not match
                            else {
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

                // Logout
                case 8:
                    System.out.println("Heading to login screen...");
                    running = false;
                    break;

                // Invalid choice
                default:
                    System.out.println("**ERROR: Choice must be an integer from 1 to 8.**\n");
                    break;
            }
        }
    }

    /*
     Method Name: adminMenu
     Return Type: void
     Parameters: N/A
     Description: Displays the main menu for regular employees.
     Dates Modified:
     * 16/05/2024
     * Raymond Zhang - Created method.
    */
    public static void adminMenu() {

    }

    /*
     Dates Modified:
     * 16/05/2024
     * Raymond Zhang - Created method. Implemented main loop.
    */
    public static void main(String[] args) {
        // Declare variables
        boolean running = true;

        // Program should keep running until forced termination
        // Always returns to home (login) screen
        while(running) {
            login();
        }
    }
}