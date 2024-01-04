package tacos.data;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.model.Ingredient;
import java.util.Optional;
import java.util.List;


@Repository
public class JdbcIngredientRepository implements IngredientRepository {
    private JdbcTemplate jdbcTemplate;
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(
                "select id, name, type from ingredient",
                this::mapRowToIngredient);
    }
    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> results = jdbcTemplate.query(
                "select id, name, type from ingredient where id=?",
                this::mapRowToIngredient,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }
    private Ingredient mapRowToIngredient(ResultSet row, int rowNum)
            throws SQLException {
        return new Ingredient(
                row.getInt("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type")));
    }
    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
                "insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }
}