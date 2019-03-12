package org.test.distance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Viktar Lebedzeu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ManagementApplication {
    public static void main(String... args) {
        SpringApplication.run(ManagementApplication.class);
    }
}
