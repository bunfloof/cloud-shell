/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class Handler
/*     */   extends URLStreamHandler
/*     */ {
/*     */   private static final String JAR_PROTOCOL = "jar:";
/*     */   private static final String FILE_PROTOCOL = "file:";
/*     */   private static final String SEPARATOR = "!/";
/*     */   private static final String CURRENT_DIR = "/./";
/*  53 */   private static final Pattern CURRENT_DIR_PATTERN = Pattern.compile("/./");
/*     */   
/*     */   private static final String PARENT_DIR = "/../";
/*     */   
/*  57 */   private static final String[] FALLBACK_HANDLERS = new String[] { "sun.net.www.protocol.jar.Handler" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static SoftReference<Map<File, JarFile>> rootFileCache = new SoftReference<>(null);
/*     */   
/*     */   private final JarFile jarFile;
/*     */   
/*     */   private URLStreamHandler fallbackHandler;
/*     */ 
/*     */   
/*     */   public Handler() {
/*  71 */     this(null);
/*     */   }
/*     */   
/*     */   public Handler(JarFile jarFile) {
/*  75 */     this.jarFile = jarFile;
/*     */   }
/*     */ 
/*     */   
/*     */   protected URLConnection openConnection(URL url) throws IOException {
/*  80 */     if (this.jarFile != null && isUrlInJarFile(url, this.jarFile)) {
/*  81 */       return JarURLConnection.get(url, this.jarFile);
/*     */     }
/*     */     try {
/*  84 */       return JarURLConnection.get(url, getRootJarFileFromUrl(url));
/*     */     }
/*  86 */     catch (Exception ex) {
/*  87 */       return openFallbackConnection(url, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isUrlInJarFile(URL url, JarFile jarFile) throws MalformedURLException {
/*  94 */     return (url.getPath().startsWith(jarFile.getUrl().getPath()) && url
/*  95 */       .toString().startsWith(jarFile.getUrlString()));
/*     */   }
/*     */ 
/*     */   
/*     */   private URLConnection openFallbackConnection(URL url, Exception reason) throws IOException {
/*     */     try {
/* 101 */       return openConnection(getFallbackHandler(), url);
/*     */     }
/* 103 */     catch (Exception ex) {
/* 104 */       if (reason instanceof IOException) {
/* 105 */         log(false, "Unable to open fallback handler", ex);
/* 106 */         throw (IOException)reason;
/*     */       } 
/* 108 */       log(true, "Unable to open fallback handler", ex);
/* 109 */       if (reason instanceof RuntimeException) {
/* 110 */         throw (RuntimeException)reason;
/*     */       }
/* 112 */       throw new IllegalStateException(reason);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void log(boolean warning, String message, Exception cause) {
/*     */     try {
/* 118 */       Level level = warning ? Level.WARNING : Level.FINEST;
/* 119 */       Logger.getLogger(getClass().getName()).log(level, message, cause);
/*     */     }
/* 121 */     catch (Exception ex) {
/* 122 */       if (warning) {
/* 123 */         System.err.println("WARNING: " + message);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private URLStreamHandler getFallbackHandler() {
/* 129 */     if (this.fallbackHandler != null) {
/* 130 */       return this.fallbackHandler;
/*     */     }
/* 132 */     for (String handlerClassName : FALLBACK_HANDLERS) {
/*     */       try {
/* 134 */         Class<?> handlerClass = Class.forName(handlerClassName);
/* 135 */         this.fallbackHandler = (URLStreamHandler)handlerClass.newInstance();
/* 136 */         return this.fallbackHandler;
/*     */       }
/* 138 */       catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 142 */     throw new IllegalStateException("Unable to find fallback handler");
/*     */   }
/*     */ 
/*     */   
/*     */   private URLConnection openConnection(URLStreamHandler handler, URL url) throws Exception {
/* 147 */     return (new URL(null, url.toExternalForm(), handler)).openConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseURL(URL context, String spec, int start, int limit) {
/* 152 */     if (spec.regionMatches(true, 0, "jar:", 0, "jar:".length())) {
/* 153 */       setFile(context, getFileFromSpec(spec.substring(start, limit)));
/*     */     } else {
/*     */       
/* 156 */       setFile(context, getFileFromContext(context, spec.substring(start, limit)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getFileFromSpec(String spec) {
/* 161 */     int separatorIndex = spec.lastIndexOf("!/");
/* 162 */     if (separatorIndex == -1) {
/* 163 */       throw new IllegalArgumentException("No !/ in spec '" + spec + "'");
/*     */     }
/*     */     try {
/* 166 */       new URL(spec.substring(0, separatorIndex));
/* 167 */       return spec;
/*     */     }
/* 169 */     catch (MalformedURLException ex) {
/* 170 */       throw new IllegalArgumentException("Invalid spec URL '" + spec + "'", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getFileFromContext(URL context, String spec) {
/* 175 */     String file = context.getFile();
/* 176 */     if (spec.startsWith("/")) {
/* 177 */       return trimToJarRoot(file) + "!/" + spec.substring(1);
/*     */     }
/* 179 */     if (file.endsWith("/")) {
/* 180 */       return file + spec;
/*     */     }
/* 182 */     int lastSlashIndex = file.lastIndexOf('/');
/* 183 */     if (lastSlashIndex == -1) {
/* 184 */       throw new IllegalArgumentException("No / found in context URL's file '" + file + "'");
/*     */     }
/*     */     
/* 187 */     return file.substring(0, lastSlashIndex + 1) + spec;
/*     */   }
/*     */   
/*     */   private String trimToJarRoot(String file) {
/* 191 */     int lastSeparatorIndex = file.lastIndexOf("!/");
/* 192 */     if (lastSeparatorIndex == -1) {
/* 193 */       throw new IllegalArgumentException("No !/ found in context URL's file '" + file + "'");
/*     */     }
/*     */     
/* 196 */     return file.substring(0, lastSeparatorIndex);
/*     */   }
/*     */   
/*     */   private void setFile(URL context, String file) {
/* 200 */     String path = normalize(file);
/* 201 */     String query = null;
/* 202 */     int queryIndex = path.lastIndexOf('?');
/* 203 */     if (queryIndex != -1) {
/* 204 */       query = path.substring(queryIndex + 1);
/* 205 */       path = path.substring(0, queryIndex);
/*     */     } 
/* 207 */     setURL(context, "jar:", null, -1, null, null, path, query, context
/* 208 */         .getRef());
/*     */   }
/*     */   
/*     */   private String normalize(String file) {
/* 212 */     if (!file.contains("/./") && !file.contains("/../")) {
/* 213 */       return file;
/*     */     }
/* 215 */     int afterLastSeparatorIndex = file.lastIndexOf("!/") + "!/".length();
/* 216 */     String afterSeparator = file.substring(afterLastSeparatorIndex);
/* 217 */     afterSeparator = replaceParentDir(afterSeparator);
/* 218 */     afterSeparator = replaceCurrentDir(afterSeparator);
/* 219 */     return file.substring(0, afterLastSeparatorIndex) + afterSeparator;
/*     */   }
/*     */   
/*     */   private String replaceParentDir(String file) {
/*     */     int parentDirIndex;
/* 224 */     while ((parentDirIndex = file.indexOf("/../")) >= 0) {
/* 225 */       int precedingSlashIndex = file.lastIndexOf('/', parentDirIndex - 1);
/* 226 */       if (precedingSlashIndex >= 0) {
/*     */         
/* 228 */         file = file.substring(0, precedingSlashIndex) + file.substring(parentDirIndex + 3);
/*     */         continue;
/*     */       } 
/* 231 */       file = file.substring(parentDirIndex + 4);
/*     */     } 
/*     */     
/* 234 */     return file;
/*     */   }
/*     */   
/*     */   private String replaceCurrentDir(String file) {
/* 238 */     return CURRENT_DIR_PATTERN.matcher(file).replaceAll("/");
/*     */   }
/*     */ 
/*     */   
/*     */   protected int hashCode(URL u) {
/* 243 */     return hashCode(u.getProtocol(), u.getFile());
/*     */   }
/*     */   
/*     */   private int hashCode(String protocol, String file) {
/* 247 */     int result = (protocol != null) ? protocol.hashCode() : 0;
/* 248 */     int separatorIndex = file.indexOf("!/");
/* 249 */     if (separatorIndex == -1) {
/* 250 */       return result + file.hashCode();
/*     */     }
/* 252 */     String source = file.substring(0, separatorIndex);
/* 253 */     String entry = canonicalize(file.substring(separatorIndex + 2));
/*     */     try {
/* 255 */       result += (new URL(source)).hashCode();
/*     */     }
/* 257 */     catch (MalformedURLException ex) {
/* 258 */       result += source.hashCode();
/*     */     } 
/* 260 */     result += entry.hashCode();
/* 261 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean sameFile(URL u1, URL u2) {
/* 266 */     if (!u1.getProtocol().equals("jar") || !u2.getProtocol().equals("jar")) {
/* 267 */       return false;
/*     */     }
/* 269 */     int separator1 = u1.getFile().indexOf("!/");
/* 270 */     int separator2 = u2.getFile().indexOf("!/");
/* 271 */     if (separator1 == -1 || separator2 == -1) {
/* 272 */       return super.sameFile(u1, u2);
/*     */     }
/* 274 */     String nested1 = u1.getFile().substring(separator1 + "!/".length());
/* 275 */     String nested2 = u2.getFile().substring(separator2 + "!/".length());
/* 276 */     if (!nested1.equals(nested2)) {
/* 277 */       String canonical1 = canonicalize(nested1);
/* 278 */       String canonical2 = canonicalize(nested2);
/* 279 */       if (!canonical1.equals(canonical2)) {
/* 280 */         return false;
/*     */       }
/*     */     } 
/* 283 */     String root1 = u1.getFile().substring(0, separator1);
/* 284 */     String root2 = u2.getFile().substring(0, separator2);
/*     */     try {
/* 286 */       return super.sameFile(new URL(root1), new URL(root2));
/*     */     }
/* 288 */     catch (MalformedURLException malformedURLException) {
/*     */ 
/*     */       
/* 291 */       return super.sameFile(u1, u2);
/*     */     } 
/*     */   }
/*     */   private String canonicalize(String path) {
/* 295 */     return path.replace("!/", "/");
/*     */   }
/*     */   
/*     */   public JarFile getRootJarFileFromUrl(URL url) throws IOException {
/* 299 */     String spec = url.getFile();
/* 300 */     int separatorIndex = spec.indexOf("!/");
/* 301 */     if (separatorIndex == -1) {
/* 302 */       throw new MalformedURLException("Jar URL does not contain !/ separator");
/*     */     }
/* 304 */     String name = spec.substring(0, separatorIndex);
/* 305 */     return getRootJarFile(name);
/*     */   }
/*     */   
/*     */   private JarFile getRootJarFile(String name) throws IOException {
/*     */     try {
/* 310 */       if (!name.startsWith("file:")) {
/* 311 */         throw new IllegalStateException("Not a file URL");
/*     */       }
/* 313 */       String path = name.substring("file:".length());
/* 314 */       File file = new File(URLDecoder.decode(path, "UTF-8"));
/* 315 */       Map<File, JarFile> cache = rootFileCache.get();
/* 316 */       JarFile result = (cache != null) ? cache.get(file) : null;
/* 317 */       if (result == null) {
/* 318 */         result = new JarFile(file);
/* 319 */         addToRootFileCache(file, result);
/*     */       } 
/* 321 */       return result;
/*     */     }
/* 323 */     catch (Exception ex) {
/* 324 */       throw new IOException("Unable to open root Jar file '" + name + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addToRootFileCache(File sourceFile, JarFile jarFile) {
/* 334 */     Map<File, JarFile> cache = rootFileCache.get();
/* 335 */     if (cache == null) {
/* 336 */       cache = new ConcurrentHashMap<>();
/* 337 */       rootFileCache = new SoftReference<>(cache);
/*     */     } 
/* 339 */     cache.put(sourceFile, jarFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUseFastConnectionExceptions(boolean useFastConnectionExceptions) {
/* 350 */     JarURLConnection.setUseFastExceptions(useFastConnectionExceptions);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/jar/Handler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */