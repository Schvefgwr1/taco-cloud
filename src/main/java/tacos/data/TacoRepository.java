package tacos.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;

import tacos.model.Taco;

public interface TacoRepository extends
        CrudRepository<Taco, Integer>,
        PagingAndSortingRepository<Taco, Integer> {

}
