package org.jeepay.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages={"org.jeepay"})
public class JeePayPayAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(JeePayPayAppliaction.class, args);
    }
}
