package com.simensan.githubwavenotifier;

import com.google.wave.api.ProfileServlet;

@SuppressWarnings("serial")
public class NotifierProfileServlet extends ProfileServlet {

   @Override
   public String getRobotName() {
      return "Github Commit Spy";
   }
}
