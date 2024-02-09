package tacos.kitchen.messaging.jms;

import tacos.model.TacoOrder;

public interface OrderReceiver {
    TacoOrder receiveOrder();
}
