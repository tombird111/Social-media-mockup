package socialmedia;

import java.io.Serializable;

public class SocialAccount implements Serializable{
	private static int accCount = 0;
	private int id;
	private String accHandle;
	private String accDesc;
	
	public SocialAccount(String handle, String description){
		this.accHandle = handle;
		this.accDesc = description;
		this.id = accCount;
		accCount++;
	}
	
	public SocialAccount(String handle){
		this.accHandle = handle;
		this.id = accCount;
		accCount++;
	}
	
	public String getHandle(){return this.accHandle;}
	public int getID(){return this.id;}
	public String getDescription(){return this.accDesc;}
	public void setDescription(String description){this.accDesc = description;}
	public void setHandle(String handle){this.accHandle = handle;}
	public static void reset(){accCount = 0;}
}