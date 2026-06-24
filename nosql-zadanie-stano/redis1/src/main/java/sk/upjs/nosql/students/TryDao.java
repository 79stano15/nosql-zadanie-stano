package sk.upjs.nosql.students;

import nosql.aislike.DaoFactory;
import nosql.aislike.StudentDao;
import nosql.aislike.entity.SimpleStudent;
import sk.upjs.nosql.RedisFactory;

import java.util.List;

public class TryDao {
    public static void main(String[] args) {
        SimpleStudentDao redisDao = RedisFactory.INSTANCE.simpleStudentDao();
        StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
        List<SimpleStudent> simpleStudents = studentDao.getSimpleStudents();
        redisDao.deleteAll();
        redisDao.save(simpleStudents.get(1));
        System.out.println(redisDao.getAll());
    }
}
