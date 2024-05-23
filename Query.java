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
     Method Name: dateQuery
     Return Type: int[] - An array of the rooms available on a certain date
     Parameters: int date - The date to search
     Description: Returns an array of available rooms on a given date
     Dates Modified:
     * 17/05/2024
       Raymond Zhang - Moved variable declarations to beginning of method.
                       Closed readRoom file reader.
     */
    public static int[] dateQuery (int date) throws IOException
    {
        // init file reader and variables
        BufferedReader readDays = new BufferedReader(new FileReader(DAYS_DB));
        BufferedReader readRoom = new BufferedReader(new FileReader(ROOMS_DB));
        List<Integer> rooms = new ArrayList<>(); // ArrayList of the rooms available on a date
        Set<Integer> allRooms = new HashSet<>(); // HashSet of all rooms in the system
        Set<Integer> reserved = new HashSet<>(); // HashSet of all reserved rooms on a date
        String line = readRoom.readLine(); // read the first line of rooms.txt
        int day;
        int[] arr;
        boolean end = false, searching;

        // loop until EOF (rooms.txt)
        while (line != null)
        {
            // append the next line to allRooms
            allRooms.add(Integer.parseInt(line));
            line = readRoom.readLine();
        }

        // loop through every day
        while (!end)
        {
            line = readDays.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                day = Integer.parseInt(line);

                // check if date matches and loop through every room available on that day
                if (day == date) {
                    searching = true;
                    while (searching)
                    {
                        line = readDays.readLine();
                        // check if there are no other rooms available on that day
                        if (line.equals(DATE_DELIMITER)) searching = false;
                        // append to array
                        else reserved.add(Integer.parseInt(line));
                    }

                    // if a room in allRooms is not in reserved, append it to the arrayList
                    for (int e : allRooms)
                    {
                        if (!reserved.contains(e)) rooms.add(e);
                    }
                }

                // otherwise, keep reading until we reach a new date
                else {
                    searching = true;
                    while (searching)
                    {
                        line = readDays.readLine();
                        if (line.equals(DATE_DELIMITER)) searching = false;
                    }
                }
            }
        }

        arr = new int[rooms.size()]; // the array which will be returned
        for (int i=0; i<rooms.size(); i++) // converts ArrayList into array
        {
            arr[i] = rooms.get(i);
        }

        // close file readers
        readDays.close();
        readRoom.close();

        return arr;
    }

    /*
     Method Name: roomQuery
     Return Type: int[] - An array of the days a certain room is available
     Parameters: int room - The rooms to search
     Description: Returns an array of days a given room is available
     Dates Modified:
     * 17/05/2024
       Raymond Zhang - Moved variable declarations to beginning of method.
    */
    public static int[] roomQuery (int room) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(DAYS_DB));
        List<Integer> days = new ArrayList<>(); // ArrayList of the days a room is available
        int day;
        int[] arr;
        boolean end = false, searching, contains;

        // loop through every day
        while (!end)
        {
            String line = br.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                day = Integer.parseInt(line);

                // loop through every available room on that day
                contains = false; // whether the day contains the room
                searching = true;
                while (searching)
                {
                    line = br.readLine();
                    // check if there are no other rooms on the day
                    if (line.equals(DATE_DELIMITER)) searching = false;
                    // check if the room is available under the current date, if it is, append to ArrayList
                    else if (Integer.parseInt(line) == room) contains = true;
                }

                // if the day does not contain the room, append to ArrayList
                if (!contains) days.add(day);
            }
        }

        arr = new int[days.size()]; // the array which will be returned
        for (int i=0; i<days.size(); i++) // converts ArrayList into array
        {
            arr[i] = days.get(i);
        }

        // close file reader
        br.close();

        return arr;
    }

    /*
     Method Name: customerQuery
     Return Type: HashMap<Integer, ArrayList<Integer>> - A hashmap (key: room, value: array of the days) of the
                                                         rooms & days a customer has booked
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
     Description: Returns the rooms a customer has booked and the days they have booked it for
     Dates Modified:
     * 17/05/2024
       Raymond Zhang - Moved variable declarations to beginning of method
    */
    public static Map<Integer, List<Integer>> customerQuery(String firstName, String lastName) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS_DB));
        Map<Integer, List<Integer>> reservations = new HashMap<>(); // HashMap to be returned
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
                            reservations.put(room, new ArrayList<>());

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
     Method Name: employeePinQuery
     Return Type: String[0] - The pin of the employee, null if the employee doesn't exist
                  String[1] - Whether the employee is admin or not (0 or 1)
     Parameters: int id - The employee id to search for
     Description: Returns the pin and admin status of an employee
     */
    public static String[] employeePinQuery (String id) throws IOException
    {
        // init variables and file reader
        String pin = null; // pin of the employee
        String admin = null; // whether the employee is admin
        String line;
        BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES_DB));

        boolean foundId = false;
        while (!foundId)
        {
            line = br.readLine();

            // check if EOF
            if (line == null) foundId = true;

            // check if the employee id matches
            else if (line.equals(id)) {
                br.readLine();
                br.readLine();

                // update pin admin and foundId
                pin = br.readLine();
                admin = br.readLine();
                foundId = true;
            }

            else {
                // skip next lines
                for (int i=0; i<4; i++) br.readLine();
            }
        }

        // close file reader
        br.close();

        return new String[]{pin, admin}; // returns an array with two indices
    }

    /*
     Method Name: allCustomers
     Return Type: HashMap<String, HashMap<Integer, ArrayList<Integer>>> - A nested hashmap representing the customers'
                                                                          names and rooms booked
     Description: Returns all customers and the rooms/days they have booked
    */
    public static Map<List<String>, Map<Integer, List<Integer>>> allCustomers() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS_DB));
        Map<List<String>, Map<Integer, List<Integer>>> customers = new HashMap<>();
        String fName, lName, line;
        List<String> name;
        List<String> db = new ArrayList<>(); // the file converted into an ArrayList

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
        name = new ArrayList<>();
        name.add(fName);
        name.add(lName);
        customers.put(name, customerQuery(fName, lName));
        for (int i=2; i<db.size()-2; i++)
        {
            // check if there is a new customer
            if (db.get(i).equals(CUSTOMER_DELIMITER))
            {
                // add this customer to the HashMap
                fName = db.get(i+1);
                lName = db.get(i+2);
                name = new ArrayList<>();
                name.add(fName);
                name.add(lName);
                customers.put(name, customerQuery(fName, lName));
            }
        }

        return customers;
    }

    /*
     Method Name: allDays
     Return Type: List<List<Integer>>
     Description: returns all days and the rooms reserved on that day. the index of the first ArrayList represents the date
     */
    public static List<List<Integer>> allDays() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(DAYS_DB));
        List<List<Integer>> days = new ArrayList<>();
        String line;
        List<Integer> rooms = new ArrayList<>();
        List<String> db = new ArrayList<>(); // the file converted into an ArrayList

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
                days.add(new ArrayList<>());
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

        return days;
    }

    /*
     Method Name: allRooms
     Return Type: List<List<Integer>>
     Description: returns all rooms in the hotel
     */
    public static List<Integer> allRooms() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(ROOMS_DB));
        List<Integer> rooms = new ArrayList<>();
        String line;

        // loop through rooms.txt and append to rooms
        line = br.readLine();
        while (line != null)
        {
            rooms.add(Integer.parseInt(line));
            line = br.readLine();
        }

        return rooms;
    }

    /*
     Method Name: allEmployees
     Return Type: List<HashMap<String, String>>
     Description: returns all employees in an ArrayList
     */
    public static List<HashMap<String, String>> allEmployees() throws IOException
    {
        // declare variables and init file reader
        BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES_DB));
        List<HashMap<String, String>> employees = new ArrayList<>();
        String id, fName, lName, pin, isAdmin;

        // loop through employees.txt and append to employees
        id = br.readLine();
        fName = br.readLine();
        lName = br.readLine();
        pin = br.readLine();
        isAdmin = br.readLine();
        while (id != null)
        {
            employees.add(new HashMap<>());
            employees.get(employees.size()-1).put("id", id);
            employees.get(employees.size()-1).put("firstName", fName);
            employees.get(employees.size()-1).put("lastName", lName);
            employees.get(employees.size()-1).put("pin", pin);
            employees.get(employees.size()-1).put("isAdmin", isAdmin);
            id = br.readLine();
            fName = br.readLine();
            lName = br.readLine();
            pin = br.readLine();
            isAdmin = br.readLine();
        }

        return employees;
    }
}