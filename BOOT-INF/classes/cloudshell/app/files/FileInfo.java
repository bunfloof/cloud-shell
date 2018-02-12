/*     */ package BOOT-INF.classes.cloudshell.app.files;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ public class FileInfo
/*     */ {
/*     */   private String name;
/*     */   private String path;
/*     */   private String permissionString;
/*     */   private String user;
/*     */   private String type;
/*     */   private long size;
/*     */   private Date lastModified;
/*     */   private boolean posix;
/*     */   
/*     */   public FileInfo(String name, String path, String permissionString, String user, String type, long size, long permission, Date lastModified, boolean posix) {
/*  33 */     this.name = name;
/*  34 */     this.path = path;
/*  35 */     this.permissionString = permissionString;
/*  36 */     this.user = user;
/*  37 */     this.type = type;
/*  38 */     this.size = size;
/*  39 */     this.lastModified = lastModified;
/*  40 */     this.posix = posix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  47 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  54 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/*  61 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(String path) {
/*  68 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPermissionString() {
/*  75 */     return this.permissionString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPermissionString(String permissionString) {
/*  82 */     this.permissionString = permissionString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/*  89 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String user) {
/*  96 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 103 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/* 110 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 117 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 124 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getLastModified() {
/* 131 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModified(Date lastModified) {
/* 138 */     this.lastModified = lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPosix() {
/* 145 */     return this.posix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosix(boolean posix) {
/* 152 */     this.posix = posix;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/FileInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */