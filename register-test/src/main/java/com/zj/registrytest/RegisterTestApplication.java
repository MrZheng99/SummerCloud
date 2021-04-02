package com.zj.registrytest;
import com.zj.base.annotation.ZRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zj
 * @date 2020/12/30 17:02
 */
@SpringBootApplication
@ZRegistry
public class RegisterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterTestApplication.class);
    }
}
