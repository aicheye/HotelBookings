# Hotel Booking System

## Control Flow

[Flowchart](https://drive.google.com/file/d/1YJkL57tfEXFMBmD-4oAJvd7AGb7UpFbH/view?usp=drive_link)

## Methods

| *Class (.java file)* | *Methods* | *Method Description(s)* |
| --- | --- | --- |
| HotelBooking (driver) | `static void main(String[] args)`<br><br>`static void login()`<br><br>`static void displayMenu(boolean isAdmin)`<br><br>`static int getDateInput()`<br><br>`static int getRoomInput(int date)`<br><br>`static int[] getReservation(String firstName, String lastName)`<br><br>`static String getCurrentUser()`<br><br>`static int dateStrToInt(String dateStr)`<br><br>`static String dateIntToStr(int days)` | **\*main**: Driver code. Continuously loops the login screen.<br><br>**login**: Continuously asks for employee ID until a valid one is entered. Then, get the employee PIN until successful or 0 is entered to go back. Will start the according menu method if successful.<br><br>**displayMenu**: Outputs the menu and asks for an integer input to select an operation. Will display additional options for admin.<br><br>**getDateInput**: Helper method for getting a valid date from the user.<br><br>**getRoomInput**: Helper method for getting a valid room on a given date.<br><br>**getReservation**: Helper method for getting a reservation made by a customer. Reservation is represented as a room and a date.<br><br>**getCurrentUser**: Accessor method for the `employeeID` variable, which represents the currently logged in employee.<br><br>**dateStrToInt**: Converts a formatted date into a number representing the number of days past since Jan 01, 2024.<br><br>**dateIntToStr**: Converts an integer representing the number of days since Jan 01, 2024 to a readable string |
| Reservations | `static boolean listAvailableRooms (int date)`<br><br>`static void listReservations (int date)`<br><br>`static void listReservations (String firstName, String lastName)`<br><br>`static boolean checkAvailability (int date, int room)`<br><br>`static void reserveCreate (String firstName, String lastName, int room, int date)`<br><br>`static void reserveCancel (String firstName, String lastName, int room, int date)`<br><br>`static void reserveChange (String firstOld, String lastOld, int room, int date, String firstNew, String lastNew)`<br><br>`static void reserveChange (String firstName, String lastName, boolean changeRoom, int roomOrDate int old, int now)` | **listAvailableRooms**: Lists all available rooms on a given date. Returns `false` if no rooms are available.<br><br>**listReservations**: Lists all reservations for a given date or name. Output a message if no reservations were made for the given date and/or name.<br><br>Sample output:<br><pre>Customer [firstName] [lastName] has booked the following rooms on [date]:<br><br>  [Room #]<br>  [Room #]</pre><br>**checkAvailability**: Checks if a room is available on a given date.<br><br>**reserveCreate**: Creates a reservation for a given room and date.<br><br>**reserveCancel**: Cancels a reservation and outputs if the reservation does not exist.<br><br>**reserveChange**: Adjusts the name, room #, and date of reservation. There are two `reserveChange` methods: one handles changing the name, and the other handles changing the date or room. |
| Query (file reader) | `static boolean reservationExists (String firstName, String lastName, int room, int date)`<br><br>`static boolean roomAvailable (int room, int date)`<br><br>`static List<Integer> getAvailableRooms(int date)`<br><br>`static Map<String, List<Integer> getReservationsDate (int date)`<br><br>`static Map<String, List<Integer>> getReservationsRoom (int room)`<br><br>`static Map<Integer, List<Integer>> getReservationsCustomer (String firstName, lastName)`<br><br>`static String[] getEmployee (String id)`<br><br>`static Map<List<String>, Map<Integer, List<Integer>>> getAllCustomers()`<br><br>`static List<List<Integer>> getAllDays()`<br><br>`static List<Integer> getAllRooms()`<br><br>`static List<HashMap<String, String>> getAllEmployees()` | **reservationExists**: Checks whether a specific reservation exists.<br><br>**roomAvailable**: Checks whether a room is available for reservation on a given date.<br><br>**getAvailableRooms**: Returns an array of available rooms on a given date.<br><br>**getReservationsDate**: Returns all reservations on a given date as a HashMaps of customers and an ArrayList of rooms.<br><br>**getReservationsRoom**: Returns all reservations for a given room as a HashMap of customer names and an ArrayList of days.<br><br>**getReservationsCustomer**: Returns the rooms a customer has booked and the days they have booked it for.<br><br>**getAllCustomers**: Returns all customers and the rooms/days they have booked.<br><br>**getAllDays**: Returns all days and the rooms reserved on that day. The index of the first ArrayList represents the date.<br><br>**getAllRooms**: Returns a list of all rooms in the hotel.<br><br>**getAllEmployees**: Returns all employees in an ArrayList. |
| Write (file writer) | `static void addEmployee (String id, String firstName, String lastName, String pin, String isAdmin)`<br><br>`static void delEmployee (String id)`<br><br>`static void addRoom (int room)`<br><br>`static void delRoom (int room)`<br><br>`static void addReserve (String firstName, string lastName, int room, int date)`<br><br>`static void delReserve (String firstName, String lastName, int room, int date)`<br><br>`static void edtReserve (String oldFirst, String oldLast, int room, int date, String newFirst, String newLast)`<br><br>`static void edtReserve (String firstName, string lastName, int code, boolean changeRoom, int roomOrDate, int old, int now)`<br><br>`static void edtPin (String id, int now)`<br><br>`static void logOp (String op)`<br><br>`static void updateAllCustomers (Map<List<String>, Map<Integer, List<Integer>>> customers)`<br><br>`static void updateAllDays(List<List<Integer>> days)`<br><br>`static void updateAllRooms(List<Integer> rooms)`<br><br>`static void updateAllEmployees (List<HashMap<String, String>> employees)` | **addEmployee**: Adds new employee to `employees.txt`.<br><br>**delEmployee**: Searches `employees.txt` for an employee ID and removes their corresponding block from the file.<br><br>**addRoom**: Adds new room number to `rooms.txt`.<br><br>**delRoom**: Completely removes room number from `rooms.txt`.<br><br>**addReserve**: Writes new reservations in `rooms.txt` as well as `customers.txt`.<br><br>**delReserve**: Deletes reservation from `customers.txt` by date and name, and updates `days.txt`.<br><br>**edtReserve**: Updates a reservation in `customers.txt` as well as `days.txt`.<br><br>**edtPin**: Updates a user’s pin in `employeeAccounts.txt`.<br><br>**logOp**: Logs operations to `log.txt`.<br><br>**updateAllCustomers**: Updates the `customers.txt` file.<br><br>**updateAllDays**: Updates the `days.txt` file.<br><br>**updateAllRooms**: Updates the `rooms.txt` file.<br><br>**updateAllEmployees**: Updates the `employees.txt` file. |

## Database Structure

<table>
  <thead>
    <tr>
      <th><p><em>File Name</em></p></th>
      <th><p><em>Example</em></p></th>
      <th><p><em>File Description</em></p></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>employees.txt</td>
      <td>
        <pre>123456
John
Doe
1234
0</pre>
      </td>
      <td>
        Stores the account information of each hotel employee in blocks of text. Each block begins with the 6-digit ID number, followed by the employee’s first name, surname, 4-digit PIN, and whether or not they are an admin.
      </td>
    </tr>
    <tr>
      <td>
        days.txt
      </td>
      <td>
        <pre>0
101
~
1
101
201
202
~
2
101
201
~
3
101
302
~</pre>
      </td>
      <td>
        <p>Stores the reservation status of each room. Each block begins with a day # and the rooms reserved on that day. Day 0 is an arbitrary date (Unix Epoch).</p><p>Each <code>~</code> indicates a new date.</p>
      </td>
    </tr>
    <tr>
      <td>
        <p>rooms.txt</p>
      </td>
      <td>
        <pre>101
201
202
301
302</pre>
      </td>
      <td>
        <p>Lists all rooms available for rent in ascending order.</p>
      </td>
    </tr>
    <tr>
      <td>
        customers.txt
      </td>
      <td>
        <pre>Jane
Doe
202
1
-
`
John
Doe
101
0
1
2
3
-
201
1
2
-
302
3
-
`
Albert
Jameson
-
`</pre>
      </td>
      <td>
        <p>Stores the reservations for each customer. Blocks start with the name of the customer who has reserved a room. Each customer has a list of rooms which they have reserved and the dates they have reserved the rooms for in an array. Day 0 is an arbitrary date (Unix Epoch)</p>
        <p>Each <code>-</code> indicates a new room which the person has reserved</p>
        <p>A <code>`</code> indicates a new customer</p>
      </td>
    </tr>
    <tr>
      <td>
        <p>log.txt</p>
      </td>
      <td>
        <div style="width:300px"></div>
        <pre>123456 0 PIN 123456 1234 5678
123456 0 RES del John Doe 101 4
000000 1 ROOM add 301
000000 1 ROOM del 301
000000 1 EE del 111111
000000 1 EE add 100101</pre>
      </td>
      <td>
        <p>Keeps track of changes made in the file system.</p><p>Each block starts with the currently signed-in employee id and if they are an admin or not.</p>
        <p>Syntax:</p>
        <ul>
          <li><code>PIN [id] [old] [new]</code>
            <ul><li>Changed PIN of <code>[id]</code> from <code>[old]</code> to <code>[new]</code></li></ul>
          </li>
          <li><code>RES [add/del] [name] [room] [date]</code>
            <ul><li>Added/removed/modified a reservation</li><li>Modifying a reservation is logged as a deletion and then an addition</li></ul>
          </li>
          <li><code>ROOM [add/del] [#]</code>
            <ul><li>Added/removed a room</li><li>Modifying a room is logged as a deletion and then an addition</li></ul>
          </li>
          <li><code>EE [add/del] [id]</code>
            <ul><li>Added/removed an employee</li></ul>
          </li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>

## Authors

- Sean Liu
- Sean Yang
- Raymond Zhang
