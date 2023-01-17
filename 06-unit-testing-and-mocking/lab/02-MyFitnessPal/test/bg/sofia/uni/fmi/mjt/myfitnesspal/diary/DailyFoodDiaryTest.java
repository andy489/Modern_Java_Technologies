package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;

import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.naming.NameNotFoundException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyFoodDiaryTest {
    private static final String FOOD_NAME_1 = "Salmon";
    private static final String FOOD_NAME_2 = "Shepard's Salad";
    private static final String FOOD_NAME_3 = "Donut";

    private static final Double SERVING_SZ_1 = 150.0;
    private static final Double SERVING_SZ_2 = 250.0;
    private static final Double SERVING_SZ_3 = 70.0;

    @Mock
    private NutritionInfoAPI nutritionInfoAPI;

    @InjectMocks
    private DailyFoodDiary dailyFoodDiary;

    @Test
    void testAddFoodWithNullMeal() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(null, FOOD_NAME_1, SERVING_SZ_1), "Food with null meal should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(Meal.LUNCH, null, SERVING_SZ_1), "Food with null name should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(Meal.LUNCH, "", SERVING_SZ_1), "Food with empty name should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(Meal.LUNCH, " ", SERVING_SZ_1), "Food with blank name should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodWithZeroServingSize() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, 0), "Food with zero serving size should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodWithNegativeServingSize() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, -1.5), "Food with negative serving size should throw IllegalArgumentsException");
    }

    @Test
    void testAddFoodSuccessfulAdd() throws UnknownFoodException {
        NutritionInfo nutritionInfo = new NutritionInfo(25, 45, 30);

        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenReturn(nutritionInfo);

        FoodEntry fe = new FoodEntry(FOOD_NAME_1, SERVING_SZ_1, nutritionInfo);

        assertEquals(fe, dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, SERVING_SZ_1), String.format("Add method should return the added food entry %s", fe));
    }

    @Test
    void testGetAllFoodEntriesNoEntries() {
        assertTrue(dailyFoodDiary.getAllFoodEntries().isEmpty(), "Collection of food entries must be empty");
    }

    @Test
    void testGetAllFoodEntriesUnmodifiableCollectionCheck() {
        assertThrows(UnsupportedOperationException.class, () -> dailyFoodDiary.getAllFoodEntries().add(null), "Collection of food entries must be unmodifiable");
    }

    @Test
    void testGetAllFoodEntriesNonEmptyList() throws UnknownFoodException {
        NutritionInfo nutritionInfo1 = new NutritionInfo(25, 45, 30);
        NutritionInfo nutritionInfo2 = new NutritionInfo(75, 5, 20);

        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenReturn(nutritionInfo1);
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_2)).thenReturn(nutritionInfo2);

        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, SERVING_SZ_1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_2, SERVING_SZ_2);

        FoodEntry fe1 = new FoodEntry(FOOD_NAME_1, SERVING_SZ_1, nutritionInfo1);
        FoodEntry fe2 = new FoodEntry(FOOD_NAME_2, SERVING_SZ_2, nutritionInfo2);

        Collection<FoodEntry> result = dailyFoodDiary.getAllFoodEntries();

        assertTrue(result.containsAll(List.of(fe1, fe2)), String.format("Collection must contain %s and %s", fe1, fe2));
        assertEquals(2, result.size(), "Collection of food entries must contain only two entries");
    }

    @Test
    void testGetAllFoodEntriesByProteinContentNoFoodEntries() {
        assertTrue(dailyFoodDiary.getAllFoodEntriesByProteinContent().isEmpty(), "Expected no food entries");
    }

    @Test
    void testGetAllFoodEntriesByProteinContentAscending() throws UnknownFoodException {
        NutritionInfo nutritionInfo1 = new NutritionInfo(25, 45, 30);
        NutritionInfo nutritionInfo2 = new NutritionInfo(75, 5, 20);
        NutritionInfo nutritionInfo3 = new NutritionInfo(50, 20, 30);

        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenReturn(nutritionInfo1);
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_2)).thenReturn(nutritionInfo2);
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_3)).thenReturn(nutritionInfo3);

        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, SERVING_SZ_1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_2, SERVING_SZ_2);
        dailyFoodDiary.addFood(Meal.DINNER, FOOD_NAME_3, SERVING_SZ_3);

        FoodEntry fe1 = new FoodEntry(FOOD_NAME_1, SERVING_SZ_1, nutritionInfo1);
        FoodEntry fe2 = new FoodEntry(FOOD_NAME_2, SERVING_SZ_2, nutritionInfo2);
        FoodEntry fe3 = new FoodEntry(FOOD_NAME_3, SERVING_SZ_3, nutritionInfo3);

        List<FoodEntry> allFoodEntriesAscSorted = dailyFoodDiary.getAllFoodEntriesByProteinContent();

        assertTrue(allFoodEntriesAscSorted.containsAll(List.of(fe1, fe2, fe3)), String.format("Collection must contain %s, %s and %s", fe1, fe2, fe3));
        assertEquals(3, allFoodEntriesAscSorted.size(), "Collection of food entries must contain only three entries");

        for (int i = 0; i < 2; i++) {
            FoodEntry left = allFoodEntriesAscSorted.get(i);
            FoodEntry right = allFoodEntriesAscSorted.get(i + 1);

            double proteinsLeft = left.servingSize() * left.nutritionInfo().proteins();
            double proteinsRight = right.servingSize() * right.nutritionInfo().proteins();

            assertTrue(proteinsLeft <= proteinsRight, "FoodEntries must be sorted ascending by proteins");
        }
    }

    @Test
    void testGetDailyCaloriesIntakePerMealNullMeal() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.getDailyCaloriesIntakePerMeal(null), "Expected IllegalArgumentException to be thrown when getDailyCaloriesIntakePerMeal method called with null meal");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealMissingMeal() {
        assertEquals(0.0, dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.DINNER), 0.0, "Expected 0.0 calories for missing meal");
    }

    @Test
    void testGetDailyCaloriesIntakePerMeal() throws UnknownFoodException {
        NutritionInfo nutritionInfo1 = new NutritionInfo(25, 45, 30);
        NutritionInfo nutritionInfo2 = new NutritionInfo(75, 5, 20);

        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenReturn(nutritionInfo1);
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_2)).thenReturn(nutritionInfo2);

        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, SERVING_SZ_1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_2, SERVING_SZ_2);

        assertEquals(106250.0, dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH), 0.0, "CaloriesIntakePerMeal not calculated correctly");
    }

    @Test
    void testGetDailyCaloriesIntake() throws UnknownFoodException {
        NutritionInfo nutritionInfo1 = new NutritionInfo(25, 45, 30);
        NutritionInfo nutritionInfo2 = new NutritionInfo(75, 5, 20);

        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenReturn(nutritionInfo1);
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_2)).thenReturn(nutritionInfo2);

        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_1, SERVING_SZ_1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD_NAME_2, SERVING_SZ_2);

        assertEquals(106250.0, dailyFoodDiary.getDailyCaloriesIntake(), 0.0, "CaloriesIntake not calculated correctly");
    }

    @Test
    void testAddFoodWithMissingNutritionInfoForFood() throws UnknownFoodException {
        when(nutritionInfoAPI.getNutritionInfo(FOOD_NAME_1)).thenThrow(UnknownFoodException.class);
        assertThrows(UnknownFoodException.class, () -> dailyFoodDiary.addFood(Meal.SNACKS, FOOD_NAME_1, SERVING_SZ_1), "Expected UnknownFoodException for missing food in DB");
    }
}
