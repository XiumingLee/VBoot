package cn.xiuminglee.vboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author xiuming
 */
@SpringBootApplication
@EnableCaching
public class VBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(VBootApplication.class, args);
    }
}
