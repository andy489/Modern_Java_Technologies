package enums;

import java.util.Arrays;

public class DaysExample {
    private final Day day;

    public DaysExample(Day day) {
        this.day = day;
    }

    public static void main(String... args) {
        DaysExample example = new DaysExample(Day.TUESDAY);
        System.out.println(example.tellItLikeItIs()); // Midweek days are so-so

        // The values() method is a special method added by the compiler
        Day[] days = Day.values();
        System.out.println(Arrays.toString(days)); // [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]

        // The name must match exactly the identifier used to declare the enum constant in this type.
        System.out.println(Day.valueOf("MONDAY")); // MONDAY

        System.out.println(Day.MONDAY.getAbbreviation()); // Mon.
        System.out.println(Day.MONDAY.ordinal()); // 0
        System.out.println(Day.MONDAY.getNum());  // 1
    }

    public String tellItLikeItIs() {
        return switch (day) {
            case MONDAY -> "Mondays are bad.";
            case FRIDAY -> "Fridays are better.";
            case SATURDAY, SUNDAY -> "Weekends are best.";
            default -> "Midweek days are so-so";
        };
    }

}
