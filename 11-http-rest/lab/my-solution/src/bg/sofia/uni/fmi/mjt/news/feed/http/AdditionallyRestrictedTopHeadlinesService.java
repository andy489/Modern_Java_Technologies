package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NoKeyWordException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullApiKeyException;

public sealed interface AdditionallyRestrictedTopHeadlinesService permits TopHeadlinesService {

    /**
     * Allows user to create Top Headlines Request only with apiKey and
     * at least one key word. Creates the request for him.
     *
     * @param apiKey   api key allowing you to access the application
     * @param keyWords arbitrary number keywords
     * @throws NullApiKeyException    if {@code apiKey} is null
     * @throws InvalidApiKeyException if {@code apiKey} is empty or blank
     * @throws NoKeyWordException     if {@code keyWords} does not have at least one non-empty and non-blank keyword
     */
    TopHeadlinesRequest makeBaseRequest(
            String apiKey,
            String... keyWords
    );

    /**
     * Allows user to create Top Headlines Request with apiKey,
     * news category and
     * at least one key word. Creates the request for him.
     *
     * @param apiKey   api key allowing you to access the application
     * @param category enum category from which the news are
     * @param keyWords arbitrary number keywords
     * @throws NullApiKeyException    if {@code apiKey} is null
     * @throws InvalidApiKeyException if {@code apiKey} is empty or blank
     * @throws NoKeyWordException     if {@code keyWords} does not have at least one non-empty and non-blank keyword
     */
    TopHeadlinesRequest makeRequestWithCategory(
            String apiKey,
            NewsCategory category,
            String... keyWords
    );

    /**
     * Allows user to create Top Headlines Request with apiKey,
     * news country code from which they come and
     * at least one key word. Creates the request for him.
     *
     * @param apiKey   api key allowing you to access the application
     * @param country  searches from news from {@code country}
     * @param keyWords arbitrary number keywords
     * @throws NullApiKeyException    if {@code apiKey} is null
     * @throws InvalidApiKeyException if {@code apiKey} is empty or blank
     * @throws NoKeyWordException     if {@code keyWords} does not have at least one non-empty and non-blank keyword
     */
    TopHeadlinesRequest makeRequestWithCountry(
            String apiKey,
            CountryCode country,
            String... keyWords);

    /**
     * Allows user to create Top Headlines Request with apiKey,
     * news country code from which they come, news category,
     * at least one key word. Creates the request for him.
     *
     * @param apiKey   api key allowing you to access the application
     * @param country  searches from news from {@code country}
     * @param category searches for news from {@code category}
     * @param keyWords arbitrary number keywords
     * @throws NullApiKeyException    if {@code apiKey} is null
     * @throws InvalidApiKeyException if {@code apiKey} is empty or blank
     * @throws NoKeyWordException     if {@code keyWords} does not have at least one non-empty and non-blank keyword
     */
    TopHeadlinesRequest makeRequestCountryAndCategory(
            String apiKey,
            CountryCode country,
            NewsCategory category,
            String... keyWords
    );

    /**
     * Allows user to create Top Headlines Request full stack of parameters.
     * apiKey, news country code from which they come, news category,
     * at least one key word, articles per page and page number. Creates the request for him.
     *
     * @param apiKey   api key allowing you to access the application
     * @param country  searches from news from {@code country}
     * @param category searches for news from {@code category}
     * @param pageSize maximum {@code pageSize} articles per page
     * @param page     articles from {@code page}
     * @param keyWords arbitrary number keywords
     * @throws NullApiKeyException    if {@code apiKey} is null
     * @throws InvalidApiKeyException if {@code apiKey} is empty or blank
     * @throws NoKeyWordException     if {@code keyWords} does not have at least one non-empty and non-blank keyword
     */
    TopHeadlinesRequest makeFullRequest(
            String apiKey,
            NewsCategory category,
            CountryCode country,
            int pageSize,
            int page,
            String... keyWords
    );
}
