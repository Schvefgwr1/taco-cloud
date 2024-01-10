package tacos;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tacos.data.IngredientRepository;
import tacos.model.Ingredient;
import tacos.model.Ingredient.Type;

@SpringBootApplication
public class TacoCloudApplication {

    @Bean
    public ApplicationRunner dataLoader(IngredientRepository repo) {
        return args -> {
            repo.deleteAll();
            repo.save(new Ingredient(1, "Flour Tortilla", Type.WRAP));
            repo.save(new Ingredient(2, "Corn Tortilla", Type.WRAP));
            repo.save(new Ingredient(3, "Ground Beef", Type.PROTEIN));
            repo.save(new Ingredient(4, "Carnitas", Type.PROTEIN));
            repo.save(new Ingredient(5, "Diced Tomatoes", Type.VEGGIES));
            repo.save(new Ingredient(6, "Lettuce", Type.VEGGIES));
            repo.save(new Ingredient(7, "Cheddar", Type.CHEESE));
            repo.save(new Ingredient(8, "Monterrey Jack", Type.CHEESE));
            repo.save(new Ingredient(9, "Salsa", Type.SAUCE));
            repo.save(new Ingredient(10, "Sour Cream", Type.SAUCE));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
