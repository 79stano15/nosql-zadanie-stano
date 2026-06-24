package nosql.aislike;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import nosql.aislike.entity.SimpleStudent;
import nosql.aislike.entity.Student;
import nosql.aislike.entity.StudijnyProgram;
import nosql.aislike.entity.Studium;

public class MysqlStudentDao implements StudentDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public MysqlStudentDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<SimpleStudent> getSimpleStudents() {
		String sql = "SELECT osoba.id as osobaid, meno, priezvisko, kodpohlavie, skratkaakadtitul, studium.id as studiumid "
				+ "FROM osoba "
				+ "JOIN studium ON osoba.id = studium.idStudent "
				+ "WHERE osoba.id > 1000000 AND osoba.id < 1100000 "
				+ "ORDER BY osobaid, studiumid";
		List<SimpleStudent> students = jdbcTemplate.query(sql, new ResultSetExtractor<List<SimpleStudent>>() {

			public List<SimpleStudent> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SimpleStudent> students = new ArrayList<>();
				SimpleStudent student = null;
				while (rs.next()) {
					long studentid = rs.getLong("osobaid");
					if (student == null || student.getId() != studentid) {
						student = new SimpleStudent();
						student.setId(studentid);
						student.setMeno(rs.getString("meno"));
						student.setPriezvisko(rs.getString("priezvisko"));
						student.setKodpohlavie(rs.getString("kodpohlavie").charAt(0));
						student.setSkratkaakadtitul(rs.getString("skratkaakadtitul").trim());
						student.setIdStudii(new ArrayList<>());
						students.add(student);
					}
					student.getIdStudii().add(rs.getLong("studiumid"));
				}
				return students;
			}
			
		});
		return students;
	}
	
	public List<Student> getAll() {
		String sql = "SELECT osoba.id as osobaid, meno, priezvisko, kodpohlavie, skratkaakadtitul, studium.id as studiumid, "
				+ "zaciatokStudia, koniecStudia, studijnyprogram.id as studijnyprogramid, skratka, popis FROM osoba "
				+ "JOIN studium ON osoba.id = studium.idStudent "
				+ "JOIN studijnyprogram ON studium.idStudijnyProgram = studijnyprogram.id "
				+ "WHERE osoba.id > 1000000 AND osoba.id < 1100000 "
				+ "ORDER BY osobaid, studiumid";
		List<Student> students = jdbcTemplate.query(sql, new ResultSetExtractor<List<Student>>() {

			public List<Student> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Student> students = new ArrayList<Student>();
				Map<Long, StudijnyProgram> programs = new HashMap<Long, StudijnyProgram>();
				Student student = null;
				while (rs.next()) {
					long studentid = rs.getLong("osobaid");
					if (student == null || student.getId() != studentid) {
						student = new Student();
						student.setId(studentid);
						student.setMeno(rs.getString("meno"));
						student.setPriezvisko(rs.getString("priezvisko"));
						student.setKodpohlavie(rs.getString("kodpohlavie").charAt(0));
						student.setSkratkaakadtitul(rs.getString("skratkaakadtitul").trim());
						students.add(student);
					}
					Studium studium = new Studium();
					studium.setId(rs.getLong("studiumid"));
					studium.setZaciatokStudia(rs.getString("zaciatokStudia").trim());
					studium.setKoniecStudia(rs.getString("koniecStudia").trim());

					long idProgram = rs.getLong("studijnyprogramid");
					StudijnyProgram program = programs.get(idProgram);
					if (program == null) {
						program = new StudijnyProgram();
						program.setId(idProgram);
						program.setPopis(rs.getString("popis").trim());
						program.setSkratka(rs.getString("skratka").trim());
						programs.put(idProgram, program);
					}
					studium.setStudijnyProgram(program);
					
					student.getStudium().add(studium);					
				}
				return students;
			}
			
		});
		return students;
	}
}
