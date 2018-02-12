/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.util.Objects;
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
/*     */ final class StringSequence
/*     */   implements CharSequence
/*     */ {
/*     */   private final String source;
/*     */   private final int start;
/*     */   private final int end;
/*     */   private int hash;
/*     */   
/*     */   StringSequence(String source) {
/*  39 */     this(source, 0, (source != null) ? source.length() : -1);
/*     */   }
/*     */   
/*     */   StringSequence(String source, int start, int end) {
/*  43 */     Objects.requireNonNull(source, "Source must not be null");
/*  44 */     if (start < 0) {
/*  45 */       throw new StringIndexOutOfBoundsException(start);
/*     */     }
/*  47 */     if (end > source.length()) {
/*  48 */       throw new StringIndexOutOfBoundsException(end);
/*     */     }
/*  50 */     this.source = source;
/*  51 */     this.start = start;
/*  52 */     this.end = end;
/*     */   }
/*     */   
/*     */   public StringSequence subSequence(int start) {
/*  56 */     return subSequence(start, length());
/*     */   }
/*     */ 
/*     */   
/*     */   public StringSequence subSequence(int start, int end) {
/*  61 */     int subSequenceStart = this.start + start;
/*  62 */     int subSequenceEnd = this.start + end;
/*  63 */     if (subSequenceStart > this.end) {
/*  64 */       throw new StringIndexOutOfBoundsException(start);
/*     */     }
/*  66 */     if (subSequenceEnd > this.end) {
/*  67 */       throw new StringIndexOutOfBoundsException(end);
/*     */     }
/*  69 */     if (start == 0 && subSequenceEnd == this.end) {
/*  70 */       return this;
/*     */     }
/*  72 */     return new StringSequence(this.source, subSequenceStart, subSequenceEnd);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  76 */     return (length() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/*  81 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  86 */     return this.source.charAt(this.start + index);
/*     */   }
/*     */   
/*     */   public int indexOf(char ch) {
/*  90 */     return this.source.indexOf(ch, this.start) - this.start;
/*     */   }
/*     */   
/*     */   public int indexOf(String str) {
/*  94 */     return this.source.indexOf(str, this.start) - this.start;
/*     */   }
/*     */   
/*     */   public int indexOf(String str, int fromIndex) {
/*  98 */     return this.source.indexOf(str, this.start + fromIndex) - this.start;
/*     */   }
/*     */   
/*     */   public boolean startsWith(CharSequence prefix) {
/* 102 */     return startsWith(prefix, 0);
/*     */   }
/*     */   
/*     */   public boolean startsWith(CharSequence prefix, int offset) {
/* 106 */     int prefixLength = prefix.length();
/* 107 */     if (length() - prefixLength - offset < 0) {
/* 108 */       return false;
/*     */     }
/* 110 */     int prefixOffset = 0;
/* 111 */     int sourceOffset = offset;
/* 112 */     while (prefixLength-- != 0) {
/* 113 */       if (charAt(sourceOffset++) != prefix.charAt(prefixOffset++)) {
/* 114 */         return false;
/*     */       }
/*     */     } 
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 122 */     if (this == obj) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (!(obj instanceof CharSequence)) {
/* 126 */       return false;
/*     */     }
/* 128 */     CharSequence other = (CharSequence)obj;
/* 129 */     int n = length();
/* 130 */     if (n != other.length()) {
/* 131 */       return false;
/*     */     }
/* 133 */     int i = 0;
/* 134 */     while (n-- != 0) {
/* 135 */       if (charAt(i) != other.charAt(i)) {
/* 136 */         return false;
/*     */       }
/* 138 */       i++;
/*     */     } 
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 145 */     int hash = this.hash;
/* 146 */     if (hash == 0 && length() > 0) {
/* 147 */       for (int i = this.start; i < this.end; i++) {
/* 148 */         hash = 31 * hash + this.source.charAt(i);
/*     */       }
/* 150 */       this.hash = hash;
/*     */     } 
/* 152 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     return this.source.substring(this.start, this.end);
/*     */   }
/*     */ }


/* Location:              /home/Bun/projects/cloud-shell/cloud-shell.jar!/org/springframework/boot/loader/jar/StringSequence.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.0.0 - February 12, 2018
 */