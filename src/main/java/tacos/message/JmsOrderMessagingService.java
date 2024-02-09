package tacos.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import tacos.model.TacoOrder;
import tacos.message.MessageConfig.*;

@Service
public class JmsOrderMessagingService implements TacoOrderMessagingService {

    //определяется в bean компоненте
    private Destination orderQueue;
    private JmsTemplate jms;
    @Autowired
    public JmsOrderMessagingService(JmsTemplate jms, Destination orderQueue) {
        this.jms = jms;
        this.jms.setMessageConverter(new CustomMessageConverterTacoOrders(new ObjectMapper()));
        this.orderQueue = orderQueue;
    }
    @Override
    public void sendOrder(TacoOrder order) {
        jms.send(orderQueue, session -> session.createObjectMessage(order));
    }

    @Override
    public void sendOrderConvert(TacoOrder order) {
        jms.convertAndSend("tacocloud.order.queue", order,
            //постобработчик, добавляет после создания сообщения к нему новый параметр
            message -> {
                message.setStringProperty("X_ORDER_SOURCE", "WEB");
                return message;
            }
        );
    }
}