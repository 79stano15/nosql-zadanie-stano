package sk.upjs.nosql.mongodbrepository.student;

import nosql.aislike.DaoFactory;
import nosql.aislike.StudentDao;
import nosql.aislike.entity.Student;
import nosql.aislike.entity.StudijnyProgram;
import nosql.aislike.entity.Studium;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
    private final StudentRepository repository;
    private final MongoTemplate mongoTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");

    public StudentService(StudentRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    // -------------------------------------------------------------------------
    // Zakladne operacie - zakladne metody pre vkladanie, mazanie a vypis studentov
    // -------------------------------------------------------------------------

    public void insertAllStudents() {
        List<Student> students;
        try {
            students = studentDao.getAll();
            if (students == null || students.isEmpty()) {
                System.err.println("Warning: No students found from MySQL, using fallback sample data.");
                students = createSampleStudents();
            }
        } catch (Exception e) {
            System.err.println("Warning: MySQL data unavailable, using fallback sample data: " + e.getMessage());
            students = createSampleStudents();
        }
        List<MongoStudent> mongoStudents = students.stream().map(MongoStudent::new).toList();
        repository.saveAll(mongoStudents);
    }

    public void printAllStudents() {
        repository.findAll().forEach(System.out::println);
    }

    public void deleteAllStudents() {
        repository.deleteAll();
    }

    // -------------------------------------------------------------------------
    // Uloha 1: projekcia - vrati meno a priezvisko podla titulu 
    // -------------------------------------------------------------------------

    public List<MenoAndPriezvisko> getByTitul(String titul) {
        return repository.findBySkratkaakadtitul(titul);
    }

    // Uloha 2: filtrovanie v DB cez $elemMatch (repo dotaz)
    public List<MongoStudent> getByRokAProgram(int rok, String skratkaProgram) {
        String zaciatokAkRok = "1.9." + rok;
        String koniecAkRok   = "31.8." + (rok + 1);
        return repository.findByStudiumSkratkaAndRok(skratkaProgram, zaciatokAkRok, koniecAkRok);
    }

    // -------------------------------------------------------------------------
    // Pomocna metoda: vypis vsetkych programov (rychla kontrola)
    // -------------------------------------------------------------------------

    public void printVsetkyProgramy() {
        List<MongoStudent> all = new ArrayList<>();
        repository.findAll().forEach(all::add);
        all.stream()
                .flatMap(s -> s.getStudium().stream())
                .map(s -> s.getSkratka() + " - " + s.getPopis())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    // -------------------------------------------------------------------------
    // Uloha 3: index - porovnanie casov (skuska indexu)
    // -------------------------------------------------------------------------

    public void testIndex() {
        Query query = new Query(Criteria.where("studium.skratka").is("I"));

        long start = System.currentTimeMillis();
        mongoTemplate.find(query, MongoStudent.class);
        long end = System.currentTimeMillis();
        System.out.println("Cas bez indexu: " + (end - start) + " ms");

        mongoTemplate.indexOps(MongoStudent.class)
                .ensureIndex(new Index("studium.skratka", Sort.Direction.ASC));

        start = System.currentTimeMillis();
        mongoTemplate.find(query, MongoStudent.class);
        end = System.currentTimeMillis();
        System.out.println("Cas s indexom: " + (end - start) + " ms");
    }

    // Uloha 4: pocty podla roku a programu (Java implementacia)
    public List<RokProgramDTO> getPoctyStudentovByRokAndProgram() {
        List<MongoStudent> all = new ArrayList<>();
        repository.findAll().forEach(all::add);

        java.util.Map<String, Integer> pocty = new java.util.TreeMap<>();

        for (MongoStudent student : all) {
            for (MongoStudium st : student.getStudium()) {
                LocalDate zaciatok = parseDate(st.getZaciatokStudia());
                if (zaciatok == null) continue;
                LocalDate koniec = parseDateOrMax(st.getKoniecStudia());

                int rokZaciatku = academicYearOf(zaciatok);
                int rokKonca = koniec.getYear() >= 9999 ? rokZaciatku : academicYearOf(koniec);

                for (int rok = rokZaciatku; rok <= rokKonca; rok++) {
                    String key = rok + "|" + st.getSkratka();
                    pocty.merge(key, 1, Integer::sum);
                }
            }
        }

        List<RokProgramDTO> result = new ArrayList<>();
        pocty.forEach((key, pocet) -> {
            String[] parts = key.split("\\|");
            result.add(new RokProgramDTO(Integer.parseInt(parts[0]), parts[1], pocet));
        });
        return result;
    }

    public record RokProgramDTO(int rok, String skratka, int pocet) {}
    public record PocetDTO(String skratka, int pocet) {}

    // -------------------------------------------------------------------------
    // Private pomocne metody
    // -------------------------------------------------------------------------

    private LocalDate parseDate(String text) {
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDate.parse(text, FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseDateOrMax(String text) {
        LocalDate parsed = parseDate(text);
        return parsed == null ? LocalDate.of(9999, 12, 31) : parsed;
    }

    private int academicYearOf(LocalDate date) {
        return date.getMonthValue() >= 9 ? date.getYear() : date.getYear() - 1;
    }

    private List<Student> createSampleStudents() {
        StudijnyProgram mch = new StudijnyProgram();
        mch.setId(1L);
        mch.setSkratka("MCH");
        mch.setPopis("Matematicko-informaticky odbor");

        StudijnyProgram inf = new StudijnyProgram();
        inf.setId(2L);
        inf.setSkratka("INF");
        inf.setPopis("Informatika");

        Student s1 = new Student();
        s1.setId(1000001L);
        s1.setMeno("Peter");
        s1.setPriezvisko("Novak");
        s1.setKodpohlavie('M');
        s1.setSkratkaakadtitul("Mgr.");
        Studium st11 = new Studium();
        st11.setId(1L);
        st11.setZaciatokStudia("1.9.1998");
        st11.setKoniecStudia("31.8.2002");
        st11.setStudijnyProgram(mch);
        s1.getStudium().add(st11);

        Student s2 = new Student();
        s2.setId(1000002L);
        s2.setMeno("Jana");
        s2.setPriezvisko("Kovacova");
        s2.setKodpohlavie('F');
        s2.setSkratkaakadtitul("Bc.");
        Studium st21 = new Studium();
        st21.setId(2L);
        st21.setZaciatokStudia("1.9.1999");
        st21.setKoniecStudia("31.8.2003");
        st21.setStudijnyProgram(inf);
        s2.getStudium().add(st21);

        Student s3 = new Student();
        s3.setId(1000003L);
        s3.setMeno("Marek");
        s3.setPriezvisko("Hurban");
        s3.setKodpohlavie('M');
        s3.setSkratkaakadtitul("Mgr.");
        Studium st31 = new Studium();
        st31.setId(3L);
        st31.setZaciatokStudia("1.9.1998");
        st31.setKoniecStudia("31.8.2000");
        st31.setStudijnyProgram(mch);
        s3.getStudium().add(st31);

        return List.of(s1, s2, s3);
    }
}
