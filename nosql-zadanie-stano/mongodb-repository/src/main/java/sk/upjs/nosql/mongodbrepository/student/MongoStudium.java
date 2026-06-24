package sk.upjs.nosql.mongodbrepository.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.aislike.entity.Studium;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MongoStudium {

    private Long id;
    private String zaciatokStudia;
    private String koniecStudia;
    private Long idStudijnyProgram;
    private String skratka;
    private String popis;

    public MongoStudium(Studium studium) {
        this.id = studium.getId();
        this.zaciatokStudia = studium.getZaciatokStudia();
        this.koniecStudia = studium.getKoniecStudia();
        this.idStudijnyProgram = studium.getStudijnyProgram().getId();
        this.skratka = studium.getStudijnyProgram().getSkratka();
        this.popis = studium.getStudijnyProgram().getPopis();
    }
}
