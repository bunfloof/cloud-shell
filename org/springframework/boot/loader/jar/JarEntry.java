/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.Manifest;
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
/*     */ class JarEntry
/*     */   extends JarEntry
/*     */   implements FileHeader
/*     */ {
/*     */   private final AsciiBytes name;
/*     */   private final AsciiBytes headerName;
/*     */   private Certificate[] certificates;
/*     */   private CodeSigner[] codeSigners;
/*     */   private final JarFile jarFile;
/*     */   private long localHeaderOffset;
/*     */   
/*     */   JarEntry(JarFile jarFile, CentralDirectoryFileHeader header, AsciiBytes nameAlias) {
/*  48 */     super((nameAlias != null) ? nameAlias.toString() : header.getName().toString());
/*  49 */     this.name = (nameAlias != null) ? nameAlias : header.getName();
/*  50 */     this.headerName = header.getName();
/*  51 */     this.jarFile = jarFile;
/*  52 */     this.localHeaderOffset = header.getLocalHeaderOffset();
/*  53 */     setCompressedSize(header.getCompressedSize());
/*  54 */     setMethod(header.getMethod());
/*  55 */     setCrc(header.getCrc());
/*  56 */     setComment(header.getComment().toString());
/*  57 */     setSize(header.getSize());
/*  58 */     setTime(header.getTime());
/*  59 */     setExtra(header.getExtra());
/*     */   }
/*     */   
/*     */   AsciiBytes getAsciiBytesName() {
/*  63 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasName(CharSequence name, char suffix) {
/*  68 */     return this.headerName.matches(name, suffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   URL getUrl() throws MalformedURLException {
/*  77 */     return new URL(this.jarFile.getUrl(), getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public Attributes getAttributes() throws IOException {
/*  82 */     Manifest manifest = this.jarFile.getManifest();
/*  83 */     return (manifest != null) ? manifest.getAttributes(getName()) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Certificate[] getCertificates() {
/*  88 */     if (this.jarFile.isSigned() && this.certificates == null) {
/*  89 */       this.jarFile.setupEntryCertificates(this);
/*     */     }
/*  91 */     return this.certificates;
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeSigner[] getCodeSigners() {
/*  96 */     if (this.jarFile.isSigned() && this.codeSigners == null) {
/*  97 */       this.jarFile.setupEntryCertificates(this);
/*     */     }
/*  99 */     return this.codeSigners;
/*     */   }
/*     */   
/*     */   void setCertificates(JarEntry entry) {
/* 103 */     this.certificates = entry.getCertificates();
/* 104 */     this.codeSigners = entry.getCodeSigners();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLocalHeaderOffset() {
/* 109 */     return this.localHeaderOffset;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/jar/JarEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */