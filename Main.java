import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static Scanner sc;
    static Scanner input = new Scanner(System.in);
    static ArrayList<Food> cart = new ArrayList<Food>();
    static ArrayList<Food> italian = foodCategorizer("Italian");
    static ArrayList<Food> american = foodCategorizer("American");
    static HashMap<Food, Integer> cartMap = new HashMap<Food, Integer>();
    static int cartSize = 0;


    public static void main(String[] args) throws Exception {
        clearConsole(); 
        User.login(); 
        mainMenu();
        
    }


    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ArrayList<Food> foodCategorizer(String category)  {
        try {
            sc = new Scanner(new File("src/resources/foods.txt"));
            ArrayList<Food> Foods = new ArrayList<Food>();
            while(sc.hasNextLine()) {
                // change this stuff slighyly later
                if (sc.nextLine().contains(category)) {
                    String name = sc.nextLine().split("=")[1].replaceAll("^\\s+", "");
                    double price = Double.parseDouble(sc.nextLine().split("=")[1].replaceAll("^\\s+", ""));
                    String restaurant = sc.nextLine().split("=")[1].replaceAll("^\\s+", "");
                    Foods.add(new Food(name, price, restaurant));
                }
            }
            return Foods;
        }
        catch(FileNotFoundException e) {
            System.out.println("Food Database not found");
            return null;
        }
    }

    public static void displayCart() throws Exception {
        clearConsole();
        // Loop through the cart and add the foods to a hashmap. The key is the food and the value is the quantity
        if (cart.size() != cartSize) {
            for (Food food : cart) {
                if (cartMap.containsKey(food)) {
                    cartMap.put(food, cartMap.get(food) + 1);
                }
                else{
                    cartMap.put(food, 1);
                }
            }
            cartSize = cart.size();
        }
        

        if (cartMap.isEmpty()) {
            System.out.println("|Your cart is empty|");
        }
        else {
            System.out.printf("%-20s %-20s %-20s %-20s\n", "Restaurant", "Name", "Price", "Quantity");
            for (Food food : cartMap.keySet()) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", food.getRestaurant(), food.getName(), food.getPrice(), cartMap.get(food));
            }
        }

        // print total cost
        double totalCost = 0;
        for (int i = 0; i < cartMap.size(); i++) {
            totalCost += cart.get(i).getPrice() * cartMap.get(cart.get(i));
        }
        System.out.println("\nSubtotal: $" + totalCost);
        System.out.printf("(HST Included): $%.2f\n" , totalCost * 1.13);
        System.out.println("\n1. Checkout");
        System.out.println("2. Edit Cart");
        System.out.println("3. Back");
        System.out.print("Please enter a number to select an action: ");
        int choice = Integer.parseInt(input.nextLine());
        switch(choice) {
            case 1:
                checkout();
                break;
            case 2:
                editCart(cartMap);
                break;    
            case 3: 
                mainMenu();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                displayCart();
        }

    }

    public static void editCart(HashMap<Food, Integer> cartMap) throws Exception {
        clearConsole();
        
        if (cartMap.size() == 0) {
            System.out.println("|Your cart is empty|");
        }
        else {
            for (Food food : cartMap.keySet()) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", food.getRestaurant(), food.getName(), food.getPrice(), cartMap.get(food));
            }   
        }

        System.out.println("\n1. Remove item");
        System.out.println("2. Edit quantity");
        System.out.println("3. Back");
        System.out.print("Please enter a number to select action: ");
        int choice = Integer.parseInt(input.nextLine());
        switch(choice) {
            case 1:
                System.out.print("Enter the name of the item you want to remove: ");
                boolean exists = false;
                String name = input.nextLine();
                for (Food food : cartMap.keySet()) {
                    if (food.getName().toLowerCase().equals(name.toLowerCase())) {
                        exists = true;
                        cart.remove(food);
                        cartMap.remove(food);
                        break;
                    }
                }
                if (!exists) {
                    System.out.println("Item does not exist in cart.");
                    Thread.sleep(3000);
                }
                editCart(cartMap);
                break;
            
            case 2:
                System.out.print("Enter the name of the item you want to edit: ");
                String name2 = input.nextLine();
                for (Food food : cartMap.keySet()) {
                    if (food.getName().toLowerCase().equals(name2.toLowerCase())) {
                        System.out.print("Enter the new quantity: ");
                        int quantity = Integer.parseInt(input.nextLine());
                        if (quantity < 0) {
                            System.out.println("Invalid quantity. Please try again.");
                            Thread.sleep(3000);
                            editCart(cartMap);
                        }
                        else {
                            cartMap.remove(food);
                            cartMap.put(food, quantity);
                            break;
                        } 
                    }
                }
                editCart(cartMap);
                break;

            case 3:
                displayCart();
                break;
        
            default:
                System.out.println("Invalid input. Please try again.");
                editCart(cartMap);
        }
    }

    public static void checkout() throws Exception{
        clearConsole();
        System.out.println("Enter your credit card number: (Type 'back' to return to cart)");
        String cardNumber = input.nextLine();
        // char[] cardNumberArray = cardNumber.toCharArray();
        if (cardNumber.toLowerCase().equals("back")) {
            displayCart();
        }
        else if (validateCard(cardNumber) == true) {
            System.out.println("Order placed successfully!\nThank you for using PhooDelivery!\nPress 'Enter to return to main menu'");
            input.nextLine();
            mainMenu();
        }
        else {
            System.out.println("Invalid card number. Please try again.");
            Thread.sleep(3000);
            checkout();
        }  
        
    }

    public static void mainMenu() throws Exception {
        clearConsole();
        
        System.out.println("Welcome to PhooDelivery!");
        System.out.println("1. Order");
        System.out.println("2. Cart");
        System.out.println("3. Exit");
        System.out.print("Please enter a number to select a category: ");
        int choice = Integer.parseInt(input.nextLine());

        switch(choice) {
            case 1:
                order();
                break;
            case 2:
                displayCart();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                Thread.sleep(3000);
                mainMenu();
        }

        

    }

    public static void order() throws Exception{
        clearConsole();
        System.out.println("1. Italian");
        System.out.println("2. American");
        System.out.println("3. Back");
        System.out.print("Please enter a number to select a category: ");
        int choice = Integer.parseInt(input.nextLine());
        
        switch(choice) {
            case 1:
                displayFoods(italian);
                break;
            case 2:
                displayFoods(american);
                break;
            case 3:
                mainMenu();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                order();
        }


    }
    
    public static void displayFoods(ArrayList<Food> foods) throws Exception {
        clearConsole();
        System.out.printf("%-20s %-20s %-20s\n", "Restaurant", "Name", "Price");
        for (Food food : foods) {
            System.out.printf("%-20s %-20s %-20s\n", food.getRestaurant(), food.getName(), food.getPrice());
        }
        purchase(foods);
    }

    public static void purchase(ArrayList<Food> foods) throws Exception {

        boolean exists = false;
        System.out.print("\nPlease enter the name of the food you would like to purchase: ");
        String foodName = input.nextLine();
        // switch statement to check if food is in the arraylist, if so confirm with the user and add to cart
        for (Food food : foods) {
            if (food.getName().toLowerCase().equals(foodName.toLowerCase())) {
                exists = true;
                System.out.print("You have selected " + food.getName() + " from " + food.getRestaurant() + ". Would you like to add this to your cart? (y/n): ");
                String choice = input.nextLine();
                if (choice.equals("y")) {
                    cart.add(food);
                    System.out.println("Added to cart!");
                    Thread.sleep(3000);
                    order();
                }
                else if (choice.equals("n")) {
                    order();
                }
                else {
                    System.out.println("Invalid input. Please try again.");
                    Thread.sleep(3000);
                    purchase(foods);
                }
            }
        }
        if (exists == false) {
            System.out.println("Food not found. Please try again.");
            Thread.sleep(3000);
            purchase(foods);
        }
    }

    private static boolean validateCard(String cardNo) {
        // validate card number using Luhn's algorithm
        int nDigits = cardNo.length();
 
    int nSum = 0;
    boolean isSecond = false;
    for (int i = nDigits - 1; i >= 0; i--) {
 
        int d = cardNo.charAt(i) - '0';
 
        if (isSecond == true)
            d = d * 2;

        nSum += d / 10;
        nSum += d % 10;
 
        isSecond = !isSecond;
    }
    return (nSum % 10 == 0);
    }

}
