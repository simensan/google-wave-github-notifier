package com.simensan.githubwavenotifier;

import java.io.IOException;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.simensan.githubwavenotifier.Model.*;

@SuppressWarnings("serial")
public class NotifierCommitServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(NotifierCommitServlet.class.getName());

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String[] d = req.getRequestURI().toString().split("/");
		String hookId = d[2];
		String jsonString = req.getParameter("payload"); 

		JSONTokener jt = new JSONTokener(jsonString);
		JSONObject jsonObject;
		JSONObject repository;
		JSONArray commits;
		String repoName = "";
		Vector<String> commitMsgs = new Vector<String>();
		
		try {
			jsonObject = new JSONObject(jt);
			repository = (JSONObject) jsonObject.get("repository");
			commits = (JSONArray) jsonObject.getJSONArray("commits");
			
			repoName = repository.getString("name");
			
			for(int i = 0; i < commits.length(); i++) {
				JSONObject commitObj = commits.getJSONObject(i);
				commitMsgs.add(commitObj.getString("message"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		Query query = pm.newQuery(ProjectTrack.class, "projectName == '" + repoName + "' && uuid == '" + hookId + "'");
		
		List<ProjectTrack> projects = (List<ProjectTrack>) query.execute();
		    
		if(projects.size() > 0) {
			ProjectTrack project = projects.get(0);
			
			for(int i = 0; i < commitMsgs.size(); i++) {
				Commit commit = new Commit(commitMsgs.get(i), project.getWaveID(), project.getWaveletID());
				pm.makePersistent(commit);
			}
				
			resp.setStatus(200);
		} else {
			log.warning("Projecttrack is null");
		}
		
		pm.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		doGet(req,rsp);
	} 
}

