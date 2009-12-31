package com.fauxpas;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.wave.api.*;

import com.fauxpas.Model.*;

@SuppressWarnings("serial")
public class FauxpasRobotServlet extends AbstractRobotServlet {	
	private static final Logger log = Logger.getLogger(FauxpasCommitServlet.class.getName());
	
	@Override
	public void processEvents(RobotMessageBundle bundle) {
		Wavelet wavelet = bundle.getWavelet();
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        
		if(bundle.getEvents().size() == 0) {
			pushMessages(bundle); //Cron tick
		} 
		else if (bundle.wasSelfAdded()) {
			log.warning("Added to wavelet");
			
			String projectName = wavelet.getTitle();
			
			log.warning("Projectname: " + projectName);
			
			UUID uuid = UUID.randomUUID();
			String uniqueid = uuid.toString().substring(0, 7);
			
			log.warning("UUID: " + uniqueid);
			
			ProjectTrack projectTrack = new ProjectTrack(projectName, 
					wavelet.getWaveId(), wavelet.getWaveletId(), uniqueid.toString());
			
			pm.makePersistent(projectTrack);
			
			Blip blip = wavelet.appendBlip();
			TextView textView = blip.getDocument();
			textView.append("Adding commit messages from project '" + projectName + "' here. \n"
							+"Use the url: http://fauxpasnotifier.appspot.com/commit/" + uniqueid
							+" in the http hook of github.");
	    }
	}
	
	@SuppressWarnings("unchecked")
	private void pushMessages(RobotMessageBundle bundle) {
		try {
			PersistenceManager pm =  PMF.getInstance().getPersistenceManager();
		    List<Commit> commits = (List<Commit>) pm.newQuery(Commit.class)
		          .execute();
		    for (Commit commit : commits) {
		    	log.warning("Waveid: " + commit.getWaveId());
		    	log.warning("Waveletid: " + commit.getWaveletId());
		    	
		    	if(commit == null || commit.getWaveId() == null || commit.getWaveletId() == null) {
		    		pm.deletePersistent(commit);
		    		continue;
		    	}
		    	
		     	Wavelet wavelet = bundle.getWavelet(commit.getWaveId(), commit.getWaveletId());
		    	log.warning("Wavelet info: " + wavelet.getTitle() + wavelet.getWaveId());
		    	
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
