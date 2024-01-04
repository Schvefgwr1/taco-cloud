package tacos.data;
import java.util.Optional;
import tacos.model.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);
}
