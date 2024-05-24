import java.io.*;
import java.util.*;
import java.time.LocalDate;


/*
 Programmer: Sean Liu
 Program Name: Reservations
 Date: 5/17/2024
 Description: Gives availability of rooms
 */
public class Reservations {

    /*
    Method Name: listAvailableRooms
    Return Type: Void, just prints out the available rooms
    Parameters: int Date - a day that user inputs from main
    Description: Just prints out all the available rooms on day
     */

    public static void listAvailableRooms(int date) {
        try {
            int[] rooms = Query.dateQuery(date);
            int size = rooms.length;//checks how many available rooms there are
            if (size == 0) {
                System.out.println("There are no rooms available");
            } else {
                System.out.println("Rooms Available:");
                int i = 0;
                while (i < size) {
                    System.out.println("Room " + rooms[i]);//prints out all available rooms
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    /*
    Method Name: checkAvailability
    Return Type: Boolean - returns true or false if room and date match
    Parameters: int Date - a day that user inputs from main
                int room - room number being checked for availability
    Description: Returns true of false if room is available on given day
    Dates modified:
    * 24/05/2024
    * Sean Yang - changed the method to use Query.roomAvailable and changed the parameter order
    */
    public static boolean checkAvailability (int room, int date)
    {
        boolean roomFound = false;
        try
        {
            roomFound = Query.roomAvailable(room, date);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return roomFound; //returns true or false depending if the conditions were met
    }

    /*
    Method Name: listReservations
    Return Type: void - prints out the day and room number if person has reservation
    Parameters: String firstName - first name of person
                String lastName - last name of person
    Description: Lists the room number and dates the room is booked for

    */
    public static void listReservations(String firstName, String lastName) {
        Object days;
        try {
            Map<Integer, List<Integer>> rooms = Query.customerQuery(firstName, lastName);
    for  (int e: rooms.keySet()) {//gets each room #
        System.out.printf("%s %s has booked Room %d for:\n", firstName, lastName, e);
        List<Integer> value = rooms.get(e);//gets the room numbers
        for (Object o : value) {
            days = o;
            System.out.printf("%10s\n", dateConverter(days));//prints out each date that the room is booked for
        }
        }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }
    /*
       Method Name: listReservations
       Return Type: void
       Parameters: String firstName - first name of person
                   String lastName - last name of person
                   Object date - date number that user is searching for
       Description:  Prints out which rooms are booked by the customer on given specific date

       */
    public static void listReservations(String firstName, String lastName, Object date) {
        try {
            Object days;
            Map<Integer, List<Integer>> rooms = Query.customerQuery(firstName, lastName);
            System.out.printf("%s %s has booked the following rooms on %s: \n",firstName,lastName, dateConverter(date));
            for  (int e: rooms.keySet()) {//gets each room #
                List<Integer> value = rooms.get(e);//value is assigned the date the room numbers are booked
                for (Object o : value) {
                    days = o;//days are being assigned
                    if (days==date){
                        System.out.printf("%4s \n", e);//prints out the room numbers
                    }
                }



            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }



    /*
    Method Name: dateConverter
    Return Type: String - returns the date in Georgian Calendar format 2024/1/1
    Parameters: int Days- number of days given by user
    Description: Returns a date

    */
    public static String dateConverter(Object days) {
        String combined;
        LocalDate startDate = LocalDate.of(2024, 1, 1);//puts in start date

        // Calculate the target date by adding the number of days to the start date
        LocalDate targetDate = startDate.plusDays(((Integer)days));

        // Extract the day, month, and year from the target date
        int day = targetDate.getDayOfMonth();
        int month = targetDate.getMonthValue();
        int year = targetDate.getYear();//runs methods that get the month, day, year of the given date.


        combined = day+"/"+month+"/"+year;//combines the date into a dd/mm/yyyy format
        return combined;
    }
}
