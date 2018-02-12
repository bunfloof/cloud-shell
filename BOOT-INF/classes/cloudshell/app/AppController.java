/*     */ package BOOT-INF.classes.cloudshell.app;
/*     */ 
/*     */ import cloudshell.app.AppContext;
/*     */ import cloudshell.app.config.ConfigManager;
/*     */ import cloudshell.app.files.FileOperations;
/*     */ import cloudshell.app.files.FileService;
/*     */ import cloudshell.app.files.FileTransfer;
/*     */ import cloudshell.app.files.PosixPermission;
/*     */ import cloudshell.app.files.copy.FileCopyProgressResponse;
/*     */ import cloudshell.app.files.copy.FileCopyRequest;
/*     */ import cloudshell.app.files.search.SearchResult;
/*     */ import cloudshell.app.terminal.PtySession;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/*     */ import org.springframework.web.bind.annotation.CrossOrigin;
/*     */ import org.springframework.web.bind.annotation.DeleteMapping;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
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
/*     */ @RestController
/*     */ @CrossOrigin(allowCredentials = "true")
/*     */ @RequestMapping({"/api"})
/*     */ public class AppController
/*     */ {
/*     */   @Autowired
/*     */   private FileService service;
/*     */   @Autowired
/*     */   private FileOperations fs;
/*     */   @Autowired
/*     */   private BCryptPasswordEncoder passwordEncoder;
/*     */   
/*     */   @PostMapping({"/app/terminal/{appId}/resize"})
/*     */   public void resizePty(@PathVariable String appId, @RequestBody Map<String, Integer> body) {
/*  62 */     ((PtySession)AppContext.INSTANCES.get(appId)).resizePty(((Integer)body.get("row")).intValue(), ((Integer)body
/*  63 */         .get("col")).intValue());
/*     */   }
/*     */   
/*     */   @PostMapping({"/app/terminal"})
/*     */   public Map<String, String> createTerminal() throws Exception {
/*  68 */     PtySession pty = new PtySession();
/*  69 */     AppContext.INSTANCES.put(pty.getId(), pty);
/*  70 */     Map<String, String> map = new HashMap<>();
/*  71 */     map.put("id", pty.getId());
/*  72 */     return map;
/*     */   }
/*     */   
/*     */   @GetMapping({"/app/files/home"})
/*     */   public Map<String, Object> listHome() throws Exception {
/*  77 */     Map<String, Object> map = new HashMap<>();
/*  78 */     map.put("files", this.service.list(System.getProperty("user.home")));
/*  79 */     map.put("folder", System.getProperty("user.home"));
/*  80 */     map.put("folderName", (new File(
/*  81 */           System.getProperty("user.home"))).getName());
/*  82 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/app/files/list/{path}"})
/*     */   public Map<String, Object> list(@PathVariable String path) throws Exception {
/*  88 */     Map<String, Object> map = new HashMap<>();
/*  89 */     String folder = null;
/*  90 */     if (path == null) {
/*  91 */       folder = System.getProperty("user.home");
/*     */     } else {
/*  93 */       folder = new String(Base64.getDecoder().decode(path), "utf-8");
/*     */     } 
/*  95 */     System.out.println("base64: " + path + " " + folder);
/*  96 */     map.put("files", this.service.list(folder));
/*  97 */     map.put("folder", folder);
/*  98 */     map.put("folderName", (new File(folder)).getName());
/*  99 */     return map;
/*     */   }
/*     */   
/*     */   @GetMapping({"/app/files/up/{path}"})
/*     */   public Map<String, Object> up(@PathVariable String path) throws Exception {
/* 104 */     Map<String, Object> map = new HashMap<>();
/* 105 */     path = new String(Base64.getDecoder().decode(path), "utf-8");
/* 106 */     if (!path.equals("/")) {
/* 107 */       path = (new File(path)).getParent();
/*     */     }
/* 109 */     map.put("files", this.service.list(path));
/* 110 */     map.put("folder", path);
/* 111 */     map.put("folderName", (new File(path)).getName());
/* 112 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/upload/{folder}/{relativePath}"})
/*     */   public void upload(@PathVariable String folder, @PathVariable String relativePath, HttpServletRequest request) throws Exception {
/* 119 */     System.out.println("uploading...");
/*     */     try {
/* 121 */       folder = new String(Base64.getDecoder().decode(folder), "utf-8");
/* 122 */       relativePath = new String(Base64.getDecoder().decode(relativePath), "utf-8");
/*     */       
/* 124 */       FileTransfer fs = new FileTransfer(false);
/* 125 */       fs.transferFile(relativePath, folder, (InputStream)request.getInputStream());
/* 126 */     } catch (Exception e) {
/* 127 */       e.printStackTrace();
/* 128 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   @GetMapping({"/app/folder/tree/home"})
/*     */   public List<Map<String, ?>> listTreeHome() {
/* 134 */     List<Map<String, ?>> list = new ArrayList<>();
/* 135 */     File f = new File(System.getProperty("user.home"));
/* 136 */     File[] files = f.listFiles();
/* 137 */     if (files != null && files.length > 0)
/* 138 */       for (File file : files) {
/* 139 */         if (file.isDirectory()) {
/*     */ 
/*     */           
/* 142 */           Map<String, Object> entry = new HashMap<>();
/* 143 */           entry.put("name", file.getName());
/* 144 */           entry.put("path", file.getAbsolutePath());
/* 145 */           entry.put("leafNode", Boolean.valueOf(!file.isDirectory()));
/* 146 */           list.add(entry);
/*     */         } 
/*     */       }  
/* 149 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/app/folder/tree/path/{encodedPath}"})
/*     */   public List<Map<String, ?>> listTreePath(@PathVariable String encodedPath) throws Exception {
/* 155 */     String path = new String(Base64.getDecoder().decode(encodedPath), "utf-8");
/*     */     
/* 157 */     List<Map<String, ?>> list = new ArrayList<>();
/* 158 */     File f = new File(path);
/* 159 */     File[] files = f.listFiles();
/* 160 */     if (files != null && files.length > 0) {
/* 161 */       for (File file : files) {
/* 162 */         if (file.isDirectory()) {
/* 163 */           Map<String, Object> entry = new HashMap<>();
/* 164 */           entry.put("name", file.getName());
/* 165 */           entry.put("path", file.getAbsolutePath());
/* 166 */           entry.put("leafNode", Boolean.valueOf(!file.isDirectory()));
/* 167 */           list.add(entry);
/*     */         } 
/*     */       } 
/*     */     }
/* 171 */     return list;
/*     */   }
/*     */   
/*     */   @GetMapping({"/app/folder/tree/fs"})
/*     */   public List<Map<String, ?>> listFsRoots() throws Exception {
/* 176 */     List<Map<String, ?>> list = new ArrayList<>();
/* 177 */     File[] files = File.listRoots();
/* 178 */     if (files != null && files.length > 0) {
/* 179 */       for (File file : files) {
/* 180 */         Map<String, Object> entry = new HashMap<>();
/* 181 */         System.out.println("Root: " + file.getName());
/* 182 */         entry.put("name", file.getAbsolutePath());
/* 183 */         entry.put("path", file.getAbsolutePath());
/* 184 */         entry.put("leafNode", Boolean.valueOf(!file.isDirectory()));
/* 185 */         list.add(entry);
/*     */       } 
/*     */     }
/* 188 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/{mode}"})
/*     */   public Map<String, String> startCopyOrMove(@PathVariable String mode, @RequestBody FileCopyRequest request) {
/* 194 */     List<String> sourceFile = request.getSourceFile();
/* 195 */     String targetFolder = request.getTargetFolder();
/* 196 */     Map<String, String> map = new HashMap<>();
/* 197 */     String id = this.fs.createFileCopyTask(sourceFile, targetFolder, "move"
/* 198 */         .equals(mode));
/* 199 */     map.put("id", id);
/* 200 */     map.put("name", this.fs.getOpName(id));
/* 201 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/progress"})
/*     */   public List<FileCopyProgressResponse> getProgress(@RequestBody List<String> idList) {
/* 207 */     return this.fs.getProgress(idList);
/*     */   }
/*     */   
/*     */   @PostMapping({"/app/fs/cancel/{id}"})
/*     */   public void cancel(@PathVariable String id) {
/* 212 */     this.fs.cancelTask(id);
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/rename"})
/*     */   public void rename(@RequestBody Map<String, String> body) throws IOException {
/* 218 */     this.fs.rename(body.get("oldName"), body.get("newName"), body.get("folder"));
/*     */   }
/*     */   
/*     */   @PostMapping({"/app/fs/delete"})
/*     */   public void delete(@RequestBody List<String> body) throws IOException {
/* 223 */     this.fs.deleteFiles(body);
/*     */   }
/*     */   
/*     */   @GetMapping({"/app/fs/files/{encodedPath}"})
/*     */   public String getText(@PathVariable String encodedPath) throws Exception {
/* 228 */     return this.service
/* 229 */       .getText(new String(Base64.getDecoder().decode(encodedPath)));
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/files/{encodedPath}"})
/*     */   public void setText(@PathVariable String encodedPath, @RequestBody String body) throws Exception {
/* 235 */     this.service.setText(new String(Base64.getDecoder().decode(encodedPath)), body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/search"})
/*     */   public Map<String, String> initSearch(@RequestBody Map<String, String> request) {
/* 242 */     String id = this.service.createSearch(request.get("folder"), request
/* 243 */         .get("searchText"));
/* 244 */     Map<String, String> response = new HashMap<>();
/* 245 */     response.put("id", id);
/* 246 */     return response;
/*     */   }
/*     */   
/*     */   @DeleteMapping({"/app/fs/search/{id}"})
/*     */   public void cancelSearch(@PathVariable String id) {
/* 251 */     this.service.cancelSearch(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/app/fs/search/{id}"})
/*     */   public SearchResult getSearchResult(@PathVariable String id, @RequestParam(defaultValue = "0", required = false) int fileIndex, @RequestParam(defaultValue = "0", required = false) int folderIndex) {
/* 258 */     return this.service.getSearchResult(id, fileIndex, folderIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/app/fs/posix/{encodedPath}"})
/*     */   public PosixPermission getPosixPerm(@PathVariable String encodedPath) throws Exception {
/* 264 */     return this.fs.getPosixPermission(new String(
/* 265 */           Base64.getDecoder().decode(encodedPath), "utf-8"));
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/fs/posix/{encodedPath}"})
/*     */   public void setPosixPerm(@PathVariable String encodedPath, @RequestBody PosixPermission perm) throws Exception {
/* 271 */     this.fs.setPosixPermission(new String(
/* 272 */           Base64.getDecoder().decode(encodedPath), "utf-8"), perm);
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/app/config"})
/*     */   public Map<String, String> getConfig() {
/* 278 */     Map<String, String> map = new HashMap<>();
/* 279 */     map.put("app.default-user", System.getProperty("app.default-user"));
/* 280 */     map.put("app.default-pass", System.getProperty("app.default-pass"));
/* 281 */     map.put("app.default-shell", System.getProperty("app.default-shell"));
/* 282 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping({"/app/config"})
/*     */   public void setConfig(@RequestBody Map<String, String> map) throws IOException {
/* 288 */     for (String key : map.keySet()) {
/* 289 */       String val = map.get(key);
/* 290 */       if (val != null && val.length() > 0) {
/* 291 */         if (key.equals("app.default-pass")) {
/* 292 */           System.setProperty(key, this.passwordEncoder.encode(val)); continue;
/*     */         } 
/* 294 */         System.setProperty(key, val);
/*     */       } 
/*     */     } 
/*     */     
/* 298 */     ConfigManager.saveUserDetails();
/*     */   }
/*     */   
/*     */   @PostMapping({"/app/fs/mkdir"})
/*     */   public void mkdir(@RequestBody Map<String, String> map) throws Exception {
/* 303 */     String dir = map.get("dir");
/* 304 */     String name = map.get("name");
/* 305 */     Path path = Paths.get(dir, new String[] { name });
/* 306 */     Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */   
/*     */   @PostMapping({"/app/fs/touch"})
/*     */   public void touch(@RequestBody Map<String, String> map) throws Exception {
/* 311 */     String dir = map.get("dir");
/* 312 */     String name = map.get("name");
/* 313 */     Path path = Paths.get(dir, new String[] { name });
/* 314 */     Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/AppController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */