package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Baverage;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Food;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.StorableByExpirationComparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SmartFridge implements SmartFridgeAPI {
    private final int totalCapacity;
    private int currentCapacity;
    private final Map<String, Queue<Storable>> storage;

    public SmartFridge(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        this.currentCapacity = 0;
        this.storage = new HashMap<>();
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        if (item == null || quantity < 0) {
            throw new IllegalArgumentException("Food cannot be null and quantity must be positive");
        }

        if (currentCapacity + quantity > totalCapacity) {
            throw new FridgeCapacityExceededException("Capacity exceeded");
        }

        storage.putIfAbsent(item.getName(), new PriorityQueue<>(new StorableByExpirationComparator()));

        for (int i = 0; i < quantity; i++) {
            storage.get(item.getName()).add(item);
        }

        currentCapacity += quantity;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("Item cannot be null, empty or blank");
        }

        if (!storage.containsKey(itemName)) {
            return new LinkedList<>();
        }

        List<Storable> retrieved = new LinkedList<>(storage.get(itemName));
        currentCapacity -= retrieved.size();
        storage.remove(itemName);
        return retrieved;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("Item cannot be null, empty or blank");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity of item must be positive");
        }

        if (!storage.containsKey(itemName) || storage.get(itemName).size() < quantity) {
            throw new InsufficientQuantityException("Invalid quantity of the specified food");
        }

        List<Storable> retrieved = new LinkedList<>();
        Queue<Storable> foods = storage.get(itemName);

        for (int i = 0; i < quantity; i++) {
            retrieved.add(foods.poll());
        }

        currentCapacity -= quantity;
        return retrieved;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("Item cannot be null, empty or blank");
        }

        Queue<Storable> items = storage.get(itemName);
        return items == null ? 0 : items.size();
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        Set<Ingredient<?>> ingredients = recipe.getIngredients();

        List<Ingredient<?>> missingIngredients = new LinkedList<>();

        for (Ingredient<?> ingredient : ingredients) {
            Queue<Storable> foods = storage.get(ingredient.item().getName());

            if (foods == null) {
                missingIngredients.add(ingredient);
            } else {
                int availableQuantity = 0;

                for (Storable food : foods) {
                    if (!food.isExpired()) {
                        availableQuantity++;
                    }
                }

                if (availableQuantity < ingredient.quantity()) {
                    missingIngredients
                            .add(new DefaultIngredient<>(ingredient.item(), ingredient.quantity() - availableQuantity));
                }
            }
        }

        return missingIngredients.iterator();
    }

    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> expired = new ArrayList<>();

        for (Queue<Storable> s : storage.values()) {
            while (s != null && !s.isEmpty() && s.peek().isExpired()) {
                expired.add(s.poll());
                currentCapacity--;
            }
        }

        return expired;
    }

    public static void main(String... args) throws FridgeCapacityExceededException {
        Storable flour = new Food("Flour", LocalDate.now().plusMonths(8));
        Storable bakingPowder = new Food("Baking powder", LocalDate.now().plusMonths(3));
        Storable whiteSugar = new Food("White sugar", LocalDate.now().plusMonths(4));
        Storable salt = new Food("Salt", LocalDate.now().plusYears(1));
        Storable milk = new Baverage("Milk", LocalDate.now().plusWeeks(1));
        Storable butter = new Food("Butter", LocalDate.now().plusWeeks(2));
        Storable egg = new Food("Egg", LocalDate.now().plusDays(6));

        Ingredient<Storable> i1 = new DefaultIngredient<>(flour, 2);
        Ingredient<Storable> i2 = new DefaultIngredient<>(bakingPowder, 3);
        Ingredient<Storable> i3 = new DefaultIngredient<>(whiteSugar, 1);
        Ingredient<Storable> i4 = new DefaultIngredient<>(salt, 1);
        Ingredient<Storable> i5 = new DefaultIngredient<>(milk, 1);
        Ingredient<Storable> i6 = new DefaultIngredient<>(butter, 3);
        Ingredient<Storable> i7 = new DefaultIngredient<>(egg, 2);

        SmartFridge iFridge = new SmartFridge(13);

        System.out.println(iFridge.getQuantityOfItem("Flour"));
        iFridge.store(flour, 2);
        System.out.println(iFridge.getQuantityOfItem("Flour"));
        iFridge.store(bakingPowder, 3);
        iFridge.store(whiteSugar, 1);
        iFridge.store(salt, 1);
        iFridge.store(milk, 1);
        iFridge.store(butter, 3);
        iFridge.store(egg, 2);
    }
}
