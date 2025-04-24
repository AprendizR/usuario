package com.renan.usuario.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Instâncias de JwtUtil e UserDetailsService injetadas pelo Spring
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor para injeção de dependências de JwtUtil e UserDetailsService
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Configuração do filtro de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cria uma instância do JwtRequestFilter com JwtUtil e UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa proteção CSRF para APIs REST (não aplicável a APIs que não mantêm estado)
                .authorizeHttpRequests(authorize -> authorize
                        //Libera o swagger
                        .requestMatchers("/v3/api-docs/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        //Libera login e criação de usuario
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll() // Permite acesso ao endpoint POST /usuario sem autenticação
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll() // Permite acesso ao endpoint de login sem autenticação
                        .requestMatchers(HttpMethod.GET, "/usuario/endereco/**").permitAll()
                        //Protege os demais endpoints de usuario
                        .requestMatchers("/usuario/**").authenticated() // Requer autenticação para qualquer endpoint que comece com /usuario/
                        //Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de sessão como stateless (sem sessão)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT antes do filtro de autenticação padrão

        // Retorna a configuração do filtro de segurança construída
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs",           // JSON principal                     :contentReference[oaicite:0]{index=0}
                "/v3/api-docs/**",        // sub-recursos (schemas, config, etc.) :contentReference[oaicite:1]{index=1}
                "/swagger-ui.html",       // página HTML                         :contentReference[oaicite:2]{index=2}
                "/swagger-ui/**",         // JS/CSS da UI                        :contentReference[oaicite:3]{index=3}
                "/swagger-resources/**",  // recursos internos                   :contentReference[oaicite:4]{index=4}
                "/webjars/**"             // assets estáticos                    :contentReference[oaicite:5]{index=5}
        );
    }


    // Configura o PasswordEncoder para criptografar senhas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna uma instância de BCryptPasswordEncoder
    }

    // Configura o AuthenticationManager usando AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Obtém e retorna o AuthenticationManager da configuração de autenticação
        return authenticationConfiguration.getAuthenticationManager();
    }

}
