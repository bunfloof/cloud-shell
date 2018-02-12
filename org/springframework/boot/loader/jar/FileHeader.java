package org.springframework.boot.loader.jar;

interface FileHeader {
  boolean hasName(CharSequence paramCharSequence, char paramChar);
  
  long getLocalHeaderOffset();
  
  long getCompressedSize();
  
  long getSize();
  
  int getMethod();
}


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/jar/FileHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */