/*    */ package BOOT-INF.classes.cloudshell.app.security;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.security.core.AuthenticationException;
/*    */ import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class CustomAuthEntryPoint
/*    */   extends BasicAuthenticationEntryPoint
/*    */ {
/*    */   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
/* 18 */     response.setStatus(401);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws Exception {
/* 23 */     setRealmName("CustomBasicAuth");
/* 24 */     super.afterPropertiesSet();
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/security/CustomAuthEntryPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */