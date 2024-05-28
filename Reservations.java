// import libraries
import java.io.*;
import java.util.*;

/*
 Programmer: Sean Liu, Sean Yang, Raymond Zhang
 Program Name: Reservations
 Last Modified: 5/27/2024
 Description: This class contains methods that allow the user to check the availability of rooms on a given date,
              check if a room is available on a given date, list all reservations for a given person, and list all
              reservations for a given date.
              It also allows the user to create, cancel, and change reservations.
 */

public class Reservations
{
    /*
     Method Name: listAvailableRooms
     Return Type: boolean - True if rooms are available and false otherwise
     Parameters: int date - A day that user inputs from main
     Description: Just prints out all the available rooms on day. Will indicate if no rooms are available.
     Dates modified:
     * 17/05/2024
       Sean Liu - Created the shell and main function of the method

     * 24/05/2024
       Raymond Zhang - Changed the return type to boolean to indicate empty rooms. Improved coding style.
       Sean Yang - Fixed issue where no rooms would be returned on a date beyond the maximum date booked. Changed
                   rooms to be an ArrayList
                
     * 25/05/2024
       Raymond Zhang - Changed print formatting to be consistent with other methods
     */
    public static boolean listAvailableRooms(int date)
    {
        // Declare variables
        int size = 0;
        List<Integer> rooms;
        try
        {
            // Get available rooms
            rooms = Query.getAvailableRooms(date);
            size = rooms.size();//checks how many available rooms there are

            // Check if any rooms are available
            if (size == 0)
            {
                System.out.printf("There are no rooms available on %s.%n%n", HotelBooking.dateIntToStr(date));
            }
            else
            {
                System.out.printf("Rooms available on %s:%n", HotelBooking.dateIntToStr(date));
                // loop over every room available on the date
                for(int i = 0; i<size; i++)
                {
                    System.out.println("  " + rooms.get(i)); // prints out all available rooms
                }
            }
        }

        // catch exceptions
        catch (IOException e)
        {
            System.out.println(e + " Problem reading file.");
        }

        return size != 0; // returns true if there are available rooms
    }


    /*
     Method Name: checkAvailability
     Return Type: Boolean - Returns true or false if room and date match
     Parameters: int Date - A day that user inputs from main
                 int room - Room number being checked for availability
     Description: Returns true of false if room is available on given day
     Dates Modified:
     * 21/05/2024
       Sean Liu - Created the main instructions of the method

     * 24/05/2024
       Sean Yang - Changed the method to use Query.roomAvailable and changed the parameter order
     */
    public static boolean checkAvailability (int room, int date)
    {
        // declare variables
        boolean available = false;

        try
        {
            available = Query.roomAvailable(room, date); //checks if the room is available on the given date
        }

        // catch exceptions
        catch (IOException e)
        {
            System.out.println(e);
        }

        return available; //returns true or false depending on if the conditions were met
    }

    /*
     Method Name: listReservations
     Return Type: void - Prints out the day and room number if person has reservation
     Parameters: String firstName - First name of person
                 String lastName - Last name of person
     Description: Lists the room number and dates the room is booked for
     Dates modified:
      * 21/05/2024
        Sean Liu - Created the main function of method

      * 23/05/2024
        Raymond Zhang - Formatted method

      * 24/05/2024
        Raymond Zhang - Change print format to be consistent with other methods

      * 27/05/2024
        Raymond Zhang - Added message to indicate if no reservations have been made
      */
    public static void listReservations(String firstName, String lastName)
    {
        // declare variables
        List<Integer> days;
        Map<Integer, List<Integer>> rooms;

        try
        {
            // Get reservations of the customer
            rooms = Query.getReservationsCustomer(firstName, lastName);

            // Check if customer has made any reservations
            if(!rooms.isEmpty())
            {
              // loop over each room number
              for (int e : rooms.keySet())
              {
                System.out.printf("%s %s has booked Room %d for:\n", firstName, lastName, e);
                days = rooms.get(e); // gets the dates that the room is booked for
                for (int d : days)
                {
                  System.out.printf("  %s\n", HotelBooking.dateIntToStr(d));// prints out each date that the room is booked for
                }
              }
            }
            // Let user know if no reservations have been made by customer
            else
            {
              System.out.printf("%s %s has not made any reservations.%n%n", firstName, lastName);
            }
        }

        // catch exceptions
        catch (IOException e)
        {
            System.out.println(e);
        }

    }
    /*
     Method Name: listReservations
     Return Type: void - Prints out the day and room number if person has reservation
     Parameters: Integer date - Date number that user is searching for
     Description:  Prints out which rooms on a given specific date
     Dates modified:
     * 21/05/2024
       Sean Liu - Created and completed method

     * 24/05/2024
       Raymond Zhang - Changed method to only use date as parameter
       
     * 25/05/2024
       Raymond Zhang - Change print format to be consistent with other methods
     */
    public static void listReservations(int date)
    {
        // Declare variables
        Map<String, List<Integer>> reservations;

        try {
            reservations = Query.getReservationsDate(date); //gets the reservations that are booked on the given date

            // Check if reservations have been made on the date
            if(reservations.isEmpty()) {
                System.out.printf("No reservations have been made on %s.%n%n", HotelBooking.dateIntToStr(date));
            }
            // Print reservations
            else {
                System.out.printf("The following reservations have been made on %s:%n", HotelBooking.dateIntToStr(date));
                for(String customer : reservations.keySet()) {
                    // output the customer's name
                    System.out.println(customer);
                    // output the room numbers
                    for(int r : reservations.get(customer)) System.out.println("  " + r);
                }
            }
        }
        // Unrecorded date was entered
        catch (IndexOutOfBoundsException e) {
            System.out.printf("No reservations have been made on %s.%n%n", HotelBooking.dateIntToStr(date));
        }
        catch(IOException e) {
            System.out.println(e + " Problem reading file.");
        }
    }

