package bg.sofia.unifmi.mjt.gifts;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Item;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.DefaultPerson;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GiftsTest {

    protected Person<?> per1, per2, per3;
    protected Gift<Priceable> gift0, gift1, gift2, gift3;
    protected Item it1, it2, it3, it4;


    @Before
    public void setup() {
        per1 = new DefaultPerson<>("100yo");
        per2 = new DefaultPerson<>(100);
        per3 = new DefaultPerson<>(3.14);

        it1 = new Item(2.50);
        it2 = new Item(3.00);
        it3 = new Item(10);
        it4 = new Item(4.50);

        gift0 = null;
        gift1 = new BirthdayGift<>(per1, per2, List.of(it1, it2));
        gift2 = new BirthdayGift<>(per1, per2, List.of(it3, it4));
        gift3 = new BirthdayGift<>(per3, per2, List.of(it1, it3));
    }

    @Test
    public void testCreatingDefaultPerson() {
        assertEquals("100yo", per1.getId());
        assertEquals(100, per2.getId());
        assertEquals(3.14, per3.getId());
    }

    @Test
    public void testGetNMostExpensiveReceivedGiftsIllegalCountOrNoGifts() {
        assertThrows(IllegalArgumentException.class, () -> per1.getNMostExpensiveReceivedGifts(-1));
        assertEquals(0, per1.getNMostExpensiveReceivedGifts(0).size());
        assertEquals(0, per1.getNMostExpensiveReceivedGifts(5).size());
    }

    @Test
    public void testReceiveGift() {
        assertThrows(IllegalArgumentException.class, () -> per2.receiveGift(gift0));
        assertThrows(WrongReceiverException.class, () -> per1.receiveGift(gift1));
    }

    @Test
    public void testGetNMostExpensiveReceivedGiftsInsertedGifts() {
        per2.receiveGift(gift1);
        per2.receiveGift(gift2);
        per2.receiveGift(gift3);
        assertFalse(per2.getNMostExpensiveReceivedGifts(1).contains(gift1));
        assertTrue(per2.getNMostExpensiveReceivedGifts(1).contains(gift2));
        assertTrue(per2.getNMostExpensiveReceivedGifts(100).contains(gift3));
        assertEquals(3, per2.getNMostExpensiveReceivedGifts(100).size());
        assertTrue(per2.getNMostExpensiveReceivedGifts(10).containsAll(List.of(gift1, gift2, gift3)));

        assertEquals(2, per2.getGiftsBy(per1).size());
        assertEquals(1, per2.getGiftsBy(per3).size());

        assertEquals(3.00, gift1.getMostExpensiveItem().getPrice(), 0.0001);
        gift1.addItem(it3);
        assertEquals(10.00, gift1.getMostExpensiveItem().getPrice(), 0.0001);
    }
}
