/*    */ package BOOT-INF.classes.cloudshell.app.files;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.tika.Tika;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FileTypeDetector
/*    */ {
/* 24 */   private Tika tika = new Tika();
/*    */ 
/*    */   
/*    */   public synchronized String getType(File file) {
/*    */     try {
/* 29 */       return this.tika.detect(file);
/* 30 */     } catch (IOException e) {
/*    */       
/* 32 */       e.printStackTrace();
/* 33 */       return "application/octet-stream";
/*    */     } 
/*    */   }
/*    */   
/*    */   public synchronized String getTypeByName(String file) {
/* 38 */     return this.tika.detect(file);
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/FileTypeDetector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */