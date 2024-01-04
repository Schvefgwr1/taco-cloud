package tacos.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.model.Ingredient;
import tacos.model.Taco;
import tacos.model.Ingredient.Type;
import tacos.model.TacoOrder;

import jakarta.validation.Valid;
import org.springframework.validation.Errors;

import tacos.data.IngredientRepository;

//добавляет автоматические логи
@Slf4j
//говорит о том что это класс контроллера
@Controller
//говорит о пути для данного контроллера
@RequestMapping("/design")
//поддержка создания объекта тако в сеансе (сеанс поддерживает хранение данных отдельного пользователя в течении нескольких запросов)
@SessionAttributes("tacoOrder")
public class DesignTacoController {


    private final IngredientRepository ingredientRepo;
    @Autowired
    public DesignTacoController(
            IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    //создает список ингредиентов и добавляет в модель
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType((List<Ingredient>) ingredients, type));
        }
    }

    //создает новый заказ и добавляет в модель
    //хранит состояние заказа, пока клиент выбирает ингредиенты несколькими запросами, благодаря session
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    //создает новое блюдо и добавляет в модель
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }

    //Valid проверяет форму тако до входа в метод, если что-то не сходится, то это записывается в errors
    @PostMapping
    public String processTaco(@Valid Taco taco,
                              Errors errors,
                              @ModelAttribute TacoOrder tacoOrder) {
        if(errors.hasErrors()) {
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }
}
