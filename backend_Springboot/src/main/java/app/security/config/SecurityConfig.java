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

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     *
     * <p>Este método define las políticas de autorización y autenticación utilizando
     * {@link HttpSecurity}. Establece reglas específicas para las rutas de la API,
     * gestiona el uso de CORS, deshabilita CSRF para la API REST y configura la autenticación
     * basada en JWT.</p>
     *
     * @param http una instancia de {@link HttpSecurity} utilizada para configurar la seguridad HTTP.
     * @return un bean de tipo {@link SecurityFilterChain} que contiene la configuración de seguridad definida.
     * @throws Exception si ocurre algún error durante la configuración.
     *
     * <p>Características de la configuración:
     * <ul>
     *   <li><b>CORS:</b> Se habilita y utiliza la configuración definida en el método {@code corsConfigurationSource()}.</li>
     *   <li><b>Rutas públicas:</b> Las siguientes rutas son accesibles sin autenticación:
     *     <ul>
     *       <li><code>/auth/login</code> y <code>/register</code>: Rutas de autenticación y registro.</li>
     *       <li>Varias rutas relacionadas con <code>/categorias</code>, <code>/preguntas</code>,
     *       <code>/respuestas</code>, <code>/roles</code>, y <code>/usuarios</code>, según las reglas de negocio.</li>
     *     </ul>
     *   </li>
     *   <li><b>Restricciones por roles:</b> Algunas rutas requieren permisos específicos, por ejemplo:
     *     <ul>
     *       <li>Rutas para crear, editar o eliminar recursos como categorías, preguntas, respuestas, y usuarios
     *       requieren roles como <code>Gestor BD</code> o <code>Administrador</code>.</li>
     *       <li>Las rutas bajo <code>/admin/**</code> están reservadas exclusivamente para administradores.</li>
     *     </ul>
     *   </li>
     *   <li><b>CSRF:</b> Deshabilitado, ya que la API utiliza un enfoque basado en tokens JWT para la autenticación.</li>
     *   <li><b>Sesión:</b> Configurada como <code>STATELESS</code> para evitar el manejo de sesiones
     *   en un entorno de API REST.</li>
     *   <li><b>JWT:</b> Se añade un filtro personalizado (<code>jwtAuthenticationFilter</code>) antes del
     *   filtro estándar <code>UsernamePasswordAuthenticationFilter</code>.</li>
     *   <li><b>Autenticación por defecto:</b> Todas las demás rutas no especificadas requieren autenticación.</li>
     * </ul>
     * </p>
     */
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
                        .requestMatchers("/categorias/crearCategoria").hasAnyRole("Gestor BD","Administrador")
                        .requestMatchers("/categorias/editar/id/{id}").hasAnyRole("Gestor BD", "Administrador")
                        .requestMatchers("/categorias/eliminar/id/{id}").hasRole("Administrador")

                        .requestMatchers("/fichasUsuario/lista").hasAnyRole("Administrador", "Gestor BD")
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

    /**
     * Configura la fuente de configuración de CORS para la aplicación.
     *
     * <p>Este método define las reglas de Cross-Origin Resource Sharing (CORS), que permiten o restringen
     * las solicitudes HTTP desde dominios externos. Es particularmente útil en aplicaciones
     * web que necesitan comunicarse con un backend desde un frontend alojado en un dominio diferente.</p>
     *
     * @return una instancia de {@link CorsConfigurationSource} con las reglas configuradas.
     *
     * <p>Detalles de configuración:
     * <ul>
     *   <li><b>setAllowedOrigins:</b> Permite solicitudes desde el dominio <code>http://localhost:4200</code>,
     *   que corresponde al frontend de la aplicación.</li>
     *   <li><b>setAllowedMethods:</b> Se permiten los métodos HTTP <code>GET</code>, <code>POST</code>,
     *   <code>PUT</code> y <code>DELETE</code>.</li>
     *   <li><b>setAllowedHeaders:</b> Permite cualquier encabezado en las solicitudes para mayor flexibilidad.</li>
     *   <li><b>setAllowCredentials:</b> Habilita el uso de credenciales (cookies, tokens JWT) en las solicitudes entre dominios.</li>
     *   <li><b>registerCorsConfiguration:</b> Aplica las configuraciones de CORS a todas las rutas del backend
     *   (<code>/</code>).</li>
     * </ul>
     * </p>
     */
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

    /**
     * Configura el gestor de autenticación para la aplicación.
     *
     * <p>Este método crea un bean de tipo {@link AuthenticationManager}, que es el componente responsable
     * de autenticar las solicitudes entrantes en el sistema. La configuración se basa en un servicio de
     * detalles de usuario personalizado y un codificador de contraseñas.</p>
     *
     * @param http una instancia de {@link HttpSecurity} utilizada para compartir configuraciones
     *             relacionadas con la seguridad.
     * @return una instancia de {@link AuthenticationManager} configurada con el servicio de detalles
     *         del usuario y el codificador de contraseñas.
     * @throws Exception si ocurre algún error durante la construcción del gestor de autenticación.
     *
     * <p>Detalles de configuración:
     * <ul>
     *   <li><b>userDetailsService:</b> Se utiliza un servicio personalizado que implementa {@link UserDetailsService}
     *   para cargar los detalles de los usuarios desde el sistema.</li>
     *   <li><b>passwordEncoder:</b> Define el codificador de contraseñas a utilizar,
     *   permitiendo la verificación de contraseñas encriptadas.</li>
     * </ul>
     * </p>
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}