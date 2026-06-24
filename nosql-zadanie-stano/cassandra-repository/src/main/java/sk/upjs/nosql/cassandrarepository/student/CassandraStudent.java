package sk.upjs.nosql.cassandrarepository.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.aislike.entity.Student;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

/*
    CREATE TABLE student (
    id bigint,
    meno text,
    priezvisko text,
    titul text,
    studia list<FROZEN<studium>>,
    PRIMARY KEY (id, priezvisko)
    );
 */

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table("student")
public class CassandraStudent {
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long id;
    @Column
    private String meno;
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String priezvisko;
    @Transient
    private char kodpohlavie;
    @Column("titul")
    private String skratkaakadtitul;
    @Column("studia")
    @CassandraType(type= CassandraType.Name.LIST,
                   typeArguments = {CassandraType.Name.UDT},
                   userTypeName = "studium"
    )
    private List<CassandraStudium> studium = new ArrayList<>();

    public CassandraStudent(Student student) {
        this.id = student.getId();
        this.meno = student.getMeno();
        this.priezvisko = student.getPriezvisko();
        this.kodpohlavie = student.getKodpohlavie();
        this.skratkaakadtitul = student.getSkratkaakadtitul();
        this.studium = student.getStudium().stream().map(CassandraStudium::new).toList();
    }
}
