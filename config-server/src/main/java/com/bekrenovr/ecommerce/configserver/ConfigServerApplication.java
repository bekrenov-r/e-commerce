package com.bekrenovr.ecommerce.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ConfigServerApplication.class);
		app.setDefaultProperties(getDefaultProperties());
		app.run(args);
	}

	private static Properties getDefaultProperties() {
		try {
			String key = new String(ConfigServerApplication.class.getResourceAsStream("/key.txt").readAllBytes());
			Properties p = new Properties();
			p.put("encrypt.key", key);
			return p;
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
