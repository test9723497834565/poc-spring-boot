package com.example.test1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class Test1Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Test1Application.class);
		app.addListeners(new ApplicationPidFileWriter()); // creates a file with the PID number of the JVM
		app.run(args);
	}
	
	
}
