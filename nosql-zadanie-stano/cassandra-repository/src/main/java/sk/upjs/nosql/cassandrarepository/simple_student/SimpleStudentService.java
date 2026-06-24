package sk.upjs.nosql.cassandrarepository.simple_student;

import nosql.aislike.DaoFactory;
import nosql.aislike.StudentDao;
import nosql.aislike.entity.SimpleStudent;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SimpleStudentService {
    private final StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
    private final SimpleStudentRepository repository;

    public SimpleStudentService(SimpleStudentRepository simpleStudentRepository) {
        this.repository = simpleStudentRepository;
    }

    public void insertOneStudent() {
        List<SimpleStudent> simpleStudents = studentDao.getSimpleStudents();
        CassandraSimpleStudent first = new CassandraSimpleStudent(simpleStudents.getFirst());
        repository.save(first);
    }
    public void insertAllStudents() {
        List<SimpleStudent> simpleStudents = studentDao.getSimpleStudents();
        simpleStudents.forEach(s -> repository.save(new CassandraSimpleStudent(s)));
    }
    public void printAllStudents() {
        repository.findAll().forEach(s -> System.out.println(s));
    }
    public void deleteAllStudents() {
        repository.deleteAll();
    }
    public List<CassandraSimpleStudent> getByPriezvisko(String priezvisko) {
        return repository.findByPriezvisko(priezvisko);
    }
    public List<CassandraSimpleStudent> getByTitul(String titul) {
        return repository.findBySkratkaakadtitul(titul);
    }
}
