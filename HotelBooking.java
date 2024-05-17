import java.util.InputMismatchException;
import java.util.Scanner;

// TODO: Testing

/*
 Programmer: Raymond Zhang
 Program name: HotelBooking
 Last Modified: 17/05/2024
 Description: Runs the main loop for the login screen and menu selection
*/


public class HotelBooking {

    /*
     Method Name: getRoomInput
     Return Type: int - A valid room number inputted by the user
     Parameters: N/A
     Description: Continuously receives input from user until a valid (above 100) room number is entered
     Dates modified:
     * 17/05/2024
       Raymond Zhang - Created and finished method. Has yet to be tested.
    */
    public static int getRoomInput() {
        // Declare variable
        int room = -1;

        // Create new Scanner
        Scanner sc = new Scanner(System.in);

        // Get the room number from user
        do {
            System.out.print("Enter the room number: ");
            try {
                room = sc.nextInt();
            }
            // User inputted non-numerical characters
            // ? Should let user know that they can only enter numbers?
            catch (InputMismatchException e) {
                room = -1;
            }

            // Check if room is invalid
            if(room < 100) {
                System.out.println("Error: Room number must be at least 100.");
            }

        } while(room < 100);

        // Close the Scanner
        sc.close();

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
       Raymond Zhang - Created and finished method. Has yet to be tested.
    */
    public static int getDateInput() {
        // Declare variable
        int date = -1;

        // Create new Scanner
        Scanner sc = new Scanner(System.in);

        // Get the date from user
        do {
            System.out.print("Enter the date: ");
            try {
                date = sc.nextInt();
            }
            // User inputted non-numerical characters
            // ? Should let user know that they can only enter numbers?
            catch (InputMismatchException e) {
                date = -1;
            }

            // Check if date is invalid
            if(date < 0) {
                System.out.println("Error: Date must be an non-negative integer value.");
            }

        } while(date < 0);

        // Close the Scanner
        sc.close();

        // Return the date
        return date;
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
       Raymond Zhang - Created method. Implemented user input for employee
       ID and PIN, with input validation loops. Method has yet to be tested,
       UI may need improvement.
    */
    public static void login() {
        // Declare variables
        int id = -1, pin = -1;
        int[] queryPin;
        boolean validID = false, validPIN = false;

        // Create new Scanner
        Scanner sc = new Scanner(System.in);

        // Welcome message
        // ? Could be stylized
        System.out.println("Welcome to the Hotel Booking System!");

        // Run until valid employee ID was found
        do {
            // Try to get employee ID
            System.out.print("Enter employee ID: ");

            try {
                id = sc.nextInt();
            }
            // User inputted non-numerical characters
            // ? Should let user know that they can only enter numbers?
            catch (InputMismatchException e) {
                id = -1;
            }

            // If id was invalid, skip query
            if(id != -1){
                // Check if employee is in system
                queryPin = Query.employeeQuery(id);
                if(queryPin[0] == -1) {
                    System.out.println("ERROR: ID was not found in system.");
                } else {
                    validID = true;
                }
            }

        } while(!validID);

        // Run until valid PIN entered or user exits
        do {
            // Try to get PIN
            System.out.print("Enter PIN (0 to return to login screen): ");

            try {
                pin = sc.nextInt();
            }
            // User inputted non-numerical characters
            // ? Should let user know that they can only enter numbers?
            catch (InputMismatchException e) {
                pin = -1;
            }

            // Break out of loop and return to log in screen
            // ? Can use return?
            if(pin == 0) {
                queryPin[1] = -1;
                validPIN = true;
            }
            // Check if pin matches system data
            else if(pin != -1) {
                if(pin == queryPin[0]) {
                    validPIN = true;
                } else {
                    System.out.println("ERROR: Incorrect PIN.");
                }
            }
        } while(!validPIN);

        // Display menu according to employee type
        switch(queryPin[1]) {
            // Employee menu
            case 0:
                defaultMenu();
                break;
            // Admin menu
            case 1:
                adminMenu();
                break;
        }
    }

    /*
     Method Name: defaultMenu
     Return Type: void
     Parameters: N/A
     Description: Displays the main menu for regular employees.
     Dates Modified:
     * 16/05/2024
       Raymond Zhang - Created method. Created menu list and implemented selection
       functionality. Options for 1, 2, 3, 8, and invalid cases have been implemented
       but not tested. UI may need improvement.
    */
    public static void defaultMenu() {
        // Declare variables
        int choice = -1, date = -1, room = -1;
        boolean running = true;
        String firstName, lastName;

        // Create new Scanner
        Scanner sc = new Scanner(System.in);

        while(running) {
            // Print menu
            System.out.println("What would you like to do?");
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
            try {
                choice = sc.nextInt();
            }
            // User inputted non-numerical characters
            // ? Should let user know that they can only enter numbers?
            catch (InputMismatchException e) {
                choice = -1;
            }

            // Run actions depending on choice
            switch(choice) {
                // List available rooms on given date
                case 1:
                    // Receive user input for the date
                    date = getDateInput();

                    // List rooms
                    Reservations.listAvailableRooms(date);
                    break;

                // List reservations on a given date
                case 2:
                    // Get the date to find available rooms
                    date = getDateInput();

                    // List reservations
                    Reservations.listReservations(date);
                    break;

                // List reservations under a name
                case 3:

                    // Get first name
                    System.out.print("Enter the first name:");
                    firstName = sc.nextLine();

                    // Get last name
                    System.out.println();
                    System.out.print("Enter the last name: ");
                    lastName = sc.nextLine();

                    // List reservations
                    Reservations.listReservations(firstName, lastName);
                    break;

                // Make a reservation
                case 4:


                    break;

                // Cancel a reservation
                case 5:

                    break;

                // Change a reservation
                case 6:

                    break;

                // Change PIN
                case 7:

                    break;

                // Logout
                case 8:
                    System.out.println("Heading to login screen...");
                    running = false;
                    break;

                // Invalid choice
                default:
                    System.out.println("ERROR: Invalid choice.");
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
       Raymond Zhang - Created method. Created menu list and implemented selection
       functionality. Options for 1, 2, 3, 8, and invalid cases have been implemented
       but not tested. UI may need improvement.
    */
    public static void adminMenu() {

    }

    /*
     Dates Modified:
      * 16/05/2024
        Raymond Zhang - Created method. Implemented main loop.
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