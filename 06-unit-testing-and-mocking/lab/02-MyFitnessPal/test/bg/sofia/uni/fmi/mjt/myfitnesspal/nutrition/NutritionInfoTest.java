package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NutritionInfoTest {

    @Test
    void testNutritionInfoWithNegativeNutrient() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(10, -1, 10), "Fats cannot be negative");
    }

    @Test
    void testNutritionInfoWithNutrientsNotSummingUpTo100() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(90, 8, 1), "Nutrient not summing up to 100");
    }

    @Test
    void testCalories() {
        NutritionInfo ni = new NutritionInfo(40, 40, 20); // 40 * 4 + 40 * 9 + 20 * 4 = 600
        assertEquals(600.0, ni.calories(), 0.0, "Expected calories to be equal to 600");
    }
}
