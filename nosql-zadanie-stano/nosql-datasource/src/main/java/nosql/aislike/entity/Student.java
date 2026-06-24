package nosql.aislike.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable{
	private static final long serialVersionUID = 5190927946812430075L;
	private Long id;
	private String meno;
	private String priezvisko;
	private char kodpohlavie;
	private String skratkaakadtitul;
	private List<Studium> studium = new ArrayList<Studium>();
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
	public List<Studium> getStudium() {
		return studium;
	}
	public void setStudium(List<Studium> studium) {
		this.studium = studium;
	}
	
	
}
