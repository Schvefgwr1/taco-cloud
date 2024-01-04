package tacos.model;
 import lombok.Data;

 @Data
public class IngredientRef {
     private final int ingredient_id;

     public IngredientRef(int ingredient_id) {
         this.ingredient_id = ingredient_id;
     }
}
