package tacos.kitchen.messaging.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import tacos.message.CustomMessageConverterTacoOrders;
import tacos.message.MessageConfig;
import tacos.model.TacoOrder;

/*** вараинт с активным считыванием, на кухне нажимается "кнопка" о готовности принять заказ
 * и приложение приостанавливается пока в очереди не появится новый заказ для приготовления
 * (как только он появится на сервере брокере метод receive, на котором остановилось выполнение приложения
 * вернет нам заказ
 ***/

/*
 * вариант с подключением конвертера сообщений, позволяет сначал считать необработанное сообщение
 * и при необходимости произвести с ним какие-то действия,
 * после уже преобразовать в объект предметной области
@Component
public class JmsOrderReceiver implements OrderReceiver {
    private JmsTemplate jms;
    private MessageConverter converter;
    @Autowired
    public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
        this.jms = jms;
        this.converter = converter;
    }
    public TacoOrder receiveOrder() throws JMSException {
        Message message = jms.receive("tacocloud.order.queue");
        return (TacoOrder) converter.fromMessage(message);
    }
}
*/

//вариант сразу со считыванием и конвертацией в объект предметной области
@Component
public class JmsOrderReceiver implements OrderReceiver {
    private JmsTemplate jms;
    public JmsOrderReceiver(JmsTemplate jms) {
        this.jms = jms;
        this.jms.setMessageConverter(new CustomMessageConverterTacoOrders(new ObjectMapper()));
    }
    @Override
    public TacoOrder receiveOrder() {
        return (TacoOrder) jms.receiveAndConvert("tacocloud.order.queue");
    }
}