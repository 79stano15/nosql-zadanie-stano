package sk.upjs.nosql.cassandrarepository.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.aislike.entity.Studium;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

/*
    CREATE TYPE IF NOT EXISTS studium (
    id bigint,
    zaciatok text,
    koniec text,
    id_studijny_program bigint,
    skratka text,
    popis text
    );

    CREATE OR REPLACE FUNCTION skratky (studia list<FROZEN<studium>>)
        RETURNS NULL ON NULL INPUT
        RETURNS list<text>
        LANGUAGE java
        AS $$
        List<String> result = new ArrayList<>();
        for (UDTValue studium : studia) {
            result.add(studium.getString("skratka"));
        }
        return result;
        $$;
 */

@NoArgsConstructor
@Getter
@Setter
@ToString
@UserDefinedType("studium")
public class CassandraStudium {
    @Column
    private Long id;
    @Column("zaciatok")
    private String zaciatokStudia;
    @Column("koniec")
    private String koniecStudia;
    @Column("id_studijny_program")
    private Long idStudijnyProgram;
    @Column
    private String skratka;
    @Column
    private String popis;

    public CassandraStudium(Studium studium) {
        this.id = studium.getId();
        this.zaciatokStudia = studium.getZaciatokStudia();
        this.koniecStudia = studium.getKoniecStudia();
        this.idStudijnyProgram = studium.getStudijnyProgram().getId();
        this.skratka = studium.getStudijnyProgram().getSkratka();
        this.popis = studium.getStudijnyProgram().getPopis();
    }
}
