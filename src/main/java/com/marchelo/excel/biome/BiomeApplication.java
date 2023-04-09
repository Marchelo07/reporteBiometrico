package com.marchelo.excel.biome;

import com.marchelo.excel.biome.Service.LecturaBiometrico;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiomeApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BiomeApplication.class, args);
	}

	@Override
	public void run (ApplicationArguments args) throws Exception{
		LecturaBiometrico lecturaBiometrico = new LecturaBiometrico();
		lecturaBiometrico.lecturaArchivo();
	}

}
