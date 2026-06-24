package sk.upjs.nosql.mongodbrepository.student;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<MongoStudent, Long> {

    // Uloha 1: projekcia - meno a priezvisko (jednoducha)
    List<MenoAndPriezvisko> findBySkratkaakadtitul(String skratkaakadtitul);
    // Uloha 2: dopyt podla programu + rok (filtrovanie v DB cez $elemMatch)
    @Query("{ 'studium': { $elemMatch: { 'skratka': ?0, 'zaciatokStudia': { $lte: ?2 }, $or: [ { 'koniecStudia': '' }, { 'koniecStudia': null }, { 'koniecStudia': { $gte: ?1 } } ] } } }")
    List<MongoStudent> findByStudiumSkratkaAndRok(String skratkaProgram,
                                                  String zaciatokAkRok,
                                                  String koniecAkRok);
}
