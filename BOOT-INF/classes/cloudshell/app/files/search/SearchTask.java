/*     */ package BOOT-INF.classes.cloudshell.app.files.search;
/*     */ 
/*     */ import cloudshell.app.files.FileInfo;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ 
/*     */ public class SearchTask
/*     */   implements Runnable
/*     */ {
/*     */   private boolean posix;
/*  37 */   private String id = UUID.randomUUID().toString(); public SearchTask(String folder, String searchText) {
/*  38 */     if (this.folder == null || this.folder.length() < 1) {
/*  39 */       this.folder = System.getProperty("user.home");
/*     */     } else {
/*  41 */       this.folder = folder;
/*     */     } 
/*     */     
/*  44 */     this.searchText = searchText.toLowerCase(Locale.ENGLISH);
/*  45 */     this
/*  46 */       .posix = !System.getProperty("os.name").toLowerCase().contains("windows");
/*     */   }
/*     */ 
/*     */   
/*  50 */   private AtomicBoolean isDone = new AtomicBoolean(false);
/*     */   
/*  52 */   private List<FileInfo> files = Collections.synchronizedList(new ArrayList<>());
/*     */   private String folder;
/*     */   private String searchText;
/*  55 */   private AtomicBoolean stopRequested = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  61 */     return this.isDone.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDone(boolean isDone) {
/*  68 */     this.isDone.set(isDone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FileInfo> getFiles() {
/*  75 */     return this.files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiles(List<FileInfo> files) {
/*  82 */     this.files = files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicBoolean getStopRequested() {
/*  89 */     return this.stopRequested;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  96 */     System.out.println("Stopping...");
/*  97 */     this.stopRequested.set(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 103 */       System.out.println("Search start");
/* 104 */       Files.walkFileTree(Paths.get(this.folder, new String[0]), (FileVisitor<? super Path>)new Object(this));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 166 */     catch (Exception e) {
/*     */       
/* 168 */       e.printStackTrace();
/*     */     } 
/* 170 */     System.out.println("Search finished");
/* 171 */     this.isDone.set(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSearchText() {
/* 178 */     return this.searchText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSearchText(String searchText) {
/* 185 */     this.searchText = searchText.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFolder() {
/* 192 */     return this.folder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFolder(String folder) {
/* 199 */     this.folder = folder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 206 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 213 */     this.id = id;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/search/SearchTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */