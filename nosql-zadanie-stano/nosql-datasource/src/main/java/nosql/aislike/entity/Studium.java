package nosql.aislike.entity;

import java.io.Serializable;

public class Studium implements Serializable {
	private static final long serialVersionUID = 8448542295643128028L;
	private Long id;
	private String zaciatokStudia;
	private String koniecStudia;
	private StudijnyProgram studijnyProgram;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getZaciatokStudia() {
		return zaciatokStudia;
	}
	public void setZaciatokStudia(String zaciatokStudia) {
		this.zaciatokStudia = zaciatokStudia;
	}
	public String getKoniecStudia() {
		return koniecStudia;
	}
	public void setKoniecStudia(String koniecStudia) {
		this.koniecStudia = koniecStudia;
	}
	public StudijnyProgram getStudijnyProgram() {
		return studijnyProgram;
	}
	public void setStudijnyProgram(StudijnyProgram studijnyProgram) {
		this.studijnyProgram = studijnyProgram;
	}
	@Override
	public String toString() {
		return "Studium [id=" + id + ", zaciatokStudia=" + zaciatokStudia + ", koniecStudia=" + koniecStudia
				+ ", studijnyProgram=" + studijnyProgram + "]";
	}
}
