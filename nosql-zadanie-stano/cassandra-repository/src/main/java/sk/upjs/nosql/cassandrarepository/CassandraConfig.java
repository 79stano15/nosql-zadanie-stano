package sk.upjs.nosql.cassandrarepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@EnableCassandraRepositories
@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    /* CREATE KEYSPACE student WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 }; */

    @Override
    protected String getKeyspaceName() {
        return "student";
    }
}
