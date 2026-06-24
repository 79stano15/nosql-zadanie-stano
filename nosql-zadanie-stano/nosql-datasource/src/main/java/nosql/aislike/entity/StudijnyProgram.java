package nosql.aislike.entity;

import java.io.Serializable;

public class StudijnyProgram implements Serializable {
	private static final long serialVersionUID = 911661511583476510L;
	private Long id;
	private String skratka;
	private String popis;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSkratka() {
		return skratka;
	}
	public void setSkratka(String skratka) {
		this.skratka = skratka;
	}
	public String getPopis() {
		return popis;
	}
	public void setPopis(String popis) {
		this.popis = popis;
	}
	@Override
	public String toString() {
		return "StudijnyProgram [id=" + id + ", skratka=" + skratka + ", popis=" + popis + "]";
	}	
}
