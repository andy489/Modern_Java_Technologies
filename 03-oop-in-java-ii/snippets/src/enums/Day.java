package enums;

// Enums extend the java.lang.Enum class implicitly
// Therefore, you cannot extend any other class in enum.
public enum Day {
    MONDAY("Mon.", 1),
    TUESDAY("Tue.", 2),
    WEDNESDAY("Wed.", 3),
    THURSDAY("Thu.", 4),
    FRIDAY("Fri.", 5),
    SATURDAY("Sat.", 6),
    SUNDAY("Sun.", 7);

    private final String abbreviation;
    private final int num;

    // Constructor is always private or default
    // You cannot create an instance of enum using the new operator
    Day(String abbreviation, int num) {
        this.abbreviation = abbreviation;
        this.num = num;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getNum() {
        return num;
    }
}
