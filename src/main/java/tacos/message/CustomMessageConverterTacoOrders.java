package tacos.message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import tacos.model.TacoOrder;

public class CustomMessageConverterTacoOrders implements MessageConverter {

    private final ObjectMapper objectMapper;

    public CustomMessageConverterTacoOrders(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        try {
            TacoOrder order = (TacoOrder) object;
            ObjectNode orderNode = objectMapper.valueToTree(order);
            orderNode.remove("user");
            String json = objectMapper.writeValueAsString(orderNode);
            Message message = session.createTextMessage(json);
            message.setStringProperty("_typeId", String.valueOf(TacoOrder.class));
            return message;
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert object to JMS message", e);
        }
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        try {
            String json = ((jakarta.jms.TextMessage) message).getText();
            Class<?> targetClass = TacoOrder.class;
            return objectMapper.readValue(json, targetClass);
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert JMS message to object", e);
        }
    }
}
