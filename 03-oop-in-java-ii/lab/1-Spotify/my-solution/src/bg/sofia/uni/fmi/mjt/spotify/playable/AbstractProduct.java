package bg.sofia.uni.fmi.mjt.spotify.playable;

import java.util.Objects;

public abstract class AbstractProduct implements Playable {
    private final String title;
    private final String artist;
    private final int year;
    private final double duration;
    private int cntPlayed;

    public AbstractProduct(String title, String artist, int year, double duration) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = duration;
    }

    // "Currently playing <type> content: <content_title>"

    public abstract String play();

    public void incrementPlay() {
        ++this.cntPlayed;
    }

    public int getTotalPlays() {
        return this.cntPlayed;
    }

    ;

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public int getYear() {
        return this.year;
    }

    public double getDuration() {
        return this.duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractProduct au)) {
            return false;
        }
        return (this.title.equals(au.getTitle()) &&
                this.artist.equals(au.getArtist()) &&
                this.year == au.getYear() &&
                this.duration == au.getDuration());
    }

    @Override
    public int hashCode() {
//        int result = 17;
//        final int HASH_NUMBER = 31;
//        result = HASH_NUMBER * result + title.hashCode();
//        result = HASH_NUMBER * result + year;
//        result = HASH_NUMBER * result + artist.hashCode();
//        return result;

        return Objects.hash(title, year, artist, duration);
    }

    public void display() {
        System.out.println(" \"" + getTitle() + "\" by " + getArtist() +
                " from " + getYear() + " with duration " + getDuration());
    }

}
