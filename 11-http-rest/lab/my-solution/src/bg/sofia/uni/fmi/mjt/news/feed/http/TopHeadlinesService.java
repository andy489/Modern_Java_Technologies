package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;

public final class TopHeadlinesService implements AdditionallyRestrictedTopHeadlinesService {
    private static final TopHeadlinesService INSTANCE = new TopHeadlinesService();

    private TopHeadlinesService() {
    }

    public static TopHeadlinesService getInstance() {
        return INSTANCE;
    }

    public TopHeadlinesRequest makeBaseRequest(
            String apiKey,
            String... keyWords
    ) {
        return buildRequest(apiKey, null, null, -1, -1, keyWords);
    }

    public TopHeadlinesRequest makeRequestWithCategory(
            String apiKey,
            NewsCategory category,
            String... keyWords
    ) {
        return buildRequest(apiKey, category, null, -1, -1, keyWords);
    }

    public TopHeadlinesRequest makeRequestWithCountry(
            String apiKey,
            CountryCode country,
            String... keyWords
    ) {
        return buildRequest(apiKey, null, country, -1, -1, keyWords);
    }

    public TopHeadlinesRequest makeRequestCountryAndCategory(
            String apiKey,
            CountryCode country,
            NewsCategory category,
            String... keyWords
    ) {
        return buildRequest(apiKey, category, country, -1, -1, keyWords);
    }

    public TopHeadlinesRequest makeFullRequest(
            String apiKey,
            NewsCategory category,
            CountryCode country,
            int pageSize,
            int page,
            String... keyWords
    ) {
        return buildRequest(apiKey, category, country, pageSize, page, keyWords);
    }

    private TopHeadlinesRequest buildRequest(
            String apiKey,
            NewsCategory category,
            CountryCode country,
            int pageSize,
            int page,
            String... keyWords) {
        return new TopHeadlinesRequest.Builder(apiKey, keyWords)
                .inCategory(category)
                .inCountry(country)
                .limitPage(pageSize)
                .goToPage(page)
                .build();
    }
}
