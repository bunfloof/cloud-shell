package org.springframework.boot.loader.archive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.jar.Manifest;

public interface Archive extends Iterable<Archive.Entry> {
  URL getUrl() throws MalformedURLException;
  
  Manifest getManifest() throws IOException;
  
  List<Archive> getNestedArchives(EntryFilter paramEntryFilter) throws IOException;
  
  public static interface EntryFilter {
    boolean matches(Archive.Entry param1Entry);
  }
  
  public static interface Entry {
    boolean isDirectory();
    
    String getName();
  }
}


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/archive/Archive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */