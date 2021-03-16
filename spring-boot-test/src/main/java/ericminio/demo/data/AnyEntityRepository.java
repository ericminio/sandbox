package ericminio.demo.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnyEntityRepository extends CrudRepository<AnyEntity, Long> {

    @Query(value = "SELECT count(*) from ANY_ENTITY where ANY_FIELD = :value", nativeQuery = true)
    Integer countWhereFieldIs(@Param("value") String value);

    List<AnyEntity> findByFieldIsNotNullOrderByField(Pageable pageable);

    List<AnyEntity> findByFieldIsNotNull();

    List<AnyEntity> findByFieldIsNotNullOrderByCreationDate();

    AnyEntity findByField(String value);
}
