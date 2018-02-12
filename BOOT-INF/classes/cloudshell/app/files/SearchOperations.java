/*    */ package BOOT-INF.classes.cloudshell.app.files;
/*    */ 
/*    */ import cloudshell.app.files.search.SearchResult;
/*    */ import cloudshell.app.files.search.SearchTask;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class SearchOperations
/*    */ {
/* 23 */   private final Map<String, SearchTask> pendingOperations = new ConcurrentHashMap<>();
/* 24 */   private final ExecutorService threadPool = Executors.newFixedThreadPool(5);
/*    */   
/*    */   public String createSearchTask(String folder, String searchText) {
/* 27 */     SearchTask task = new SearchTask(folder, searchText);
/* 28 */     this.pendingOperations.put(task.getId(), task);
/* 29 */     this.threadPool.submit((Runnable)task);
/* 30 */     return task.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public SearchResult getSearchResult(String id, int fileIndex, int folderIndex) {
/* 35 */     SearchTask task = this.pendingOperations.get(id);
/*    */     
/* 37 */     SearchResult res = new SearchResult(task.isDone(), (List)task.getFiles().stream().skip(fileIndex).collect(Collectors.toList()));
/* 38 */     return res;
/*    */   }
/*    */   
/*    */   public void cancelSearch(String id) {
/* 42 */     SearchTask task = this.pendingOperations.get(id);
/* 43 */     task.stop();
/*    */   }
/*    */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/BOOT-INF/classes/cloudshell/app/files/SearchOperations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */