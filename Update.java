import java.io.*;

/*
 Programmer: Sean Yang
 Program Name: Update
 Date: 23/05/2024
 Description: Interface between HotelBooking class and Write class
 */

public class Update {
    /*
     Method Name: reserveCreate
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 int room - room number
                 int date - given date
     Description: Creates a reservation for a given room and date
     Dates modified:
     * 23/05/2024
     * Sean Yang - Created and completed method (tested)

     * 24/05/2024
     * Sean Yang - Update error messages for consistency with HotelBookings
     */
    public static void reserveCreate(String firstName, String lastName, int room, int date) {
        try
        {
            // check if the room is available
            if (Query.roomAvailable(room, date))
            {
                // add to reservations
                Write.addReserve(firstName, lastName, room, date);
                System.out.println("Reservation created successfully."); // output success
            }
            else
            {
                System.out.println("*ERROR: The room you are trying to book is not available on that date."); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveCancel
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 int room - room number
                 int date - given date
     Description: Cancels a reservation and outputs if the reservation does not exist
     Dates modified:
     * 23/05/2024
     * Sean Yang - reworked function to be more concise

     * 24/05/2024
     * Sean Yang - Update error messages for consistency with HotelBookings
     */
    public static void reserveCancel(String firstName, String lastName, int room, int date) {
        try
        {
            // check if the reservation exists
            if (Query.reservationExists(firstName, lastName, room, date))
            {
                // remove from reservations
                Write.delReserve(firstName, lastName, room, date);
                System.out.println("Reservation cancelled successfully."); // output success
            }
            else
            {
                System.out.println("**ERROR: The reservation you are trying to cancel does not exist."); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveChange (Changing the name)
     Parameters: String oldFirst - first name of person
                 String oldLast - last name of person
                 int room - room number
                 int date - given date
                 String newFirst - the first name to change to
                 String newLast - the last name to change to
     Description: Changes the name a reservation is under
     Dates modified:
     * 23/05/2024
     * Sean Yang - Created and completed method (tested)

     * 24/05/2024
     * Sean Yang - Update error messages for consistency with HotelBookings
     */
    public static void reserveChange(String oldFirst, String oldLast, int room, int date, String newFirst, String newLast) {
        try
        {
            // check if the reservation exists
            if (Query.reservationExists(oldFirst, oldLast, room, date))
            {
                // change reservation
                Write.edtReserve(oldFirst, oldLast, room, date, newFirst, newLast);
                System.out.println("Reservation changed successfully."); // output success
            }
            else
            {
                System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    /*
     Method Name: reserveChange (changing the date or room)
     Parameters: String firstName - first name of person
                 String lastName - last name of person
                 boolean changeRoom - whether the user wants to change the room (false: change the date)
                 int dateOrRoom - either the date or the room, whichever doesn't change
                 int old - the old value of the date/room
                 int now - the new value of the date/room
     Description: Changes a reservation (either changing the date or the room #)
     Dates modified:
     * 23/05/2024
     * Sean Yang - Created and completed method (tested)

     * 24/05/2024
     * Sean Yang - Update error messages for consistency with HotelBookings
     */
    public static void reserveChange(String firstName, String lastName, boolean changeRoom, int dateOrRoom, int old, int now) {
        try
        {
            // runs if the user wants to change the room number
            if (changeRoom)
            {
                // check if the reservation exists
                if (Query.reservationExists(firstName, lastName, old, dateOrRoom))
                {
                    // update reservation
                    Write.edtReserve(firstName, lastName, true, dateOrRoom, old, now);
                    System.out.println("Reservation changed successfully."); // output success
                }
                else
                {
                    System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
                }
            }
            // runs if the user wants to change the date
            if (!changeRoom)
            {
                // check if the reservation exists
                if (Query.reservationExists(firstName, lastName, dateOrRoom, old))
                {
                    // update reservation
                    Write.edtReserve(firstName, lastName, false, dateOrRoom, old, now);
                    System.out.println("Reservation changed successfully."); // output success
                }
                else
                {
                    System.out.println("**ERROR: The reservation you are trying to change does not exist.**"); // output error
                }
            }
        }
        // output if there is an issue reading the file
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
