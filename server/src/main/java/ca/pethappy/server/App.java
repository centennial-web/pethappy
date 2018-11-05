package ca.pethappy.server;

import ca.pethappy.server.security.models.TokenProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
public class App implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Bean
    @ConfigurationProperties(prefix = "security.token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }

    private final int serverPort;
    private final String springApplicationName;

    @Autowired
    public App(@Value("${server.port}") int serverPort,
               @Value("${spring.application.name}") String springApplicationName) {
        this.serverPort = serverPort;
        this.springApplicationName = springApplicationName;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("{} is started on port {}", springApplicationName, serverPort);
    }
}
