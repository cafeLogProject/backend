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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class SqlScriptRunner implements CommandLineRunner {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;
    private final FollowDataInitializer followDataInitializer;

    private static final List<String> TABLES = Arrays.asList(
        "cafe_db",
        "user_tb", 
        "draft_review_tb",
        "review_tb",
        "follow_tb"
    );

    private static final List<String> SQL_SCRIPTS = Arrays.asList(
        "sql/01_cafe.sql",
        "sql/02_user.sql", 
        "sql/03_draft.sql",
        "sql/04_review.sql"
        // "sql/05_follow.sql" // follow 관계는 서비스를 직접 호출해서 설정
    );

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking if tables are empty...");
        
        if (!areTablesEmpty()) {
            log.info("Tables already contain data. Skipping SQL script execution.");
            return;
        }

        log.info("All tables are empty. Starting SQL script execution...");
        try {
            executeScripts();
            log.info("SQL scripts executed successfully. Initializing follow relationships...");
            followDataInitializer.initialize();
        } catch (Exception e) {
            log.error("Initialization failed: {}", e.getMessage());
            throw e;
        }
    }

    private boolean areTablesEmpty() {
        try (Connection conn = dataSource.getConnection()) {
            for (String table : TABLES) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + table)) {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        log.info("Table {} is not empty", table);
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            log.error("Error checking tables: {}", e.getMessage());
            return false;
        }
    }

    private void executeScripts() {
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