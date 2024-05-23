import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Update {
    /*
  Method Name: reserveCancel
  Return Type: void - (placeholder)
  Parameters: String firstName - first name of person
              String lastName - last name of person
              int room - room number
              int date - given date
  Description: (Placeholder) prints out if customer has room booked on given date
  DOES NOT PRINT OUT ERROR MESSAGE

  */
    public static void reserveCancel(String firstName, String lastName, int room, int date) {
        try {
            Scanner sc = new Scanner(System.in);
            String input;
            boolean roomfound = false;
            Map<Integer, List<Integer>> rooms = Query.customerQuery(firstName, lastName);
            if (rooms.containsKey(room)) {
                for (int e : rooms.keySet()) {//gets each room #
                    if (e == room) {
                        roomfound = true;//changes if is found
                        List<Integer> value = rooms.get(e);//gets the days that the room is booked for.
                        if (value.contains(date)) {
                            System.out.printf("Customer does have room %d on day %d\n", room, date);//placeholder for actual cancellation
                            System.out.println("Do you want to continue with cancellation? 1 for confirm, -1 for cancel");
                            boolean cancel = true;
                            do {
                                   input = sc.nextLine();
                                if (input.equals("-1")) {
                                    System.out.println("canceled");
                                    cancel = false;
                                } else if (input.equals("1")) {
                                    System.out.println("confirmed");
                                    cancel = false;
                                    //put in canceler
                                }
                                else{
                                    System.out.println("Input mismatch, enter again");
                                }//checks if the inputs are the ones required
                            } while (cancel);//keeps running while user confirms their choice
                            break;
                        }
                    }else{
                        roomfound= false;
                    }
                }
                if (!roomfound){
                        System.out.println("roomnotfound");//prints out error message
                }

            }
            else{
                System.out.printf("Customer does not have Room %d reserved",room);
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
