package tacos.kitchen;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tacos.model.TacoOrder;
import tacos.kitchen.KitchenUI;
@Component
public class RabbitOrderListener {
    private KitchenUI ui;
    @Autowired
    public RabbitOrderListener() {
        this.ui = null;
    }
    @RabbitListener(queues = "tacoorders")
    public void receiveOrder(TacoOrder order) {
        ui.displayOrder(order);
    }
}