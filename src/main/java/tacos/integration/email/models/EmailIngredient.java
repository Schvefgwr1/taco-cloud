package tacos.integration.email.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailIngredient {
    private final String name;
    private final String type;
}
