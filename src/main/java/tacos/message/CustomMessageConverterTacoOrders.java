package tacos.message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import tacos.model.TacoOrder;

import java.util.Arrays;

@Component
public class CustomMessageConverterTacoOrders implements MessageConverter {

    private final ObjectMapper objectMapper;
    private final Jackson2JsonMessageConverter converter;

    public CustomMessageConverterTacoOrders(
            ObjectMapper objectMapper,
            Jackson2JsonMessageConverter converter
    ) {
        this.objectMapper = objectMapper;
        this.converter = converter;
    }

    @SneakyThrows
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) {
        TacoOrder order = (TacoOrder) object;
        ObjectNode orderNode = objectMapper.valueToTree(order);
        orderNode.remove("user");
        String json = objectMapper.writeValueAsString(orderNode);
        Message message = converter.toMessage(json, messageProperties);
        return message;
    }

    @SneakyThrows
    @Override
    public Object fromMessage(Message message)  {
        String json = (String) converter.fromMessage(message);
        return objectMapper.readValue(json, TacoOrder.class);
    }
}