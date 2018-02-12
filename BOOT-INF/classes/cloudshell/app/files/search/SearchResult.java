/*    */ package BOOT-INF.classes.cloudshell.app.files.search;
/*    */ 
/*    */ import cloudshell.app.files.FileInfo;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SearchResult
/*    */ {
/*    */   private boolean isDone;
/*    */   private List<FileInfo> files;
/*    */   
/*    */   public SearchResult(boolean isDone, List<FileInfo> files) {
/* 28 */     this.files = new ArrayList<>();
/*    */     this.isDone = isDone;
/*    */     this.files = files;
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 34 */     return this.isDone;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDone(boolean isDone) {
/* 41 */     this.isDone = isDone;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FileInfo> getFiles() {
/* 48 */     return this.files;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFiles(List<FileInfo> files) {
/* 55 */     this.files = files;
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/search/SearchResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */