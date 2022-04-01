package creational.design.pattern.factory;

import creational.design.pattern.banknote.Banknote;
import creational.design.pattern.banknote.BanknoteType;
import creational.design.pattern.banknote.Dollar;
import creational.design.pattern.banknote.Euro;
import creational.design.pattern.banknote.Ruble;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class MoneyFactoryTest {

    @Test
    public void testDollarPrint(){
        Banknote bn = MoneyFactory.of(BanknoteType.DOLLAR);

        assertEquals(Dollar.PRINT, bn.print());
        assertTrue(bn instanceof Dollar);
    }

    @Test
    public void testEuroPrint(){
        Banknote bn = MoneyFactory.of(BanknoteType.EURO);

        assertEquals(Euro.PRINT, bn.print());
        assertTrue(bn instanceof Euro);
    }

    @Test
    public void testRublePrint(){
        Banknote bn = MoneyFactory.of(BanknoteType.RUBLE);

        assertEquals(Ruble.PRINT, bn.print());
        assertTrue(bn instanceof Ruble);
    }
}
