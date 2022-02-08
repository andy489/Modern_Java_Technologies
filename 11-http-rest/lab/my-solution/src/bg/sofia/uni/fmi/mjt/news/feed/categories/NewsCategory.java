package bg.sofia.uni.fmi.mjt.news.feed.categories;

public enum NewsCategory {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    private final String originalName;

    NewsCategory(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return originalName;
    }
}
