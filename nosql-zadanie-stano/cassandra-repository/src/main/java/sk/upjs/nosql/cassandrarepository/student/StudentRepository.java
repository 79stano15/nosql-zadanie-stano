package sk.upjs.nosql.cassandrarepository.student;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<CassandraStudent, MapId> {
    @AllowFiltering
    List<PriezviskoAndTitul> findBySkratkaakadtitul(String titul);
}
