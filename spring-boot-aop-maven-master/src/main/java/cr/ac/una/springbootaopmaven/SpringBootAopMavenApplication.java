package cr.ac.una.springbootaopmaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cr.ac.una.springbootaopmaven", "service.impl"})  // Agrega aquí el paquete donde se encuentra TareaServiceImpl
public class SpringBootAopMavenApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAopMavenApplication.class, args);
    }
}