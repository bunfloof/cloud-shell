/*     */ package BOOT-INF.classes.cloudshell.app.files;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileTransfer
/*     */ {
/*  22 */   private final byte[] BUFFER = new byte[8192];
/*     */ 
/*     */   
/*     */   private boolean compress;
/*     */ 
/*     */   
/*     */   public FileTransfer(boolean compress) {
/*  29 */     this.compress = compress;
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferFile(String relativePath, String folder, InputStream in) {
/*  34 */     File f = new File(folder, relativePath);
/*  35 */     File parent = f.getParentFile();
/*  36 */     parent.mkdirs();
/*  37 */     System.out.println("Creating folder: " + parent.getAbsolutePath());
/*  38 */     System.out.println("Creating file: " + f.getAbsolutePath()); 
/*  39 */     try { FileOutputStream out = new FileOutputStream(f); 
/*  40 */       try { copyStream(in, out);
/*  41 */         out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/*  42 */     { e.printStackTrace(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void transferFiles(List<String> files, OutputStream out) {
/*  47 */     if (!this.compress) { 
/*  48 */       try { InputStream in = new FileInputStream(files.get(0)); 
/*  49 */         try { copyStream(in, out);
/*  50 */           in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/*  51 */       { e.printStackTrace(); }
/*     */        }
/*     */     else { 
/*  54 */       try { ZipOutputStream zout = new ZipOutputStream(out); 
/*  55 */         try { for (String file : files)
/*  56 */           { File f = new File(file);
/*  57 */             if (f.isDirectory())
/*  58 */             { if (!walkTree(f, zout, f.getName()))
/*     */               
/*     */               { 
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
/*  79 */                 zout.close(); return; }  continue; }  boolean writing = false; try { InputStream in = new FileInputStream(f); try { ZipEntry ze = new ZipEntry(f.getName()); ze.setSize(f.length()); writing = true; zout.putNextEntry(ze); copyStream(in, zout); zout.closeEntry(); writing = false; in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e) { e.printStackTrace(); }  if (writing) { zout.close(); return; }  }  zout.close(); } catch (Throwable throwable) { try { zout.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/*  80 */       { e.printStackTrace(); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean walkTree(File folder, ZipOutputStream zout, String relativePath) {
/*  87 */     System.out.println("Walking: " + folder
/*  88 */         .getAbsolutePath() + " - " + relativePath);
/*     */     try {
/*  90 */       File[] files = folder.listFiles();
/*  91 */       if (files != null) {
/*  92 */         for (File f : files) {
/*  93 */           if (f.isDirectory()) {
/*  94 */             if (!walkTree(f, zout, combine(relativePath, f
/*  95 */                   .getName(), File.separator))) {
/*  96 */               return false;
/*     */             }
/*     */           } else {
/*  99 */             boolean writing = false; 
/* 100 */             try { InputStream in = new FileInputStream(f); 
/* 101 */               try { ZipEntry ze = new ZipEntry(combine(relativePath, f
/* 102 */                       .getName(), File.separator));
/* 103 */                 ze.setSize(f.length());
/* 104 */                 writing = true;
/* 105 */                 zout.putNextEntry(ze);
/* 106 */                 copyStream(in, zout);
/* 107 */                 zout.closeEntry();
/* 108 */                 writing = false;
/* 109 */                 in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 110 */             { e.printStackTrace(); }
/*     */             
/* 112 */             if (writing) {
/* 113 */               throw new Exception("error reading file: " + f
/* 114 */                   .getAbsolutePath());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/* 119 */     } catch (Exception e) {
/* 120 */       e.printStackTrace();
/* 121 */       return false;
/*     */     } 
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyStream(InputStream in, OutputStream out) throws IOException {
/*     */     while (true) {
/* 129 */       int x = in.read(this.BUFFER);
/* 130 */       if (x == -1)
/*     */         break; 
/* 132 */       out.write(this.BUFFER, 0, x);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String combine(String path1, String path2, String separator) {
/* 137 */     if (path2.startsWith(separator)) {
/* 138 */       path2 = path2.substring(1);
/*     */     }
/* 140 */     if (!path1.endsWith(separator)) {
/* 141 */       return path1 + separator + path2;
/*     */     }
/* 143 */     return path1 + path2;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/FileTransfer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */