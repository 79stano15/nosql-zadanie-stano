package nosql.aislike.entity;

import java.io.Serializable;
import java.util.List;

public class SimpleStudent implements Serializable{

	private static final long serialVersionUID = -6705159174200908663L;
	private Long id;
	private String meno;
	private String priezvisko;
	private char kodpohlavie;
	private String skratkaakadtitul;
	private List<Long> idStudii;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMeno() {
		return meno;
	}
	public void setMeno(String meno) {
		this.meno = meno;
	}
	public String getPriezvisko() {
		return priezvisko;
	}
	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}
	public char getKodpohlavie() {
		return kodpohlavie;
	}
	public void setKodpohlavie(char kodpohlavie) {
		this.kodpohlavie = kodpohlavie;
	}
	public String getSkratkaakadtitul() {
		return skratkaakadtitul;
	}
	public void setSkratkaakadtitul(String skratkaakadtitul) {
		this.skratkaakadtitul = skratkaakadtitul;
	}
	public List<Long> getIdStudii() {
		return idStudii;
	}
	public void setIdStudii(List<Long> idStudii) {
		this.idStudii = idStudii;
	}
	@Override
	public String toString() {
		return "SimpleStudent [id=" + id + ", meno=" + meno + ", priezvisko=" + priezvisko + ", kodpohlavie="
				+ kodpohlavie + ", skratkaakadtitul=" + skratkaakadtitul + ", idStudii=" + idStudii + "]";
	}
}
