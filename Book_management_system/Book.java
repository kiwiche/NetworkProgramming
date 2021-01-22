import java.util.*;
import java.io.Serializable;

public class Book implements Serializable
{
	private float price;
	private String bookname;
	private String author;
	private int borrower;
	
	public Book()
	{}
	
	public Book(String bookname,String author,float price)
	{
		this.bookname = bookname;
		this.author = author;
		this.price = price;
		borrower = -1;
	}
	
	public void setBook(String bookname,String author,float price)
	{
		this.bookname = bookname;
		this.author = author;
		this.price = price;
	}
	
	public void addId(int borrower)
	{
		this.borrower = borrower;
	}
	
	public void setBorrower(int borrower)
	{
		this.borrower = borrower;
	}
	
	public String getBookName() 
	{
		// TODO Auto-generated method stub
		return bookname;
	}
	
	public int getBorrower()
	{
		return borrower;
	}
	
	public void printBook()
	{
		System.out.println(bookname + "," + author + "," + price + "," + borrower);
	}
}
