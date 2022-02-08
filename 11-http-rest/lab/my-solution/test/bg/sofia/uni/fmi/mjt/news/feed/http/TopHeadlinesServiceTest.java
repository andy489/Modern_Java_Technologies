package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.InvalidApiKeyException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NoKeyWordException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullApiKeyException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopHeadlinesServiceTest {

    private final String MY_API_KEY = "a7f847b2d41c4d4ca9d7940e4136b1d1";

    private final String KEYWORD1 = "the";
    private final String KEYWORD2 = "because";

    private final int ARTICLES_PER_SIZE_RESTRICTION = 50;
    private final int INITIAL_PAGE = 0; // same as 1 [0 is equivalent to 1 for page in the api]

    private final TopHeadlinesService service = TopHeadlinesService.getInstance();

    @Test
    public void testTopHeadlinesServiceSingleton() {
        assertSame(service, TopHeadlinesService.getInstance());
    }

    @Test
    public void testMakeRequestWithNullApiKey() {
        assertThrows(
                NullApiKeyException.class,
                () -> service.makeBaseRequest(null, KEYWORD1),
                "apyKey must not be null");
    }

    @Test
    public void testMakeRequestWithEmptyApiKey() {
        assertThrows(
                InvalidApiKeyException.class,
                () -> service.makeBaseRequest("", KEYWORD1),
                "apyKey must not be empty");
    }

    @Test
    public void testMakeRequestWithBlankApiKey() {
        assertThrows(
                InvalidApiKeyException.class,
                () -> service.makeBaseRequest(" ", KEYWORD1),
                "apyKey must not be blank");
    }

    @Test
    public void testMakeRequestWithEmptyKeyWords() {
        assertThrows(
                NoKeyWordException.class,
                () -> service.makeBaseRequest(MY_API_KEY, new String[]{}),
                "At least one non-empty and non-blank keyword is needed");
    }

    @Test
    public void testMakeRequestWithEmptyKeyWord() {
        assertThrows(
                NoKeyWordException.class,
                () -> service.makeBaseRequest(MY_API_KEY, ""),
                "At least one non-empty and non-blank keyword is needed");
    }

    @Test
    public void testMakeRequestWithBlankKeyWord() {
        assertThrows(
                NoKeyWordException.class,
                () -> service.makeBaseRequest(MY_API_KEY, " "),
                "At least one non-empty and non-blank keyword is needed");
    }

    @Test
    public void testMakeRequestApiKeyAndOneKeyWord() {
        TopHeadlinesRequest request = service.makeBaseRequest(MY_API_KEY, KEYWORD1);

        assertEquals(MY_API_KEY, request.getApiKey(),
                "MY_API_KEY not stored correctly when BaseRequest");
        assertEquals(List.of(KEYWORD1), request.getKeyWords(),
                "KEYWORD1 not stored correctly when BaseRequest");
        assertEquals(ARTICLES_PER_SIZE_RESTRICTION, request.getPageSize(),
                "ARTICLES_PER_SIZE_RESTRICTION not stored correctly when BaseRequest");
        assertEquals(request.getPage(), INITIAL_PAGE,
                "INITIAL_PAGE not stored correctly when BaseRequest");
    }

    @Test
    public void testMakeRequestApiKeyAndTwoKeyWords() {
        TopHeadlinesRequest request = service.makeBaseRequest(MY_API_KEY, KEYWORD1, KEYWORD2);

        assertEquals(MY_API_KEY, request.getApiKey(),
                "MY_API_KEY not stored correctly when TwoKeyWordsRequest");
        assertEquals(List.of(KEYWORD1, KEYWORD2), request.getKeyWords(),
                "KEYWORD1 not stored correctly when TwoKeyWordsRequest");
        assertEquals(ARTICLES_PER_SIZE_RESTRICTION, request.getPageSize(),
                "ARTICLES_PER_SIZE_RESTRICTION not stored correctly when TwoKeyWordsRequest");
        assertEquals(INITIAL_PAGE, request.getPage(),
                "INITIAL_PAGE not stored correctly when TwoKeyWordsRequest");
    }

    @Test
    public void testMakeRequestCountryCode() {
        TopHeadlinesRequest request = service.makeRequestWithCountry(MY_API_KEY, CountryCode.IN, KEYWORD2);

        assertEquals(request.getCountryCode(), CountryCode.IN,
                "CountryCode not stored correctly when RequestWithCountry");
    }

    @Test
    public void testMakeRequestNewsCategory() {
        TopHeadlinesRequest request = service.makeRequestWithCategory(MY_API_KEY, NewsCategory.HEALTH, KEYWORD2);

        assertEquals(request.getCategory(), NewsCategory.HEALTH,
                "NewsCategory not stored correctly when RequestWithCategory");
    }

    @Test
    public void testMakeRequestCountryCodeNewsCategory() {
        TopHeadlinesRequest request = service.makeRequestCountryAndCategory(MY_API_KEY, CountryCode.AR, NewsCategory.HEALTH, KEYWORD1);

        assertEquals(request.getCountryCode(), CountryCode.AR,
                "CountryCode not stored correctly when RequestCountryAndCategory");
        assertEquals(request.getCategory(), NewsCategory.HEALTH,
                "NewsCategory not stored correctly when RequestCountryAndCategory");
    }

    @Test
    public void testMakeRequestFullStackArgs() {
        TopHeadlinesRequest request = service.makeFullRequest(
                MY_API_KEY,
                NewsCategory.HEALTH,
                CountryCode.NG,
                2,
                2,
                KEYWORD1, KEYWORD2
        );

        assertEquals(MY_API_KEY, request.getApiKey(),
                "MY_API_KEY error when RequestFullStackArgs");
        assertEquals(NewsCategory.HEALTH, request.getCategory(),
                "NewsCategory error when RequestFullStackArgs");
        assertEquals(CountryCode.NG, request.getCountryCode(),
                "CountryCode error when RequestFullStackArgs");
        assertEquals(2, request.getPageSize(),
                "pageSize error when RequestFullStackArgs");
        assertEquals(2, request.getPage(),
                "page error when RequestFullStackArgs");
        assertEquals(List.of(KEYWORD1, KEYWORD2), request.getKeyWords(),
                "Keywords error when RequestFullStackArgs");
    }

    @Test
    public void testMakeRequestURL() {
        TopHeadlinesRequest request = service.makeBaseRequest(
                MY_API_KEY,
                KEYWORD1
        );

        assertTrue(request.toString().startsWith("https://newsapi.org/v2/top-headlines?"),
                "URL error when BaseRequest (start string)");
        assertTrue(request.toString().endsWith("q=the&pageSize=50&page=0&apiKey=a7f847b2d41c4d4ca9d7940e4136b1d1"),
                "URL error when BaseRequest (end string)");
    }
}
