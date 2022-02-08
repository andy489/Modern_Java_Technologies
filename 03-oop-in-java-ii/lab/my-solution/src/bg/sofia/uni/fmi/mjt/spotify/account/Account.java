package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public abstract class Account {

    private final String email;
    private final Library library;
    private double totalListenTime;
    private int totalListenContent;

    {
        this.totalListenTime = 0.0;
        this.totalListenContent = 0;
    }

    public Account(final String email, Library library) {
        this.email = email;
        this.library = library;
    }

    public String getEmail() {
        return this.email;
    }

    public abstract int getAdsListenedTo();

    public abstract AccountType getType();

    public void listen(Playable playable) {
        System.out.println(playable.play());
        this.totalListenTime += playable.getDuration();
        ++this.totalListenContent;
    }

    public Library getLibrary() {
        return this.library;
    }

    public double getTotalListenTime() {
        return this.totalListenTime;
    }

    public int getTotalListenContent() {
        return this.totalListenContent;
    }

    public abstract void display();

    public abstract double revenue();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account acc = (Account) obj;
        return this.email.equals(acc.getEmail());
    }

    @Override // simple hash code
    public int hashCode() { // (a = b) => (a.hash = b.hash) (<= is not always true)
        int len = email.length();
        int left = email.charAt(0);
        int right = email.charAt(len - 1);
        int middle = email.charAt((left + right) / 2);
        return left * 929 + middle * 29 * right;
    }
}
