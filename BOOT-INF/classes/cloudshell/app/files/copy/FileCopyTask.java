/*     */ package BOOT-INF.classes.cloudshell.app.files.copy;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileCopyTask
/*     */   implements Runnable
/*     */ {
/*     */   private long total;
/*     */   private long copied;
/*     */   private List<String> src;
/*     */   private String target;
/*     */   private boolean move;
/*     */   private String status;
/*  26 */   private final byte[] BUFFER = new byte[8192];
/*  27 */   private List<String> errorMessage = new ArrayList<>();
/*     */   private boolean hasErrors = false;
/*  29 */   private AtomicBoolean stopRequested = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */   
/*     */   private String id;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileCopyTask(List<String> src, String target, boolean move) {
/*  39 */     this.id = UUID.randomUUID().toString();
/*  40 */     this.src = src;
/*  41 */     this.target = target;
/*  42 */     this.move = move;
/*  43 */     this.status = "Waiting...";
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  48 */     for (String s : this.src) {
/*  49 */       File f = new File(s);
/*  50 */       calculateSize(f, this.target);
/*  51 */       if (this.stopRequested.get()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  56 */     for (String s : this.src) {
/*  57 */       File f = new File(s);
/*  58 */       moveFiles(f, this.target);
/*  59 */       if (this.stopRequested.get()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  64 */     this.status = "Finished";
/*     */   }
/*     */   
/*     */   public double getProgress() {
/*  68 */     if (this.total < 1L) {
/*  69 */       return 0.0D;
/*     */     }
/*  71 */     return this.copied * 100.0D / this.total;
/*     */   }
/*     */   
/*     */   private void calculateSize(File f, String targetFolder) {
/*  75 */     this.status = "Preparing...";
/*  76 */     if (this.stopRequested.get()) {
/*     */       return;
/*     */     }
/*  79 */     String name = f.getName();
/*  80 */     File targetFile = new File(targetFolder, name);
/*     */     
/*  82 */     if (f.isDirectory()) {
/*  83 */       File[] files = null;
/*     */       try {
/*  85 */         files = f.listFiles();
/*  86 */         if (this.stopRequested.get()) {
/*     */           return;
/*     */         }
/*  89 */       } catch (Exception e) {
/*  90 */         e.printStackTrace();
/*     */       } 
/*  92 */       if (files == null || files.length < 1) {
/*     */         return;
/*     */       }
/*  95 */       for (File file : files) {
/*  96 */         calculateSize(file, targetFile.getAbsolutePath());
/*     */       }
/*     */     } else {
/*  99 */       this.total += f.length();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveFiles(File f, String targetFolder) {
/* 104 */     this.status = this.move ? "Moving files ..." : "Copying files ...";
/* 105 */     if (this.stopRequested.get()) {
/*     */       return;
/*     */     }
/* 108 */     String name = f.getName();
/* 109 */     File targetFile = new File(targetFolder, name);
/*     */     
/* 111 */     if (f.isDirectory()) {
/* 112 */       targetFile.mkdirs();
/* 113 */       File[] files = null;
/*     */       try {
/* 115 */         files = f.listFiles();
/* 116 */         if (this.stopRequested.get()) {
/*     */           return;
/*     */         }
/* 119 */       } catch (Exception e) {
/* 120 */         e.printStackTrace();
/*     */       } 
/* 122 */       if (files == null || files.length < 1) {
/*     */         return;
/*     */       }
/* 125 */       for (File file : files) {
/* 126 */         moveFiles(file, targetFile.getAbsolutePath());
/* 127 */         if (this.stopRequested.get()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } else {
/* 132 */       copy(f, targetFile);
/*     */     } 
/*     */     
/* 135 */     if (this.move && 
/* 136 */       !f.delete()) {
/* 137 */       this.hasErrors = true;
/* 138 */       this.errorMessage.add("Error deleting:  " + this.src + ";");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void copy(File src, File target) {
/* 144 */     if (this.stopRequested.get()) {
/*     */       return;
/*     */     }
/*     */     
/* 148 */     try { InputStream in = new FileInputStream(src); 
/* 149 */       try { OutputStream out = new FileOutputStream(target); 
/* 150 */         try { while (!this.stopRequested.get()) {
/* 151 */             int x = in.read(this.BUFFER);
/* 152 */             if (x == -1)
/*     */               break; 
/* 154 */             out.write(this.BUFFER, 0, x);
/* 155 */             this.copied += x;
/* 156 */             Thread.sleep(10L);
/*     */           } 
/* 158 */           out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 159 */     { this.hasErrors = true;
/* 160 */       e.printStackTrace();
/* 161 */       this.errorMessage.add("Error copying:  " + src + " to " + target + " - " + e
/* 162 */           .getMessage());
/*     */       return; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotal() {
/* 171 */     return this.total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTotal(long total) {
/* 178 */     this.total = total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCopied() {
/* 185 */     return this.copied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCopied(long copied) {
/* 192 */     this.copied = copied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTarget() {
/* 199 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String target) {
/* 206 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 213 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/* 220 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getErrorMessage() {
/* 227 */     return this.errorMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorMessage(List<String> errorMessage) {
/* 234 */     this.errorMessage = errorMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHasErrors() {
/* 241 */     return this.hasErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasErrors(boolean hasErrors) {
/* 248 */     this.hasErrors = hasErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicBoolean getStopRequested() {
/* 255 */     return this.stopRequested;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStopRequested(AtomicBoolean stopRequested) {
/* 262 */     this.stopRequested = stopRequested;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 269 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 276 */     return (this.src.size() == 1) ? this.src.get(0) : (
/* 277 */       (String)this.src.get(0) + (this.src.size() - 1) + " files");
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 281 */     this.stopRequested.set(true);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/copy/FileCopyTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */