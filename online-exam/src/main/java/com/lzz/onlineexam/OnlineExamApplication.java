package com.lzz.onlineexam;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableSwagger2Doc
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.lzz.onlineexam.dao")
//@EnableGlobalMethodSecurity(securedEnabled=true , prePostEnabled=true)
public class OnlineExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineExamApplication.class, args);
    }

}
