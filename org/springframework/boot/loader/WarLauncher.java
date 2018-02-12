/*    */ package org.springframework.boot.loader;
/*    */ 
/*    */ import org.springframework.boot.loader.archive.Archive;
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
/*    */ public class WarLauncher
/*    */   extends ExecutableArchiveLauncher
/*    */ {
/*    */   private static final String WEB_INF = "WEB-INF/";
/*    */   private static final String WEB_INF_CLASSES = "WEB-INF/classes/";
/*    */   private static final String WEB_INF_LIB = "WEB-INF/lib/";
/*    */   private static final String WEB_INF_LIB_PROVIDED = "WEB-INF/lib-provided/";
/*    */   
/*    */   public WarLauncher() {}
/*    */   
/*    */   protected WarLauncher(Archive archive) {
/* 43 */     super(archive);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isNestedArchive(Archive.Entry entry) {
/* 48 */     if (entry.isDirectory()) {
/* 49 */       return entry.getName().equals("WEB-INF/classes/");
/*    */     }
/*    */     
/* 52 */     return (entry.getName().startsWith("WEB-INF/lib/") || entry
/* 53 */       .getName().startsWith("WEB-INF/lib-provided/"));
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 58 */     (new WarLauncher()).launch(args);
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/WarLauncher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */