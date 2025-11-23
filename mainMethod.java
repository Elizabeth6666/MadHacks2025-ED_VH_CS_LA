public final class mainMethod {
  private mainMethod() {}
  public static void main(String[] args) {
    Recipe testRecipe = new Recipe("test");
    System.out.println("Hello");
    RecipeToDB testRecToDB = new RecipeToDB(testRecipe);
  }
}
