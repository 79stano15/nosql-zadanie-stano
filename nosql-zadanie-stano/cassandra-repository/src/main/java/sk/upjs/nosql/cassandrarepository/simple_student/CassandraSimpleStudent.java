package sk.upjs.nosql.cassandrarepository.simple_student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.aislike.entity.SimpleStudent;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    CREATE TABLE simple_student (
        id bigint PRIMARY KEY,
        meno text,
        priezvisko text,
        titul text,
        id_studii set<bigint>
    );
 */


@NoArgsConstructor
@Getter
@Setter
@ToString
@Table("simple_student")
public class CassandraSimpleStudent {
    @PrimaryKey
    private Long id;
    @Column
    private String meno;
    @Column
    @Indexed
    private String priezvisko;
    @Transient
    private char kodpohlavie;
    @Column("titul")
    private String skratkaakadtitul;
    @Column("id_studii")
    private Set<Long> idStudii;

    public CassandraSimpleStudent(SimpleStudent student) {
        this.id = student.getId();
        this.meno = student.getMeno();
        this.priezvisko = student.getPriezvisko();
        this.kodpohlavie = student.getKodpohlavie();
        this.skratkaakadtitul = student.getSkratkaakadtitul();
        this.idStudii = new HashSet<>(student.getIdStudii());
    }
}
