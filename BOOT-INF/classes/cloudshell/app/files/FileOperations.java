/*     */ package BOOT-INF.classes.cloudshell.app.files;
/*     */ 
/*     */ import cloudshell.app.files.PosixPermission;
/*     */ import cloudshell.app.files.copy.FileCopyProgressResponse;
/*     */ import cloudshell.app.files.copy.FileCopyTask;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.PosixFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFileAttributes;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class FileOperations
/*     */ {
/*  35 */   private final Map<String, FileCopyTask> pendingOperations = new ConcurrentHashMap<>();
/*  36 */   private final ExecutorService threadPool = Executors.newFixedThreadPool(5);
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosixPermission(String file, PosixPermission perm) throws Exception {
/*  41 */     Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(Paths.get(file, new String[0]), new java.nio.file.LinkOption[0]);
/*     */     
/*  43 */     if (perm.isExecutable()) {
/*  44 */       permissions.add(PosixFilePermission.OWNER_EXECUTE);
/*     */     } else {
/*  46 */       permissions.remove(PosixFilePermission.OWNER_EXECUTE);
/*     */     } 
/*     */     
/*  49 */     if ("READ_WRITE".equals(perm.getOwnerAccess())) {
/*  50 */       permissions.add(PosixFilePermission.OWNER_READ);
/*  51 */       permissions.add(PosixFilePermission.OWNER_WRITE);
/*  52 */     } else if ("READ_ONLY".equals(perm.getOwnerAccess())) {
/*  53 */       permissions.add(PosixFilePermission.OWNER_READ);
/*  54 */       permissions.remove(PosixFilePermission.OWNER_WRITE);
/*  55 */     } else if ("WRITE_ONLY".equals(perm.getOwnerAccess())) {
/*  56 */       permissions.remove(PosixFilePermission.OWNER_READ);
/*  57 */       permissions.add(PosixFilePermission.OWNER_WRITE);
/*     */     } else {
/*  59 */       permissions.remove(PosixFilePermission.OWNER_READ);
/*  60 */       permissions.remove(PosixFilePermission.OWNER_WRITE);
/*     */     } 
/*     */     
/*  63 */     if ("READ_WRITE".equals(perm.getGroupAccess())) {
/*  64 */       permissions.add(PosixFilePermission.GROUP_READ);
/*  65 */       permissions.add(PosixFilePermission.GROUP_WRITE);
/*  66 */     } else if ("READ_ONLY".equals(perm.getGroupAccess())) {
/*  67 */       permissions.add(PosixFilePermission.GROUP_READ);
/*  68 */       permissions.remove(PosixFilePermission.GROUP_WRITE);
/*  69 */     } else if ("WRITE_ONLY".equals(perm.getGroupAccess())) {
/*  70 */       permissions.remove(PosixFilePermission.GROUP_READ);
/*  71 */       permissions.add(PosixFilePermission.GROUP_WRITE);
/*     */     } else {
/*  73 */       permissions.remove(PosixFilePermission.GROUP_READ);
/*  74 */       permissions.remove(PosixFilePermission.GROUP_WRITE);
/*  75 */       permissions.remove(PosixFilePermission.GROUP_EXECUTE);
/*     */     } 
/*     */     
/*  78 */     if ("READ_WRITE".equals(perm.getOtherAccess())) {
/*  79 */       permissions.add(PosixFilePermission.OTHERS_READ);
/*  80 */       permissions.add(PosixFilePermission.OTHERS_WRITE);
/*  81 */     } else if ("READ_ONLY".equals(perm.getOtherAccess())) {
/*  82 */       permissions.add(PosixFilePermission.OTHERS_READ);
/*  83 */       permissions.remove(PosixFilePermission.OTHERS_WRITE);
/*  84 */     } else if ("WRITE_ONLY".equals(perm.getOtherAccess())) {
/*  85 */       permissions.remove(PosixFilePermission.OTHERS_READ);
/*  86 */       permissions.add(PosixFilePermission.OTHERS_WRITE);
/*     */     } else {
/*  88 */       permissions.remove(PosixFilePermission.OTHERS_READ);
/*  89 */       permissions.remove(PosixFilePermission.OTHERS_WRITE);
/*  90 */       permissions.remove(PosixFilePermission.OTHERS_EXECUTE);
/*     */     } 
/*     */     
/*  93 */     Files.setPosixFilePermissions(Paths.get(file, new String[0]), permissions);
/*     */   }
/*     */   
/*     */   public PosixPermission getPosixPermission(String file) throws Exception {
/*  97 */     Path path = Paths.get(file, new String[0]);
/*  98 */     PosixFileAttributeView view = Files.<PosixFileAttributeView>getFileAttributeView(path, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/*     */     
/* 100 */     PosixFileAttributes attrs = view.readAttributes();
/* 101 */     PosixPermission perm = new PosixPermission();
/* 102 */     perm.setOwner(attrs.owner().getName());
/* 103 */     perm.setGroup(attrs.group().getName());
/* 104 */     Set<PosixFilePermission> permissions = attrs.permissions();
/* 105 */     perm.setExecutable(permissions
/* 106 */         .contains(PosixFilePermission.OWNER_EXECUTE));
/*     */     
/* 108 */     String ownerPerm = "NONE";
/* 109 */     if (permissions.contains(PosixFilePermission.OWNER_READ) && permissions
/* 110 */       .contains(PosixFilePermission.OWNER_WRITE)) {
/* 111 */       ownerPerm = "READ_WRITE";
/* 112 */     } else if (permissions.contains(PosixFilePermission.OWNER_READ)) {
/* 113 */       ownerPerm = "READ_ONLY";
/* 114 */     } else if (permissions.contains(PosixFilePermission.OWNER_WRITE)) {
/* 115 */       ownerPerm = "WRITE_ONLY";
/*     */     } 
/* 117 */     perm.setOwnerAccess(ownerPerm);
/*     */     
/* 119 */     String groupPerm = "NONE";
/* 120 */     if (permissions.contains(PosixFilePermission.GROUP_READ) && permissions
/* 121 */       .contains(PosixFilePermission.GROUP_WRITE)) {
/* 122 */       groupPerm = "READ_WRITE";
/* 123 */     } else if (permissions.contains(PosixFilePermission.GROUP_READ)) {
/* 124 */       groupPerm = "READ_ONLY";
/* 125 */     } else if (permissions.contains(PosixFilePermission.GROUP_WRITE)) {
/* 126 */       groupPerm = "WRITE_ONLY";
/*     */     } 
/* 128 */     perm.setGroupAccess(groupPerm);
/*     */     
/* 130 */     String otherPerm = "NONE";
/* 131 */     if (permissions.contains(PosixFilePermission.OTHERS_READ) && permissions
/* 132 */       .contains(PosixFilePermission.OTHERS_WRITE)) {
/* 133 */       otherPerm = "READ_WRITE";
/* 134 */     } else if (permissions.contains(PosixFilePermission.OTHERS_READ)) {
/* 135 */       otherPerm = "READ_ONLY";
/* 136 */     } else if (permissions.contains(PosixFilePermission.OTHERS_WRITE)) {
/* 137 */       otherPerm = "WRITE_ONLY";
/*     */     } 
/* 139 */     perm.setOtherAccess(otherPerm);
/* 140 */     return perm;
/*     */   }
/*     */ 
/*     */   
/*     */   public void rename(String oldName, String newName, String folder) throws IOException {
/* 145 */     Files.move(Paths.get(folder, new String[] { oldName }), Paths.get(folder, new String[] { newName }), new java.nio.file.CopyOption[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteFiles(List<String> files) throws IOException {
/* 150 */     for (String file : files) {
/* 151 */       System.out.println("Delete: " + file);
/* 152 */       File f = new File(file);
/* 153 */       if (f.isDirectory()) {
/* 154 */         File[] children = f.listFiles();
/* 155 */         if (children != null) {
/* 156 */           deleteFiles((List<String>)Arrays.<File>asList(children).stream()
/* 157 */               .map(a -> a.getAbsolutePath())
/* 158 */               .collect(Collectors.toList()));
/*     */         }
/*     */       } 
/* 161 */       if (!f.delete()) {
/* 162 */         throw new IOException("Unable to delete: " + f
/* 163 */             .getAbsolutePath());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, FileCopyTask> getPendingOperations() {
/* 172 */     return this.pendingOperations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String createFileCopyTask(List<String> sourceFile, String destinationFolder, boolean move) {
/* 183 */     FileCopyTask cp = new FileCopyTask(sourceFile, destinationFolder, move);
/* 184 */     this.pendingOperations.put(cp.getId(), cp);
/* 185 */     this.threadPool.submit((Runnable)cp);
/* 186 */     return cp.getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FileCopyProgressResponse> getProgress(List<String> idList) {
/* 194 */     List<FileCopyProgressResponse> list = new ArrayList<>();
/* 195 */     for (String id : idList) {
/* 196 */       FileCopyTask cp = this.pendingOperations.get(id);
/*     */ 
/*     */ 
/*     */       
/* 200 */       FileCopyProgressResponse r = new FileCopyProgressResponse(cp.getId(), cp.getName(), (int)cp.getProgress(), cp.getStatus(), String.join("/n", cp.getErrorMessage()), cp.isHasErrors());
/* 201 */       list.add(r);
/*     */     } 
/* 203 */     return list;
/*     */   }
/*     */   
/*     */   public String getOpName(String id) {
/* 207 */     return ((FileCopyTask)this.pendingOperations.get(id)).getName();
/*     */   }
/*     */   
/*     */   public void cancelTask(String id) {
/* 211 */     FileCopyTask cp = this.pendingOperations.get(id);
/* 212 */     cp.cancel();
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/FileOperations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */