package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Video extends AbstractProduct {
    private final PlayableType type;

    {
        this.type = PlayableType.VIDEO;
    }

    public Video(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        this.incrementPlay();
        return "Currently playing video content: " + this.getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Video vi)) {
            return false;
        }
        return (super.equals(obj) && this.type.equals(vi.type));
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
