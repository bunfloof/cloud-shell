/*    */ package BOOT-INF.classes.cloudshell.app;
/*    */ 
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ 
/*    */ @Controller
/*    */ public class SinglePageAppController {
/*    */   @RequestMapping({"/", "/app/**", "/login"})
/*    */   public String index() {
/* 10 */     return "/index.html";
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/SinglePageAppController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */