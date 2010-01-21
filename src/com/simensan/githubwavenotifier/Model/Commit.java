package com.simensan.githubwavenotifier.Model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Commit {

   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Long id;

   @Persistent
   private String waveId;

   @Persistent
   private String waveletId;

   @Persistent
   private String msg;

   @Persistent
   private Boolean isPublished;

   public Commit(String msg, String waveId, String waveletId) {
      this.msg = msg;
      this.waveId = waveId;
      this.waveletId = waveletId;
      this.isPublished = Boolean.FALSE;
   }
   
   public Long getId() {
      return id;
   }

   public String getWaveId() {
      return waveId;
   }

   public String getWaveletId() {
      return waveletId;
   }

   public String getMsg() {
      return msg;
   }
   
   public Boolean isPublished() {
	   return isPublished;
   }
}
