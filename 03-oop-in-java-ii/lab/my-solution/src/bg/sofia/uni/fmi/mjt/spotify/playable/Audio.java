package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Audio extends AbstractProduct {
    private final PlayableType type;

    {
        this.type = PlayableType.AUDIO;
    }

    public Audio(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        this.incrementPlay();
        return "Currently playing audio content: " + this.getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Audio au)) {
            return false;
        }
        return (super.equals(obj) && this.type.equals(au.type));
        // we do not check "cntPlayed" as the default equals would do!
    }

    @Override
    public int hashCode() {
        final int hashPrimeNumber = 31;
        return super.hashCode() * hashPrimeNumber + type.hashCode();
    }

    @Override
    public void display() {
        System.out.print(type);
        super.display();
    }
}
