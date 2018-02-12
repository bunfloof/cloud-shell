/*    */ package BOOT-INF.classes.cloudshell.app.terminal;
/*    */ 
/*    */ import cloudshell.app.AppContext;
/*    */ import cloudshell.app.Application;
/*    */ import cloudshell.app.terminal.PtySession;
/*    */ import io.jsonwebtoken.Jwts;
/*    */ import java.net.URI;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.web.socket.CloseStatus;
/*    */ import org.springframework.web.socket.TextMessage;
/*    */ import org.springframework.web.socket.WebSocketSession;
/*    */ import org.springframework.web.socket.handler.AbstractWebSocketHandler;
/*    */ import org.springframework.web.util.UriComponentsBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TerminalWebsocketHandler
/*    */   extends AbstractWebSocketHandler
/*    */ {
/* 24 */   private static final Logger logger = LoggerFactory.getLogger(cloudshell.app.terminal.TerminalWebsocketHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
/* 29 */     logger.info("Incoming session: " + session.getId() + " url: " + session
/* 30 */         .getUri());
/*    */     
/* 32 */     synchronized (this) {
/* 33 */       URI uri = session.getUri();
/*    */       
/* 35 */       MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
/*    */       
/*    */       try {
/* 38 */         String token = (String)params.getFirst("token");
/* 39 */         Jwts.parser().setSigningKey(Application.SECRET_KEY)
/* 40 */           .parseClaimsJws(token);
/* 41 */       } catch (Exception exception) {
/* 42 */         exception.printStackTrace();
/* 43 */         session.close(CloseStatus.BAD_DATA);
/*    */         
/*    */         return;
/*    */       } 
/* 47 */       String appId = (String)params.getFirst("id");
/* 48 */       PtySession app = (PtySession)AppContext.INSTANCES.get(appId);
/* 49 */       if (app == null) {
/* 50 */         logger.error("No instance for session id: " + session.getId() + " data: " + uri);
/*    */         
/* 52 */         session.close(CloseStatus.BAD_DATA);
/*    */         return;
/*    */       } 
/* 55 */       app.setWs(session);
/* 56 */       AppContext.SESSION_MAP.put(session.getId(), app);
/* 57 */       app.start();
/*    */     } 
/*    */     
/* 60 */     logger.info("Handshake complete session: " + session.getId() + " url: " + session
/* 61 */         .getUri());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
/* 68 */     System.out.println("Message received: " + (String)message.getPayload());
/*    */     
/* 70 */     PtySession app = (PtySession)AppContext.SESSION_MAP.get(session.getId());
/*    */     
/* 72 */     if (app == null) {
/* 73 */       logger.error("Invalid app id: " + session.getId());
/* 74 */       session.close(CloseStatus.BAD_DATA);
/*    */       
/*    */       return;
/*    */     } 
/* 78 */     app.sendData((String)message.getPayload());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
/* 84 */     System.out.println("Session closed: " + session.getId());
/* 85 */     PtySession app = (PtySession)AppContext.SESSION_MAP.get(session.getId());
/* 86 */     if (app != null) {
/* 87 */       app.close();
/* 88 */       AppContext.SESSION_MAP.remove(session.getId());
/* 89 */       logger.info("Session closed");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/terminal/TerminalWebsocketHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */