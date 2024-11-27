package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Clase de configuración para mapeado CORS backend-frontend. Se anota con @Configuration para que el sistema la maneje como tal.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura los mapeos de CORS (Cross-Origin Resource Sharing) para la aplicación.
     *
     * <p>Este método permite definir qué dominios, métodos HTTP, encabezados y configuraciones
     * relacionadas con cookies están permitidos cuando se interactúa con el backend desde
     * otras fuentes (por ejemplo, un frontend).</p>
     *
     * @param registry el objeto {@link CorsRegistry} que se utiliza para registrar
     *                 configuraciones de CORS en la aplicación.
     *
     * <p>Configuraciones aplicadas:
     * <ul>
     *   <li><b>allowedOrigins:</b> Permite solicitudes desde <code>http://localhost:4200</code>,
     *   que corresponde al dominio donde se ejecuta el frontend.</li>
     *   <li><b>allowedMethods:</b> Se permite el uso de los métodos HTTP <code>GET</code>,
     *   <code>POST</code>, <code>PUT</code> y <code>DELETE</code>.</li>
     *   <li><b>allowedHeaders:</b> No se aplican restricciones sobre los encabezados
     *   enviados en las solicitudes.</li>
     *   <li><b>allowCredentials:</b> Se permite el uso de cookies para autenticar solicitudes.</li>
     * </ul>
     * </p>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // url del frontend, así lo conectamos
                .allowedMethods("GET", "POST", "PUT", "DELETE") // qué operaciones http se van a permitir
                .allowedHeaders("*") // no aplicamos restricciones a los encabezados
                .allowCredentials(true); // permitir cookies
    }

    /**
     * Define una configuración personalizada de CORS para la aplicación.
     *
     * <p>Este método crea un bean de tipo {@link UrlBasedCorsConfigurationSource}
     * que contiene las configuraciones necesarias para permitir solicitudes
     * entre dominios (CORS) desde el frontend al backend.</p>
     *
     * @return una instancia de {@link UrlBasedCorsConfigurationSource} con las configuraciones de CORS aplicadas.
     *
     * <p>Detalles de configuración:
     * <ul>
     *   <li><b>setAllowedOrigins:</b> Se permite el origen <code>http://localhost:4200</code>, que corresponde
     *   a la URL del frontend.</li>
     *   <li><b>setAllowedMethods:</b> Se permiten los métodos HTTP <code>GET</code>, <code>POST</code>,
     *   <code>PUT</code> y <code>DELETE</code>.</li>
     *   <li><b>setAllowedHeaders:</b> Se permite cualquier encabezado en las solicitudes.</li>
     *   <li><b>setAllowCredentials:</b> Se habilita el uso de credenciales, como cookies o tokens
     *   de autenticación.</li>
     *   <li><b>registerCorsConfiguration:</b> Aplica las configuraciones a todas las rutas del backend (<code>/</code>).</li>
     * </ul>
     * </p>
     */
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
