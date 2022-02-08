package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class PremiumAccount extends Account {
    static private final double PREMIUM_CHARGE = 25.0;

    private final AccountType type;

    public PremiumAccount(String email, Library library) {
        super(email, library);
    }

    {
        type = AccountType.PREMIUM;
    }

    @Override
    public int getAdsListenedTo() {
        return 0;
    }

    @Override
    public AccountType getType() {
        return type;
    }

    @Override
    public void display() {
        System.out.println(type + " user with email " + getEmail() + ".");
    }

    @Override
    public double revenue() {
        return PREMIUM_CHARGE;
    }
}
