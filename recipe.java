import java.util.ArrayList;

class Recipe {
    private ArrayList<String> ingredients = new ArrayList<>();  // Ingredients
    private String recipeName;                                      // Recipe Name
    private ArrayList<String> allergens = new ArrayList<>();        // Allergens
    private double cookTimeInMinutes;                               // Cook Time in Minutes
    private String cookMethod;                                      // Cook Method (No cook, microwave, oven, etc)
    private String recipeID = "ID";       
    private String instructions = "";

    public Recipe(String name) {
        this.recipeName = name;
    }

    // Mutators
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
    public void addCookingTime(double cookTimeInMinutes) {
        this.cookTimeInMinutes = cookTimeInMinutes;
    }
    public void addCookMethod(String cookMethod) {
        this.cookMethod = cookMethod;
    }
    public void addAllergens(String allergen) {
        allergens.add(allergen);
    }
    public void removeIngredient(String ingredient) {
        for (int i=0; i < ingredients.size(); i++) {
            if (ingredients.get(i).equalsIgnoreCase(ingredient)) {
                ingredients.remove(i);
                break;
            }
        }
    }
    public void removeAllergen(String allergen) {
        allergens.remove(allergen);
    }
    public void setID(String id) {
        this.recipeID = id;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // Ingredient Accessors
    // public int getIngredientAmount(String ingredient) {
    //     for (int i=0; i < ingredients.size(); i++) {
    //         if (ingredients.get(i).equalsIgnoreCase(ingredient)) {
    //             return ingredients.get(i).getAmountNumber();
    //         }
    //     }
    //     return -1;
    // }
    // public String getIngredientMeasurement(String ingredient) {
    //     for (int i=0; i < ingredients.size(); i++) {
    //         if (ingredients.get(i).getNameIngredient().equalsIgnoreCase(ingredient)) {
    //             return ingredients.get(i).getMeasurement();
    //         }
    //     }
    //     return "None";
    // }

    // Recipe Accessors
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    public String getName() {
        return recipeName;
    }
    public String getCookMethod() {
        return cookMethod;
    }
    public double getCookTimeInMinutes() {
        return cookTimeInMinutes;
    }
    public String getRecipeID() {
        return recipeID;
    }
    public ArrayList<String> getAllergens() {
        return allergens;
    }
    public String getInstructions() {
        return instructions;
    }
}