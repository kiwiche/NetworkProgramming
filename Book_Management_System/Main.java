import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class main
{
	public static void main(String[] args)throws IOException, ClassNotFoundException
	{
		LinkedList<Book> lib = new LinkedList<Book>();
		LinkedList<Client> user = new LinkedList<Client>();
		LinkedList<Record> log = new LinkedList<Record>();
		
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date date = new Date();

		boolean exit = true;
		int ind = -1;

		while(exit)
		{
			try{
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Book.dat"));
				Object obj = objectInputStream.readObject();
				lib = (LinkedList<Book>)obj;
				objectInputStream.close();
			}catch(Exception e){	
				
			}
			
			try{
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Client.dat"));
				Object obj = objectInputStream.readObject();
				user = (LinkedList<Client>)obj;
				objectInputStream.close();
			}catch(Exception e){
				
			}
			
			try{
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Record.dat"));
				Object obj = objectInputStream.readObject();
				log = (LinkedList<Record>)obj;
				objectInputStream.close();
			}catch(Exception e){
				
			}
			
			System.out.println("Enter your choice 1 add book 2 query book 3 delete book 4 modify book");
			System.out.println("5 borrow book 6 return book 7 print book 8 exit");
			System.out.println("9 add user 10 query user 11 delete user 12 modify user 13 print user 14 print out record log");
			int choice = scanner.nextInt();
			scanner.nextLine();

			if(choice == 1)
			{
				System.out.println("Enter book name");
				String bookname = scanner.nextLine();
				
				System.out.println("Enter book author");
				String author = scanner.nextLine();
				
				System.out.println("Enter book price");
				float price = scanner.nextFloat();
				
				lib.add(new Book(bookname, author, price));	
				
				System.out.println("Add the book success");
			}
			else if(choice == 2)
			{
				System.out.println("Enter book name");
				String bookname = scanner.nextLine();
				
				for(int i = 0;i < lib.size();i++)
				{
					if(lib.get(i).getBookName().equals(bookname))
					{
						ind = i;
					}
				}
				
				if(ind == -1)
				{
					System.out.println("The book not find");
				}
				else
				{
					if(lib.get(ind).getBorrower() == -1)
					{
						System.out.println("The book have not been borrowed");
					}
					else
					{
						lib.get(ind).printBook();

						System.out.println("The book have been borrowed by");
						
						user.get(lib.get(ind).getBorrower()).print();
					}
				}
			}
			else if(choice == 3)
			{
				System.out.println("Enter book name");
				String bookname = scanner.nextLine();
				
				for(int i = 0;i < lib.size();i++)
				{
					if(lib.get(i).getBookName().equals(bookname))
					{
						ind = i;
					}
				}
				
				try{
					lib.remove(ind);
				}catch(Exception e){
					System.out.println("The book not find");
				}
				
				for(int i = 0;i < lib.size();i++)
				{
					lib.get(i).printBook();
				}
			}
			else if(choice == 4)
			{
				System.out.println("Enter book name");
				String bookName = scanner.nextLine();
				
				for(int i = 0;i < lib.size();i++)
				{
					if(lib.get(i).getBookName().equals(bookName))
					{
						ind = i;
					}
				}
				
				if(ind == -1)
				{
					System.out.println("The book not find");
				}
				else
				{
					System.out.println("Enter new book name");
					String newBookname = scanner.nextLine();
					
					System.out.println("Enter new book author");
					String author = scanner.nextLine();
					
					System.out.println("Enter new book price");
					float price = scanner.nextFloat();
					
					lib.get(ind).setBook(newBookname,author,price);
					
					
					for(int i = 0;i < lib.size();i++)
					{
						lib.get(i).printBook();
					}
				}

			}
			else if(choice == 5)
			{
				System.out.println("Enter the book you want to borrowed");
				String bookname = scanner.nextLine();
				
				
				for(int i = 0;i < lib.size();i++)
				{
					if(lib.get(i).getBookName().equals(bookname))
					{
						ind = i;
					}	
				}
				
				if(ind == -1)
				{
					System.out.println("The book not find");
				}
				else
				{
					if(lib.get(ind).getBorrower() == -1)
					{
						System.out.println("Enter the user Id");
						int userid = scanner.nextInt();
						scanner.nextLine();

						lib.get(ind).addId(userid);
	
						log.add(new Record(userid,bookname,"borrow",format.format(date)));
	
						System.out.println("Borrowed success");		
					}
					else
					{
						System.out.println("The book have been borrowed");
					}
				}
			}
			else if(choice == 6)
			{
				System.out.println("Enter the book you want to return");
				String bookname = scanner.nextLine();
						
				for(int i = 0;i < lib.size();i++)
				{
					if(lib.get(i).getBookName().equals(bookname))
					{
						ind = i;
					}
				}
				
				if(ind == -1)
				{
					System.out.println("The book not find");
				}
				else
				{
					log.add(new Record(lib.get(ind).getBorrower(),bookname,"return",format.format(date)));
					
					lib.get(ind).setBorrower(-1);
					
					System.out.println("Returned success");
				}
			}
			else if(choice == 7)
			{
				System.out.println("book name, book author, book price");
				
				for(int i = 0;i < lib.size();i++)
				{
					lib.get(i).printBook();
				}
			}
			else if(choice == 8)
			{
				exit = false;
				
				System.out.println("Exit");
			}
			
			else if(choice == 9)
			{
				System.out.println("Enter your username");
				String username = scanner.nextLine();
				
				System.out.println("Enter your user password");
				String password = scanner.nextLine();

				int id = user.size();

				user.add(new Client(id,username,password));
	
				user.get(id).print();
			}
			else if(choice == 10)
			{
				System.out.println("Enter your user Id");
				int id = scanner.nextInt();
				scanner.nextLine();

				for(int i = 0;i < user.size();i++)
				{
					if(user.get(i).getId() == id)
					{
						ind = i;
					}
				}		
				
				if(ind == -1)
				{
					System.out.println("Your user Id not find");
				}
				else
				{
					user.get(ind).print();

					for(int i = 0;i < lib.size();i++)
					{
						if(lib.get(i).getBorrower() == id)
						{
							lib.get(i).printBook();
						}
					}
				}
			}
			else if(choice == 11)
			{
				System.out.println("Enter the user Id");
				int id = scanner.nextInt();
				scanner.nextLine();
				
				for(int i = 0;i < user.size();i++)
				{
					if(user.get(i).getId() == id)
					{
						ind = i;
					}
				}
				
				try{
					user.remove(ind);
				}catch(Exception e){
					System.out.println("The user not find");
				}
				
				for(int i = 0;i < user.size();i++)
				{
					user.get(i).print();
				}
			}
			else if(choice == 12)
			{
				System.out.println("Enter your user Id");
				int id = scanner.nextInt();
				scanner.nextLine();
				
				for(int i = 0;i <user.size();i++)
				{
					if(user.get(i).getId() == id)
					{
						ind = i;
					}
				}
				
				if(ind == -1)
				{
					System.out.println("The user not find");
				}
				else
				{
					System.out.println("Enter your new username");
					String username = scanner.nextLine();
					
					System.out.println("Enter your new password");
					String password = scanner.nextLine();
					
					user.get(ind).setName(username,password);
					
					System.out.println("The user data modify success");	
				}
			}
			else if(choice == 13)
			{
				System.out.println("id, user name");
				
				for(int i = 0;i < user.size();i++)
				{
					user.get(i).print();
				}
			}
			
			else if(choice == 14)
			{
				System.out.println("The library record log list");
				System.out.println("id, book name, status, time");

				for(int i = 0;i <log.size();i++)
				{
					log.get(i).printlog();
				}
			}
			else
			{
				System.out.println("Your choice is wrong");
				System.out.println("Please try again");
			}

			try{
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Book.dat"));
				objectOutputStream.writeObject(lib);
		        objectOutputStream.close();
			}catch(Exception e){
			
			}
			
			try{
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Client.dat"));
				objectOutputStream.writeObject(user);
				objectOutputStream.close();
			}catch(Exception e){
			
			}
			
			try{
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Record.dat"));
				objectOutputStream.writeObject(log);
				objectOutputStream.close();
			}catch(Exception e){
			
			}
		}
	}
}
