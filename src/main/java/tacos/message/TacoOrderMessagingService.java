package tacos.message;

import tacos.model.TacoOrder;

public interface TacoOrderMessagingService {
    void sendOrder(TacoOrder order);
    void sendOrderConvert(TacoOrder order);
}