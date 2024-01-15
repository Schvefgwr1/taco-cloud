package tacos.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tacos.model.TacoOrder;
import tacos.model.User;

import java.util.Date;
import java.util.List;

public interface OrderRepository
        extends CrudRepository<TacoOrder, Long>  {

    //запросы распознанные по  названию
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(
            String deliveryZip,
            Date startDate,
            Date endDate
    );

    List<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

    //запросы написанные вручную
    @Query("SELECT o FROM TacoOrder o WHERE o.deliveryCity = :city")
    List<TacoOrder> readOrdersDeliveredInSeattle(String city);
}
