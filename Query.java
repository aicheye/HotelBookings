import java.util.*;
import java.io.*;

/*
 Programmer: Sean Yang, Raymond Zhang
 Program Name: Query
 Date: 16/05/2024
 Description: Performs various queries on database (.txt) files
*/

public class Query
{
    // Constants for filenames
    private static final String DAYS_DB = "days.txt";
    private static final String CUSTOMERS_DB = "customers.txt";
    private static final String EMPLOYEES_DB = "employees.txt";
    private static final String ROOMS_DB = "rooms.txt";

    // Constants for file delimiters
    private static final String DATE_DELIMITER = "~"; // used in days.txt
    private static final String ROOM_DELIMITER = "-"; // used in customers.txt
    private static final String CUSTOMER_DELIMITER = "`"; // used in customers.txt

    /*
     Method Name: reservationExists
     Return Type: boolean - Whether the reservation exists
     Parameters: firstName - the first name of the customer
                 lastName - the last name of the customer
                 room - the room number
                 date - the date for the reservation
     Description: checks whether a specific reservation exists
     Dates Modified:
     * 24/05/2024
       Sean Yang - Created and completed (tested)
     */
    public static boolean reservationExists(String firstName, String lastName, int room, int date) throws IOException
    {
        // declare variables
        Map<List<String>, Map<Integer, List<Integer>>> customers = getAllCustomers();
        boolean exists = false;
        ArrayList<String> name = new ArrayList<String>();
        name.add(firstName);
        name.add(lastName);

        // check if the reservation exists
        if (customers.containsKey(name) &&
                customers.get(name).containsKey(room) &&
                customers.get(name).get(room).contains(date))
            exists = true;

        return exists;
    }

    /*
     Method Name: roomAvailable
     Return Type: boolean - Whether the room is available
     Parameters: room - the room number
                 date - the date to check
     Description: checks whether a specific room is available for reservation on a given date
     Dates Modified:
     * 24/05/2024
       Sean Yang - Created and completed (tested)

     * 25/05/2024
       Sean Yang - Fixed issue where roomAvailable would throw an error if the date was beyond the maximum date booked
     */
    public static boolean roomAvailable(int room, int date) throws IOException
    {
        // declare variables
        List<List<Integer>> days = getAllDays();
        List<Integer> rooms = getAllRooms();
        boolean available = false;

        // check if the date is within the range of days
        if (date <= days.size() - 1)
        {
            // check if the room is available
            if (rooms.contains(room) && !days.get(date).contains(room))
                available = true;
        }

        // if the date is beyond the range of days, the room is available
        else
        {
            available = true;
        }

        return available;
    }

    /*
     Method Name: getAvailableRooms
     Return Type: int[] - An array of the rooms available on a certain date
     Parameters: int date - The date to search
     Description: Returns an array of available rooms on a given date
     Dates Modified:
     * 16/05/2024
       Sean Yang - Created and completed method (tested)

     * 17/05/2024
       Sean Yang - Fixed method to close file reader
       Raymond Zhang - Moved variable declarations to beginning of method.
                       Closed readRoom file reader.

     * 24/05/2024
       Sean Yang - Fixed issue where no rooms would be returned on a date beyond the maximum date booked. Now returns
                   an ArrayList instead of an array.

     * 25/05/2024
       Sean Yang - Renamed method to getAvailableRooms to improve clarity since all methods in this class are queries
     */
    public static List<Integer> getAvailableRooms(int date) throws IOException
    {
        // init file reader and variables
        List<Integer> rooms = getAllRooms(); // all the rooms in the hotel
        List<List<Integer>> days = getAllDays(); // all the days rooms have been booked for
        List<Integer> available = new ArrayList<Integer>(); // available rooms on that date

        // check if the date has been booked
        if (date <= days.size() - 1)
        {
            // loop over all rooms in the hotel
            for (int room : rooms)
            {
                if (!days.get(date).contains(room)) available.add(room); // if the room is not booked, add it
            }
        }
        // if the date has not been booked, return all days
        else
        {
            available = rooms;
        }

        return available;
    }

