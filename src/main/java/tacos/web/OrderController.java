package tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.data.UserRepository;
import tacos.model.Taco;
import tacos.model.TacoOrder;
import tacos.data.OrderRepository;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import tacos.model.User;

import java.security.Principal;

//добавляет автоматические логи
@Slf4j
//говорит о том что это класс контроллера
@Controller
//говорит о пути для данного контроллера
@RequestMapping("/orders")
//поддержка создания объекта тако в сеансе (сеанс поддерживает хранение данных отдельного пользователя в течении нескольких запросов)
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepo;
    private UserRepository userRepo;
    public OrderController(OrderRepository orderRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order,
                               Errors errors,
                               SessionStatus sessionStatus,
                               Principal principal) {
        if(errors.hasErrors()) {
            return "orderForm";
        }
        for(Taco taco: order.getTacos()) {
            taco.setTacoOrder(order);
        }

        User user = userRepo.findUserByUsername(principal.getName());
        order.setUser(user);

        orderRepo.save(order);

        //так как объект заказа был в сеансе, то мы его завершаем, чтобы успешно создавать новые заказы
        sessionStatus.setComplete();
        return "redirect:/";
    }
}