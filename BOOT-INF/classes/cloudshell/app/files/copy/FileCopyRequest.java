/*    */ package BOOT-INF.classes.cloudshell.app.files.copy;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class FileCopyRequest
/*    */ {
/*    */   private List<String> sourceFile;
/*    */   private String targetFolder;
/*    */   
/*    */   public List<String> getSourceFile() {
/* 20 */     return this.sourceFile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSourceFile(List<String> sourceFile) {
/* 27 */     this.sourceFile = sourceFile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetFolder() {
/* 34 */     return this.targetFolder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTargetFolder(String targetFolder) {
/* 41 */     this.targetFolder = targetFolder;
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/copy/FileCopyRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */