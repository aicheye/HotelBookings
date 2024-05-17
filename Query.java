import java.util.*;
import java.io.*;

/*
 Programmer: Sean Yang
 Program Name: Query
 Date: 16/05/2024
 Description: Performs various queries on database (.txt) files
 */

public class Query
{
    // Create file readers
    static final String DAYS = "days.txt";
    static final String CUSTOMERS = "customers.txt";
    static final String EMPLOYEES = "employees.txt";

    // Constants for file delimiters
    static final String DATE_DELIMITER = "~"; // used in days.txt
    static final String ROOM_DELIMITER = "-"; // used in customers.txt
    static final String CUSTOMER_DELIMITER = "`"; // used in customers.txt

    /*
     Method Name: dateQuery
     Return Type: int[] - An array of the rooms available on a certain date
     Parameters: int date - The date to search
     Description: Returns an array of available rooms on a given date
     */
    public static int[] dateQuery (int date) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(DAYS));
        List<Integer> rooms = new ArrayList<>(); // ArrayList of the rooms available on a date

        // loop through every day
        boolean end = false;
        while (!end)
        {
            String line = br.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                int day = Integer.parseInt(line);

                // check if date matches and loop through every room available on that day
                if (day == date) {
                    boolean searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        // check if there are no other rooms available on that day
                        if (line.equals(DATE_DELIMITER)) searching = false;
                        // append to array
                        else rooms.add(Integer.parseInt(line));
                    }
                }

                // otherwise, keep reading until we reach a new date
                else {
                    boolean searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        if (line.equals(DATE_DELIMITER)) searching = false;
                    }
                }
            }
        }

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
    public static int[] roomQuery (int room) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(DAYS));
        List<Integer> days = new ArrayList<>(); // ArrayList of the days a room is available

        // loop through every day
        boolean end = false;
        while (!end)
        {
            String line = br.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                int day = Integer.parseInt(line);

                // loop through every available room on that day
                boolean searching = true;
                while (searching)
                {
                    line = br.readLine();
                    // check if there are no other rooms on the day
                    if (line.equals(DATE_DELIMITER)) searching = false;
                    // check if the room is available under the current date, if it is, append to ArrayList
                    else if (Integer.parseInt(line) == room) days.add(day);
                }
            }
        }

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
    public static Map<Integer, List<Integer>> customerQuery(String firstName, String lastName) throws IOException
    {
        // init file reader and variables
        BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS));
        Map<Integer, List<Integer>> reservations = new HashMap<>(); // HashMap to be returned

        // loop through every customer
        boolean end = false;
        while (!end)
        {
            String line = br.readLine();
            // check if EOF
            if (line == null) end = true;
            // if not EOF continue
            else
            {
                String f = line;
                String l = br.readLine();

                // if the first and last name matches, loop through all their reservations
                if (f.equals(firstName) && l.equals(lastName))
                {
                    boolean searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        // check if we are at a new customer or not
                        if (line.equals(CUSTOMER_DELIMITER)) searching = false;
                        // if we are not at a new customer, continue looping
                        else {
                            int room = Integer.parseInt(line); // create a new variable for the current room

                            // create a new key-value pair in reservations
                            reservations.put(room, new ArrayList<>());

                            // loop until we are at end of room
                            boolean inRoom = true;
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
                    boolean searching = true;
                    while (searching)
                    {
                        line = br.readLine();
                        if (line.equals("`")) searching = false;
                    }
                }
            }
        }

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
        BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES));

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

        return new int[]{pin, admin}; // returns an array with two indices
    }
}
