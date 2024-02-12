package tacos.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tacos.model.TacoOrder;
@Service
public class KafkaOrderMessagingService implements OrderMessagingService {
    private KafkaTemplate<String, String> kafkaTemplate;
    private CustomMessageConverterTacoOrders converter;
    @Autowired
    public KafkaOrderMessagingService(
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.converter = new CustomMessageConverterTacoOrders(new ObjectMapper());
    }
    @Override
    public void sendOrder(TacoOrder order) throws JsonProcessingException {
        String message = converter.toMessage(order);
        kafkaTemplate.send("tacocloud.orders.topic", message);
    }
}