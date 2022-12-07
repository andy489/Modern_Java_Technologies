package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class FreeAccount extends Account {

    private static final int ADS_TAX_COUNT = 5;
    private static final double TAX_PLAYED = 0.1;

    private final AccountType type;

    public FreeAccount(String email, Library library) {
        super(email, library);
    }

    {
        type = AccountType.FREE;
    }

    @Override
    public int getAdsListenedTo() {
        return super.getTotalListenContent() / ADS_TAX_COUNT;
    }

    @Override
    public AccountType getType() {
        return type;
    }

    @Override
    public void display() {
        System.out.println(type + " user " + getEmail() + " listened " + getAdsListenedTo() + " ads.");
    }

    @Override
    public double revenue() {
        int cntAds = this.getAdsListenedTo();
        return cntAds * TAX_PLAYED;
    }
}
