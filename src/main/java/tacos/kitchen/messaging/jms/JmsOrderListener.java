package tacos.kitchen.messaging.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import tacos.model.TacoOrder;
import tacos.kitchen.KitchenUI;

/***
 * Метод receiveOrder() снабжен аннотацией @JmsListener, которая
 * превращает его в приемник сообщений, прослушивающий место назначения tacocloud.order.queue.
 * Он не имеет отношения к  JmsTemplate и не вызывается кодом приложения явно – фреймворк Spring
 * автоматически проверяет поступление сообщений в указанное место
 * назначения и в нужный момент вызывает метод receiveOrder(), передавая ему полученный экземпляр TacoOrder
***/

@Profile("jms-listener")
@Component
public class JmsOrderListener {
    private KitchenUI ui;
    @Autowired
    public JmsOrderListener(KitchenUI ui) {
        this.ui = ui;
    }
    @JmsListener(destination = "tacocloud.order.queue")
    public void receiveOrder(TacoOrder order) {
        ui.displayOrder(order);
    }
}