package com.example.demoRealTimeNews;

import com.example.demoRealTimeNews.resources.NewsController;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.Random;

@SpringBootApplication
public class DemoRealTimeNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRealTimeNewsApplication.class, args);
	}
}
