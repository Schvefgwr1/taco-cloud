package tacos.integration.email;

import org.springframework.integration.core.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tacos.integration.email.models.EmailOrder;

@Component
public class OrderSubmitMessageHandler implements GenericHandler<EmailOrder> {
    private RestTemplate rest;
    public OrderSubmitMessageHandler() {
        this.rest = new RestTemplate();
    }
    @Override
    public Object handle(EmailOrder order, MessageHeaders headers) {
        rest.postForObject("http://localhost:8080/api/orders/fromEmail", order, String.class);
        return null;
    }
}