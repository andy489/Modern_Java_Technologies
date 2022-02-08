package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;

public interface PackingService<T extends Priceable> {

    /**
     * @param sender   the sender of the item @t
     * @param receiver the receiver of the item @t
     * @param item     the item to wrap
     * @throws IllegalArgumentException if any of @sender, @receiver, @item is null
     * @return the item @t wrapped in gift from @sender to @receiver
     */
    Gift<T> pack(Person<?> sender, Person<?> receiver, T item);

    /**
     * @param sender   the sender of the items @t
     * @param receiver the receiver of the items @t
     * @param items    the items to wrap
     * @throws IllegalArgumentException if any of @sender, @receiver, any item in @items is null
     * @return the items @items wrapped in gift from @sender to @receiver
     */
    Gift<T> pack(Person<?> sender, Person<?> receiver, T... items);

    /**
     * @param gift the gift to unwrap
     * @throws IllegalArgumentException if @gift is null
     * @return an unmodifiable copy of the items in the gift @gift.
     */
    Collection<T> unpack(Gift<T> gift);

}
