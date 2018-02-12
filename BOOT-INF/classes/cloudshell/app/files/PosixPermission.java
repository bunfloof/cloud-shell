/*    */ package BOOT-INF.classes.cloudshell.app.files;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PosixPermission
/*    */ {
/*    */   private String owner;
/*    */   private String group;
/*    */   private String ownerAccess;
/*    */   private String groupAccess;
/*    */   private String otherAccess;
/*    */   private boolean executable;
/*    */   
/*    */   public String getOwner() {
/* 19 */     return this.owner;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOwner(String owner) {
/* 26 */     this.owner = owner;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getGroup() {
/* 33 */     return this.group;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setGroup(String group) {
/* 40 */     this.group = group;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOwnerAccess() {
/* 47 */     return this.ownerAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOwnerAccess(String ownerAccess) {
/* 54 */     this.ownerAccess = ownerAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getGroupAccess() {
/* 61 */     return this.groupAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setGroupAccess(String groupAccess) {
/* 68 */     this.groupAccess = groupAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOtherAccess() {
/* 75 */     return this.otherAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOtherAccess(String otherAccess) {
/* 82 */     this.otherAccess = otherAccess;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isExecutable() {
/* 89 */     return this.executable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExecutable(boolean executable) {
/* 96 */     this.executable = executable;
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/PosixPermission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */