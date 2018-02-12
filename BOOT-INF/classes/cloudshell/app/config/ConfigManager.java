/*     */ package BOOT-INF.classes.cloudshell.app.config;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import org.bouncycastle.asn1.x500.X500Name;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.cert.X509v3CertificateBuilder;
/*     */ import org.bouncycastle.operator.ContentSigner;
/*     */ import org.bouncycastle.operator.OperatorCreationException;
/*     */ import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
/*     */ import org.springframework.core.env.Environment;
/*     */ 
/*     */ public class ConfigManager
/*     */ {
/*  39 */   private static Path CONFIG_PATH = null;
/*     */   
/*     */   private static final String CERTIFICATE_ALIAS = "cloudshell-cert";
/*     */   private static final String CERTIFICATE_ALGORITHM = "RSA";
/*     */   private static final String CERTIFICATE_DN = "CN=easy.cloudshell.app, O=easy.cloudshell.app, L=easy.cloudshell.app, ST=il, C=c";
/*     */   private static final int CERTIFICATE_BITS = 2048;
/*     */   
/*     */   static {
/*  47 */     Path configPath = null;
/*  48 */     String configPathStr = System.getenv("EASY_CLOUD_SHELL_CONFIG_DIR");
/*  49 */     if (configPathStr == null || configPathStr.length() < 1) {
/*  50 */       configPath = Paths.get(System.getProperty("user.home"), new String[] { ".easy-cloud-shell" });
/*     */     } else {
/*     */       
/*  53 */       configPath = Paths.get(configPathStr, new String[] { ".easy-cloud-shell" });
/*     */     } 
/*  55 */     CONFIG_PATH = configPath;
/*     */     
/*  57 */     try { Files.createDirectories(configPath, (FileAttribute<?>[])new FileAttribute[0]); }
/*  58 */     catch (FileAlreadyExistsException fileAlreadyExistsException) {  }
/*  59 */     catch (IOException e)
/*  60 */     { e.printStackTrace();
/*  61 */       System.exit(-1); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveUserDetails() throws FileNotFoundException, IOException {
/*  69 */     Path userConfigPath = CONFIG_PATH.resolve("users.properties");
/*  70 */     Properties prop = new Properties();
/*  71 */     prop.put("app.default-user", System.getProperty("app.default-user"));
/*  72 */     prop.put("app.default-pass", System.getProperty("app.default-pass"));
/*  73 */     prop.put("app.default-shell", System.getProperty("app.default-shell"));
/*     */     
/*  75 */     OutputStream out = new FileOutputStream(userConfigPath.toFile()); 
/*  76 */     try { prop.store(out, "User details");
/*  77 */       out.close(); }
/*     */     catch (Throwable throwable) { try { out.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  81 */      } public static void loadUserDetails(Environment env) { Path userConfigPath = CONFIG_PATH.resolve("users.properties");
/*  82 */     Properties prop = new Properties();
/*     */     
/*  84 */     if (Files.exists(userConfigPath, new java.nio.file.LinkOption[0])) {
/*     */       
/*  86 */       try { InputStream in = new FileInputStream(userConfigPath.toFile()); 
/*  87 */         try { prop.load(in);
/*  88 */           in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (FileNotFoundException e)
/*  89 */       { e.printStackTrace(); }
/*  90 */       catch (IOException e)
/*  91 */       { e.printStackTrace(); }
/*     */     
/*     */     }
/*     */     
/*  95 */     if (prop.size() < 1) {
/*  96 */       prop.put("app.default-user", env
/*  97 */           .getProperty("app.default-admin-user"));
/*  98 */       prop.put("app.default-pass", env
/*  99 */           .getProperty("app.default-admin-pass"));
/* 100 */       prop.put("app.default-shell", "auto");
/*     */     } 
/*     */     
/* 103 */     for (String propName : prop.stringPropertyNames()) {
/* 104 */       System.setProperty(propName, prop.getProperty(propName));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkAndConfigureSSL() {
/*     */     try {
/* 116 */       Path certPath = CONFIG_PATH.resolve("cert.p12");
/* 117 */       Path certConf = CONFIG_PATH.resolve("cert.properties");
/* 118 */       if (!Files.exists(certConf, new java.nio.file.LinkOption[0])) {
/* 119 */         createSelfSignedCertificate(certPath, certConf);
/*     */       }
/*     */       
/* 122 */       System.out.println("Loading existing certificate");
/* 123 */       Properties certProp = new Properties();
/* 124 */       InputStream in = new FileInputStream(certConf.toFile()); 
/* 125 */       try { certProp.load(in);
/* 126 */         in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/* 128 */        System.setProperty("server.ssl.key-store-password", certProp
/* 129 */           .getProperty("key-store-password"));
/* 130 */       System.setProperty("server.ssl.key-alias", certProp
/* 131 */           .getProperty("key-alias"));
/* 132 */       System.setProperty("server.ssl.key-store", certPath.toString());
/* 133 */     } catch (Exception e) {
/* 134 */       e.printStackTrace();
/* 135 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createSelfSignedCertificate(Path certPath, Path certConf) throws IOException, NoSuchAlgorithmException, OperatorCreationException, CertificateException, KeyStoreException {
/* 143 */     String keystorePassword = UUID.randomUUID().toString();
/* 144 */     X509Certificate cert = null;
/*     */ 
/*     */     
/* 147 */     KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
/* 148 */     keyPairGenerator.initialize(2048, new SecureRandom());
/* 149 */     KeyPair keyPair = keyPairGenerator.generateKeyPair();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(new X500Name("CN=easy.cloudshell.app, O=easy.cloudshell.app, L=easy.cloudshell.app, ST=il, C=c"), BigInteger.valueOf(System.currentTimeMillis()), new Date(System.currentTimeMillis() - 86400000L), new Date(System.currentTimeMillis() + 315360000000L), new X500Name("CN=easy.cloudshell.app, O=easy.cloudshell.app, L=easy.cloudshell.app, ST=il, C=c"), SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
/*     */     
/* 160 */     JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
/*     */     
/* 162 */     ContentSigner signer = builder.build(keyPair.getPrivate());
/*     */     
/* 164 */     byte[] certBytes = certBuilder.build(signer).getEncoded();
/*     */     
/* 166 */     CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
/*     */     
/* 168 */     cert = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
/*     */     
/* 170 */     KeyStore keyStore = KeyStore.getInstance("PKCS12");
/* 171 */     keyStore.load(null, null);
/* 172 */     keyStore.setKeyEntry("cloudshell-cert", keyPair.getPrivate(), keystorePassword
/* 173 */         .toCharArray(), new Certificate[] { cert });
/*     */ 
/*     */     
/* 176 */     OutputStream out = new FileOutputStream(certPath.toFile()); 
/* 177 */     try { keyStore.store(out, keystorePassword.toCharArray());
/* 178 */       out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 180 */      Properties certProps = new Properties();
/* 181 */     certProps.setProperty("key-store-password", keystorePassword);
/* 182 */     certProps.setProperty("key-alias", "cloudshell-cert");
/* 183 */     certProps.setProperty("key-store", certPath.toString());
/*     */     
/* 185 */     OutputStream outputStream1 = new FileOutputStream(certConf.toFile()); 
/* 186 */     try { certProps.store(outputStream1, "Easy cloud shell self signed certificate details");
/*     */       
/* 188 */       outputStream1.close(); } catch (Throwable throwable) { try { outputStream1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 190 */      System.out.println("Self signed certificate created");
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/config/ConfigManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */