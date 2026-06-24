package sk.upjs.nosql.redisrepository;

import nosql.aislike.DaoFactory;
import nosql.aislike.StudentDao;
import nosql.aislike.entity.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class RedisRepositoryApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RedisRepositoryApplication.class, args);
        RedisStudentRepository studentRepository = context.getBean(RedisStudentRepository.class);
        StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
        List<Student> students = studentDao.getAll();
        studentRepository.deleteAll();
//        RedisStudent student1 = new RedisStudent(students.get(0));
//        studentRepository.save(student1);
//        Optional<RedisStudent> byId = studentRepository.findById(student1.getId());
//        System.out.println(byId.get());

        List<RedisStudent> redisStudents = students.stream()
                                        .map(student -> new RedisStudent(student)).toList();
        studentRepository.saveAll(redisStudents);
        var najahalovuList = studentRepository.findByPriezvisko("Najahalovu");
        System.out.println(najahalovuList);

    }

}
