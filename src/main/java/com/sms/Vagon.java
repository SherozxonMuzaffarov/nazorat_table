package com.sms;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Vagon {

	public static void main(String[] args) {
//		SpringApplication.run(Vagon.class, args);
		
        ApplicationContext contexto = new SpringApplicationBuilder(Vagon.class)
                .web(WebApplicationType.SERVLET)          
                .headless(false)
                .run(args);
	}  

}
