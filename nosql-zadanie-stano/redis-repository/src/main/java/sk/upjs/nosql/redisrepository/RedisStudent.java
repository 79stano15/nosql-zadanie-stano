package sk.upjs.nosql.redisrepository;

import lombok.*;
import nosql.aislike.entity.Student;
import nosql.aislike.entity.Studium;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash("PGStudent")
public class RedisStudent {
    @Id
    private Long id;
    private String meno;
    @Indexed
    private String priezvisko;
    private char kodpohlavie;
    private String skratkaakadtitul;
    private List<Studium> studium = new ArrayList<Studium>();

    public RedisStudent(Student student) {
        this.id = student.getId();
        this.meno = student.getMeno();
        this.priezvisko = student.getPriezvisko();
        this.kodpohlavie = student.getKodpohlavie();
        this.skratkaakadtitul = student.getSkratkaakadtitul();
        this.studium = student.getStudium();
    }
}
