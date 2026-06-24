package sk.upjs.nosql.mongodbrepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.upjs.nosql.mongodbrepository.student.StudentService;
import java.util.Scanner;

@SpringBootApplication
public class MongodbRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbRepositoryApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(StudentService studentService) {
        return args -> {
            // task selection: system property 'task' or env var 'TASK'
            String task = System.getProperty("task");
            if (task == null || task.isBlank()) {
                task = System.getenv().getOrDefault("TASK", "all");
            }
            if (task == null || task.isBlank()) task = "all";

            System.out.println("Running task: " + task);

            switch (task.toLowerCase()) {
                case "all":
                    System.out.println("=== Mazanie starych dat ===");
                    studentService.deleteAllStudents();

                    System.out.println("=== Vkladanie studentov ===");
                    studentService.insertAllStudents();

                    System.out.println("=== Vsetky studijne programy ===");
                    studentService.printVsetkyProgramy();

                    System.out.println("=== Vypis vsetkych studentov ===");
                    studentService.printAllStudents();

                    System.out.println("\n===== ULOHA 1: Projekcia - meno a priezvisko podla akademickeho titulu =====");
                    System.out.println("Studenti s titulom Mgr.:");
                    studentService.getByTitul("Mgr.").forEach(s ->
                            System.out.println("  " + s.getMeno() + " " + s.getPriezvisko())
                    );

                    System.out.println("\n===== ULOHA 2: Studenti v danom roku a programu =====");
                    System.out.println("Studenti v roku 1998 na programe MCH:");
                    studentService.getByRokAProgram(1998, "MCH")
                            .forEach(s -> System.out.println("  " + s.getMeno() + " " + s.getPriezvisko()));

                    System.out.println("\n===== ULOHA 3: Index na studijne programy =====");
                    studentService.testIndex();

                    System.out.println("\n===== ULOHA 4: Pocty studentov podla rokov a studijnych programov =====");
                    System.out.printf("%-10s %-20s %s%n", "Rok", "Program", "Pocet");
                    System.out.println("-".repeat(40));
                    studentService.getPoctyStudentovByRokAndProgram()
                            .forEach(r -> System.out.printf("%-10d %-20s %d%n", r.rok(), r.skratka(), r.pocet()));
                    break;

                case "insert":
                    studentService.deleteAllStudents();
                    studentService.insertAllStudents();
                    break;

                case "printall":
                case "print_all":
                    studentService.printAllStudents();
                    break;

                case "zad1":
                case "projection":
                    studentService.getByTitul("Mgr.").forEach(s ->
                            System.out.println(s.getMeno() + " " + s.getPriezvisko())
                    );
                    break;

                case "zad2db":
                case "zad2_db":
                    studentService.getByRokAProgram(1998, "MCH")
                            .forEach(s -> System.out.println(s.getMeno() + " " + s.getPriezvisko()));
                    break;

                case "index":
                    studentService.testIndex();
                    break;

                case "interactive":
                case "cli":
                    // run interactive REPL in the console
                    this.runInteractive(studentService);
                    break;

                case "zad4db":
                case "zad4_db":
                    System.out.printf("%-10s %-20s %s%n", "Rok", "Program", "Pocet");
                    System.out.println("-".repeat(40));
                    studentService.getPoctyStudentovByRokAndProgram()
                            .forEach(r -> System.out.printf("%-10d %-20s %d%n", r.rok(), r.skratka(), r.pocet()));
                    break;

                default:
                    System.out.println("Unknown task: " + task + ". Available: all, insert, printAll, zad1, zad2db, index, zad4db");
                    break;
            }
        };
    }

    private void runInteractive(StudentService studentService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===== INTERAKTIVNY REZIM =====");
        System.out.println("Moznost spustat ulohy podle vlastneho vyberu.");
        System.out.print("Zmazat a znovu vlozit data? (y/n): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("y")) {
            studentService.deleteAllStudents();
            studentService.insertAllStudents();
            System.out.println("OK - data su pripravene.\n");
        }

        while (true) {
            System.out.println();
            System.out.println("MENU ULOH:");
            System.out.println(" 1) Uloha 1: Projekcia - meno/priezvisko podla akademickeho titulu");
            System.out.println(" 2) Uloha 2: Studenti v danom roku a programu");
            System.out.println(" 3) Uloha 3: Test indexu (porovnanie casov)");
            System.out.println(" 4) Uloha 4: Pocty studentov podla rokov a programov");
            System.out.println(" q) Ukoncit");
            System.out.print("\nZadaj cislo ulohy (1-4) alebo 'q' na ukoncenie: ");
            String cmd = scanner.nextLine().trim();
            if (cmd.equalsIgnoreCase("q")) break;
            switch (cmd) {
                case "1":
                    System.out.print("Zadaj akademicky titul (napr Mgr., Bc., Dr.): ");
                    String titul = scanner.nextLine().trim();
                    if (!titul.isEmpty()) {
                        System.out.println("\nStudenti s titulom " + titul + ":");
                        var results = studentService.getByTitul(titul);
                        if (results.isEmpty()) {
                            System.out.println("  Nikoho sa nenaslo.");
                        } else {
                            results.forEach(s -> System.out.println("  " + s.getMeno() + " " + s.getPriezvisko()));
                        }
                    } else {
                        System.out.println("Chyba: Titul nemoze byt prazdny.");
                    }
                    break;
                case "2":
                    System.out.print("Zadaj akademicky rok (napr 1998): ");
                    String rokStr = scanner.nextLine().trim();
                    System.out.print("Zadaj skratku studijneho programu (napr MCH, INF): ");
                    String prog = scanner.nextLine().trim();
                    try {
                        int rok = Integer.parseInt(rokStr);
                        System.out.println("\nStudenti v roku " + rok + " na programe " + prog + ":");
                        var results = studentService.getByRokAProgram(rok, prog);
                        if (results.isEmpty()) {
                            System.out.println("  Nikoho sa nenaslo.");
                        } else {
                            results.forEach(s -> System.out.println("  " + s.getMeno() + " " + s.getPriezvisko()));
                        }
                    } catch (Exception e) {
                        System.out.println("Chyba: Neplatny rok " + rokStr);
                    }
                    break;
                case "3":
                    System.out.println("\nSpustavam test indexu...");
                    studentService.testIndex();
                    break;
                case "4":
                    System.out.println("\nPocty studentov podla rokov a studijnych programov:");
                    System.out.printf("%-10s %-20s %s%n", "Rok", "Program", "Pocet");
                    System.out.println("-".repeat(40));
                    studentService.getPoctyStudentovByRokAndProgram().forEach(r -> System.out.printf("%-10d %-20s %d%n", r.rok(), r.skratka(), r.pocet()));
                    break;
                default:
                    System.out.println("Chyba: Neznamy prikaz " + cmd + ". Skus znova.");
            }
        }
        System.out.println("\nUkoncenie interaktivneho rezimu.");
    }
}
