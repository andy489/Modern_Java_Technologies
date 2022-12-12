package formatter;

import java.util.Calendar;
import java.util.Formatter;

public class FormatterExamples {
    public static void main(String... args) {
        double balance = 6_217.584;

        Formatter f = new Formatter();

        System.out.println(f.format("Amount diff since statement: $ %,.2f", balance));
        System.out.format("Local time: %tT", Calendar.getInstance());
    }
}
