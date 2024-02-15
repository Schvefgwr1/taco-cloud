package tacos.integration.email.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class EmailTaco {
    private final String name;
    private List<String> ingredients;
}
