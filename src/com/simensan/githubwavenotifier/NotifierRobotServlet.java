package com.simensan.githubwavenotifier;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.wave.api.*;
import com.simensan.githubwavenotifier.Model.*;


@SuppressWarnings("serial")
public class NotifierRobotServlet extends AbstractRobotServlet {	
	private static final Logger log = Logger.getLogger(NotifierCommitServlet.class.getName());
	
	@Override
	public void processEvents(RobotMessageBundle bundle) {
		Wavelet wavelet = bundle.getWavelet();
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        
		if(bundle.getEvents().size() == 0) {
			pushMessages(bundle); //Cron tick
		} else if (bundle.wasSelfAdded()) {
			String projectName = wavelet.getTitle();

			UUID uuid = UUID.randomUUID();
			String uniqueid = uuid.toString().substring(0, 7);
			
			ProjectTrack projectTrack = new ProjectTrack(projectName, 
					wavelet.getWaveId(), wavelet.getWaveletId(), uniqueid.toString());
			
			pm.makePersistent(projectTrack);
			
			Blip blip = wavelet.appendBlip();
			TextView textView = blip.getDocument();
			textView.append("Adding commit messages from project '" + projectName + "' here. \n"
							+"Use the url: http://<your app url>.appspot.com/commit/" + uniqueid
							+" in the http hook of github.");
	    }
	}
	
	@SuppressWarnings("unchecked")
	private void pushMessages(RobotMessageBundle bundle) {
		try {
			PersistenceManager pm =  PMF.getInstance().getPersistenceManager();
		    List<Commit> commits = (List<Commit>) pm.newQuery(Commit.class).execute();
		    
		    for (Commit commit : commits) {
		    	
		    	if(commit == null || commit.getWaveId() == null || commit.getWaveletId() == null) {
		    		pm.deletePersistent(commit);
		    		continue;
		    	}
		    	
		     	Wavelet wavelet = bundle.getWavelet(commit.getWaveId(), commit.getWaveletId());
		    	
	    		if (wavelet != null) {
	    			Blip blip = wavelet.appendBlip();
	    			TextView view = blip.getDocument();
	    			view.append("Commit: " + commit.getMsg());
	    		}
	    		
	    		pm.deletePersistent(commit);
		    }
		    
		    pm.close();
		 } catch (DatastoreTimeoutException e) {
		 }
	}
}