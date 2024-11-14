package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // url del frontend, así lo conectamos
                .allowedMethods("GET", "POST", "PUT", "DELETE") // qué operaciones http se van a permitir
                .allowedHeaders("*") // no aplicamos restricciones a los encabezados
                .allowCredentials(true); // permitir cookies
    }

    @Bean(name = "customCorsConfigurationSource")
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));  // Origen del frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));  // Métodos HTTP
        config.setAllowedHeaders(List.of("*"));  // Permitir todos los encabezados
        config.setAllowCredentials(true);  // Permite credenciales (cookies, tokens)
        source.registerCorsConfiguration("/**", config);  // Aplica a todas las rutas
        return source;
    }
}
