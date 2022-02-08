package bg.sofia.uni.fmi.mjt.news.feed.country.codes;

public enum CountryCode {
    AE("ae", "United Arab Emirates"),
    AR("ar", "Argentina"),
    AT("at", "Austria"),
    AU("au", "Australia"),
    BE("be", "Belgium"),
    BG("bg", "Bulgaria"),
    BR("br", "Brazil"),
    CA("ca", "Canada"),
    CH("ch", "Switzerland"),
    CN("cn", "China"),
    CO("co", "Colombia"),
    CU("cu", "Cuba"),
    CZ("cz", "Czech Republic"),
    DE("de", "Germany"),
    EG("eg", "Egypt"),
    FR("fr", "France"),
    GB("gb", "United Kingdom"),
    GR("gr", "Greece"),
    HK("hk", "Hong Kong"),
    HU("hu", "Hungary"),
    ID("id", "Indonesia"),
    IE("ie", "Ireland"),
    IL("il", "Israel"),
    IN("in", "India"),
    IT("it", "Italy"),
    JP("jp", "Japan"),
    KR("kr", "South Korea"),
    LT("lt", "Lithuania"),
    LV("lv", "Latvia"),
    MA("ma", "Morocco"),
    MX("mx", "Mexico"),
    MY("my", "Malaysia"),
    NG("ng", "Nigeria"),
    NL("nl", "Netherlands"),
    NO("no", "Norway"),
    NZ("nz", "New Zealand"),
    PH("ph", "Philippines"),
    PL("pl", "Poland"),
    PT("pt", "Portugal"),
    RO("ro", "Romania"),
    RS("rs", "Serbia"),
    RU("ru", "Russia"),
    SA("sa", "Saudi Arabia"),
    SE("se", "Sweden"),
    SG("sg", "Singapore"),
    SI("si", "Slovenia"),
    SK("sk", "Slovakia"),
    TH("th", "Thailand"),
    TR("tr", "Turkey"),
    TW("tw", "Taiwan"),
    UA("ua", "Ukraine"),
    US("us", "United States"),
    VE("ve", "Venezuela"),
    ZA("za", "South Africa");

    private final String originalCode;
    private final String fullCountryName;

    CountryCode(String originalCode, String fullCountryName) {
        this.originalCode = originalCode;
        this.fullCountryName = fullCountryName;
    }

    @Override
    public String toString() {
        return originalCode;
    }
}
