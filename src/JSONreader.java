import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class JSONreader {
    public static void main(String[] args) throws IOException, ParseException {
        String a = "node/23637223";
        String b = "way/32989823";
        String c = "abcdefg/4738743";
        String a1 = a.replaceAll("[^1-9]", "");
        String b1 = b.replaceAll("[^1-9]", "");
        String c1 = c.replaceAll("[^1-9]", "");
        System.out.println(a1);
        System.out.println(a1);
        System.out.println(c1);
    }

}
