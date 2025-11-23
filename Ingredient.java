public class Ingredient {
    private String nameIngredient;
    private int amountNumber;
    private String measurement;

    public Ingredient (String nameIngredient, int amountNumber, String measurement) {
      this.nameIngredient = nameIngredient;
      this.amountNumber = amountNumber;
      this.measurement = measurement;
    }

    // Accessors
    public String getNameIngredient() {
        return nameIngredient;
    }
    public int getAmountNumber() {
      return amountNumber;
    }
    public String getMeasurement() {
      return measurement;
    }

    public String toString() {
      return nameIngredient + "-" + amountNumber + "-" + measurement;
    }

    // Mutators
    public void changeAmount(int amountNumber) {
       this.amountNumber = amountNumber;
    }
    public void changeMeasurement(String measurement) {
       this.measurement = measurement;
    }
    public void changeIngredientName(String nameIngredient) {
       this.nameIngredient = nameIngredient;
    }
}