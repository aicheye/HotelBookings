import javax.naming.Name;
import java.lang.reflect.Array;
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
    // Constants for filenames
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
        // declare variables
        Map<List<String>, Map<Integer, List<Integer>>> customers = Query.allCustomers(); // get all customers
        List<List<Integer>> days = Query.allDays(); // get all days
        boolean customerExists = false; // whether the customer is in the database
        boolean customerBookedRoom = false; // whether the customer has previously booked this room
        List<String> Name = new ArrayList<String>(); // ArrayList representing the customer's full name
        Name.add(firstName); // add first name
        Name.add(lastName); // add last name

        // check if the customer exists and update customerExists
        if (customers.containsKey(Name))
        {
            customerExists = true;
            // check if the customer has booked the room and update customerBookedRoom
            if (customers.get(Name).containsKey(room)) customerBookedRoom = true;
        }

        // case 1: the customer does not exist in the database
        if (!customerExists)
        {
            // add a new key-value pair
            customers.put(Name, new HashMap<Integer, List<Integer>>());
            // add the room to the new key-value pair
            customers.get(Name).put(room, new ArrayList<Integer>());
            // add the date to the room
            customers.get(Name).get(room).add(date);
        }

        // case 2: the customer exists in the database but has not booked this room previously
        if (customerExists && !customerBookedRoom)
        {
            // add the room to the customer's bookings
            customers.get(Name).put(room, new ArrayList<Integer>());
            // add the date to the room
            customers.get(Name).get(room).add(date);
        }

        // case 3: the customer exists in the database and has booked this room previously
        if (customerExists && customerBookedRoom)
        {
            // add the date to the room
            customers.get(Name).get(room).add(date);
        }

        // update days
        // case 1: the date has been booked previously
        if (date <= days.size()-1)
        {
            // add the room to the date
            days.get(date).add(room);
        }

        // case 2: the date has not been booked previously
        else
        {
            // keep adding until the size of days is large enough
            while (days.size()-1 < date) days.add(new ArrayList<>());
            // add the room to the date
            days.get(date).add(room);
        }

        // write to file using allCustomers and allDays
        allCustomers(customers);
        allDays(days);
    }

    /*
     Method Name: delReserve
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
                 int room - The room number
                 int date - The date
     Description: Deletes a reservation from customers.txt and days.txt
     */
    public static void delReserve(String firstName, String lastName, int room, int date) throws IOException
    {
        // declare variables
        Map<List<String>, Map<Integer, List<Integer>>> customers = Query.allCustomers(); // get all customers
        List<List<Integer>> days = Query.allDays(); // get all days
        List<String> name = new ArrayList<>(); // ArrayList representing the customer's full name
        name.add(firstName);
        name.add(lastName);

        // remove reservation from customers
        customers.get(name).get(room).remove(date);
        // check if the room booking is now empty and remove the room
        if (customers.get(name).get(room).size() == 0) customers.get(name).remove(room);
        // check if the customer has no remaining reservations and remove the room
        if (customers.get(name).size() == 0) customers.remove(name);

        // remove reservation from days
        days.get(date).remove((Integer) room);

        // write to file using allCustomers and allDays
        allCustomers(customers);
        allDays(days);
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

    public static void edtPin(String id, String newPin) {

    }

    /*
     Method Name: allCustomers
     Parameters: Map<List<String>, Map<Integer, List<Integer>>> customers: a java representation of customers.txt
     Description: converts java parseable data into days.txt data structure
     */
    public static void allCustomers(Map<List<String>, Map<Integer, List<Integer>>> customers) throws IOException
    {
        // init file writer and variables
        BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMERS_DB));
        boolean first = true; // whether this is the first pass of the outer loop

        // loop over every customer
        for (List<String> name : customers.keySet())
        {
            // write their name to the file
            if (!first) bw.write("\n"); // write a newline if this is not the first name in the file
            else first = false;
            bw.write(name.get(0));
            bw.write("\n");
            bw.write(name.get(1));
            bw.write("\n");

            // loop over every room they have booked
            for (int room : customers.get(name).keySet())
            {
                // write the room to the file
                bw.write(String.valueOf(room));
                bw.write("\n");

                // loop over every day they have booked that room for
                for (int date : customers.get(name).get(room))
                {
                    // write the date to the file
                    bw.write(String.valueOf(date));
                    bw.write("\n");
                }

                // write the room delimiter
                bw.write(ROOM_DELIMITER);
                bw.write("\n");
            }

            // write the customer delimiter
            bw.write(CUSTOMER_DELIMITER);
        }

        // close file writer
        bw.close();
    }

    /*
     Method Name: allDays
     Parameters: List<List<Integer>> days: a java representation of days.txt: each index represents a date with reservations
     Description: converts java parseable data into days.txt data structure
     */
    public static void allDays(List<List<Integer>> days) throws IOException
    {
        // init file writer and variables
        BufferedWriter bw = new BufferedWriter(new FileWriter(DAYS_DB));

        // loop over every date
        for (int i=0; i<days.size(); i++)
        {
            // write the current date to the file
            if (i != 0) bw.write("\n"); // write a newline if this is not day 0
            bw.write(String.valueOf(i));
            bw.write("\n");

            // loop over every room booked on that date
            for (int room : days.get(i))
            {
                // write the room to the file
                bw.write(String.valueOf(room));
                bw.write("\n");
            }

            // write the date delimiter
            bw.write(DATE_DELIMITER);
        }

        // close file writer
        bw.close();
    }

    /*
     Method Name: allRooms
     Parameters: List<Integer> rooms - an ArrayList containing every room
     Description: converts java parseable data into rooms.txt data structure
     */
    public static void allRooms(List<Integer> rooms) throws IOException
    {
        // init file writer and variables
        BufferedWriter bw = new BufferedWriter(new FileWriter(ROOMS_DB));

        // loop over every date
        for (int i=0; i<rooms.size(); i++)
        {
            // write the current room to the file
            if (i != 0) bw.write("\n"); // write a newline if this is not day 0
            bw.write(String.valueOf(rooms.get(i)));
        }

        // close file writer
        bw.close();
    }

    /*
     Method Name: allEmployees
     Parameters: List<HashMap<String, String>> employees - an ArrayList containing data about every employee
     Description: converts java parseable data into employees.txt data structure
     */
    public static void allEmployees(List<HashMap<String, String>> employees) throws IOException
    {
        // init file writer and variables
        BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEES_DB));

        // loop over every date
        for (int i=0; i<employees.size(); i++)
        {
            // write the current employee to the file
            if (i != 0) bw.write("\n"); // write a newline if this is not day 0
            bw.write(String.valueOf(employees.get(i).get("id")));
            bw.write("\n");
            bw.write(String.valueOf(employees.get(i).get("firstName")));
            bw.write("\n");
            bw.write(String.valueOf(employees.get(i).get("lastName")));
            bw.write("\n");
            bw.write(String.valueOf(employees.get(i).get("pin")));
            bw.write("\n");
            bw.write(String.valueOf(employees.get(i).get("isAdmin")));
        }

        // close file writer
        bw.close();
    }
}
