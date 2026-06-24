package nosql.aislike;

import java.util.List;

import nosql.aislike.entity.SimpleStudent;
import nosql.aislike.entity.Student;

public interface StudentDao {
	List<Student> getAll();
	List<SimpleStudent> getSimpleStudents();
}
