package com.nvent.qops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class App 
{
    public static void main( String[] args )
    {
		SpringApplication.run(App.class, args);
    }
}
