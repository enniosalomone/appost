package com.appost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // (exclude = {AppostApplication.class })
@ServletComponentScan
//@ComponentScan({"com.appost.Controller"})
public class AppostApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppostApplication.class, args);
	}

}
