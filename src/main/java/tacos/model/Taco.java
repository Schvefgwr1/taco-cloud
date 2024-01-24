package tacos.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt = new Date();

    //валидация данных в spring задается через модель
    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @NotNull
    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany(targetEntity=Ingredient.class)
    @JoinTable(name="Ingredient_Ref", joinColumns = { @JoinColumn(name="taco") },
            inverseJoinColumns = { @JoinColumn(name="ingredient") })
    List<Ingredient> ingredients;

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

}
