package be.seeseemelk.todo.services;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class TodoMigrationService
{
	@ConfigProperty(name = "quarkus.datasource.reactive.url")
	String datasourceUrl;

	@ConfigProperty(name = "quarkus.datasource.username")
	String datasourceUsername;

	@ConfigProperty(name = "quarkus.datasource.password")
	String datasourcePassword;

	public void runFlywayMigration(@Observes StartupEvent event)
	{
		Flyway.configure()
			.dataSource(getDatasourceUrl(), datasourceUsername, datasourcePassword)
			.load()
			.migrate();
	}

	private String getDatasourceUrl()
	{
		if (datasourceUrl.startsWith("vertx-reactive:"))
			return "jdbc:" + datasourceUrl.substring("vertx-reactive:".length());
		else
			return "jdbc:" + datasourceUrl;
	}
}
