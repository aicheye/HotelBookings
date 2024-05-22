import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

            for (int i = 0; i < size; i++) {
                if (dateAvailable[i] == room) {
                    roomfound = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return roomfound;
    }

    public static String mapToStringConversion(Map<Integer, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;//a map to string converter, there wasn't a method that could be used D:
    }

    /*
    Method Name: listReservations
    Return Type: void - prints out the day and room number if person has reservation
    Parameters: String firstName - first name of person
                String lastName - last name of person
    Description: Returns true of false if room is available on given day

    */
    public static void listReservations(String firstName, String lastName) {
        String date;
        try {
            try {
                String mainInfo = mapToStringConversion(Query.customerQuery(firstName, lastName));

                if (mainInfo.equals("{}")) {
                    System.out.println("This person has no rooms booked");
                } else {
                    date = dateConverter(Integer.parseInt(mainInfo.substring(6, mainInfo.length() - 2)));//use dateConverter here
                    String room = mainInfo.substring(1, 4);
                    System.out.printf("%s %s has booked room number %s on %s", firstName, lastName, room, date);
                }//day needs to be changed to standard
            } catch (NumberFormatException n) {
                System.out.println(n);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
    Method Name: dateConverter
    Return Type: String - returns the date in Georgian Calendar format 2024/1/1
    Parameters: int Days- number of days given by user
    Description: Returns a date

    */
    public static String dateConverter(int days) {
        String combined;
        LocalDate startDate = LocalDate.of(2024, 1, 1);//puts in start date

        // Calculate the target date by adding the number of days to the start date
        LocalDate targetDate = startDate.plusDays(days);

        // Extract the day, month, and year from the target date
        int day = targetDate.getDayOfMonth();
        int month = targetDate.getMonthValue();
        int year = targetDate.getYear();


        combined = year+"/"+month+"/"+day;
        return combined;
    }
}







