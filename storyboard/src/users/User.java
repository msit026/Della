package users;

public class User {
	private String uname;
	private String pwd;
	
	public User(String un, String p)
	{
		uname = un;
		pwd = p;
	}
	
	public User(String un)
	{
		uname = un;
		pwd = null;
	}
	
	public String getUname()
	{
		return uname;
	}
	
	public void setUname(String un)
	{
		uname = un;
	}
	
	public String getPassword()
	{
		return pwd;
	}
	
	public void setPassword(String p)
	{
		pwd = p;
	}
	
}
