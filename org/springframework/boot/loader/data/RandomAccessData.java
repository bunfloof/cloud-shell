package org.springframework.boot.loader.data;

import java.io.IOException;
import java.io.InputStream;

public interface RandomAccessData {
  InputStream getInputStream() throws IOException;
  
  RandomAccessData getSubsection(long paramLong1, long paramLong2);
  
  byte[] read() throws IOException;
  
  byte[] read(long paramLong1, long paramLong2) throws IOException;
  
  long getSize();
}


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/data/RandomAccessData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */