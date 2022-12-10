package bg.sofia.uni.fmi.mjt.exchange;

public class DummyCurrencyConverter implements CurrencyConverter {
    @Override
    public double getExchangeRate(Currency from, Currency to) throws UnknownCurrencyException {
        return 1.234;
    }
}
