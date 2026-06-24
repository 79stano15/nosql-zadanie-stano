package sk.upjs.nosql.cassandrarepository;

import nosql.aislike.DaoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sk.upjs.nosql.cassandrarepository.simple_student.SimpleStudentService;
import sk.upjs.nosql.cassandrarepository.student.StudentService;

@SpringBootApplication
public class CassandraRepositoryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CassandraRepositoryApplication.class, args);
        SimpleStudentService simpleStudentService = context.getBean(SimpleStudentService.class);
    //    testOneSimpleStudent(simpleStudentService);
    //    testAllSimpleStudents(simpleStudentService);
        StudentService studentService = context.getBean(StudentService.class);
        testAllStudents(studentService);
    }

    public static void testOneSimpleStudent(SimpleStudentService service) {
        service.insertOneStudent();
        service.printAllStudents();
    }
    public static void testAllSimpleStudents(SimpleStudentService service) {
        service.deleteAllStudents();
        service.insertAllStudents();
//        service.printAllStudents();
        System.out.println(service.getByPriezvisko("Guta"));
        System.out.println(service.getByTitul("RNDr."));
    }
    public static void testAllStudents(StudentService service) {
        service.deleteAllStudents();
        service.insertAllStudents();
//        service.printAllStudents();
        System.out.println(service.getStudentByIdAndPriezvisko(1006326, "Guta"));
        System.out.println(service.getByTitul("RNDr."));
    }
}