    /*
     Method Name: reserveCreate
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 int room - room number
                 int date - given date
     Description: Creates a reservation for a given room and date
     Dates modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 24/05/2024
       Sean Yang - Update error messages for consistency with HotelBookings

     * 27/05/2024
       Sean Yang - Moved method from Update.java to Reservations.java
     */
    public static void reserveCreate(String firstName, String lastName, int room, int date) {
        try
        {
            // check if the room is available
            if (Query.roomAvailable(room, date))
            {
                // add to reservations
                Write.addReserve(firstName, lastName, room, date);
                System.out.println("Reservation created successfully."); // output success
            }
            else
            {
                System.out.println("*ERROR: The room you are trying to book is not available on that date."); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveCancel
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 int room - room number
                 int date - given date
     Description: Cancels a reservation and outputs if the reservation does not exist
     Dates modified:
     * 23/05/2024
       Sean Yang - reworked function to be more concise

     * 24/05/2024
       Sean Yang - Update error messages for consistency with HotelBookings

     * 27/05/2024
       Sean Yang - Moved method from Update.java to Reservations.java
     */
    public static void reserveCancel(String firstName, String lastName, int room, int date) {
        try
        {
            // check if the reservation exists
            if (Query.reservationExists(firstName, lastName, room, date))
            {
                // remove from reservations
                Write.delReserve(firstName, lastName, room, date);
                System.out.println("Reservation cancelled successfully."); // output success
            }
            else
            {
                System.out.println("**ERROR: The reservation you are trying to cancel does not exist."); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveChange (Changing the name)
     Parameters: String oldFirst - first name of person
                 String oldLast - last name of person
                 int room - room number
                 int date - given date
                 String newFirst - the first name to change to
                 String newLast - the last name to change to
     Description: Changes the name a reservation is under
     Dates modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 24/05/2024
       Sean Yang - Update error messages for consistency with HotelBookings

     * 27/05/2024
       Sean Yang - Moved method from Update.java to Reservations.java
     */
    public static void reserveChange(String oldFirst, String oldLast, int room, int date, String newFirst, String newLast) {
        try
        {
            // check if the reservation exists
            if (Query.reservationExists(oldFirst, oldLast, room, date))
            {
                // change reservation
                Write.edtReserve(oldFirst, oldLast, room, date, newFirst, newLast);
                System.out.println("Reservation changed successfully."); // output success
            }
            else
            {
                System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveChange (changing the date or room)
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 boolean changeRoom - whether the user wants to change the room (false: change the date)
                 int dateOrRoom - either the date or the room, whichever doesn't change
                 int old - the old value of the date/room
                 int now - the new value of the date/room
     Description: Changes a reservation (either changing the date or the room #)
     Dates modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 24/05/2024
       Sean Yang - Update error messages for consistency with HotelBookings

     * 27/05/2024
       Sean Yang - Moved method from Update.java to Reservations.java
     */
    public static void reserveChange(String firstName, String lastName, boolean changeRoom, int dateOrRoom, int old, int now) {
        try
        {
            // runs if the user wants to change the room number
            if (changeRoom)
            {
                // check if the reservation exists
                if (Query.reservationExists(firstName, lastName, old, dateOrRoom))
                {
                    // update reservation
                    Write.edtReserve(firstName, lastName, true, dateOrRoom, old, now);
                    System.out.println("Reservation changed successfully."); // output success
                }
                else
                {
                    System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
                }
            }
            // runs if the user wants to change the date
            if (!changeRoom)
            {
                // check if the reservation exists
                if (Query.reservationExists(firstName, lastName, dateOrRoom, old))
                {
                    // update reservation
                    Write.edtReserve(firstName, lastName, false, dateOrRoom, old, now);
                    System.out.println("Reservation changed successfully."); // output success
                }
                else
                {
                    System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
                }
            }
        }

        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}