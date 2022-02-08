package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.comparators.GiftComparator;
import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class DefaultPerson<I> implements Person<I> {

    private final I id;

    // From which person -> gift received: personGift
    Map<Person<?>, List<Gift<?>>> personGift;

    // All gifts received ordered by price in descending order
    Set<Gift<?>> giftsReceived;

    {
        personGift = new HashMap<>();
        giftsReceived = new TreeSet<>(new GiftComparator().reversed());
    }

    public DefaultPerson(I id) {
        this.id = id;
    }

    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("~Invalid gifts count.");
        }

        List<Gift<?>> nMostExpensiveGifts = new ArrayList<>();

        if (n == 0) {
            return List.copyOf(nMostExpensiveGifts);
        }

        final int size = Math.min(n, giftsReceived.size());

        Iterator<Gift<?>> iterator = giftsReceived.iterator();
        for (int i = 0; i < size && iterator.hasNext(); ++i) {
            nMostExpensiveGifts.add(iterator.next());
        }

        return List.copyOf(nMostExpensiveGifts);
    }

    @Override
    public I getId() {
        return id;
    }

    @Override
    public void receiveGift(Gift<?> gift) {
        if (gift == null) {
            throw new IllegalArgumentException("~Invalid gift received.");
        }

        if (!id.equals(gift.getReceiver().getId())) {
            throw new WrongReceiverException("~Wrong receiver.");
        }

        if (!personGift.containsKey(gift.getSender())) {
            personGift.put(gift.getSender(), new ArrayList<>());
        }

        personGift.get(gift.getSender()).add(gift);

        giftsReceived.add(gift);
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        if (person == null) {
            throw new IllegalArgumentException("~Invalid person.");
        }

        ArrayList<Gift<?>> toReturn = new ArrayList<>();

        if (!personGift.containsKey(person)) {
            return List.copyOf(toReturn);
        }

        return List.copyOf(personGift.get(person));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof DefaultPerson<?> otherPerson) {
            return id.equals(otherPerson);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
