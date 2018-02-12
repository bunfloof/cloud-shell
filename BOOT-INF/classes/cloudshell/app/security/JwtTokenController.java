/*    */ package BOOT-INF.classes.cloudshell.app.security;
/*    */ 
/*    */ import cloudshell.app.Application;
/*    */ import io.jsonwebtoken.Jwts;
/*    */ import java.security.Principal;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.web.bind.annotation.CrossOrigin;
/*    */ import org.springframework.web.bind.annotation.GetMapping;
/*    */ import org.springframework.web.bind.annotation.PostMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
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
/*    */ @RestController
/*    */ @CrossOrigin(allowCredentials = "true")
/*    */ @RequestMapping({"/api"})
/*    */ public class JwtTokenController
/*    */ {
/*    */   @PostMapping({"/token"})
/*    */   public Map<String, String> generateToken(Principal principal) {
/* 31 */     Map<String, String> map = new HashMap<>();
/*    */     
/* 33 */     String token = Jwts.builder().setSubject(principal.getName()).signWith(Application.SECRET_KEY).compact();
/* 34 */     map.put("token", token);
/* 35 */     map.put("posix", (
/* 36 */         !System.getProperty("os.name").toLowerCase().contains("windows") ? 1 : 0) + "");
/* 37 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   @PostMapping({"/auth"})
/*    */   public void checkToken() {}
/*    */   
/*    */   @GetMapping({"/token/temp"})
/*    */   public Map<String, String> generateTempToken(Principal principal) {
/* 46 */     Map<String, String> map = new HashMap<>();
/*    */ 
/*    */ 
/*    */     
/* 50 */     String token = Jwts.builder().setSubject(principal.getName()).setExpiration(new Date(System.currentTimeMillis() + 60000L)).signWith(Application.SECRET_KEY).compact();
/* 51 */     map.put("token", token);
/* 52 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/security/JwtTokenController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */