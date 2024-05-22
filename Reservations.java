import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
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

    */
    public static boolean checkAvailability(int date, int room) {
        boolean roomfound = false;
        try {
            int[] dateAvailable = Query.dateQuery(date);
            int size = dateAvailable.length;

            for (int j : dateAvailable) {
                if (j == room) {
                    roomfound = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return roomfound;
    }

    public static Object listToStringConversion(List<Integer> list) {
        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    /*
    Method Name: listReservations
    Return Type: void - prints out the day and room number if person has reservation
    Parameters: String firstName - first name of person
                String lastName - last name of person
    Description: Prints out the room number and dates the room is booked for

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
            System.out.println(dateConverter(days));//prints out each date that the room is booked for
        }

    }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }
    /*
    public static String listReservations(String firstName, String lastName, int date) {
        String checkTemp = null;
        try {
            checkTemp = mapToStringConversion(Query.customerQuery(firstName, firstName));
            return checkTemp;
        } catch (IOException e) {
            System.out.println(e);
        }

        return checkTemp;
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
        int year = targetDate.getYear();


        combined = year+"/"+month+"/"+day;
        return combined;
    }
}
