/*     */ package BOOT-INF.classes.cloudshell.app.terminal;
/*     */ 
/*     */ import com.jcraft.jsch.ChannelShell;
/*     */ import com.jcraft.jsch.JSch;
/*     */ import com.jcraft.jsch.Session;
/*     */ import com.jcraft.jsch.UserInfo;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public class SshPtyProcess
/*     */ {
/*     */   private JSch jsch;
/*     */   private Session session;
/*     */   private String host;
/*     */   private String user;
/*     */   private int port;
/*     */   private String keyFile;
/*     */   private String passphrase;
/*     */   private String password;
/*     */   private OutputStream out;
/*     */   private ChannelShell shell;
/*     */   private InputStream in;
/*     */   
/*     */   public SshPtyProcess(String host, String user, int port, String password, String keyFile, String passphrase) {
/*  44 */     this.host = host;
/*  45 */     this.user = user;
/*  46 */     this.port = port;
/*  47 */     this.password = password;
/*  48 */     this.keyFile = keyFile;
/*  49 */     this.passphrase = passphrase;
/*     */   }
/*     */   
/*     */   public void connect() throws Exception {
/*  53 */     MyUserInfo info = new MyUserInfo(this);
/*  54 */     this.jsch = new JSch();
/*  55 */     JSch.setConfig("MaxAuthTries", "5");
/*     */     
/*  57 */     if (this.keyFile != null && this.keyFile.length() > 0) {
/*  58 */       this.jsch.addIdentity(this.keyFile);
/*     */     }
/*     */     
/*  61 */     this.session = this.jsch.getSession(this.user, this.host, this.port);
/*     */     
/*  63 */     this.session.setUserInfo((UserInfo)info);
/*     */     
/*  65 */     this.session.setPassword(info.getPassword());
/*     */     
/*  67 */     this.session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
/*     */ 
/*     */     
/*  70 */     this.session.connect();
/*     */     
/*  72 */     System.out.println("Client version: " + this.session.getClientVersion());
/*  73 */     System.out.println("Server host: " + this.session.getHost());
/*  74 */     System.out.println("Server version: " + this.session.getServerVersion());
/*  75 */     System.out.println("Hostkey: " + this.session
/*  76 */         .getHostKey().getFingerPrint(this.jsch));
/*     */     
/*  78 */     this.shell = (ChannelShell)this.session.openChannel("shell");
/*  79 */     this.in = this.shell.getInputStream();
/*  80 */     this.out = this.shell.getOutputStream();
/*  81 */     this.shell.connect();
/*     */   }
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
/*     */   public JSch getJsch() {
/* 126 */     return this.jsch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJsch(JSch jsch) {
/* 133 */     this.jsch = jsch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Session getSession() {
/* 140 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSession(Session session) {
/* 147 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 154 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHost(String host) {
/* 161 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 168 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String user) {
/* 175 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 182 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 189 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeyFile() {
/* 196 */     return this.keyFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyFile(String keyFile) {
/* 203 */     this.keyFile = keyFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassphrase() {
/* 210 */     return this.passphrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassphrase(String passphrase) {
/* 217 */     this.passphrase = passphrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 224 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 231 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOut() {
/* 238 */     return this.out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOut(OutputStream out) {
/* 245 */     this.out = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelShell getShell() {
/* 252 */     return this.shell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShell(ChannelShell shell) {
/* 259 */     this.shell = shell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getIn() {
/* 266 */     return this.in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIn(InputStream in) {
/* 273 */     this.in = in;
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     try {
/* 278 */       this.shell.disconnect();
/* 279 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*     */     try {
/* 283 */       this.session.disconnect();
/* 284 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 288 */     System.out.println("Ssh shell closed");
/*     */   }
/*     */   
/*     */   public void waitFor() {
/* 292 */     while (this.shell.isConnected()) {
/*     */       try {
/* 294 */         Thread.sleep(1000L);
/* 295 */       } catch (InterruptedException e) {
/*     */         
/* 297 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/terminal/SshPtyProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */