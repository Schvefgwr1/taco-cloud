package tacos.model;

import lombok.Data;

@Data
public class Ingredient {

    private final int id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
