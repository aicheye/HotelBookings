import java.io.*;
import java.util.*;
import java.time.LocalDate;

/*
 Programmer: Sean Liu
 Program Name: Reservations
 Date: 5/17/2024
 Description: Gives availability of rooms
 */
public class Reservations
{

    /*
    Method Name: listAvailableRooms
    Return Type: boolean - true if rooms are available and false otherwise
    Parameters: int date - a day that user inputs from main
    Description: Just prints out all the available rooms on day. Will indicate if no rooms are available.
    Dates modified:
    * 17/05/2024
      Sean Liu - Created the shell and main function of the method.

    * 24/05/2024
      Raymond Zhang - Changed return type to boolean to indicate empty rooms. Improved coding style.
      Sean Yang - Fixed issue where no rooms would be returned on a date beyond the maximum date booked. Changed
                  rooms to be an ArrayList
    */

    public static boolean listAvailableRooms(int date)
    {
        // Declare variables
        int size = 0;
        List<Integer> rooms;
        try {
            // Get available rooms
            rooms = Query.getAvailableRooms(date);
            size = rooms.size();//checks how many available rooms there are

            // Check if any rooms are available
            if (size == 0)
            {
                System.out.printf("There are no rooms available on %s.%n%n", dateConverter(date));
            }
            else
            {
                System.out.printf("Rooms available on %s:%n", dateConverter(date));
                // loop over every room available on the date
                for(int i = 0; i<size; i++)
                {
                    System.out.println("Room " + rooms.get(i)); // prints out all available rooms
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
    Return Type: Boolean - returns true or false if room and date match
    Parameters: int Date - a day that user inputs from main
                int room - room number being checked for availability
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
    Return Type: void - prints out the day and room number if person has reservation
    Parameters: String firstName - first name of person
                String lastName - last name of person
    Description: Lists the room number and dates the room is booked for
    Dates modified:
     * 21/05/2024
       Sean Liu - created the main function of method

     * 23/05/2024
     * Raymond Zhang - Formatted method.

     * 24/05/2024
     * Raymond Zhang - Change print format to be consistent with other methods
    */
    public static void listReservations(String firstName, String lastName)
    {
        // declare variables
        List<Integer> days;

        try
        {
            Map<Integer, List<Integer>> rooms = Query.getReservations(firstName, lastName);
            // loop over each room number
            for (int e : rooms.keySet())
            {
                System.out.printf("%s %s has booked Room %d for:\n", firstName, lastName, e);
                days = rooms.get(e); // gets the dates that the room is booked for
                for (int d : days)
                {
                    System.out.printf("%10s\n", dateConverter(d));// prints out each date that the room is booked for
                }
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
       Return Type: void
       Parameters: Integer date - date number that user is searching for
       Description:  Prints out which rooms on a given specific date
       Dates modified:
        * 21/05/2024
          Sean Liu

        * 24/05/2024
          Raymond Zhang - Changed method to only use date as parameter.
       */
    public static void listReservations(int date)
    {
        // Declare variables
        List<Integer> rooms;

        try {
            rooms = Query.getAllDays().get(date);

            // Check if reservations have been made on the date
            if(rooms.isEmpty()) {
                System.out.printf("No reservations have been made on %s.%n%n", dateConverter(date));
            }
            // Print reservations
            else {
                System.out.printf("The following reservations have been made on %s:%n", dateConverter(date));
                for(Integer r : rooms) {
                    System.out.printf("%10d\n", r);// prints out each date that the room is booked for
                }
            }
        }
        // Unrecorded date was entered
        catch (IndexOutOfBoundsException e) {
            System.out.printf("No reservations have been made on %s.%n%n", dateConverter(date));
        }
        catch(IOException e) {
            System.out.println(e + " Problem reading file.");
        }
    }


    /*
    Method Name: dateConverter
    Return Type: String - returns the date in dd/mm/yyyy format
    Parameters: int Days- number of days given by user
    Description: Returns a date given the number of days from the start of the year
    Date Modified:
    * 22/05/2024
      Sean Liu - Created dateConverter to accommodate printing and localized time.

    * 24/05/2024
      Raymond Zhang - Formatted date string to be consistent with main class
    */
    public static String dateConverter(int days)
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
}