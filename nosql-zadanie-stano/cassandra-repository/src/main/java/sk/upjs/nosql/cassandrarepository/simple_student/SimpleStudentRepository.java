package sk.upjs.nosql.cassandrarepository.simple_student;

import org.springframework.data.cassandra.repository.AllowFiltering;import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SimpleStudentRepository extends CrudRepository<CassandraSimpleStudent, Long> {
    List<CassandraSimpleStudent> findByPriezvisko(String priezvisko);
    @AllowFiltering
    List<CassandraSimpleStudent> findBySkratkaakadtitul(String titul);
}
