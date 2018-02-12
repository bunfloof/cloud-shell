/*     */ package BOOT-INF.classes.cloudshell.app.files.copy;
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
/*     */ public class FileCopyProgressResponse
/*     */ {
/*     */   private int progress;
/*     */   private String status;
/*     */   private String name;
/*     */   private String errors;
/*     */   private boolean hasErrors;
/*     */   private String id;
/*     */   
/*     */   public FileCopyProgressResponse(String id, String name, int progress, String status, String errors, boolean hasErrors) {
/*  27 */     this.id = id;
/*  28 */     this.name = name;
/*  29 */     this.progress = progress;
/*  30 */     this.status = status;
/*  31 */     this.errors = errors;
/*  32 */     this.hasErrors = hasErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getProgress() {
/*  39 */     return this.progress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProgress(int progress) {
/*  46 */     this.progress = progress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/*  53 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/*  60 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrors() {
/*  67 */     return this.errors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrors(String errors) {
/*  74 */     this.errors = errors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHasErrors() {
/*  81 */     return this.hasErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasErrors(boolean hasErrors) {
/*  88 */     this.hasErrors = hasErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  95 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 102 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 109 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 116 */     this.name = name;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/copy/FileCopyProgressResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */