package ericminio.demo.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnyEntityRepository extends CrudRepository<AnyEntity, Long> {

    @Query(value = "SELECT count(*) from ANY_ENTITY where ANY_FIELD = :value", nativeQuery = true)
    Integer countWhereFieldIs(@Param("value") String value);
}
