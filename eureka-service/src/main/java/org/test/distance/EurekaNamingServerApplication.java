package org.test.distance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Spring boot Eureka naming server application
 * @author Viktar Lebedzeu
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaNamingServerApplication {

    public static void main(String... args) {
        SpringApplication.run(EurekaNamingServerApplication.class, args);
    }
}
