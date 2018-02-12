package org.springframework.boot.loader.jar;

import org.springframework.boot.loader.data.RandomAccessData;

interface CentralDirectoryVisitor {
  void visitStart(CentralDirectoryEndRecord paramCentralDirectoryEndRecord, RandomAccessData paramRandomAccessData);
  
  void visitFileHeader(CentralDirectoryFileHeader paramCentralDirectoryFileHeader, int paramInt);
  
  void visitEnd();
}


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/jar/CentralDirectoryVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */