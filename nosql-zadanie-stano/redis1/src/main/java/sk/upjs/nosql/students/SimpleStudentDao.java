package sk.upjs.nosql.students;

import nosql.aislike.entity.SimpleStudent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class SimpleStudentDao {
    private final RedisTemplate<String, SimpleStudent> redisTemplate;
    private HashOperations<String, Long, SimpleStudent> hashOperations;
    private final String KEY = "PGSimpleStudents";

    public SimpleStudentDao(RedisTemplate<String, SimpleStudent> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }
    public List<SimpleStudent> getAll() {
        return hashOperations.values(KEY);
    }
    public SimpleStudent getById(long id) {
        return hashOperations.get(KEY, id);
    }
    public void save(SimpleStudent student) {
        hashOperations.put(KEY, student.getId(), student);
    }
    public void deleteById(long id) {
        hashOperations.delete(KEY, id);
    }
    public void deleteAll() {
        redisTemplate.delete(KEY);
    }
}
