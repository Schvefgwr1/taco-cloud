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
            repo.save(new Ingredient("Flour Tortilla", Type.WRAP));
            repo.save(new Ingredient("Corn Tortilla", Type.WRAP));
            repo.save(new Ingredient("Ground Beef", Type.PROTEIN));
            repo.save(new Ingredient("Carnitas", Type.PROTEIN));
            repo.save(new Ingredient("Diced Tomatoes", Type.VEGGIES));
            repo.save(new Ingredient("Lettuce", Type.VEGGIES));
            repo.save(new Ingredient("Cheddar", Type.CHEESE));
            repo.save(new Ingredient("Monterrey Jack", Type.CHEESE));
            repo.save(new Ingredient("Salsa", Type.SAUCE));
            repo.save(new Ingredient("Sour Cream", Type.SAUCE));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
