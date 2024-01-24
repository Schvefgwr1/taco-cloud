package tacos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class TacoOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date placedAt;

    //NotBlank проверяет заполнены ли все данные в форме по каждому члену формы
    @NotBlank(message="Delivery name is required")
    private String deliveryName;

    @NotBlank(message="Street is required")
    private String deliveryStreet;

    @NotBlank(message="City is required")
    private String deliveryCity;

    @NotBlank(message="State is required")
    private String deliveryState;

    @NotBlank(message="Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message="Not valid a credit card number")
    private String ccNumber;

    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    //длина трехзначного числа 3 (код карты)
    @Digits(integer=3, fraction=0, message="Invalid CVV")
    private String ccCVV;

    @ManyToMany(targetEntity=Taco.class)
    @JoinTable(name="Taco_Ref", joinColumns = { @JoinColumn(name="taco_order") },
            inverseJoinColumns = { @JoinColumn(name="taco") })
    private List<Taco> tacos = new ArrayList<>();

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }

    public void setUser(User user) {
        this.user = user;
        this.deliveryCity = user.getCity();
        this.deliveryStreet = user.getStreet();
        this.deliveryZip = user.getZip();
    }
}
