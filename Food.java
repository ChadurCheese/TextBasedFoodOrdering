public class Food {
    private String category;
    private String name;
    private double price;
    private String restaurant;
    
    public Food(String name, double price, String restaurant) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }
    
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getRestaurant() {
        return restaurant;
    }



    
}