    /*
     Method Name: getReservations
     Return Type: HashMap<Integer, ArrayList<Integer>> - A hashmap (key: room, value: array of the days) of the
                                                         rooms & days a customer has booked
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
     Description: Returns the rooms a customer has booked and the days they have booked it for
     Dates Modified:
     * 16/05/2024
       Sean Yang - Created and completed method (tested)

     * 17/05/2024
       Sean Yang - Fixed method to close file reader
       Raymond Zhang - Moved variable declarations to beginning of method

     * 25/05/2024
       Sean Yang - Renamed method to getReservations to increase clarity since all methods in this class are queries
    */
    public static Map<Integer, List<Integer>> getReservations(String firstName, String lastName) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS_DB));
        Map<Integer, List<Integer>> reservations = new HashMap<Integer, List<Integer>>(); // HashMap to be returned
        String line, f, l;
        int room;
        boolean end = false, searching, inRoom;

        // loop through every customer
        while (!end)
        {
            line = br.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                f = line;
                l = br.readLine();

                // if the first and last name matches, loop through all their reservations
                if (f.equals(firstName) && l.equals(lastName))
                {
                    searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        // check if we are at a new customer or not
                        if (line.equals(CUSTOMER_DELIMITER)) searching = false;
                        // if we are not at a new customer, continue looping
                        else {
                            room = Integer.parseInt(line); // create a new variable for the current room

                            // create a new key-value pair in reservations
                            reservations.put(room, new ArrayList<Integer>());

                            // loop until we are at end of room
                            inRoom = true;
                            while (inRoom) {
                                line = br.readLine();
                                // check if we are at a new room
                                if (line.equals(ROOM_DELIMITER)) inRoom = false;
                                    // if we are not at a new room add to the room ArrayList
                                else {
                                    reservations.get(room).add(Integer.parseInt(line));
                                }
                            }
                        }
                    }
                }

                // if the first and last name do not match, keep reading until we reach a new customer
                else
                {
                    searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        if (line.equals("`")) searching = false;
                    }
                }
            }
        }

        // close file reader
        br.close();

        return reservations;
    }

    /*
     Method Name: getEmployeePin
     Return Type: String[0] - The pin of the employee, null if the employee doesn't exist
                  String[1] - Whether the employee is admin or not (0 or 1)
     Parameters: int id - The employee id to search for
     Description: Returns the pin and admin status of an employee
     Dates Modified:
     * 16/05/2024
       Sean Yang - Created and completed method (tested)

     * 17/05/2024
       Sean Yang - Fixed method to close file reader

     * 25/05/2024
       Sean Yang - Rewrote method using allEmployees method to improve conciseness
                   Renamed method to getEmployeePin to reduce verboseness since all methods in this class are queries
     */
    public static String[] getEmployeePin(String id) throws IOException
    {
        // declare variables
        List<HashMap<String, String>> employees = getAllEmployees();
        String pin = null;
        String isAdmin = null;

        // loop over all employees
        for (HashMap<String, String> emp : employees)
        {
            // if the employee id matches, return the pin and admin status
            if (emp.get("id").equals(id))
            {
                pin = emp.get("pin");
                isAdmin = emp.get("isAdmin");
            }
        }

        return new String[]{pin, isAdmin};
    }

    /*
     Method Name: getAllCustomers
     Return Type: HashMap<String, HashMap<Integer, ArrayList<Integer>>> - A nested hashmap representing the customers'
                                                                          names and rooms booked
     Description: Returns all customers and the rooms/days they have booked
     Dates Modified:
     * 22/05/2024
       Sean Yang - Created and completed methods (tested)

     * 23/05/2024
       Sean Yang - Fix method to close file reader
    */
    public static Map<List<String>, Map<Integer, List<Integer>>> getAllCustomers() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS_DB));
        Map<List<String>, Map<Integer, List<Integer>>> customers = new HashMap<List<String>, Map<Integer, List<Integer>>>();
        String fName, lName, line;
        List<String> name;
        List<String> db = new ArrayList<String>(); // the file converted into an ArrayList

        // append each line to db
        line = br.readLine();
        while (line != null)
        {
            db.add(line);
            line = br.readLine();
        }


        // loop over customers.txt and find each customer
        fName = db.get(0);
        lName = db.get(1);
        name = new ArrayList<String>();
        name.add(fName);
        name.add(lName);
        customers.put(name, getReservations(fName, lName));
        for (int i=2; i<db.size()-2; i++)
        {
            // check if there is a new customer
            if (db.get(i).equals(CUSTOMER_DELIMITER))
            {
                // add this customer to the HashMap
                fName = db.get(i+1);
                lName = db.get(i+2);
                name = new ArrayList<String>();
                name.add(fName);
                name.add(lName);
                customers.put(name, getReservations(fName, lName));
            }
        }
        return customers;
    }

    /*
     Method Name: getAllDays
     Return Type: List<List<Integer>>
     Description: returns all days and the rooms reserved on that day. the index of the first ArrayList represents the date
     Dates Modified:
     * 22/05/2024
       Sean Yang - Created and completed methods (tested)

     * 23/05/2024
       Sean Yang - Fix method to close file reader
     */
    public static List<List<Integer>> getAllDays() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(DAYS_DB));
        List<List<Integer>> days = new ArrayList<List<Integer>>();
        String line;
        List<Integer> rooms = new ArrayList<Integer>();
        List<String> db = new ArrayList<String>(); // the file converted into an ArrayList

        // append each line to db
        line = br.readLine();
        while (line != null)
        {
            db.add(line);
            line = br.readLine();
        }

        // loop over days.txt and find the rooms reserved on each day
        for (int i=1; i<db.size(); i++)
        {
            // if we are at a new date, add the rooms to days and clear rooms
            if (db.get(i).equals(DATE_DELIMITER))
            {
                days.add(new ArrayList<Integer>());
                days.get(days.size()-1).addAll(rooms);
                rooms.clear();
                i++;
            }
            // if we are not at a new date, append the next room to the ArrayList
            else
            {
                rooms.add(Integer.parseInt(db.get(i)));
            }
        }

        // close file reader
        br.close();

        return days;
    }

    /*
     Method Name: getAllRooms
     Return Type: List<List<Integer>>
     Description: returns all rooms in the hotel
     Dates Modified:
     * 22/05/2024
       Sean Yang - Created and completed methods (tested)

     * 23/05/2024
       Sean Yang - Fix method to close file reader
     */
    public static List<Integer> getAllRooms() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(ROOMS_DB));
        List<Integer> rooms = new ArrayList<Integer>();
        String line;

        // loop through rooms.txt and append to rooms
        line = br.readLine();
        while (line != null)
        {
            rooms.add(Integer.parseInt(line));
            line = br.readLine();
        }

        // close BufferedReader
        br.close();

        return rooms;
    }

    /*
     Method Name: getAllEmployees
     Return Type: List<HashMap<String, String>>
     Description: returns all employees in an ArrayList
     Dates Modified:
     * 22/05/2024
       Sean Yang - Created and completed methods (tested)

     * 23/05/2024
       Sean Yang - Fix method to close file reader
     */
    public static List<HashMap<String, String>> getAllEmployees() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES_DB));
        List<HashMap<String, String>> employees = new ArrayList<HashMap<String, String>>();
        String id, fName, lName, pin, isAdmin;

        // loop through employees.txt and append to employees
        id = br.readLine();
        fName = br.readLine();
        lName = br.readLine();
        pin = br.readLine();
        isAdmin = br.readLine();
        while (id != null)
        {
            // put to HashMap
            employees.add(new HashMap<String, String>());
            employees.get(employees.size()-1).put("id", id);
            employees.get(employees.size()-1).put("firstName", fName);
            employees.get(employees.size()-1).put("lastName", lName);
            employees.get(employees.size()-1).put("pin", pin);
            employees.get(employees.size()-1).put("isAdmin", isAdmin);

            // read from file
            id = br.readLine();
            fName = br.readLine();
            lName = br.readLine();
            pin = br.readLine();
            isAdmin = br.readLine();
        }

        // close file reader
        br.close();

        return employees;
    }
}