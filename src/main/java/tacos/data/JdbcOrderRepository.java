package tacos.data;

import java.sql.Types;
import java.util.*;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tacos.model.Ingredient;
import tacos.model.IngredientRef;
import tacos.model.Taco;
import tacos.model.TacoOrder;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into taco_order "
                                + "(delivery_name, delivery_street, delivery_city, "
                                + "delivery_state, delivery_zip, cc_number, "
                                + "cc_expiration, cc_cvv, placed_at) "
                                + "values (?,?,?,?,?,?,?,?,?)",
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
                );
        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                order.getDeliveryName(),
                                order.getDeliveryStreet(),
                                order.getDeliveryCity(),
                                order.getDeliveryState(),
                                order.getDeliveryZip(),
                                order.getCcNumber(),
                                order.getCcExpiration(),
                                order.getCcCVV(),
                                order.getPlacedAt()));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTaco(orderId, taco);
        }
        return order;
    }

    private long saveTaco(Long orderId, Taco taco) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into taco "
                                + "(name, taco_order_id, created_at)"
                                + "values (?,?,?)",
                        Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP
                );

        pscf.setReturnGeneratedKeys(true);
        taco.setCreatedAt(new Date());

        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                orderId,
                                taco.getCreatedAt()
                        ));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        List<IngredientRef> ingredientRefs = new ArrayList<>();
        for(Ingredient ingredient: taco.getIngredients()) {
            ingredientRefs.add(new IngredientRef(ingredient.getId()));
        }

        saveIngredientRefs(tacoId, ingredientRefs);

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientsRefs) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into ingredient_ref "
                                + "(taco_id, ingredient_id)"
                                + "values (?,?)",
                        Types.INTEGER, Types.INTEGER
                );

        for(IngredientRef ingredient_ref: ingredientsRefs) {
            PreparedStatementCreator psc =
                    pscf.newPreparedStatementCreator(
                            Arrays.asList(
                                    tacoId,
                                    ingredient_ref.getIngredient_id()
                            ));

            jdbcOperations.update(psc);

        }
    }
}
