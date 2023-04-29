package ink.whi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SaiForumApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SaiForumApplication.class).allowCircularReferences(true).run(args);
    }
}
