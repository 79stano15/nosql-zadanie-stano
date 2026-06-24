package sk.upjs.nosql;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Configuration
public class CassandraConfig {
    public static final String KEYSPACE = "template";
    @Bean
    public DriverConfigLoader loader() {
        return DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT
                        , Duration.of(1, ChronoUnit.MINUTES))
                .build();
    }
    @Bean
    public CqlSession cqlSession(DriverConfigLoader loader) {
        return CqlSession.builder()
                .withConfigLoader(loader)
                .withKeyspace(KEYSPACE)
                .build();
    }
    @Bean
    public CqlTemplate cqlTemplate(CqlSession cqlSession) {
        return new CqlTemplate(cqlSession);
    }
}
