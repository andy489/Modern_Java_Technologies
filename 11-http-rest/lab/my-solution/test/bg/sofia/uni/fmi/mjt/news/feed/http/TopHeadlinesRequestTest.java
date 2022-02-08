package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.InvalidApiKeyException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NoKeyWordException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullApiKeyException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.NullKeyWordsException;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TopHeadlinesRequestTest {
    private static final String testApiKey = "a7f847b2d41c4d4ca9d7940e4136b1d1";

    private static final String testKeyword1 ="premiere";
    private static final String testKeyword2 = "title";
    private static final String testKeyword3 = "year";

    @Test
    public void testTopHeadlinesRequestNullKeyPhrase() {
        assertThrows(NullKeyWordsException.class, () -> new TopHeadlinesRequest
                .Builder(testApiKey, (String[]) null)
                .build(), "NullKeyWordsException must be thrown when null keyPhrase is provided");
    }

    @Test
    public void testTopHeadlinesRequestEmptyKeyPhrase() {
        assertThrows(NoKeyWordException.class, () -> new TopHeadlinesRequest
                .Builder(testApiKey, "")
                .build(), "NoKeyWordException must be thrown when empty keyPhrase is provided");
    }

    @Test
    public void testTopHeadlinesRequestBlankKeyPhrase() {
        assertThrows(NoKeyWordException.class, () -> new TopHeadlinesRequest
                .Builder(testApiKey, " ")
                .build(), "NoKeyWordException must be thrown when blank keyPhrase is provided");
    }

    @Test
    public void testTopHeadlinesRequestNullApiKey() {
        assertThrows(NullApiKeyException.class, () -> new TopHeadlinesRequest
                .Builder(null, testKeyword1)
                .build(), "NullApiKeyException must be thrown when null apiKey is provided");
    }

    @Test
    public void testTopHeadlinesRequestEmptyApiKey() {
        assertThrows(InvalidApiKeyException.class, () -> new TopHeadlinesRequest
                .Builder("", testKeyword1)
                .build(), "InvalidApiKeyException must be thrown when empty apiKey is provided");
    }

    @Test
    public void testTopHeadlinesRequestBlankApiKey() {
        assertThrows(InvalidApiKeyException.class, () -> new TopHeadlinesRequest
                .Builder(" ", testKeyword1)
                .build(), "InvalidApiKeyException must be thrown when blank apiKey is provided");
    }

    @Test
    public void testTopHeadlinesRequestApiKey() {
        assertEquals(testApiKey, new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .build().getApiKey(), "ApiKey not stored correctly");
    }

    @Test
    public void testTopHeadlinesRequestOneKeyWord() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .build();

        assertEquals(request.getKeyWords(), List.of("premiere"),
                "Request must have only one keyWord");
    }

    @Test
    public void testTopHeadlinesRequestTwoKeyWords() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1, testKeyword2)
                .build();

        assertEquals(request.getKeyWords(), List.of("premiere", "title"),
                "Request must have exactly two keyWords");
    }

    @Test
    public void testTopHeadlinesRequestThreeKeyWords() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1, testKeyword2, testKeyword3)
                .build();

        assertEquals(request.getKeyWords(), List.of("premiere", "title", "year"),
                "Request must have exactly three keyWords");
    }

    @Test
    public void testTopHeadlinesRequestInCategory() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .inCategory(NewsCategory.ENTERTAINMENT)
                .build();

        assertSame(request.getCategory(), NewsCategory.ENTERTAINMENT,
                String.format("Request must be of %s category", NewsCategory.ENTERTAINMENT));
    }

    @Test
    public void testTopHeadlinesRequestInCountry() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .inCountry(CountryCode.US)
                .build();

        assertSame(request.getCountryCode(), CountryCode.US,
                String.format("Request must be of %s category", CountryCode.US));
    }

    @Test
    public void testTopHeadlinesRequestLimitTitlesPerPage() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .limitPage(2)
                .build();

        assertEquals(request.getPageSize(), 2,
                "Request must be limited to 2 articles per page");
    }

    @Test
    public void testTopHeadlinesRequestLimitTitlesPerPageInvalidLimit() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .limitPage(-2)
                .build();

        assertSame(request.getPageSize(), 50,
                "Request must be limited to 50 (max) articles per page");
    }

    @Test
    public void testTopHeadlinesRequestLimitTitlesPerPageExceedingLimit() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .limitPage(100)
                .build();

        assertSame(request.getPageSize(), 50,
                "Request must be limited to 50 (max) articles per page");
    }

    @Test
    public void testTopHeadlinesRequestGoToPage() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .limitPage(1)
                .goToPage(2)
                .build();

        assertEquals(request.getPage(), 2,
                "Request must be on page page 2");
    }

    @Test
    public void testTopHeadlinesRequestAllCustomParametersBuilder() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .inCountry(CountryCode.US)
                .inCategory(NewsCategory.ENTERTAINMENT)
                .limitPage(2)
                .goToPage(2)
                .build();

        assertSame(request.getCountryCode(), CountryCode.US,
                String.format("Request must from %s category", CountryCode.DE));
        assertSame(request.getCategory(), NewsCategory.ENTERTAINMENT,
                String.format("Request must from %s category", NewsCategory.ENTERTAINMENT));
        assertEquals(request.getPageSize(), 2,
                "Request must be limited to 2 articles per page");
        assertEquals(request.getPage(), 2,
                "Request must be on page 2");
    }

    @Test
    public void testTopHeadlinesRequestURLString() {
        var request = new TopHeadlinesRequest
                .Builder(testApiKey, testKeyword1)
                .inCountry(CountryCode.US)
                .inCategory(NewsCategory.ENTERTAINMENT)
                .limitPage(2)
                .goToPage(2)
                .build();

        String actualURL = request.toString();
        String expectedURL = "https://newsapi.org/v2/top-headlines?" +
                "q=" +
                testKeyword1 +
                "&country=" +
                CountryCode.US +
                "&category=" +
                NewsCategory.ENTERTAINMENT +
                "&pageSize=2&page=2&apiKey=" +
                testApiKey;

        assertEquals(expectedURL, actualURL, "Incorrect URL build");
    }
}
