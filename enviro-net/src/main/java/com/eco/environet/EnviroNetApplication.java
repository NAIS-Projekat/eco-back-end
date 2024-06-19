package com.eco.environet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class EnviroNetApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnviroNetApplication.class, args);
	}

	@EventListener({ApplicationReadyEvent.class})
	public void applicationReadyEvent() {
		System.out.println("Application started ... launching browser now");
		browse("http://localhost:8080/swagger-ui/index.html");
	}

	public static void browse(String url) {
		if(Desktop.isDesktopSupported()){
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}else{
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
