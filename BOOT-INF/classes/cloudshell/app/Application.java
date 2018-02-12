/*     */ package BOOT-INF.classes.cloudshell.app;
/*     */ 
/*     */ import cloudshell.app.config.ConfigManager;
/*     */ import cloudshell.app.security.CustomAuthEntryPoint;
/*     */ import cloudshell.app.security.JwtAuthorizationFilter;
/*     */ import cloudshell.app.terminal.TerminalWebsocketHandler;
/*     */ import io.jsonwebtoken.SignatureAlgorithm;
/*     */ import io.jsonwebtoken.security.Keys;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.servlet.Filter;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.CommandLineRunner;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.security.authentication.AuthenticationProvider;
/*     */ import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
/*     */ import org.springframework.security.config.http.SessionCreationPolicy;
/*     */ import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/*     */ import org.springframework.security.web.AuthenticationEntryPoint;
/*     */ import org.springframework.web.servlet.config.annotation.CorsRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*     */ import org.springframework.web.socket.WebSocketHandler;
/*     */ import org.springframework.web.socket.config.annotation.EnableWebSocket;
/*     */ import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
/*     */ import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @EnableWebSecurity
/*     */ @EnableWebSocket
/*     */ @SpringBootApplication
/*     */ public class Application
/*     */   extends WebSecurityConfigurerAdapter
/*     */   implements WebSocketConfigurer, WebMvcConfigurer, CommandLineRunner
/*     */ {
/*     */   @Autowired
/*     */   private Environment env;
/*     */   
/*     */   static {
/*  50 */     Security.addProvider((Provider)new BouncyCastleProvider());
/*  51 */     ConfigManager.checkAndConfigureSSL();
/*     */   }
/*     */ 
/*     */   
/*  55 */   public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
/*     */   
/*     */   @Autowired
/*     */   private CustomAuthEntryPoint auth;
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  61 */     SpringApplication.run(cloudshell.app.Application.class, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
/*  66 */     System.out.println("Registered ws");
/*  67 */     registry.addHandler((WebSocketHandler)new TerminalWebsocketHandler(), new String[] { "/term*"
/*  68 */         }).setAllowedOrigins(new String[] { "*" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCorsMappings(CorsRegistry registry) {
/*  73 */     registry.addMapping("/**");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configure(HttpSecurity http) throws Exception {
/*  78 */     ((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((HttpSecurity)((HttpSecurity)http.httpBasic().authenticationEntryPoint((AuthenticationEntryPoint)this.auth).and()).cors().and())
/*  79 */       .csrf().disable()).authorizeRequests().antMatchers(new String[] { "/term**"
/*  80 */         })).permitAll().and()).authorizeRequests().antMatchers(new String[] { "/bin/**"
/*  81 */         })).permitAll().and()).authorizeRequests().antMatchers(new String[] { "/api/**"
/*  82 */         })).authenticated().and()).authorizeRequests().antMatchers(new String[] { "/**"
/*  83 */         })).permitAll().and())
/*  84 */       .addFilter((Filter)new JwtAuthorizationFilter(authenticationManager()))
/*  85 */       .sessionManagement()
/*  86 */       .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public BCryptPasswordEncoder passwordEncoder() {
/*  91 */     return new BCryptPasswordEncoder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(AuthenticationManagerBuilder auth) throws Exception {
/*  97 */     Object object = new Object(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     auth.authenticationProvider((AuthenticationProvider)object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String... args) throws Exception {
/* 139 */     ConfigManager.loadUserDetails(this.env);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/Application.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */