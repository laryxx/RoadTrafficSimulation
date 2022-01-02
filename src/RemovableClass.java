import java.util.ArrayList;
import java.util.Random;

public class RemovableClass {
    public static void main(String[] args){
//        ArrayList<Integer> arr = new ArrayList<>();
//        int n = 3;
//        for(int i = 0; i < 100; i++) {
//            var num = (Math.pow(Math.floor(Math.random()* 10), 2))/(10);
//            arr.add((int) num);
//            System.out.println(num);
//        }
//        int val = 0;
//        for(int i = 0; i<arr.size(); i++){
//            val = val + arr.get(i);
//        }
        for(int i = 0; i < 100; i++) {
            Random rand = new Random();
            int n = rand.nextInt(4);
            System.out.println(n);
        }
    }
}
