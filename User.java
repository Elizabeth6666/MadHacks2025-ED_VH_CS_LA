import java.util.ArrayList;

public class User {
  
  private ArrayList <String> allergies;
  private ArrayList <String> methods;
  private ArrayList<Recipe> favoriteRecipes;
  
  private static int userCounter = 1;
  private String userID = "ID";
  private String username;
  private boolean student;
  
  //constructor
  public User(String username, boolean student) {
    this.username = username;
    this.student = student;
    
    this.favoriteRecipes = new ArrayList<>();
    this.allergies = new ArrayList<>();
    this.methods = new ArrayList<>();
    
    this.userID += userCounter;
    userCounter += 1;
  }

  //getters & setters
  public String getUsername() {
      return username;
  }
  public void setUsername(String username) {
      this.username = username;
  }
  public boolean getStudent() {
      return student;
  }
  public void setStudent(boolean student) {
      this.student = student
  }
  public ArrayList<Recipe> getFavoriteRecipes() {
      return favoriteRecipes;
  }
  public ArrayList<String> getAllergies() {
      return allergies;
  }
  
  //methods
  public void addAllergy(String allergen) {
    allergies.add(allergen);
  }
  public void removeAllergy(String allergen) {
      allergies.remove(allergen);
  }
  public void addFavoriteRecipe(Recipe Recipe) {
    favoriteRecipes.add(recipe);
  }
  public void removeFavoriteRecipe(Recipe recipe) {
      favoriteRecipes.remove(recipe);
  }
  public void prepMethod(String method) {
    methods.add(method);
  }
  public String printUserSummary() {
    return "User: " + username +
            "\nStudent: " + student +
            "\nAllergies: " + allergies +
            "\nPreferred Methods (oven, airfryer, stove, microwave): " + methods +
            "\nFavorite Recipes: " + favoriteRecipes.size();
  }
  
  

}
