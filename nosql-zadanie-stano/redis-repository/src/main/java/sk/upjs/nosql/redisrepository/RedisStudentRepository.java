package sk.upjs.nosql.redisrepository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisStudentRepository extends CrudRepository<RedisStudent, Long> {
    List<RedisStudent> findByPriezvisko(String priezvisko);
}
