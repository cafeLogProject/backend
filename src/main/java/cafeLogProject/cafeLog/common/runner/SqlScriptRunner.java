package cafeLogProject.cafeLog.common.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class SqlScriptRunner implements CommandLineRunner {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    // Define SQL scripts in order of execution
    private static final List<String> SQL_SCRIPTS = Arrays.asList(
            "sql/01_cafe.sql",
            "sql/02_user.sql",
            "sql/03_draft.sql",
            "sql/04_review.sql",
            "sql/05_follow.sql"
    );

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting SQL script execution...");
        boolean hasErrors = false;

        for (String scriptPath : SQL_SCRIPTS) {
            try {
                Resource resource = resourceLoader.getResource("classpath:" + scriptPath);

                if (!resource.exists()) {
                    log.error("SQL script not found: {}", scriptPath);
                    hasErrors = true;
                    continue;
                }

                log.info("Executing SQL script: {}", scriptPath);
                ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
                log.info("Successfully executed SQL script: {}", scriptPath);

            } catch (Exception e) {
                log.error("Error executing SQL script {}: {}", scriptPath, e.getMessage());
                hasErrors = true;
            }
        }

        if (hasErrors) {
            log.warn("SQL scripts execution completed with some errors");
        } else {
            log.info("All SQL scripts executed successfully");
        }
    }
}