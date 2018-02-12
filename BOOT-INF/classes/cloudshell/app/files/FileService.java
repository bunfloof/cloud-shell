/*     */ package BOOT-INF.classes.cloudshell.app.files;
/*     */ 
/*     */ import cloudshell.app.files.FileInfo;
/*     */ import cloudshell.app.files.FileTypeDetector;
/*     */ import cloudshell.app.files.SearchOperations;
/*     */ import cloudshell.app.files.search.SearchResult;
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service
/*     */ public class FileService
/*     */ {
/*  37 */   private boolean posix = !System.getProperty("os.name").toLowerCase().contains("windows");
/*     */   
/*     */   @Autowired
/*     */   private SearchOperations searchOp;
/*     */   
/*     */   @Autowired
/*     */   private FileTypeDetector typeDetector;
/*     */ 
/*     */   
/*     */   public String createSearch(String folder, String searchText) {
/*  47 */     return this.searchOp.createSearchTask(folder, searchText);
/*     */   }
/*     */   
/*     */   public void cancelSearch(String id) {
/*  51 */     this.searchOp.cancelSearch(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public SearchResult getSearchResult(String id, int fileIndex, int folderIndex) {
/*  56 */     return this.searchOp.getSearchResult(id, fileIndex, folderIndex);
/*     */   }
/*     */   
/*     */   private String getFileType(File f) {
/*  60 */     return this.typeDetector.getType(f);
/*     */   }
/*     */   
/*     */   public List<FileInfo> list(String path) {
/*  64 */     System.out.println("Listing file: " + path);
/*  65 */     File[] files = (new File(path)).listFiles();
/*  66 */     List<FileInfo> list = new ArrayList<>();
/*  67 */     if (files == null) {
/*  68 */       return list;
/*     */     }
/*  70 */     for (File f : files) {
/*     */ 
/*     */       
/*  73 */       FileInfo info = new FileInfo(f.getName(), f.getAbsolutePath(), null, null, f.isDirectory() ? "Directory" : getFileType(f), f.length(), -1L, new Date(f.lastModified()), this.posix);
/*  74 */       Set<PosixFilePermission> filePerm = null;
/*     */       try {
/*  76 */         filePerm = Files.getPosixFilePermissions(f.toPath(), new java.nio.file.LinkOption[0]);
/*  77 */         String permission = PosixFilePermissions.toString(filePerm);
/*  78 */         info.setPermissionString(permission);
/*     */         
/*  80 */         FileOwnerAttributeView fv = Files.<FileOwnerAttributeView>getFileAttributeView(f
/*  81 */             .toPath(), FileOwnerAttributeView.class, new java.nio.file.LinkOption[0]);
/*  82 */         info.setUser(fv.getOwner().getName());
/*  83 */       } catch (Exception e) {
/*     */         
/*  85 */         info.setPermissionString("---");
/*     */       } 
/*     */       
/*  88 */       list.add(info);
/*     */     } 
/*  90 */     list.sort((a, b) -> {
/*     */           String type1 = a.getType();
/*     */           String type2 = b.getType();
/*  93 */           return (type1.equals("Directory") && type2.equals("Directory")) ? 0 : (type1.equals("Directory") ? -1 : (type2.equals("Directory") ? 1 : 0));
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     return list;
/*     */   }
/*     */   
/*     */   public void setText(String path, String text) throws Exception {
/* 107 */     Files.writeString(Paths.get(path, new String[0]), text, new java.nio.file.OpenOption[0]);
/*     */   }
/*     */   
/*     */   public String getText(String path) throws Exception {
/* 111 */     return new String(Files.readAllBytes(Paths.get(path, new String[0])), "utf-8");
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/FileService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */