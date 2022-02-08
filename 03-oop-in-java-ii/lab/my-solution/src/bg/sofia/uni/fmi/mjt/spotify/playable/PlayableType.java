package bg.sofia.uni.fmi.mjt.spotify.playable;

public enum PlayableType {
    AUDIO("A"),
    VIDEO("V");

    private final String abbreviation;

    PlayableType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }
}
