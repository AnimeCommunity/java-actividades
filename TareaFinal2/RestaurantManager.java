import java.util.*;

abstract class Dish {
    protected String name;
    protected List<Ingredient> ingredients;
    protected double productionCost;
    protected double sellingPrice;

    public Dish(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
        calculateProductionCost();
        this.sellingPrice = this.productionCost + 1000;
    }

    public abstract void calculateProductionCost();

    public String getName() {
        return name;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}

class Food extends Dish {
    private boolean containsSugar;
    private boolean isHot;

    public Food(String name, List<Ingredient> ingredients, boolean containsSugar, boolean isHot) {
        super(name, ingredients);
        this.containsSugar = containsSugar;
        this.isHot = isHot;
        calculateProductionCost();
        this.sellingPrice = this.productionCost + 1000;
    }

    @Override
    public void calculateProductionCost() {
        double sum = ingredients.stream().mapToDouble(i -> i.getPrice()).sum();
        if (isHot) {
            productionCost = sum * 1.2;
        } else {
            productionCost = sum * 1.1;
        }
        if (containsSugar) {
            productionCost += 400;
        }
    }
}

class Drink extends Dish {
    private boolean isMilkshake;
    private boolean isJuice;
    private boolean isAlcoholic;

    public Drink(String name, List<Ingredient> ingredients, boolean isMilkshake, boolean isJuice, boolean isAlcoholic) {
        super(name, ingredients);
        this.isMilkshake = isMilkshake;
        this.isJuice = isJuice;
        this.isAlcoholic = isAlcoholic;
        calculateProductionCost();
        this.sellingPrice = this.productionCost + 1000;
    }

    @Override
    public void calculateProductionCost() {
        double sum = ingredients.stream().mapToDouble(i -> i.getPrice()).sum();
        if (isMilkshake) {
            productionCost = sum * 1.15;
        } else if (isJuice) {
            productionCost = sum * 1.10;
        } else {
            productionCost = sum;
        }
        if (isAlcoholic) {
            productionCost += 400;
        }
    }
}

class Dessert extends Dish {
    private boolean isHot;
    private boolean isCold;
    private int calories;

    public Dessert(String name, List<Ingredient> ingredients, boolean isHot, boolean isCold, int calories) {
        super(name, ingredients);
        this.isHot = isHot;
        this.isCold = isCold;
        this.calories = calories;
        calculateProductionCost();
        this.sellingPrice = this.productionCost + 1000;
    }

    @Override
    public void calculateProductionCost() {
        double sum = ingredients.stream().mapToDouble(i -> i.getPrice()).sum();
        if (isHot) {
            productionCost = sum * 1.20;
        } else if (isCold) {
            productionCost = sum * 1.12;
        } else {
            productionCost = sum;
        }
    }

    public int getCalories() {
        return calories;
    }
}

class Ingredient {
    private String name;
    private double price;
    private int quantity;

    public Ingredient(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    @Override
    public String toString() {
        return name + ": $" + price + " (Stock: " + quantity + ")";
    }
}

public class RestaurantManager {
    private static double money = 100000;
    private static Map<String, Ingredient> inventory = new HashMap<>();
    private static List<Dish> dishes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeIngredients();
        initializeDishes();
        menu();
    }

    private static void initializeIngredients() {
        inventory.put("Flour", new Ingredient("Flour", 300, 10));
        inventory.put("Sugar", new Ingredient("Sugar", 200, 10));
        inventory.put("Milk", new Ingredient("Milk", 250, 10));
        inventory.put("Egg", new Ingredient("Egg", 100, 10));
        inventory.put("Water", new Ingredient("Water", 50, 10));
        inventory.put("Fruit", new Ingredient("Fruit", 150, 10));
    }

    private static void initializeDishes() {
        List<Ingredient> list1 = List.of(inventory.get("Flour"), inventory.get("Egg"), inventory.get("Sugar"));
        List<Ingredient> list2 = List.of(inventory.get("Fruit"), inventory.get("Water"));

        dishes.add(new Food("Pancake", list1, true, true));
        dishes.add(new Food("Omelette", List.of(inventory.get("Egg"), inventory.get("Milk")), false, true));

        dishes.add(new Drink("Orange Juice", list2, false, true, false));
        dishes.add(new Drink("Milkshake", List.of(inventory.get("Milk"), inventory.get("Fruit")), true, false, false));

        dishes.add(new Dessert("Ice Cream", List.of(inventory.get("Milk"), inventory.get("Sugar")), false, true, 300));
        dishes.add(new Dessert("Hot Pie", List.of(inventory.get("Flour"), inventory.get("Fruit")), true, false, 500));
    }

    private static void menu() {
        while (true) {
            System.out.println("\n--- Restaurant Menu ---");
            System.out.println("a. Show current money");
            System.out.println("b. Show ingredient inventory");
            System.out.println("c. Buy ingredient");
            System.out.println("d. Show dish inventory");
            System.out.println("e. Make a dish");
            System.out.println("f. Sell a dish");
            System.out.println("g. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "a":
                    System.out.println("Current money: $" + money);
                    break;
                case "b":
                    inventory.values().forEach(System.out::println);
                    break;
                case "c":
                    buyIngredient();
                    break;
                case "d":
                    dishes.forEach(d -> System.out.println(d.getName() + ": $" + d.getSellingPrice()));
                    break;
                case "e":
                    makeDish();
                    break;
                case "f":
                    sellDish();
                    break;
                case "g":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void buyIngredient() {
        System.out.print("Enter ingredient name: ");
        String name = scanner.nextLine();
        Ingredient ing = inventory.get(name);
        if (ing != null) {
            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());
            double cost = ing.getPrice() * qty;
            if (money >= cost) {
                ing.addQuantity(qty);
                money -= cost;
                System.out.println("Bought " + qty + " of " + name);
            } else {
                System.out.println("Not enough money.");
            }
        } else {
            System.out.println("Ingredient not found.");
        }
    }

    private static void makeDish() {
        System.out.print("Enter dish name to make: ");
        String name = scanner.nextLine();
        Optional<Dish> dishOpt = dishes.stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
        if (dishOpt.isPresent()) {
            Dish dish = dishOpt.get();
            boolean canMake = true;
            for (Ingredient ing : dish.getIngredients()) {
                if (ing.getQuantity() <= 0) {
                    System.out.println("Not enough " + ing.getName());
                    canMake = false;
                }
            }
            if (canMake) {
                for (Ingredient ing : dish.getIngredients()) {
                    ing.reduceQuantity(1);
                }
                System.out.println("Made one " + dish.getName());
            }
        } else {
            System.out.println("Dish not found.");
        }
    }

    private static void sellDish() {
        System.out.print("Enter dish name to sell: ");
        String name = scanner.nextLine();
        Optional<Dish> dishOpt = dishes.stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
        if (dishOpt.isPresent()) {
            Dish dish = dishOpt.get();
            money += dish.getSellingPrice();
            System.out.println("Sold one " + dish.getName() + " for $" + dish.getSellingPrice());
        } else {
            System.out.println("Dish not found.");
        }
    }
}
