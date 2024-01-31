package tacos.web.api.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tacos.model.Ingredient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TacoRestClient {
    private RestTemplate rest;

    public TacoRestClient() {
        this.rest = new RestTemplate();
    }

    //get for object возвращает объект из запроса созданный из тела ответа
    public Ingredient getIngredientById(Integer id) {
        Map<String, String> urlParams= new HashMap<>();
        urlParams.put("id", Integer.toString(id));
        return rest.getForObject(
                "http://localhost:8080/ingredients/{id}",
                Ingredient.class,
                urlParams
        );
    }
    
    //getForEntity возвращает всё тело ответа с доп. инф.
    //также показана возможность передаать арг не списком а параметрами
    public Ingredient getIngredientByIdWithResponse(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity =
                rest.getForEntity(
                        "http://localhost:8080/ingredients/{id}",
                        Ingredient.class, 
                        ingredientId
                );
        log.info("Fetched time: {}",
                responseEntity.getHeaders().getDate());
        return responseEntity.getBody();
    }

    public void updateIngredient(Ingredient ingredient) {
        rest.put("http://localhost:8080/ingredients/{id}",
                ingredient, ingredient.getId());
    }

    public void deleteIngredient(Ingredient ingredient) {
        rest.delete("http://localhost:8080/ingredients/{id}",
                ingredient.getId());
    }

    //просто создает объект
    public Ingredient createIngredient(Ingredient ingredient) {
        return rest.postForObject("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }

    //создает объект и возвращает информацию о нем URI
    public java.net.URI createIngredientLoc(Ingredient ingredient) {
        return rest.postForLocation("http://localhost:8080/ingredients",
                ingredient);
    }

    //создает объект и возвраащет информацию о нем телом ответа
    public Ingredient createIngredientEntity(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity =
                rest.postForEntity("http://localhost:8080/ingredients",
                        ingredient,
                        Ingredient.class);
        log.info("New resource created at {}",
                responseEntity.getHeaders().getLocation());
        return responseEntity.getBody();
    }

}
