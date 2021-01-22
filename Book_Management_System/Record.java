import java.util.*;
import java.io.Serializable;

public class Record implements Serializable
{
	private int userid;
	private String bookname;
	private String status;
	private String time;
	
	public Record()
	{}
	
	public Record(int userid,String bookname,String status,String time)
	{
		this.userid = userid;
		this.bookname = bookname;
		this.status = status;
		this.time = time;
	}
	
	public int getId()
	{
		return userid;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void printlog()
	{
		System.out.println(userid + "," + bookname + "," + status + "," + time);
	}
}
