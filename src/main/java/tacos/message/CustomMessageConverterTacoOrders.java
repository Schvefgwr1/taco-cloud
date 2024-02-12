package tacos.message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.messaging.converter.MessageConversionException;
import tacos.model.TacoOrder;

public class CustomMessageConverterTacoOrders  {

    private final ObjectMapper objectMapper;

    public CustomMessageConverterTacoOrders(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toMessage(Object object) throws JsonProcessingException {
        try {
            TacoOrder order = (TacoOrder) object;
            ObjectNode orderNode = objectMapper.valueToTree(order);
            orderNode.remove("user");
            return objectMapper.writeValueAsString(orderNode);
        }
        catch (Exception e) {
            return "Bad process of parsing";
        }
    }
    public TacoOrder fromMessage(String message) throws MessageConversionException {
        try {
            Class<?> targetClass = TacoOrder.class;
            return (TacoOrder) objectMapper.readValue(message, targetClass);
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert JMS message to object", e);
        }
    }
}
