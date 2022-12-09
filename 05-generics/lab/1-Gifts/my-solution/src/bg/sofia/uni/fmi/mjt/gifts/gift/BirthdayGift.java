package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.comparators.ItemComparator;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class BirthdayGift<T extends Priceable> implements Gift<T> {

    private final PriorityQueue<T> giftItems;
    private final Person<?> sender;
    private final Person<?> receiver;
    private double giftPrice;

    {
        giftItems = new PriorityQueue<>(new ItemComparator().reversed());
        giftPrice = 0.0;
    }

    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;

        if (items != null) {
            for (T current : items) {
                if (current != null) {
                    giftPrice += current.getPrice();
                }

                giftItems.add(current);
            }
        }
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        return giftPrice;
    }

    @Override
    public void addItem(T priceable) {
        if (priceable == null) {
            throw new IllegalArgumentException("~Invalid item.");
        }

        giftPrice += priceable.getPrice();
        giftItems.add(priceable);
    }

    @Override
    public boolean removeItem(T priceable) {
        if (priceable == null) {
            return false;
        }

        if (!giftItems.contains(priceable)) {
            return false;
        }

        giftPrice -= priceable.getPrice();

        return giftItems.remove(priceable);
    }

    @Override
    public Collection<T> getItems() {
        return List.copyOf(giftItems);
    }

    @Override
    public T getMostExpensiveItem() {
        if (giftItems.isEmpty()) {
            return null;
        }

        return giftItems.peek();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof BirthdayGift<?> otherGift) {

            if (!otherGift.receiver.equals(this.receiver) ||
                    !otherGift.sender.equals(this.sender)) {
                return false;
            }

            return this.giftItems.equals(otherGift.giftItems);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, giftItems);
    }

}
