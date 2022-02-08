package bg.sofia.uni.fmi.mjt.spotify.account;

public enum AccountType {
    FREE("F"),
    PREMIUM("P");

    private String abbreviation;

    AccountType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }
}
