package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultPackingService<T extends Priceable> implements PackingService<T> {
    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T item) {
        if (sender == null || receiver == null || item == null) {
            throw new IllegalArgumentException("~Invalid arguments.");
        }

        List<T> items = new ArrayList<>();
        items.add(item);

        return new BirthdayGift<>(sender, receiver, items);
    }

    @Override
    public final Gift<T> pack(Person<?> sender, Person<?> receiver, T... items) {
        if (sender == null || receiver == null || items == null) {
            throw new IllegalArgumentException("~Invalid arguments.");
        }

        for (T currentItem : items) {
            if (currentItem == null) {
                throw new IllegalArgumentException("~Cannot pack invalid item.");
            }
        }

        return new BirthdayGift<>(sender, receiver, List.of(items));
    }

    @Override
    public Collection<T> unpack(Gift<T> gift) {
        if (gift == null) {
            throw new IllegalArgumentException("~Invalid arguments.");
        }

        return gift.getItems();
    }
}
