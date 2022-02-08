package bg.sofia.uni.fmi.mjt.gifts.gift;

public class Item implements Priceable {

    private final double price;

    public Item(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
