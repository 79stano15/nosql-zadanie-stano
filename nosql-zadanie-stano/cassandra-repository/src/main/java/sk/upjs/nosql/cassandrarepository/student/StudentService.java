package sk.upjs.nosql.cassandrarepository.student;

import nosql.aislike.DaoFactory;
import nosql.aislike.StudentDao;
import nosql.aislike.entity.Student;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.stereotype.Service;
import sk.upjs.nosql.cassandrarepository.simple_student.SimpleStudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
    private final StudentRepository repository;

    public StudentService(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    public void insertAllStudents() {
        List<Student> students = studentDao.getAll();
        List<CassandraStudent> cassandraStudents = students.stream().map(s -> new CassandraStudent(s)).toList();
        repository.saveAll(cassandraStudents);
    }
    public void printAllStudents() {
        repository.findAll().forEach(s -> System.out.println(s));
    }
    public void deleteAllStudents() {
        repository.deleteAll();
    }
    public CassandraStudent getStudentByIdAndPriezvisko(long id, String priezvisko) {
        MapId mapId = BasicMapId.id("id", id).with("priezvisko", priezvisko);
        return repository.findById(mapId).orElse(null);
    }
    public List<PriezviskoAndTitul> getByTitul(String titul) {
        return repository.findBySkratkaakadtitul(titul);
    }
}
