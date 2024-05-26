# Hotel Booking System

Sean Liu, Sean Yang, Raymond Zhang

## Requirements

You will not have to worry about the requirements stage, as this document will effectively serve as your project requirements document.  Your client (i.e., Mr. Skuja) has requested that you design a hotel reservation booking system.  Your system must have the following features:

- It must store the information of hotel’s employee accounts in a saved file.
  - Each employee should have a 6-digit employee ID number, a first & last name, and a 4-digit persona identification number (PIN) which works as a password.
  - Your system should also have an administrator account with the employee ID 000000 and the default password of **1234**
- When your system begins, it should prompt the user to enter their employee ID number and then their PIN.
  - If they enter a valid employee number but an incorrect PIN, your program should give an error message informing them that it is an incorrect PIN.
  - If they do not enter a valid employee number, your software should print a message stating that the employee number does not exist.
  - If an incorrect PIN is given, your program should enter a loop, prompted for the user to retry their password, or enter 0 to go back to the login screen.
  - If the login is successful, the user should be given access to a menu of options.
  - Once the user finishes with the reservation and logs out, this login process should **loop infinitely**.  The device your software is intended to run on will be running constantly, and will be powered off when the software is to be shut down.
- Once a user is successfully logged in, they should be able to use a menu system to do any of the following:
  - List the available rooms for a given date
  - List all the reservations for a given date
  - Look up all the reservations made under a certain name
  - Make a reservation for a room
    - This should ask for the first and last name of a customer, and a date
    - The system will then output a list of rooms available on that date, and allow the user to choose one of them
    - The system should then save the reservation in your file system, including the customer name, the room booked, the date, and the employee who made the reservation
  - Cancel a reservation for a room
    - This should ask for the first and last name of a customer, and then list all of the reservations they have made by date and room number
    - The system should then allow the user to choose which reservation to cancel, or to abort the cancellation
    - The system should then update the reservation file system
  - Change the details on a reservation
    - Customer name should be able to be changed
    - Dates and room number should be able to be changed as well, but the system should check that the room is available on the new date.
  - Change their PIN number
  - Log out (return to the user login loop)
- If the user logs in on the administrator account, they should have additional options:
  - Add a hotel room
  - Delete a hotel room
  - Add an employee
  - Delete an employee
- Any time the user makes a change to something in the system which should be persistent information – whether booking or cancelling a reservation, changing a PIN, adding or deleting rooms or employees – those details should be saved to a file.
- Your system should ensure that any actions performed make sense (for example, a user should not be able to book a room that is already reserved on a certain date, or cancel a reservation that does not exist) and should make use of input validation loops whenever input is allowed.  It should not be possible to crash the system.
