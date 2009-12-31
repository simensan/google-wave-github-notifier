package com.fauxpas;

import com.google.wave.api.ProfileServlet;

@SuppressWarnings("serial")
public class FauxpasProfileServlet extends ProfileServlet {

   @Override
   public String getRobotName() {
      return "Github Commit Spy";
   }
}
