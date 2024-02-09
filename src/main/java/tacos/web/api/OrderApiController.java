package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.model.TacoOrder;
import tacos.message.TacoOrderMessagingService;
import tacos.kitchen.messaging.jms.OrderReceiver;

@RestController
@RequestMapping(path="/api/orders", produces="application/json")
@CrossOrigin(origins="http://localhost:8080")
@JsonComponent
public class OrderApiController {

    private OrderRepository repo;
    private TacoOrderMessagingService messageService;
    private TacoRepository tacoRepository;
    private UserRepository userRepository;
    private OrderReceiver orderReceiver;
    @Autowired
    public OrderApiController(
            OrderRepository repo,
            TacoRepository tacoRepository,
            UserRepository userRepository,
            TacoOrderMessagingService messageService,
            OrderReceiver orderReceiver
    ) {
        this.repo = repo;
        this.messageService = messageService;
        this.tacoRepository = tacoRepository;
        this.userRepository = userRepository;
        this.orderReceiver = orderReceiver;
    }

    @GetMapping
    public Iterable<TacoOrder> getOrders() {
        return repo.findAll();
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TacoOrder postOrder(@RequestBody OrderForm orderForm) {
        TacoOrder order = orderForm.toTacoOrder(tacoRepository, userRepository);
        messageService.sendOrderConvert(order);
        return repo.save(order);
    }

    @GetMapping(value="/receive_order")
    public TacoOrder getReceiveOrder() {
        return orderReceiver.receiveOrder();
    }
}