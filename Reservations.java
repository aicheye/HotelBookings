import java.io.*;
import java.util.Scanner;

public class Reservations {
    public static void listAvailableRooms(int date) {
        try {
            int[] rooms = Query.dateQuery(date);
            int size = rooms.length;
            if (size ==0){
                System.out.println("There are no rooms avaiable");
            }
            else{
            for (int i=0; i<size; i++){
                System.out.println(rooms[i]);
            }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        ;
    }
    
}
