package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

import java.util.Objects;

public class EscapeRoom implements Ratable {
    private final String name;
    private final Theme theme;
    private final Difficulty difficulty;
    private final int maxTimeToEscape;
    private final double priceToPlay;
    private final int maxReviewsCount;

    private final Review[] reviews;
    private int currentReviewIndex;
    private int ratingsCount;
    private int ratingsSum;

    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount) {
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;

        currentReviewIndex = 0;
        ratingsCount = 0;
        ratingsSum = 0;

        reviews = new Review[maxReviewsCount];
    }

    @Override
    public double getRating() {
        if (ratingsCount > 0) {
            return ratingsSum * 1.0 / ratingsCount;
        }

        return 0.0;
    }

    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
        int actualReviewsCount = Math.min(ratingsCount, maxReviewsCount);
        Review[] result = new Review[actualReviewsCount];

        int firstReview = (ratingsCount < maxReviewsCount) ? 0 : currentReviewIndex;

        for (int i = 0; i < actualReviewsCount; i++) {
            result[i] = reviews[(firstReview + i) % maxReviewsCount];
        }

        return result;
    }

    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        reviews[currentReviewIndex++] = review;

        if (currentReviewIndex >= maxReviewsCount) {
            currentReviewIndex = 0;
        }

        ratingsSum += review.rating();
        ratingsCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EscapeRoom escapeRoom = (EscapeRoom) o;

        return (escapeRoom.getName().equals(name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("{Name: %s, theme: %s, diff: %s, escape time: %d, price: %.2f}",
                name, theme, difficulty, maxTimeToEscape, priceToPlay);
    }
}
