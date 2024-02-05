package com.example.SpringBookStore;

//import com.example.SpringBookStore.storage.StorageProperties;
//import com.example.SpringBookStore.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
//@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication

public class SpringBookStoreApplication {

	public static void main(String[] args) {
		/*String command = "C:\\xampp\\mysql\\bin\\mysqld.exe";
		try{
			Process process = Runtime.getRuntime().exec(command);
			//Process process1 = Runtime.getRuntime().exec("c:\\xampp\\apache")
		}catch (IOException e){
			e.printStackTrace();
		}*/
		SpringApplication.run(SpringBookStoreApplication.class, args);
	}
	}
