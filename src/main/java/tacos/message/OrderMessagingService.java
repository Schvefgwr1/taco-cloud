package tacos.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import tacos.model.TacoOrder;

public interface OrderMessagingService {
    public void sendOrder(TacoOrder order) throws JsonProcessingException;
}
