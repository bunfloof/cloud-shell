/*    */ package BOOT-INF.classes.cloudshell.app.security;
/*    */ 
/*    */ import cloudshell.app.Application;
/*    */ import io.jsonwebtoken.Claims;
/*    */ import io.jsonwebtoken.Jws;
/*    */ import io.jsonwebtoken.Jwts;
/*    */ import java.io.IOException;
/*    */ import java.util.Collections;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.security.authentication.AuthenticationManager;
/*    */ import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.core.context.SecurityContextHolder;
/*    */ import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JwtAuthorizationFilter
/*    */   extends BasicAuthenticationFilter
/*    */ {
/*    */   public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
/* 34 */     super(authenticationManager);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
/* 41 */     UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
/*    */     
/* 43 */     if (authentication == null) {
/* 44 */       filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
/* 49 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*    */   }
/*    */ 
/*    */   
/*    */   private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
/* 54 */     String token = request.getHeader("Authorization");
/* 55 */     if (token != null && token.length() > 0 && token.startsWith("Bearer")) {
/*    */       
/*    */       try {
/*    */         
/* 59 */         Jws<Claims> parsedToken = Jwts.parser().setSigningKey(Application.SECRET_KEY).parseClaimsJws(token.replace("Bearer ", ""));
/*    */         
/* 61 */         String username = ((Claims)parsedToken.getBody()).getSubject();
/*    */         
/* 63 */         if (username != null && username.length() > 0) {
/* 64 */           return new UsernamePasswordAuthenticationToken(System.getProperty("app.default-user"), null, 
/* 65 */               Collections.emptyList());
/*    */         }
/* 67 */       } catch (Exception exception) {
/* 68 */         exception.printStackTrace();
/*    */       } 
/*    */     }
/*    */     
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/security/JwtAuthorizationFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */