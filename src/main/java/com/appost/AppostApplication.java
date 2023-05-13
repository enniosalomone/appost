package com.appost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication // (exclude = {AppostApplication.class })
@ServletComponentScan
public class AppostApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppostApplication.class, args);
	}

}
