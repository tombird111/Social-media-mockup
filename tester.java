import socialmedia.AccountIDNotRecognisedException;
import socialmedia.BadSocialMedia;
import socialmedia.IllegalHandleException;
import socialmedia.InvalidHandleException;
import socialmedia.SocialMediaPlatform;
import socialmedia.SocialMedia;

public class tester {
	public static void main(String[] args) throws Exception {
		SocialMedia media = new SocialMedia();
		//Acounts
		media.createAccount("Keith");
		media.createAccount("Dave");
		//Posts
		media.createPost("Dave", "post 0");
		media.commentPost("Keith", 0, "Hey"); //1
		media.commentPost("Keith", 1, "Commmenty"); //2
		media.endorsePost("Dave", 0); //3
		media.commentPost("Dave", 0, "Howdy"); //4
		media.endorsePost("Keith", 0); //5
		media.commentPost("Keith", 0, "Yo"); //6
		media.endorsePost("Keith", 0); //7
		System.out.println(media.showPostChildrenDetails(0));
		/*System.out.println("total og " + media.getTotalOriginalPosts());
		System.out.println("total endo " + media.getTotalEndorsmentPosts());
		System.out.println("total comments " + media.getTotalCommentPosts());*/
	}
}