package tacos.message;

import tacos.model.TacoOrder;

public interface OrderMessagingService {
    public void sendOrder(TacoOrder order);
}
