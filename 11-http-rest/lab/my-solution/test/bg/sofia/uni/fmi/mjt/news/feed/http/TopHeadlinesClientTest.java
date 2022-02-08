package bg.sofia.uni.fmi.mjt.news.feed.http;

import bg.sofia.uni.fmi.mjt.news.feed.categories.NewsCategory;
import bg.sofia.uni.fmi.mjt.news.feed.country.codes.CountryCode;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.BadGetawayException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.news.feed.exceptions.InternalServerErrorException;
import bg.sofia.uni.fmi.mjt.news.feed.response.ArticleObj;
import bg.sofia.uni.fmi.mjt.news.feed.response.ResponseObj;
import bg.sofia.uni.fmi.mjt.news.feed.response.Source;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TopHeadlinesClientTest {

    private static final Gson gson = new Gson();

    private static final Source source1 = new Source("the-washington-post", "The Washington Post");
    private static final Source source2 = new Source("reuters", "Reuters");

    private static final ArticleObj article1 = new ArticleObj(
            source1,
            "Lena H. Sun, Joel Anderson, Dan Keating",
            "Tom Cruise's 'Mission Impossible '7' have been delayed until 2024",
            "Paramount Pictures said the seventh installments in the MI franchise would be delayed",
            "https://www.cnbc.com/2022/01/21/tom-cruises-mission-impossible-7-and-8-have-been-delayed.html",
            "https://image.cnbcfm.com/api/v1/image/106561656-1591103885678gettyimages-904768640.jpeg?v=1591103906",
            "2022-01-21T20:22:02Z",
            "The Tom Cruise led \"Mission Impossible 7\" is moving on the calendar once again."
    );

    private static final ArticleObj article2 = new ArticleObj(
            source2,
            "Vivian Yee",
            "Atlas V rocket launches 2 surveillance satellites for US Space Force",
            "The twin spacecraft will help keep tabs on the traffic in geosynchronous orbit.",
            "https://www.space.com/atlas-v-space-force-satellite-launch-january-2022",
            "https://cdn.mos.cms.futurecdn.net/SyzDFu6BipWV8FDzG4n4iE-1200-80.jpeg",
            "2022-01-21T19:53:25Z",
            "A powerful United Launch Alliance (ULA) Atlas V rocket launched"
    );

    private static final String testApiKey = "a7f847b2d41c4d4ca9d7940e4136b1d1";

    private static String requestOK;
    private static String requestERR;

    private static TopHeadlinesRequest request;
    private static TopHeadlinesClient client;

    private static final HttpClient httpClientMock = mock(HttpClient.class);
    private static final HttpResponse httpResponseMock = mock(HttpResponse.class);

    @BeforeAll
    public static void setUp() throws IOException, InterruptedException {
        List<ArticleObj> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);

        ResponseObj responseOK = new ResponseObj(
                "ok",
                null,
                null,
                articles.size(),
                articles
        );

        ResponseObj responseERR = new ResponseObj(
                "error",
                "parameterMissing",
                "Required parameters are missing.",
                0,
                null
        );

        requestOK = gson.toJson(responseOK);
        requestERR = gson.toJson(responseERR);

        request = new TopHeadlinesRequest
                .Builder(testApiKey, "the")
                .build();

        client = new TopHeadlinesClient(httpClientMock);

        lenient().when(
                httpClientMock.send(Mockito.any(HttpRequest.class),
                        ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())
        ).thenReturn(httpResponseMock);
    }

    @Test
    public void testSendRequestBadRequest() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        when(httpResponseMock.body()).thenReturn(requestERR);

        assertThrows(BadRequestException.class, () -> client.sendRequest(request),
                "Must throw BadRequestException");
    }

    @Test
    public void testSendRequestOkRequestArticlesCount() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(requestOK);

        ResponseObj responseObj = client.sendRequest(request);

        assertEquals(2, responseObj.getArticles().size(), "Must have two articles");
    }

    @Test
    public void testSendRequestOkRequestArticles() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(requestOK);

        ResponseObj responseObj = client.sendRequest(request);

        assertEquals(gson.toJson(article1), gson.toJson(responseObj.getArticles().get(0)),
                "Article not correctly stored");
        assertEquals(gson.toJson(article2), gson.toJson(responseObj.getArticles().get(1)),
                "Article not correctly stored");
    }

    @Test
    public void testSendRequestOkRequestTotalResults() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(requestOK);

        ResponseObj responseObj = client.sendRequest(request);

        assertEquals(2, responseObj.getTotalResults(), "Must have 2 articles");
    }

    @Test
    public void testSendRequestUnauthorized() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        when(httpResponseMock.body()).thenReturn(requestERR);

        assertThrows(UnauthorizedException.class, () -> client.sendRequest(request),
                "Must throw UnauthorizedException when status code 401 returned");
    }

    @Test
    public void testSendRequestTooManyRequests() {
        when(httpResponseMock.statusCode()).thenReturn(429);
        when(httpResponseMock.body()).thenReturn(requestERR);

        assertThrows(TooManyRequestsException.class, () -> client.sendRequest(request),
                "Must throw TooManyRequestsException when status code 429 returned");
    }

    @Test
    public void testSendRequestInternalServerError() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
        when(httpResponseMock.body()).thenReturn(requestERR);

        assertThrows(InternalServerErrorException.class, () -> client.sendRequest(request),
                "Must throw TooManyRequestsException when status code 500 returned");
    }

    @Test
    public void testSendRequestBadGetaway() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_GATEWAY);
        when(httpResponseMock.body()).thenReturn(requestERR);

        assertThrows(BadGetawayException.class, () -> client.sendRequest(request),
                "Must throw TooManyRequestsException when status code 501 returned");
    }

    // End-to-end test. Do not judge.
    @Test
    public void notAUnitTest() throws IOException, InterruptedException {
        final String MY_API_KEY = "a7f847b2d41c4d4ca9d7940e4136b1d1";

        TopHeadlinesRequest request = TopHeadlinesService
                .getInstance()
                .makeFullRequest(
                        MY_API_KEY,
                        NewsCategory.SPORTS,
                        CountryCode.GB,
                        3,
                        2,
                        "united"
                );

        HttpClient httpClient = HttpClient.newBuilder().build();

        TopHeadlinesClient client = new TopHeadlinesClient(httpClient);

        client.sendRequest(request);

//        System.out.println(gson.toJson(client.getArticles()));
//        System.out.printf("Results per page: %d%n", client.getArticles().size());
//        System.out.printf("Total results: %d%n", client.getTotalResults());
    }
}
