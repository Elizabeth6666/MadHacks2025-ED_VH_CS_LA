import java.util.ArrayList;

public final class mainMethod {
  private mainMethod() {}
  public static void main(String[] args) {
    // Recipe testRecipe = new Recipe("test");
    // testRecipe.setInstructions("A");
    // testRecipe.addAllergens("B");
    // testRecipe.addAllergens("C");
    // testRecipe.addCookMethod("Oven");
    // testRecipe.addCookingTime(20.0);
    // testRecipe.addIngredient("D");
    // System.out.println("Hello");
    // RecipeToDB testRecToDB = new RecipeToDB(testRecipe);
    DBToRecipe test = new DBToRecipe("All","None");
    ArrayList<Recipe> banana = test.sort();
    for (Recipe apple : banana) {
      System.out.println(apple);
    }
  }
}
