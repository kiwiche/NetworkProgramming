import java.util.*;
import java.io.Serializable;

public class Client implements Serializable
{
	private int id;
	private String username;
	private String password;
	
	public Client()
	{}
	
	public Client(int id,String username,String password)
	{
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public void setName(String username,String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return username;
	}
	
	public void print()
	{
		System.out.println(id + "," + username + "," + password);
	}
}
