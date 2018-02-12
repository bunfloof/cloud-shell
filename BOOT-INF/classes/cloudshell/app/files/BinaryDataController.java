/*     */ package BOOT-INF.classes.cloudshell.app.files;
/*     */ 
/*     */ import cloudshell.app.Application;
/*     */ import cloudshell.app.files.FileTransfer;
/*     */ import cloudshell.app.files.FileTypeDetector;
/*     */ import io.jsonwebtoken.Claims;
/*     */ import io.jsonwebtoken.Jws;
/*     */ import io.jsonwebtoken.Jwts;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.CrossOrigin;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
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
/*     */ @RestController
/*     */ @CrossOrigin(allowCredentials = "true")
/*     */ @RequestMapping({"/bin"})
/*     */ public class BinaryDataController
/*     */ {
/*     */   @Autowired
/*     */   private FileTypeDetector typeDetector;
/*     */   
/*     */   private void validateToken(String token) throws Exception {
/*  50 */     Jws<Claims> parsedToken = Jwts.parser().setSigningKey(Application.SECRET_KEY).parseClaimsJws(token);
/*     */     
/*  52 */     String username = ((Claims)parsedToken.getBody()).getSubject();
/*     */     
/*  54 */     if (username != null && username.length() > 0 && username
/*  55 */       .equals(System.getProperty("app.default-user"))) {
/*  56 */       System.out.println("Token and user is valid");
/*     */       
/*     */       return;
/*     */     } 
/*  60 */     throw new Exception("Invalid username: " + username);
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/image/{encodedPath}"})
/*     */   public ResponseEntity<byte[]> getImage(@PathVariable String encodedPath, @RequestParam String token) throws Exception {
/*  66 */     validateToken(token);
/*  67 */     String path = new String(Base64.getDecoder().decode(encodedPath));
/*  68 */     byte[] b = Files.readAllBytes(Paths.get(path, new String[0]));
/*  69 */     HttpHeaders httpHeaders = new HttpHeaders();
/*  70 */     httpHeaders.add("Content-Type", getImageType(path));
/*  71 */     ResponseEntity<byte[]> resp = new ResponseEntity(b, (MultiValueMap)httpHeaders, HttpStatus.OK);
/*     */     
/*  73 */     return resp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/blob/{encodedPath}"})
/*     */   public void getBlob(@PathVariable String encodedPath, @RequestParam String token, HttpServletRequest request, HttpServletResponse resp) throws Exception {
/*  80 */     validateToken(token);
/*  81 */     String path = new String(Base64.getDecoder().decode(encodedPath));
/*  82 */     System.out.println("Get file: " + path);
/*  83 */     long fileSize = Files.size(Paths.get(path, new String[0]));
/*  84 */     String r = request.getHeader("Range");
/*  85 */     long upperBound = fileSize - 1L;
/*  86 */     long lowerBound = 0L;
/*  87 */     if (r != null) {
/*  88 */       System.out.println(r);
/*  89 */       String rstr = r.split("=")[1];
/*  90 */       String[] arr = rstr.split("-");
/*  91 */       lowerBound = Long.parseLong(arr[0]);
/*  92 */       if (arr.length > 1 && arr[1].length() > 0) {
/*  93 */         upperBound = Long.parseLong(arr[1]);
/*     */       }
/*     */     } 
/*     */     
/*  97 */     System.out.println("lb: " + lowerBound + " ub: " + fileSize);
/*     */     
/*  99 */     if (r != null) {
/* 100 */       resp.setStatus(206);
/*     */     }
/* 102 */     resp.addHeader("Content-Length", (fileSize - lowerBound) + "");
/* 103 */     resp.addHeader("Content-Range", "bytes " + lowerBound + "-" + upperBound + "/" + fileSize);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     resp.addHeader("Content-Type", this.typeDetector.getType(new File(path)));
/* 110 */     long rem = fileSize - lowerBound;
/* 111 */     InputStream in = new FileInputStream(path); try {
/* 112 */       in.skip(lowerBound);
/* 113 */       byte[] b = new byte[8192];
/* 114 */       while (rem > 0L) {
/* 115 */         int x = in.read(b, 0, (int)((rem > b.length) ? b.length : rem));
/* 116 */         if (x == -1)
/*     */           break; 
/* 118 */         resp.getOutputStream().write(b, 0, x);
/* 119 */         rem -= x;
/*     */       } 
/* 121 */       in.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         in.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     }  } @GetMapping({"/download"})
/*     */   public void downloadFiles(@RequestParam(name = "folder") String encodedFolder, @RequestParam(name = "files", required = false) String encodedFiles, @RequestParam String token, HttpServletResponse response) throws Exception {
/* 131 */     validateToken(token);
/* 132 */     int fileCount = 0;
/*     */     
/* 134 */     String folder = new String(Base64.getDecoder().decode(encodedFolder), "utf-8");
/*     */ 
/*     */     
/* 137 */     List<String> fileList = new ArrayList<>();
/*     */     
/* 139 */     if (encodedFiles != null) {
/* 140 */       String files = new String(Base64.getDecoder().decode(encodedFiles), "utf-8");
/*     */       
/* 142 */       String[] arr1 = files.split("/");
/* 143 */       if (arr1 != null && arr1.length > 0) {
/* 144 */         fileCount = arr1.length;
/* 145 */         for (String str : arr1) {
/* 146 */           fileList.add((new File(folder, str)).getAbsolutePath());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     if (fileList.size() == 0) {
/* 152 */       throw new IOException();
/*     */     }
/*     */     
/* 155 */     String name = "download.zip";
/*     */     
/* 157 */     boolean compress = false;
/*     */     
/* 159 */     if (fileCount == 1) {
/* 160 */       File f = new File(fileList.get(0));
/* 161 */       name = f.getName();
/* 162 */       compress = f.isDirectory();
/* 163 */       if (name.length() < 1) {
/* 164 */         name = "files";
/*     */       }
/*     */     } else {
/* 167 */       compress = true;
/*     */     } 
/*     */     
/* 170 */     response.addHeader("Content-Disposition", "attachment; filename=/"" + name + ".zip/"");
/*     */     
/* 172 */     response.setContentType("application/octet-stream");
/* 173 */     FileTransfer fs = new FileTransfer(compress);
/* 174 */     fs.transferFiles(fileList, (OutputStream)response.getOutputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getImageType(String path) {
/* 182 */     return this.typeDetector.getTypeByName(path);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/BinaryDataController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */