/*    */ package BOOT-INF.classes.cloudshell.app.security;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Properties;
/*    */ import org.springframework.security.core.GrantedAuthority;
/*    */ import org.springframework.security.core.authority.SimpleGrantedAuthority;
/*    */ import org.springframework.security.core.userdetails.User;
/*    */ import org.springframework.security.core.userdetails.UserDetails;
/*    */ import org.springframework.security.core.userdetails.UserDetailsService;
/*    */ import org.springframework.security.core.userdetails.UsernameNotFoundException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebshellUserDetailsService
/*    */   implements UserDetailsService
/*    */ {
/* 27 */   Properties users = new Properties();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WebshellUserDetailsService() {
/* 33 */     loadProperties();
/*    */   }
/*    */   
/*    */   private void loadProperties() {
/* 37 */     this.users.clear();
/* 38 */     String propertyPath = System.getenv("easy-web-shell.config-path");
/* 39 */     if (propertyPath == null) {
/* 40 */       propertyPath = System.getProperty("user.home");
/*    */     }
/*    */     
/* 43 */     File f = new File(propertyPath, ".users");
/*    */     
/* 45 */     if (f.exists()) {
/* 46 */       try { InputStream in = new FileInputStream(f); 
/* 47 */         try { this.users.load(in);
/* 48 */           in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (FileNotFoundException e)
/*    */       
/* 50 */       { e.printStackTrace(); }
/* 51 */       catch (IOException e)
/*    */       
/* 53 */       { e.printStackTrace(); }
/*    */     
/*    */     }
/*    */     
/* 57 */     if (this.users.size() < 1) {
/* 58 */       this.users.put("admin", "admin");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
/* 65 */     if (!this.users.containsKey(username)) {
/* 66 */       return null;
/*    */     }
/* 68 */     String password = this.users.getProperty(username);
/* 69 */     List<GrantedAuthority> authorities = new ArrayList<>();
/* 70 */     authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
/* 71 */     return (UserDetails)new User(username, password, authorities);
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/security/WebshellUserDetailsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */