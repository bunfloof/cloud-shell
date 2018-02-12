/*    */ package BOOT-INF.classes.cloudshell.app;
/*    */ 
/*    */ import cloudshell.app.terminal.PtySession;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AppContext
/*    */ {
/* 17 */   public static final Map<String, PtySession> INSTANCES = new ConcurrentHashMap<>();
/* 18 */   public static final Map<String, PtySession> SESSION_MAP = new ConcurrentHashMap<>();
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/AppContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */