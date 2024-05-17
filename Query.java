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
    static final String DAYS_DB = "days.txt";
    static final String CUSTOMERS_DB = "customers.txt";
    static final String EMPLOYEES_DB = "employees.txt";
    static final String ROOMS_DB = "rooms.txt";

    // Constants for file delimiters
    static final String DATE_DELIMITER = "~"; // used in days.txt
    static final String ROOM_DELIMITER = "-"; // used in customers.txt
    static final String CUSTOMER_DELIMITER = "`"; // used in customers.txt

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
     Return Type: int[0] - The pin of the employee, -1 if the employee doesn't exist
                  int[1] - Whether the employee is admin or not (0 or 1)
     Parameters: int id - The employee id to search for
     Description: Returns the pin and admin status of an employee
     */
    public static int[] employeePinQuery (int id) throws IOException
    {
        // init variables and file reader
        int pin = -1; // pin of the employee
        int admin = -1; // whether the employee is admin
        BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES_DB));

        boolean foundId = false;
        while (!foundId)
        {
            // check if the employee id matches
            if (Integer.parseInt(br.readLine()) == id) {
                br.readLine();
                br.readLine();

                // update pin admin and foundId
                pin = Integer.parseInt(br.readLine());
                admin = Integer.parseInt(br.readLine());
                foundId = true;
            }

            else {
                // skip next lines
                for (int i=0; i<4; i++) br.readLine();
            }
        }

        // close file reader
        br.close();

        return new int[]{pin, admin}; // returns an array with two indices
    }
}