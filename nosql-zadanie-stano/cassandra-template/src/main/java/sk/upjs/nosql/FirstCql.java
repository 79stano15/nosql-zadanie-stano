package sk.upjs.nosql;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DriverException;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.ResultSetExtractor;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.data.cassandra.core.cql.generator.CreateTableCqlGenerator;
import org.springframework.data.cassandra.core.cql.keyspace.CreateTableSpecification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
/* CREATE KEYSPACE template WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 }; */

public class FirstCql {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(CassandraConfig.class);
        CqlTemplate template = context.getBean(CqlTemplate.class);
        CqlSession session = context.getBean(CqlSession.class);
        template.execute("DROP TABLE IF EXISTS first");
        template.execute("CREATE TABLE IF NOt EXISTS first " +
                "(id uuid PRIMARY KEY, " +
                " value text)");
        for (int i = 0; i < 10; i++) {
            template.execute("INSERT INTO first (id, value) VALUES (?, ?)",
                    UUID.randomUUID(), "value" + i);
        }
        template.query("SELECT * FROM first", new RowMapper<Void>() {

            @Override
            public Void mapRow(Row row, int rowNum) throws DriverException {
                System.out.println("ID: " + row.getUuid("id") +
                                   " Value: " + row.getString("value"));
                return null;
            }
        });
        template.execute("DROP TABLE IF EXISTS second");
        CreateTableSpecification specification = CreateTableSpecification.createTable("second")
                .ifNotExists()
                .partitionKeyColumn("id_oddelenia", DataTypes.BIGINT)
                .clusteredKeyColumn("name", DataTypes.TEXT)
                .column("salary", DataTypes.DECIMAL);
        String cql = CreateTableCqlGenerator.toCql(specification);
        System.out.println(cql);
        template.execute(cql);

        PreparedStatement statement = session.prepare("INSERT INTO second (id_oddelenia, name, salary) VALUES (?, ?, ?)");
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            BoundStatement bound = statement.bind((long) (Math.random() * 3) + 1,
                    "clovek",
                    new BigDecimal(Math.random() * 4000 + 1000).setScale(2, RoundingMode.HALF_UP));
            template.execute(bound);
        }
        System.out.println("Po jednom: " + (System.nanoTime() - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        BatchStatementBuilder batchStatementBuilder = BatchStatement.builder(BatchType.LOGGED);
        for (int i = 0; i < 10; i++) {
            BoundStatement bound = statement.bind((long) (Math.random() * 3) + 1,
                    "clovek" + i,
                    new BigDecimal(Math.random() * 4000 + 1000).setScale(2, RoundingMode.HALF_UP));
            batchStatementBuilder.addStatement(bound);
        }
        template.execute(batchStatementBuilder.build());
        System.out.println("naraz: " + (System.nanoTime() - start) / 1_000_000 + " ms");

        template.query("SELECT * FROM second", new ResultSetExtractor<Void>() {
            @Override
            public Void extractData(ResultSet resultSet) throws DriverException, DataAccessException {
                resultSet.forEach(row -> System.out.println(row.getLong("id_oddelenia") + " " + row.getString("name")
                                  + " " + row.getBigDecimal("salary")));
                return null;
            }
        });
        session.close();
    }
}
