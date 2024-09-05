package socialmedia;

import java.util.ArrayList;
import java.io.Serializable;

public class SocialPost implements Serializable{
	protected static int postCount = 0;
	protected int id;
	protected String message;
	protected int handleID;
	protected ArrayList<Integer> childIDs = new ArrayList<Integer>();
	
	public SocialPost(int newHandleID, String message){
		this.handleID = newHandleID;
		this.message = message;
		this.id = postCount;
		postCount++;
	}
	
	public void addChildID(int newChildID){this.childIDs.add(newChildID);}
	public int getPostID(){return this.id;}
	public int getHandleID(){return this.handleID;}
	public String getMessage(){return this.message;}
	public ArrayList<Integer> getChildIDs(){return this.childIDs;}
	public void removeChildID(int id){
		for(int index = 0; index < childIDs.size(); index++){ //For every child ID in the list
			if(id == childIDs.get(index)){ //If the child ID is the same as the ID to be removed
				this.childIDs.remove(index);
			}
		}
	}
	public static void reset(){postCount = 0;}
}

class Comment extends SocialPost{
	private int parentID;
	
	public Comment(int newParent, int newHandleID, String message){
		super(newHandleID, message);
		this.parentID = newParent;
	}
	
	public void setOrphan(){this.parentID = -1;}
	public int getParentID(){return this.parentID;}
}

class Endorsement extends SocialPost{
	private int parentID;
	
	public Endorsement(int newParent, int newHandleID, String message){
		super(newHandleID, message);
		this.parentID = newParent;
	}
	
	public static String makeEndorsementString(SocialPost postToEndorse, String handle){ //CHANGE THIS SO HANDLE POINTS TO ENDORSED HANDLE
		return "EP@" + handle + ": " + postToEndorse.getMessage();
	}
	public int getParentID(){return this.parentID;}
}