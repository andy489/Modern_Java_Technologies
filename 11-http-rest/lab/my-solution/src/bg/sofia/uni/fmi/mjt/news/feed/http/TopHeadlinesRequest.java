package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.InvalidApiKeyException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NoKeyWordException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullKeyWordsException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullApiKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class TopHeadlinesRequest {
    private static final int NEWS_PER_PAGE_RESTRICTION = 50;
    private static final int INITIAL_PAGE = 0; // same as 1
    private static final int TOTAL_PAGES_RESTRICTION = 3;

    private static final String KEY_WORD = "q=";
    private static final String KEY_WORD_SEPARATOR = "+";
    private static final String ADD_ARG = "&";
    private static final String COUNTRY = "country=";
    private static final String CATEGORY = "category=";
    private static final String PAGE_SIZE = "pageSize=";
    private static final String PAGE = "page=";
    private static final String API_KEY = "apiKey=";
    private static final String URL = "https://newsapi.org/v2/top-headlines?";

    private final String apiKey;
    private final List<String> keyWords;

    private final CountryCode countryCode;
    private final NewsCategory newsCategory;

    private final int pageSize;
    private final int page;

    private TopHeadlinesRequest(Builder builder) {
        this.apiKey = builder.apiKey;
        this.keyWords = builder.keyWords;

        this.countryCode = builder.countryCode;
        this.newsCategory = builder.newsCategory;

        this.pageSize = builder.pageSize;
        this.page = builder.page;
    }

    public String getApiKey() {
        return apiKey;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public NewsCategory getCategory() {
        return newsCategory;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        var url = new StringBuilder(URL);

        if (!keyWords.isEmpty()) {
            url
                    .append(KEY_WORD)
                    .append(String.join(KEY_WORD_SEPARATOR, keyWords))
                    .append(ADD_ARG);
        }

        if (countryCode != null) {
            url
                    .append(COUNTRY)
                    .append(countryCode)
                    .append(ADD_ARG);
        }

        if (newsCategory != null) {
            url
                    .append(CATEGORY)
                    .append(newsCategory)
                    .append(ADD_ARG);

        }

        url
                .append(PAGE_SIZE)
                .append(pageSize)
                .append(ADD_ARG)
                .append(PAGE)
                .append(page)
                .append(ADD_ARG);

        url.append(API_KEY)
                .append(apiKey);

        return url.toString();
    }

    public static class Builder {
        private final String apiKey;
        private final List<String> keyWords;

        private CountryCode countryCode;
        private NewsCategory newsCategory;

        private int pageSize = NEWS_PER_PAGE_RESTRICTION;
        private int page = INITIAL_PAGE;

        {
            keyWords = new ArrayList<>();
        }

        public Builder(String apiKey, String... keyWords) {
            if (apiKey == null) {
                throw new NullApiKeyException("Null apiKey provided");
            }

            if (apiKey.isEmpty() || apiKey.isBlank()) {
                throw new InvalidApiKeyException("Empty or blank apiKey provided");
            }

            if (keyWords == null) {
                throw new NullKeyWordsException("Null key words");
            }

            this.apiKey = apiKey;

            Stream.of(keyWords)
                    .filter(Predicate.not(String::isBlank).and(Predicate.not(String::isBlank)))
                    .forEach(this.keyWords::add);

            if (this.keyWords.isEmpty()) {
                throw new NoKeyWordException("At least one valid keyword required");
            }
        }

        public Builder inCountry(CountryCode countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder inCategory(NewsCategory newsCategory) {
            this.newsCategory = newsCategory;
            return this;
        }

        public Builder limitPage(int pageSize) {
            if (pageSize >= 0 && pageSize <= NEWS_PER_PAGE_RESTRICTION) { // user friendly limitPage
                this.pageSize = pageSize;
            }

            return this;
        }

        public Builder goToPage(int page) {
            if (page > INITIAL_PAGE && page <= TOTAL_PAGES_RESTRICTION) { // user friendly listToPage
                this.page = page;
            }

            return this;
        }

        public TopHeadlinesRequest build() {
            return new TopHeadlinesRequest(this);
        }
    }
}
