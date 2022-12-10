package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    @Test
    void testFoodEntryWithInvalidFoodName() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("", 10.0, new NutritionInfo(33.33, 33.34, 33.33)), "Food name cannot be empty");
    }

    @Test
    void testFoodEntryWithNullNutrition() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("Pasta", 10.0, null), "Food name cannot be empty");
    }

    @Test
    void testFoodEntryWithNegativeServingSize() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("Pasta", -1.0, new NutritionInfo(33.33, 33.34, 33.33)), "Serving size cannot be 0 or negative");
    }
}
