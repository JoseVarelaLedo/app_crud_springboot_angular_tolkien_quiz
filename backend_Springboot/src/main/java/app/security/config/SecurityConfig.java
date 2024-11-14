package app.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import app.security.filter.JwtAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()             // sólo para pruebas!!!!!!!!!

                        .requestMatchers("/auth/login", "/register").permitAll()
                        .requestMatchers("/login").permitAll()

                        .requestMatchers("/categorias").permitAll()
                        .requestMatchers("/categorias/buscarCategoriaPorId/id/{id}").permitAll()
                        .requestMatchers("/categorias/noPag").permitAll()
                        .requestMatchers("/categorias/crearCategoria").hasRole("Administrador")
                        .requestMatchers("/categorias/editar/id/{id}").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/categorias/eliminar/id/{id}").hasRole("Administrador")

//                       .requestMatchers("/fichasUsuario").permitAll()
                        .requestMatchers("/fichasUsuario/lista").hasAnyRole("Administrador", "")
                        .requestMatchers("/fichasUsuario/insertarUsuarios").permitAll()
                        .requestMatchers("/fichasUsuario/buscarUsuarioPorId/id/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST,"/fichasUsuario/crearFichaDeUsuario").permitAll()
                        .requestMatchers("/fichasUsuario/editar/id/{id}").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/fichasUsuario/eliminar/id/{id}").hasRole("Administrador")

                        .requestMatchers("/preguntas").permitAll()
                        .requestMatchers("/preguntas/contenido/{textoPregunta}").permitAll()
                        .requestMatchers("/preguntas//categoria/{categoriaId}").permitAll()
                        .requestMatchers("/preguntas/buscarPreguntaPorId/id/{id}").permitAll()
                        .requestMatchers("/preguntas/crearPregunta").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/preguntas/editar/id/{id}").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/preguntas/eliminar/id/{id}").hasRole("Administrador")

                        .requestMatchers("/respuestas").permitAll()
                        .requestMatchers("/respuestas/buscarRespuestaPorId/id/{id}").permitAll()
                        .requestMatchers("/respuestas/crearRespuesta").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/respuestas/editar/id/{id}").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/respuestas/eliminar/id/{id}").hasRole("Administrador")

                        .requestMatchers("/roles"). permitAll()

                        .requestMatchers("/usuarios").permitAll()
                        .requestMatchers("/usuarios/buscarUsuarioPorId/id/{id}").permitAll()
                        .requestMatchers("/usuarios/porRol").permitAll()
                        .requestMatchers("/usuarios/buscarNickname").hasRole("Administrador")
                        .requestMatchers("/usuarios/buscarPorFecha").permitAll()
                        .requestMatchers("/usuarios/verificarNickname").permitAll()
                        .requestMatchers("/usuarios/crearUsuario").hasRole( "Administrador")
                        .requestMatchers("/usuarios/editar/id/{id}").hasRole("Administrador")
                        .requestMatchers("/usuarios/eliminar/id/{id}").hasRole("Administrador")


                        .requestMatchers("/admin/**").hasRole("Administrador")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));                  // Origen permitido
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));              // Métodos HTTP permitidos
        config.setAllowedHeaders(List.of("*"));                                     // Permitir todos los encabezados
        config.setAllowCredentials(true);                                              // Permite credenciales (cookies y JWT)
        source.registerCorsConfiguration("/**", config);                       // Aplica a todas las rutas
        return source;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}