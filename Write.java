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
     Dates Modified:
     * 21/05/2024
       Sean Yang - Created and completed method

     * 23/05/2024
       Sean Yang - Found some issues with the method and rewrote it using Query.allCustomers and Query.allDays, now
                   works as expected.
     */
    public static void addReserve(String firstName, String lastName, int room, int date) throws IOException
    {
        // declare variables
        Map<List<String>, Map<Integer, List<Integer>>> customers = Query.getAllCustomers(); // get all customers
        List<List<Integer>> days = Query.getAllDays(); // get all days
        boolean customerExists = false; // whether the customer is in the database
        boolean customerBookedRoom = false; // whether the customer has previously booked this room
        List<String> Name = new ArrayList<String>(); // ArrayList representing the customer's full name
        Name.add(firstName);
        Name.add(lastName);
        String op; // the log message

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
        updateAllCustomers(customers);
        updateAllDays(days);

        // log changes
        op = "RES add";
        op += " " + firstName + " " + lastName;
        op += " " + room;
        op += " " + date;
        logOp(op);
    }

    /*
     Method Name: delReserve
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
                 int room - The room number
                 int date - The date
     Description: Deletes a reservation from customers.txt and days.txt
     Dates Modified
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void delReserve(String firstName, String lastName, int room, int date) throws IOException
    {
        // declare variables
        Map<List<String>, Map<Integer, List<Integer>>> customers = Query.getAllCustomers(); // get all customers
        List<List<Integer>> days = Query.getAllDays(); // get all days
        List<String> name = new ArrayList<>(); // ArrayList representing the customer's full name
        name.add(firstName);
        name.add(lastName);
        String op; // the log message

        // remove reservation from customers
        customers.get(name).get(room).remove((Integer) date);
        // check if the room booking is now empty and remove the room
        if (customers.get(name).get(room).size() == 0) customers.get(name).remove(room);
        // check if the customer has no remaining reservations and remove the room
        if (customers.get(name).size() == 0) customers.remove(name);

        // remove reservation from days
        days.get(date).remove((Integer) room);

        // write to file using allCustomers and allDays
        updateAllCustomers(customers);
        updateAllDays(days);

        // log changes
        op = "RES del";
        op += " " + firstName + " " + lastName;
        op += " " + room;
        op += " " + date;
        logOp(op);
    }

    /*
     Method Name: edtReserve (changing the name)
     Parameters: String oldFirst - The first name of the reservation
                 String oldLast - The last name of the reservation
                 int room - The room number
                 int date - The date
                 String newFirst - The new first name
                 String newLast - The new last name
     Description: Updates a reservation in customers.txt and days.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void edtReserve(String oldFirst, String oldLast, int room, int date, String newFirst, String newLast) throws IOException
    {
        delReserve(oldFirst, oldLast, room, date); // delete the old reservation
        addReserve(newFirst, newLast, room, date); // add a new reservation
    }

    /*
     Method Name: edtReserve (changing the room or date)
     Parameters: String firstName - The first name of the customer
                 String lastName - The last name of the customer
                 boolean changeRoom - Whether the user wants to change the room
                 int dateOrRoom - The date or room, whichever is unchanged
                 int old - The old value
                 int now - The new value
     Description: Updates a reservation in customers.txt and days.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void edtReserve(String firstName, String lastName, boolean changeRoom, int dateOrRoom, int old, int now) throws IOException
    {
        // if the user wants to change the room
        if (changeRoom) {
            delReserve(firstName, lastName, old, dateOrRoom); // delete the old reservation
            addReserve(firstName, lastName, now, dateOrRoom); // add a new reservation with the new room
        }

        // if the user wants to change the date
        else {
            delReserve(firstName, lastName, dateOrRoom, old); // delete the old reservation
            addReserve(firstName, lastName, dateOrRoom, now); // add a new reservation with the new date
        }
    }

    /*
     Method Name: addRoom
     Parameters: int room - The room to be added
     Description: Adds a room to rooms.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)
     */
    public static void addRoom(int room) throws IOException
    {
        // declare variables
        List<Integer> rooms = Query.getAllRooms();
        String op; // the log message

        // append to ArrayList
        rooms.add(room);

        // write using allRooms
        updateAllRooms(rooms);

        // log changes
        op = "ROOM add";
        op += " " + room;
        logOp(op);
    }

    /*
     Method Name: delRoom
     Parameters: int room - The room to be removed
     Description: Deletes a room from rooms.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void delRoom(int room) throws IOException
    {
        // declare variables
        List<Integer> rooms = Query.getAllRooms();
        String op; // the log message

        // remove from ArrayList
        rooms.remove((Integer) room);

        // write using allRooms
        updateAllRooms(rooms);

        // log changes
        op = "ROOM del";
        op += " " + room;
        logOp(op);
    }

    /*
     Method Name: addEmployee
     Parameters: String id - The id of the employee
                 String firstName - The employee's first name
                 String lastName - The employee's last name
                 String pin - The employee's login pin
                 String isAdmin - Whether this employee is an admin or not
     Description: Adds a new employee to employees.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void addEmployee(String id, String firstName, String lastName, String pin, String isAdmin) throws IOException
    {
        // declare variables
        List<HashMap<String, String>> employees = Query.getAllEmployees();
        HashMap<String, String> newEmployee = new HashMap<String, String>();
        String op; // the log message

        // init new employee and add to ArrayList
        newEmployee.put("id", id);
        newEmployee.put("firstName", firstName);
        newEmployee.put("lastName", lastName);
        newEmployee.put("pin", pin);
        newEmployee.put("isAdmin", isAdmin);
        employees.add(newEmployee);

        // write to file using allEmployees
        updateAllEmployees(employees);

        // log changes
        op = "EE add";
        op += " " + id;
        logOp(op);
    }

    /*
     Method Name: delEmployee
     Parameters: String id - The employee to remove
     Description: Deletes an employee entry from employees.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void delEmployee(String id) throws IOException
    {
        // declare variables
        List<HashMap<String, String>> employees = Query.getAllEmployees();
        int index = -1; // the index of the employee to be removed
        String op; // the log message

        // loop over the employees and locate the correct id to remove
        for (int i=0; i<employees.size(); i++)
        {
            // if the id matches, save the index
            if (employees.get(i).get("id").equals(id)) index = i;
        }

        // remove this employee from the database if they exist
        if (index != -1) employees.remove(index);

        // write to file using allEmployees
        updateAllEmployees(employees);

        // log changes
        op = "EE del";
        op += " " + id;
        logOp(op);
    }

    /*
     Method Name: edtPin
     Parameters: String id - The employee to edit
                 String newPin - The employee's pin
     Description: Change's an employees pin in employees.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method
     */
    public static void edtPin(String id, String newPin) throws IOException
    {
        // declare variables
        List<HashMap<String, String>> employees = Query.getAllEmployees();
        String oldPin = "";
        String op; // the log message

        // loop over the employees and locate the correct id to update
        for (HashMap<String, String> e : employees) {
            // if the id matches, change their pin and save the old pin
            if (e.get("id").equals(id))
            {
                e.put("pin", newPin);
                oldPin = e.get("oldPin");
            }
        }

        // write to file using allEmployees
        updateAllEmployees(employees);

        // log changes
        op = "PIN";
        op += " " + id;
        op += " " + oldPin;
        op += " " + newPin;
        logOp(op);
    }

    /*
     Method Name: logUser
     Parameters: String id - the id of the user currently logged in
                 String isAdmin - whether the user is admin (1 for admin, 0 for normal user)
     Description: logs a new user in log.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)
     */
    public static void logUser(String id, String isAdmin) throws IOException
    {
        // init file writer
        BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_DB, true));

        // write to file
        bw.write("\n");
        bw.write(id);
        bw.write(" ");
        bw.write(isAdmin);

        // close file writer
        bw.close();
    }

    /*
     Method Name: logOp
     Parameters: String op - the operation to be logged
     Description: logs an operation to log.txt
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)
     */
    public static void logOp(String op) throws IOException
    {
        // init file writer
        BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_DB, true));

        // write to file
        bw.write("\n");
        bw.write(op);

        // close file writer
        bw.close();
    }

    /*
     Method Name: updateAllCustomers
     Parameters: Map<List<String>, Map<Integer, List<Integer>>> customers: a java representation of customers.txt
     Description: converts java parseable data into days.txt data structure
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 25/05/2024
       Sean Yang - Changed the method name to updateAllCustomers for clarity
     */
    public static void updateAllCustomers(Map<List<String>, Map<Integer, List<Integer>>> customers) throws IOException
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
     Method Name: updateAllDays
     Parameters: List<List<Integer>> days: a java representation of days.txt: each index represents a date with reservations
     Description: converts java parseable data into days.txt data structure
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 25/05/2024
       Sean Yang - Changed the method name to updateAllDays for clarity
     */
    public static void updateAllDays(List<List<Integer>> days) throws IOException
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
     Method Name: updateAllRooms
     Parameters: List<Integer> rooms - an ArrayList containing every room
     Description: converts java parseable data into rooms.txt data structure
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 25/05/2024
       Sean Yang - Changed the method name to updateAllRooms for clarity
     */
    public static void updateAllRooms(List<Integer> rooms) throws IOException
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
     Method Name: updateAllEmployees
     Parameters: List<HashMap<String, String>> employees - an ArrayList containing data about every employee
     Description: converts java parseable data into employees.txt data structure
     Dates Modified:
     * 23/05/2024
       Sean Yang - Created and completed method (tested)

     * 25/05/2024
       Sean Yang - Changed the method name to updateAllEmployees for clarity
     */
    public static void updateAllEmployees(List<HashMap<String, String>> employees) throws IOException
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
