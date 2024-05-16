import java.util.*;

/*
 Programmer: Sean Yang
 Program Name: Query
 Date: 16/05/2024
 Description: Performs various queries on database (.txt) files
 */

public class Query
{
    /*
     Method Name: dateQuery
     Return Type: int[] - An array of the rooms available on a certain date
     Parameters: int date - The date to search
     Description: Returns an array of available rooms on a given date
     */
    public static int[] dateQuery (int date)
    {
        List<Integer> rooms = new ArrayList<>(); // ArrayList of the rooms available on a date
        int[] arr = new int[rooms.size()]; // the array which will be returned
        for (int i=0; i<rooms.size(); i++) // converts ArrayList into array
        {
            arr[i] = rooms.get(i);
        }
        return arr;
    }

    /*
     Method Name: roomQuery
     Return Type: int[] - An array of the days a certain room is available
     Parameters: int room - The rooms to search
     Description: Returns an array of days a given room is available
     */
    public static int[] roomQuery (int room)
    {
        List<Integer> days = new ArrayList<>(); // ArrayList of the days a room is available
        int[] arr = new int[days.size()]; // the array which will be returned
        for (int i=0; i<days.size(); i++) // converts ArrayList into array
        {
            arr[i] = days.get(i);
        }
        return arr;
    }

    /*
     Method Name: customerQuery
     Return Type: HashMap<Integer, ArrayList<Integer>> - A hashmap (key: room, value: array of the days) of the
                                                         rooms & days a customer has booked
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
     Description: Returns the rooms a customer has booked and the days they have booked it for
     */
    public static Map<Integer, List<Integer>> customerQuery(String firstName, String lastName)
    {
        Map<Integer, List<Integer>> reservations = new HashMap<>(); // HashMap to be returned
        return reservations;
    }

    /*
     Method Name: employeeQuery
     Return Type: int[0] - The pin of the employee, -1 if the employee doesn't exist
                  int[1] - Whether the employee is admin or not (0 or 1)
     Parameters: int id - The employee id to search for
     Description: Returns various details about an employee
     */
    public static int[] employeeQuery (int id)
    {
        int pin = -1; // pin of the employee
        boolean admin = false; // whether the employee is admin
        return new int[]{pin, admin ? 1 : 0}; // returns an array with two indices
    }
}
