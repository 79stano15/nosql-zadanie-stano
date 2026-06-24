package sk.upjs.nosql.mongodbrepository.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.aislike.entity.Student;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Document("student")
public class MongoStudent {

    @Id
    private Long id;
    private String meno;
    private String priezvisko;
    private String skratkaakadtitul;
    private List<MongoStudium> studium = new ArrayList<>();

    public MongoStudent(Student student) {
        this.id = student.getId();
        this.meno = student.getMeno();
        this.priezvisko = student.getPriezvisko();
        this.skratkaakadtitul = student.getSkratkaakadtitul();
        this.studium = student.getStudium().stream()
                .map(MongoStudium::new)
                .toList();
    }
}
