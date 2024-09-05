package socialmedia;

import java.io.*;
import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform {
	private ArrayList<SocialAccount> accountList = new ArrayList<SocialAccount>();
	private ArrayList<SocialPost> postList = new ArrayList<SocialPost>();
	
	//--- ACCOUNT METHODS ----
	
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		if(validHandleCheck(handle)){ //Check if the handle is valid
			if(legalHandleCheck(handle)){ //Check if the handle is legal
				SocialAccount newAccount = new SocialAccount(handle, description); //Create an account with a description
				accountList.add(newAccount); //Add the new account to the list
				return newAccount.getID(); //Return the accounts ID
			} else {
				throw new IllegalHandleException(); //Throw an exception if the handle is illegal
			}
		} else {
			throw new InvalidHandleException(); //Throw an exception if the handle is invalid
		}
	}
	
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		if(validHandleCheck(handle)){ //Check if the handle is valid
			if(legalHandleCheck(handle)){ //Check if the handle is legal
				SocialAccount newAccount = new SocialAccount(handle); //Create an account with a description
				accountList.add(newAccount); //Add the new account to the list
				return newAccount.getID(); //Return the accounts ID
			} else {
				throw new IllegalHandleException(); //Throw an exception if the handle is illegal
			}
		} else {
			throw new InvalidHandleException(); //Throw an exception if the handle is invalid
		}
	}
	
	/**
	This method checks if a handle is valid (non empty, not over 30 characters, and contains no whitespaces), returning false if it is invalid.
	
	@param handle - the handle you wish to check
	@return boolean on true or false based on whether is passes the checks
	*/
	private boolean validHandleCheck(String handle){
		if(handle.length() != 0 && handle.length() <= 30){ //Check if the handle is not empty and less than 30 characters
			for(int index = 0; index < handle.length(); index++){ //For every letter in the handle
				if(handle.charAt(index) == ' '){ //Check if it is a whitespace
					return false; //If it is, return false
				}
			}
			return true; //If you make it through the loop, then return true
		} else {
			return false; //If the handle length is 0 or over 30, then return false
		}
	}
	
	/**
	This method checks if a handle is legal (is not an already used handle) returning false if it is illegal.
	
	@param handle - the handle you wish to check
	@return boolean on true or false based on whether is passes the checks
	*/
	private boolean legalHandleCheck(String handle){
		for (SocialAccount currentAccount : accountList){ //For every account in the accountList
			if(currentAccount.getHandle() == handle){ //Check if its handle is the same as the new handle
				return false; //If it is, return false
			}
		}
		return true; //If you make it through the loop, there are no duplicates, so return true
	}
	
	/**
	This method checks the accountList for an account with a matching handle, returning the index in the list, or throwing a HandleNotRecognisedException if there is no match
	
	@param handle - the handle to look for
	@return the index of the account within the accountList that has the matching handle, or -1 if there are no matches
	*/
	private int findHandleIndex(String handle) throws HandleNotRecognisedException {
		for (int index = 0; index < accountList.size(); index++){ //For every account in the accountList
			if(accountList.get(index).getHandle() == handle){ //Check if its handle is the same as the inputted handle
				return index; //Return the index where the match occurs
			}
		}
		return -1; //Return -1 if you have not found a matching handle
	}
	
	/**
	This method checks the accountList for an account with a matching id, returning the index in the list, or throwing a AccountIDNotRecognisedException if there is no match
	
	@param id - the id to look for
	@return the index of the account within the accountList that has the matching handle, or -1 if there are no matches
	*/
	private int findIDIndex(int id){
		for (int index = 0; index < accountList.size(); index++){ //For every account in the accountList
			if(accountList.get(index).getID() == id){ //Check if its id is the same as the inputted id
				return index; //Return the index where the match occurs
			}
		}
		return -1;
	}
	
	public void removeAccount(String handle) throws HandleNotRecognisedException { //Remove account by a handle
		int index = findHandleIndex(handle);
		if(index != -1){
			int deleteIndex = 0;
			int accountID = accountList.get(index).getID(); //Get the account ID of the handle
			for(SocialPost post : postList){ //For every post in the postlist
				if(post.getHandleID() == accountID){ //If the posts handleID is the same as the accountID
					deleteChildrenAndParentIDs(post); //Handle related posts based on the deletion of the post
					postList.remove(deleteIndex); //Remove the post from the list
				} else {
					deleteIndex++; //Increment the deleteIndex by 1 if there is no match
				}
			}
			accountList.remove(index);
		} else {
			throw new HandleNotRecognisedException();
		}
	}
	
	public void removeAccount(int id) throws AccountIDNotRecognisedException { //Remove account by ID
		int index = findIDIndex(id);
		if(index != -1){
			ArrayList<SocialPost> postsToRemove = new ArrayList<SocialPost>();
			for(SocialPost post : postList){ //For every post in the postlist
				if(post.getHandleID() == id){ //If the posts handleID is the same as the id
					postsToRemove.add(post); //Add the post to a list of posts to remove
				}
			}
			for(SocialPost deletingPost : postsToRemove){
				deleteChildrenAndParentIDs(deletingPost);
			}
			postList.removeAll(postsToRemove);
			accountList.remove(index);
		} else {
			throw new AccountIDNotRecognisedException();
		}
	}
	
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException { //Update description by a handle
		int index = findHandleIndex(handle);
		if(index != -1){
			accountList.get(index).setDescription(description);
		} else {
			throw new HandleNotRecognisedException();
		}
	}
	
	public void updateAccountDescription(int id, String description) throws AccountIDNotRecognisedException { //Update description by an ID
		int index = findIDIndex(id);
		if(index != -1){
			accountList.get(index).setDescription(description);
		} else {
			throw new AccountIDNotRecognisedException();
		}
	}
	
	public void changeAccountHandle(String oldHandle, String newHandle) throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		int index = findHandleIndex(oldHandle); //Find the index of the matching oldHandle
		if(index != -1){ //If a matching index is found
			if(validHandleCheck(newHandle)){ //Check if the new handle is valid
				if(legalHandleCheck(newHandle)){ //Check if the new handle is legal
					accountList.get(index).setHandle(newHandle); //Set the handle at the index to the new handle
				} else {
					throw new IllegalHandleException(); //Throw an exception if the handle is illegal
				}
			} else {
				throw new InvalidHandleException(); //Throw an exception if the handle is invalid
			}
		}
	}
	
	public String showAccount(String handle) throws HandleNotRecognisedException{
		int index = findHandleIndex(handle); //Find the index of the account
		String accountInfo = "";
		if(index != -1){ //If there is an account
			SocialAccount account = accountList.get(index); //Get the account from the list
			accountInfo = "ID: " + account.getID() +
				" Handle: " + account.getHandle() +
				" Description: " + account.getDescription() +
				" Post count: " + countTotalPosts(account) +
				" Endorse count: " + getAccountEndorsements(account);
		} else {
			throw new HandleNotRecognisedException();
		}
		return accountInfo;
	}
	
	/**
	This method takes a SocialAccount and counts the number of posts in the postList whose handleID matches the accountID
	
	@param account - the account whose total posts we are counting
	@return count - the number of posts made by the account
	*/
	private int countTotalPosts(SocialAccount account){
		int count = 0;
		int accountID = account.getID();
		for(SocialPost post : postList){ //For every post in the postList
			if(accountID == post.getHandleID()){ //Check if post was created by the account
				count++;
			}
		}
		return count;
	}
	
	//--- END OF ACCOUNT METHODS ---
	
	//--- POST METHODS ---
	
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		int index = findHandleIndex(handle); //Find the index of the handle to use
		if(index != -1){ //If an index was found
			int accountID = accountList.get(index).getID(); //Get the id of the handle
			if(message.length() != 0 &&  message.length() <= 100){ //If the message is between 0 and 100 characters
				SocialPost newPost = new SocialPost(accountID, message); //Make a new message using the accountID, and the message
				postList.add(newPost); //Add it to the list
				return newPost.getPostID(); //Return the posts ID
			} else {
				throw new InvalidPostException(); //Throw an exception if the post is invalid
			}
		} else {
			throw new HandleNotRecognisedException();
		}
	}
	
	/**
	This method checks the list of posts, checking the id, and returning the index of the matching post within the list, or throwing an exception if it is not present
	
	@param id - The id of the post you wish to find
	@return return the index of the matching post within the list, or -1 if there is no matching post
	*/
	private int findPostIndex(int id){
		for(int index = 0; index < postList.size(); index++){ //For every post within the list
			if(id == postList.get(index).getPostID()){ //Check if the post ID is the same as the id to search for
				return index; //Return the index if it is
			}
		}
		return -1;
	}
	
	public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		int accIndex = findHandleIndex(handle); //Find the index of the handle
		if(accIndex != -1){ //If an index was found
			int accountID = accountList.get(accIndex).getID(); //Get the ID of the account making the endorsement
			int postIndex = findPostIndex(id); //Find the index of the post to endorse
			if(postIndex != -1){ //If a post was found
				SocialPost origPost = postList.get(postIndex);
				if((origPost instanceof Endorsement) == false){ //If the post to endorse is not an Endorsement
					int origID = origPost.getHandleID(); //Find the ID of the original post
					int origAccIndex = findIDIndex(origID); //Get the account index in the list based on the original accounts ID
					String origHandle = "";
					if(origAccIndex != -1){
						origHandle = accountList.get(origAccIndex).getHandle(); //Get the handle from the account within the list based on index
					} else {
						origHandle = "The author of this post cannot be found";
					}
					String endorseString = Endorsement.makeEndorsementString(origPost, origHandle); //This handle needs to be the handle of the original post
					SocialPost newEndorse = new Endorsement(origPost.getPostID(), accountID, endorseString); //Create a new endorsement with the PostID, the accountID, and the endorsement string
					origPost.addChildID(newEndorse.getPostID()); //Add the id of the endorsement to the childID list of the original post
					postList.add(newEndorse); //Add the endorsement to the post list
					return newEndorse.getPostID();
				} else {
					throw new NotActionablePostException();
				}
			} else {
				throw new PostIDNotRecognisedException();
			}
		} else {
			throw new HandleNotRecognisedException();
		}
	}
	
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException{
		int accIndex = findHandleIndex(handle); //Find the index of the handle
		if(accIndex != -1){ //If an index was found
			int accountID = accountList.get(accIndex).getID(); //Get the ID of the account making the comment
			int postIndex = findPostIndex(id); //Find the index of the post to comment on
			if(postIndex != -1){ //If a post was found
				SocialPost origPost = postList.get(postIndex); //Get the original post from the list
				if((origPost instanceof Endorsement) == false){ //If the post to comment on is not an Endorsement
					if(message.length() != 0 ||  message.length() <= 100){ //If the comment message is not empty and under 100 characters
						SocialPost newComment = new Comment(origPost.getPostID(), accountID, message); //Create a new comment using the original post ID, account ID and the message
						origPost.addChildID(newComment.getPostID()); //Add the comments id to the list of child ids of the original post
						postList.add(newComment);
						return newComment.getPostID();
					} else {
						throw new InvalidPostException();
					}
				} else {
					throw new NotActionablePostException();
				}
			} else {
				throw new PostIDNotRecognisedException();
			}
		} else {
			throw new HandleNotRecognisedException();
		}
	}
	
	public void deletePost(int id) throws PostIDNotRecognisedException {
		int postIndex = findPostIndex(id); //Get the index of the post to delete
		if(postIndex != -1){ //If a post is found
			SocialPost postToDel = postList.get(postIndex); //Get the post you wish to delete
			deleteChildrenAndParentIDs(postToDel);
			postList.remove(postIndex);
		} else {
			throw new PostIDNotRecognisedException();
		}
	}
	
	/**
	This method is used during the deletion of a post, and takes the list of ChildIDs of a given post.
	Comments on the post are set as orphans, and endorsements are deleted.
	If the post is a child of another post, then the ID of the deleted post is removed from the parents ChildID list
	
	@param postToDel - the SocialPost you are deleting
	*/
	private void deleteChildrenAndParentIDs(SocialPost postToDel){
		ArrayList<Integer> childIDList = postToDel.getChildIDs(); //Get a list of all the child ID's related to the post to delete
		for(int childID : childIDList){ //For every child ID related to the post to delete
			SocialPost currentPost = postList.get(findPostIndex(childID)); //Find the post within the postList related to the ID
			if(currentPost instanceof Endorsement){ //If the post is an endorsement
				postList.remove(findPostIndex(currentPost.getPostID())); //Remove the endorsement based on its index
			} else { //If the post is a comment
				Comment currentComment = (Comment)currentPost;
				currentComment.setOrphan(); //Set the child as an orphan
			}
		}
		if(postToDel instanceof Endorsement){ //If the post is an endorsement
			Endorsement endorsement = (Endorsement)postToDel; //Downcast the postToDelete as an endorsement
			int parentIndex = findPostIndex(endorsement.getParentID()); //Get the index of its parent
			postList.get(parentIndex).removeChildID(endorsement.getPostID()); //Remove the child ID from the parents ChildIDList within the postList
		} else if (postToDel instanceof Comment) { //If the post is a comment
			Comment comment = (Comment)postToDel; //Downcast the postToDelete as a comment
			int parentIndex = findPostIndex(comment.getParentID()); //Get the index of its parent
			if(parentIndex != -1){ //If the child has a parent
				postList.get(parentIndex).removeChildID(comment.getPostID()); //Remove the child ID from the parents ChildIDList within the postList
			}
		}
	}
	
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		int postIndex = findPostIndex(id); //Find the index of the post with the inputted id
		if(postIndex != -1){
			SocialPost postToShow = postList.get(postIndex); //Get the post to show from the list
			return createPostString(postToShow);
		} else {
			throw new PostIDNotRecognisedException();
		}
	}
	
	/**
	This method takes a SocialPost and creates a string based on its information, returning it as a string
	
	@param postToShow - the posts information you wish to create from a string
	@return makeString - the string containing information about the post
	*/
	private String createPostString(SocialPost postToShow){
		int accountIndex = findIDIndex(postToShow.getHandleID()); //Get the index of the authors account from the posts ID
		String accountHandle = "";
		if(accountIndex != -1){
			accountHandle = accountList.get(accountIndex).getHandle(); //Get the handle from the account list, based on the id attached to the post
		} else {
			accountHandle = "The author of this post cannot be found"; //Mention that an author cannot be found if there is no matching account
		}
		String makeString = "ID: " + postToShow.getPostID() +
			" Account: " + accountHandle +
			" No. endorsements: " + getPostEndorsementNumber(postToShow) + 
			"  | No. comments: " + getPostCommentNumber(postToShow) + " " +
			postToShow.getMessage();
		return makeString;
	}
	
	/**
	This method takes a SocialPost and counts the number of comments related to it
	
	@param post - the post to count the number of comments for
	@return count - the number of comments related to the post
	*/
	private int getPostCommentNumber(SocialPost post){
		ArrayList<Integer> childList = post.getChildIDs(); //Get a list of childIDs from the list
		int count = 0; //Begin counting the number of comments
		for (int childID : childList){ //For every child ID
			int index = findPostIndex(childID); //Find the index of the post ID
			if(postList.get(index) instanceof Comment){ //If the post at the index of the id is a comment
				count++; //Add one to the count
			}
		}
		return count;
	}
	
	/**
	This method takes a SocialPost and counts the number of endorsements it has recieved
	
	@param post - the post to count the number of endorsements for
	@return count - the number of endorsements the post has recieved
	*/
	private int getPostEndorsementNumber(SocialPost post){
		ArrayList<Integer> childList = post.getChildIDs(); //Get a list of childIDs from the list
		int count = 0; //Begin counting the number of endorsements
		for (int childID : childList){ //For every child ID
			int index = findPostIndex(childID); //Find the index of the post ID
			if(postList.get(index) instanceof Endorsement){ //If the post at the index of the id is an endorsement
				count++; //Add one to the count
			}
		}
		return count;
	} 
	
	public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
		int index = findPostIndex(id);
		StringBuilder totalString = new StringBuilder("");
		if(index != -1){
			SocialPost basePost = postList.get(index);
			if((basePost instanceof Endorsement) == false){
				appendInfo(basePost, 0, totalString);
			} else {
				throw new NotActionablePostException();
			}
		} else {
			throw new PostIDNotRecognisedException();
		}
		return totalString;
	}
	
	
	private void appendInfo(SocialPost post, int depth, StringBuilder builder){
		String postString = createPostString(post);
		String buffer = "|";
		for(int depthCount = 0; depthCount < depth; depthCount++){
			buffer = "   " + buffer;
		}
		String fullPostString = buffer + postString + "\n";
		builder.append(fullPostString);
		for(int childPostID : post.getChildIDs()){
			int childIndex = findPostIndex(childPostID);
			if(childIndex != -1){
				SocialPost postToAppend = postList.get(childIndex);
				if((postToAppend instanceof Endorsement) == false){
					appendInfo(postToAppend, depth + 1, builder);
				}
			}
		}
	}
	
	//--- END OF POST METHODS ---
	
	//--- ANALYTIC METHODS ---
	
	public int getNumberOfAccounts(){return accountList.size();}
	
	public int getTotalOriginalPosts(){
		int count = 0;
		for(SocialPost post : postList){ //For every post in the postList
			if(((post instanceof Endorsement) || (post instanceof Comment)) == false){ //If the post is not an endorsement or a comment
				count++;
			}
		}
		return count;
	}
	
	public int getTotalEndorsmentPosts(){
		int count = 0;
		for(SocialPost post : postList){ //For every post in the postList
			if(post instanceof Endorsement){ //If the post is an endorsement
				count++;
			}
		}
		return count;
	}
	
	public int getTotalCommentPosts(){ 
		int count = 0;
		for(SocialPost post : postList){ //For every post in the postList
			if(post instanceof Comment){ //If the post is an comment
				count++;
			}
		}
		return count;
	}
	
	public int getMostEndorsedPost(){
		int largestID = 0;
		int largestCount = 0;
		for(SocialPost post : postList){ //For every post
			if((post instanceof Endorsement) == false){ //Check that it is not an endorsement
				int currentCount = getPostEndorsementNumber(post); //Count the number of endorsements for a given post
				if(currentCount > largestCount){ //If the post has more endorsements than the current leader
					largestID = post.getPostID(); //Set the id of the new leader
					largestCount = currentCount; //Set the count of the new leader
				}
			}
		}
		return largestID;
	}
	
	public int getMostEndorsedAccount(){
		int largestID = 0;
		int largestCount = 0;
		for(SocialAccount account : accountList){ //For every account in the list
			int currentCount = getAccountEndorsements(account); //Get its total number of endorsements
			if(currentCount > largestCount){ //If the currentCount is greater than the current leader
				largestID = account.getID(); //Set the id of the new leader
				largestCount = currentCount; //Set the count of the new leader
			}
		}
		return largestID;
	}
	
	/**
	This method takes an account and counts the number of endorsements it has recieved on its posts
	
	@param account - the account to count the number of endorsements for
	*/
	private int getAccountEndorsements(SocialAccount account){
		int count = 0;
		int accountID = account.getID(); //Get the accounts ID
		for(SocialPost post : postList){ //For every post in the postList
			if(post instanceof Endorsement){ //If the post is an endorsement
				Endorsement endorsement = (Endorsement)post; //Downcast to its endorsement
				int parentID = endorsement.getParentID(); //Find the parent of the endorsement
				SocialPost parentPost = postList.get(findPostIndex(parentID)); //Get the parent from the list
				if(accountID == parentPost.getHandleID()){ //Check if the endorsed post was created by the account
					count++;
				}
			}
		}
		return count;
	}
	
	//--- END OF ANALYTIC METHODS ---
	
	//--- MANAGEMENT RELATED METHODS ---
	
	public void erasePlatform(){
		postList.clear(); //Clear the account list
		accountList.clear(); //Clear the post list
		SocialAccount.reset(); //Reset the SocialAccount counter
		SocialPost.reset(); //Reset the SocialPost counter
	}
	
	public void savePlatform(String filename) throws IOException {
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(new FileOutputStream(filename)); //Create an ObjectOutputStream
			out.writeObject(postList); //Write the postList
			out.writeObject(accountList); //Write the accountList
		} catch (IOException e) {
		} finally {
			if(out != null){
				out.close(); //Close the OutputStream if it is open
			}
		}
	}
	
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(new FileInputStream(filename));
			Object obj = in.readObject();
			if(obj instanceof ArrayList){
				postList = (ArrayList)obj;
			}
			obj = in.readObject();
			if(obj instanceof ArrayList){
				accountList = (ArrayList)obj;
			}
		} catch(IOException e) {
		} finally {
			if(in != null){
				in.close();
			}
		}
	}
	
	//--- END OF MANAGEMENT RELATED METHODS ---
}