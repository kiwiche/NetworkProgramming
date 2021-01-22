import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Spiderleg 
{
	// We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
		private static final String USER_AGENT = "Chrome/56.0.2924.87";
		private List<String> title = new LinkedList<String>();
		private List<String> links = new LinkedList<String>();
		
		private Document htmlDocument;
		public List<String> getTitle() {return title;}
		public List<String> getLinks() {return links;}
		
		public boolean crawl(String url) // Give it a URL and it makes an HTTP request for a web page
		{
			try{
				Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
				
				Document htmlDocument = connection.get();
				
				this.htmlDocument = htmlDocument;
				
				if(connection.response().statusCode() == 200)
				{
					System.out.println("\nVisiting Received web page at " + url);
				}

				if(!connection.response().contentType().contains("text/html"))
				{
					System.out.println("Failure Retrieved something other than HTML");
					return false;
				}
				
				Elements linksOnPage = htmlDocument.select("h3.r a");
	
				System.out.println("Find(" + linksOnPage.size() + ") links");
	
				
				for(Element link: linksOnPage)
				{
					this.title.add(link.text());
					this.links.add(link.absUrl("href"));
				}
				
				
				for(int i= 0;i < links.size();i++)
                {
               	 System.out.print(title.get(i));
               	 System.out.println(links.get(i));
                }
				
				return true;
			}
			catch(IOException ioe){
				return false;
			}
		}

		public boolean searchForWord(String searchWord)  // Tries to find a word on the page
		{
			if(htmlDocument == null)
			{
				System.out.println("Error!! Empty page");
				
				return false;
				
			}
			
			System.out.println("Searching for the word'" + searchWord + "' ....");
			
			String bodyText = htmlDocument.body().text();
			
			return bodyText.toLowerCase().contains(searchWord.toLowerCase());
		}
		
		
		public boolean crawlimg(String url)
		{
			try{
				Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
				
				Document htmlDocument = connection.get();
				
				this.htmlDocument = htmlDocument;
				
				if(connection.response().statusCode() == 200)
				{
					System.out.println("\nVisiting Received web page at " + url);
				}
				
				
				if(!connection.response().contentType().contains("text/html"))
				{
					System.out.println("Failure Retrieved something other than HTML");
					return false;
				}
				
				Elements linksOnPage = htmlDocument.select("img");
				
				System.out.println("Find(" + linksOnPage.size() + ") links");
				
				for(Element link: linksOnPage)
				{
					this.links.add(link.attr("src"));
				}
			
				return true;
			}
			catch(IOException ioe){
				return false;
			}
		}
}
