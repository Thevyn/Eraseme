package esito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

    @Autowired
    DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
