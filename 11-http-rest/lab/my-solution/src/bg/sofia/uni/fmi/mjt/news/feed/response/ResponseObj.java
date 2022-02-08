package bg.sofia.uni.fmi.mjt.news.feed.response;

import java.util.List;

public class ResponseObj {
    private String status;
    private String code;
    private String message;
    private int totalResults;
    private List<ArticleObj> articles;

    public ResponseObj(
            String status,
            String code,
            String message,
            int totalResults,
            List<ArticleObj> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
        this.code = code;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<ArticleObj> getArticles() {
        return articles;
    }
}
