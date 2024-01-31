package tacos.web.api;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tacos.data.OrderRepository;
import tacos.model.Taco;
import tacos.data.TacoRepository;
import tacos.model.TacoOrder;

import java.util.Optional;


//сообщает о том, что контроллер работает с REST API
@RestController
//обрабатывает запросы /api/tacos, в формале json, при желании можно указать xml
@RequestMapping(path="/api/tacos",
        produces="application/json")
//дада те самые корсы
@CrossOrigin(origins={"https://localhost:8080", "https://tacocloud_front.com"})
public class TacoController {
    private TacoRepository tacoRepo;
    private OrderRepository orderRepo;
    public TacoController(TacoRepository tacoRepo, OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
        this.tacoRepo = tacoRepo;
    }
    @GetMapping()
    public Iterable<Taco> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Integer id) {
        //Optional - класс, который хранит возможный объект Null, избегая NullPointerException
        Optional<Taco> opt = tacoRepo.findById(id);
        if(opt.isPresent()) {
            return new ResponseEntity<>(opt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * пример JSON В BODY запроса для создания тестового тако
     * <p>
     * {
     *   "id": null,
     *   "createdAt": null,
     *   "name": "test_api_taco",
     *   "ingredients": [
     *     {
     *         "id": 1,
     *         "name": "Flour Tortilla",
     *         "type": "WRAP"
     *     },
     *     {
     *         "id": 2,
     *         "name": "Grilled Chicken",
     *         "type": "PROTEIN"
     *     },
     *     {
     *         "id": 3,
     *         "name": "Lettuce",
     *         "type": "VEGGIES"
     *     },
     *     {
     *         "id": 4,
     *         "name": "Cheddar Cheese",
     *         "type": "CHEESE"
     *     },
     *     {
     *         "id": 5,
     *         "name": "Salsa",
     *         "type": "SAUCE"
     *     }
     *   ]
     * }
     */
    //consumes говорит о формате данных от пользователя
    @PostMapping(consumes="application/json")
    //задает статус 201 created (оздан ресурс)
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }

    //PUT - противоположность GET, предпологает ПОЛНУЮ замену созданного ресурса данными от пользователя
    @PutMapping(path="/{orderId}", consumes="application/json")
    public TacoOrder putOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody TacoOrder order) {
        order.setId(orderId);
        return orderRepo.save(order);
    }

    //PATCH - предпологает частичное изменение уже существующего ресурса
    @PatchMapping(path="/{orderId}", consumes="application/json")
    public TacoOrder patchOrder(@PathVariable("orderId") Long orderId,
                                @RequestBody TacoOrder patch) {
        TacoOrder order = orderRepo.findById(orderId).get();
        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(patch.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(patch.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(patch.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(patch.getDeliveryZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
        return orderRepo.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            orderRepo.deleteById(orderId);
        } catch (EmptyResultDataAccessException ignored) {}
    }

}