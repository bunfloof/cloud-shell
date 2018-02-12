/*     */ package BOOT-INF.classes.cloudshell.app.terminal;
/*     */ 
/*     */ import cloudshell.app.terminal.SshPtyProcess;
/*     */ import com.pty4j.PtyProcess;
/*     */ import com.pty4j.PtyProcessBuilder;
/*     */ import com.pty4j.WinSize;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.springframework.web.socket.TextMessage;
/*     */ import org.springframework.web.socket.WebSocketMessage;
/*     */ import org.springframework.web.socket.WebSocketSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PtySession
/*     */   implements Runnable
/*     */ {
/*     */   private String id;
/*     */   private WebSocketSession ws;
/*     */   private Thread t;
/*     */   private PtyProcess pty;
/*     */   private OutputStream out;
/*     */   private InputStream os;
/*     */   private boolean ptyAllowed;
/*     */   private SshPtyProcess proc;
/*     */   
/*     */   public PtySession() throws Exception {
/*  37 */     this.id = UUID.randomUUID().toString();
/*     */     
/*  39 */     this.ptyAllowed = true;
/*     */ 
/*     */     
/*  42 */     (new String[1])[0] = "cmd";
/*  43 */     (new String[2])[0] = "/bin/bash"; (new String[2])[1] = "-l";
/*  44 */     (new String[1])[0] = System.getProperty("app.default-shell"); String[] cmd = "auto".equals(System.getProperty("app.default-shell")) ? (System.getProperty("os.name").toLowerCase().contains("windows") ? new String[1] : new String[2]) : new String[1];
/*     */ 
/*     */     
/*     */     try {
/*  48 */       Map<String, String> env = new HashMap<>();
/*  49 */       env.put("TERM", "xterm");
/*  50 */       PtyProcessBuilder pb = new PtyProcessBuilder(cmd);
/*  51 */       pb.setRedirectErrorStream(true);
/*  52 */       pb.setEnvironment(env);
/*  53 */       this.pty = pb.start();
/*  54 */       this.os = this.pty.getInputStream();
/*  55 */       this.out = this.pty.getOutputStream();
/*  56 */       System.out.println("Pty created");
/*  57 */     } catch (Exception e) {
/*  58 */       e.printStackTrace();
/*  59 */       if ("true".equals(System.getProperty("app.fallback-local-ssh"))) {
/*  60 */         this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  67 */           .proc = new SshPtyProcess(System.getProperty("app.local-ssh-host"), System.getProperty("app.local-ssh-user"), Integer.parseInt(System.getProperty("app.local-ssh-port")), System.getProperty("app.local-ssh-password"), System.getProperty("app.local-ssh-keyfile"), System.getProperty("app.local-ssh-passphrase"));
/*  68 */         this.proc.connect();
/*  69 */         this.os = this.proc.getIn();
/*  70 */         this.out = this.proc.getOut();
/*     */       } 
/*     */     } 
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
/*     */   public void start() {
/* 101 */     this.t = new Thread(this);
/* 102 */     this.t.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 108 */       byte[] buf = new byte[512];
/*     */       try {
/*     */         while (true) {
/* 111 */           int x = this.os.read(buf);
/* 112 */           if (x == -1) {
/* 113 */             System.out.println("Stream end");
/*     */             break;
/*     */           } 
/* 116 */           this.ws.sendMessage((WebSocketMessage)new TextMessage(new String(buf, 0, x, "utf-8")));
/*     */         }
/*     */       
/* 119 */       } catch (Exception e) {
/* 120 */         e.printStackTrace();
/*     */       } 
/*     */       
/* 123 */       if (this.ptyAllowed && this.pty != null) {
/* 124 */         System.out.println("Waiting for pty...");
/* 125 */         this.pty.waitFor();
/* 126 */       } else if (this.proc != null) {
/* 127 */         System.out.println("Waiting fot proc...");
/* 128 */         this.proc.waitFor();
/*     */       } 
/* 130 */     } catch (Exception e) {
/* 131 */       e.printStackTrace();
/*     */     } finally {
/* 133 */       if (this.pty != null) {
/* 134 */         this.pty.destroy();
/*     */       }
/* 136 */       if (this.proc != null) {
/* 137 */         this.proc.close();
/*     */       }
/*     */     } 
/* 140 */     System.out.println("Thread finished");
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
/*     */   public void sendData(String text) {
/*     */     try {
/* 168 */       this.out.write(text.getBytes("utf-8"));
/* 169 */       this.out.flush();
/*     */       
/* 171 */       System.out.println("sent text");
/* 172 */     } catch (Exception e) {
/* 173 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 178 */     if (this.pty != null) {
/* 179 */       this.pty.destroyForcibly();
/*     */     }
/* 181 */     if (this.proc != null) {
/* 182 */       this.proc.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void resizePty(int row, int col) {
/* 187 */     if (this.pty != null) {
/* 188 */       this.pty.setWinSize(new WinSize(col, row));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 196 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 203 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketSession getWs() {
/* 210 */     return this.ws;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWs(WebSocketSession ws) {
/* 217 */     this.ws = ws;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Thread getT() {
/* 224 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setT(Thread t) {
/* 231 */     this.t = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PtyProcess getPty() {
/* 238 */     return this.pty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPty(PtyProcess pty) {
/* 245 */     this.pty = pty;
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/terminal/PtySession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */