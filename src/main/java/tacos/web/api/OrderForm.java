package tacos.web.api;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.model.Taco;
import tacos.model.TacoOrder;
import tacos.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
public class OrderForm {

    private String deliveryName;

    private String deliveryStreet;

    private String deliveryCity;

    private String deliveryState;

    private String deliveryZip;

    @CreditCardNumber(message="Not valid a credit card number")
    private String ccNumber;

    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    //длина трехзначного числа 3 (код карты)
    @Digits(integer=3, fraction=0, message="Invalid CVV")
    private String ccCVV;

    private List<Integer> tacosId;

    private long userId;

    public TacoOrder toTacoOrder(TacoRepository tacoRep, UserRepository userRep) {
        TacoOrder order = new TacoOrder();

        order.setDeliveryName(this.deliveryName);
        order.setDeliveryStreet(this.deliveryStreet);
        order.setDeliveryCity(this.deliveryCity);
        order.setDeliveryState(this.deliveryState);
        order.setDeliveryZip(this.deliveryZip);
        order.setCcNumber(this.ccNumber);
        order.setCcExpiration(this.ccExpiration);
        order.setCcCVV(this.ccCVV);
        order.setPlacedAt(new Date());

        for(Object tacoId: tacosId) {
            Optional<Taco> taco = tacoRep.findById((int) tacoId);
            if(taco.isPresent()) {
                order.addTaco(taco.get());
            }
            if(order.getTacos() == null) {
                return null;
            }
        }

        Optional<User> user = userRep.findById(this.userId);
        if(user.isPresent()) {
            order.setUser(user.get());
        }
        else {
            return null;
        }
        return order;
    }
}
