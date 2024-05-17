import java.util.*;
import java.io.*;

/*
 Programmer: Sean Yang
 Program Name: Write
 Date: 17/05/2024
 Description: Performs various writes to database (.txt) files
 */

public class Write
{
    // Create file readers
    static final String DAYS_DB = "days.txt";
    static final String CUSTOMERS_DB = "customers.txt";
    static final String EMPLOYEES_DB = "employees.txt";
    static final String ROOMS_DB = "rooms.txt";
    static final String LOG_DB = "log.txt";

    // Constants for file delimiters
    static final String DATE_DELIMITER = "~"; // used in days.txt
    static final String ROOM_DELIMITER = "-"; // used in customers.txt
    static final String CUSTOMER_DELIMITER = "`"; // used in customers.txt

    /*
     Method Name: addReserve
     Parameters: String firstName - The first name of the customer making the reservation
                 String lastName - The last name of the customer making the reservation
                 int room - The room number they are reserving
                 int date - The date they are reserving
     Description: Adds a reservation to customers.txt and updates days.txt
     */
    public static void addReserve(String firstName, String lastName, int room, int date) throws IOException
    {
        // init file readers and store the files represented as ArrayLists
        BufferedReader readCustomers = new BufferedReader(new FileReader(CUSTOMERS_DB));
        BufferedReader readDays = new BufferedReader(new FileReader(DAYS_DB));

        // store each line of both files in an item in an ArrayList
        List<String> customers = new ArrayList<>();
        String line = readCustomers.readLine();
        while (line != null)
        {
            customers.add(line);
            line = readCustomers.readLine();
        }
        List<String> days = new ArrayList<>();
        line = readDays.readLine();
        while (line != null)
        {
            days.add(line);
            line = readDays.readLine();
        }

        // check if the customer already exists in customers.txt
        boolean customerExists = Query.customerQuery(firstName, lastName).size() > 0;
        // if the customer exists, check if they have already booked this room
        boolean customerBookedRoom = false;
        if (customerExists) customerBookedRoom = Query.customerQuery(firstName, lastName).containsKey(room);

        // if the customer exists but the room doesn't, loop through the file and append a new room and date
        if (customerExists && !customerBookedRoom)
        {
            for (int i=0; i<customers.size()-1; i++)
            {
                // check if the current index and the next index are the customer's name
                if (customers.get(i).equals(firstName) && customers.get(i+1).equals(lastName))
                {
                    int index = getIndexRoom(room, i, customers);

                    // add the new room and date to the customer
                    customers.add(index, String.valueOf(room));
                    customers.add(index+1, String.valueOf(date));
                    customers.add(index+2, ROOM_DELIMITER);
                }
            }
        }

        // if the customer exists and the room exists, loop through the file and append a new date
        else if (customerBookedRoom)
        {
            for (int i=0; i<customers.size()-1; i++)
            {
                if (customers.get(i).equals(firstName) && customers.get(i).equals(lastName))
                {
                }
            }
        }

        // BufferedWriter writeCustomers = new BufferedWriter(new FileWriter(CUSTOMERS_DB, false));
        // BufferedWriter writeDays = new BufferedWriter(new FileWriter(DAYS_DB, false));
    }

    /*
     Method Name: getIndexRoom
     Return Type: int - The line # to insert a room
     Parameters: int room - The room # to insert
                 int i - The line # of the customer's first name
                 List<String> customers - An ArrayList representing customers.txt
     Description: Calculates the location to insert a room
     */
    private static int getIndexRoom(int room, int i, List<String> customers) {
        int index = i + 2; // place to store the customer's reservation
        int prev = i + 2; // the index of the previous room
        int next = i + 2; // the index of the next room
        int j = i + 2; // current index

        // loop until we find the appropriate place for the room (in ascending order)
        boolean searching = true;
        while (searching)
        {
            // check if we are at the end of the customer
            if (customers.get(j+1).equals(CUSTOMER_DELIMITER))
            {
                searching = false;

                // find the appropriate place to insert the room (edge cases)
                if (Integer.parseInt(customers.get(prev)) > room) index = prev;
                if (Integer.parseInt(customers.get(next)) < room)
                {
                    // find the location of the next customer and append the room there
                    boolean atNext = false;
                    int k = next + 1;
                    while (!atNext) {
                        if (customers.get(k).equals("-"))
                        {
                            atNext = true;
                            index = k + 1;
                        }
                        k++;
                    }
                }
            }
            // otherwise continue
            else
            {
                // check if we are at a new room
                if (customers.get(j).equals(ROOM_DELIMITER))
                {
                    prev = next;
                    next = j + 1;
                    // check if the room is in between prev and next
                    if (Integer.parseInt(customers.get(prev)) < room &&
                            Integer.parseInt(customers.get(next)) > room)
                    {
                        // if it is, set the index to next and break the loop
                        index = next;
                        searching = false;
                    }
                }
            }
            j++; // increment iterator
        }
        return index;
    }

    private static int getIndexDay(int room, int i, List<String> customers) {
        return 0;
    }

    public static void delReserve(String firstName, String lastName, int room, int date)
    {

    }

    public static void edtReserve(String oldFirst, String oldLast, int code, int room, int date, String newFirst, String newLast)
    {

    }

    public static void edtReserve(String firstName, String lastName, int code, int roomOrDate, int old, int now)
    {

    }

    public static void addRooms(int room)
    {

    }

    public static void delRoom(int room)
    {

    }

    public static void addEmployee(int id, String firstName, String lastName, int room, int date)
    {

    }

    public static void delEmployee(int id)
    {

    }
}
