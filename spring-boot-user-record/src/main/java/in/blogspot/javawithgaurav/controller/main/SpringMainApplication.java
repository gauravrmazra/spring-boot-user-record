package in.blogspot.javawithgaurav.controller.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringApplicationConfiguration
@ComponentScan(basePackages={"in.blogspot.javawithgaurav.contoller", "in.blogspot.javawithgaurav.service"})
public class SpringMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMainApplication.class, args);
    }
}
