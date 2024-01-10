package tacos.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class IngredientRef {

     private final int ingredient_id;

     public IngredientRef(int ingredient_id) {
         this.ingredient_id = ingredient_id;
     }
}
