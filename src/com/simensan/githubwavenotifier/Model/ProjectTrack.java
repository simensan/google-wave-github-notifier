package com.simensan.githubwavenotifier.Model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ProjectTrack {

   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Long id;

   @Persistent
   private String projectName;
   
   @Persistent
   private String waveID;

   @Persistent
   private String waveletID;
   
   @Persistent
   private String uuid;

   public ProjectTrack(String projectName, String waveID, String waveletID, String uuid) {
      this.projectName = projectName;
      this.waveID = waveID;
      this.waveletID = waveletID;
      this.uuid = uuid;
   }
   
   public Long getId() {
      return id;
   }

   public String getWaveID() {
      return waveID;
   }

   public String getWaveletID() {
      return waveletID;
   }

   public String getProjectName() {
      return projectName;
   }
   
   public String getUuid() {
	   return uuid;
   }
}
