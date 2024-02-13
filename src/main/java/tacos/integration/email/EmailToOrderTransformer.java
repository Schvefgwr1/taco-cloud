package tacos.integration.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import tacos.integration.email.models.EmailOrder;
import tacos.integration.email.models.EmailIngredient;
import tacos.integration.email.models.EmailTaco;

@Component
public class EmailToOrderTransformer extends AbstractMailMessageTransformer<EmailOrder> {
    private static Logger log = LoggerFactory.getLogger(EmailToOrderTransformer.class);
    private static final String SUBJECT_KEYWORDS = "TACO ORDER";
    @Override
    protected AbstractIntegrationMessageBuilder<EmailOrder> doTransform(Message mailMessage) {
        EmailOrder tacoOrder = processPayload(mailMessage);
        return MessageBuilder.withPayload(tacoOrder);
    }
    private EmailOrder processPayload(Message mailMessage) {
        try {
            String subject = mailMessage.getSubject();
            if (subject.toUpperCase().contains(SUBJECT_KEYWORDS)) {
                String email =
                        ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
                String content = mailMessage.getContent().toString();
                return parseEmailToOrder(email, content);
            }
        } catch (MessagingException e) {
            log.error("MessagingException: {}", e);
        } catch (IOException e) {
            log.error("IOException: {}", e);
        }
        return null;
    }
    private EmailOrder parseEmailToOrder(String email, String content) {
        EmailOrder order = new EmailOrder(email);
        String[] lines = content.split("\\r?\\n");
        for (String line : lines) {
            if (line.trim().length() > 0 && line.contains(":")) {
                String[] lineSplit = line.split(":");
                String tacoName = lineSplit[0].trim();
                String ingredients = lineSplit[1].trim();
                String[] ingredientsSplit = ingredients.split(",");
                List<String> ingredientCodes = new ArrayList<>();
                for (String ingredientName : ingredientsSplit) {
                    String code = lookupIngredientCode(ingredientName.trim());
                    if (code != null) {
                        ingredientCodes.add(code);
                    }
                }
                EmailTaco taco = new EmailTaco(tacoName);
                taco.setIngredients(ingredientCodes);
                order.addTaco(taco);
            }
        }
        return order;
    }
    private String lookupIngredientCode(String ingredientName) {
        for (EmailIngredient ingredient : ALL_INGREDIENTS) {
            String ucIngredientName = ingredientName.toUpperCase();
            if (LevenshteinDistance.getDefaultInstance()
                    .apply(ucIngredientName, ingredient.getName()) < 3 ||
                    ucIngredientName.contains(ingredient.getName()) ||
                    ingredient.getName().contains(ucIngredientName)) {
                return ingredient.getName();
            }
        }
        return null;
    }
    private static EmailIngredient[] ALL_INGREDIENTS = new EmailIngredient[] {
            new EmailIngredient("FLTO", "FLOUR TORTILLA"),
            new EmailIngredient("COTO", "CORN TORTILLA"),
            new EmailIngredient("GRBF", "GROUND BEEF"),
            new EmailIngredient("CARN", "CARNITAS"),
            new EmailIngredient("TMTO", "TOMATOES"),
            new EmailIngredient("LETC", "LETTUCE"),
            new EmailIngredient("CHED", "CHEDDAR"),
            new EmailIngredient("JACK", "MONTERREY JACK"),
            new EmailIngredient("SLSA", "SALSA"),
            new EmailIngredient("SRCR", "SOUR CREAM")
    };
}