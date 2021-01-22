import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.ucanaccess.jdbc.JackcessOpenerInterface;

public class main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JList<String> list = new JList();
	private List<String> title = new LinkedList<String>();
	private List<String> links = new LinkedList<String>();
	private String item[] = {"url","image"};
	private String page[] = {"1","2","3","4","5"};
	private int tag;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)throws SQLException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					crawler frame = new crawler();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public crawler() {
		DefaultListModel<String> model = new DefaultListModel<>();
		list  = new JList(model);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 610, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("keyword");
		lblNewLabel.setBounds(48, 38, 46, 15);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(121, 35, 120, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JComboBox comboBox = new JComboBox(item);
		comboBox.setBounds(298, 35, 46, 21);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox(page);
		comboBox_1.setBounds(375, 35, 53, 21);
		comboBox_1.setVisible(false);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(48, 113, 502, 255);
		contentPane.add(scrollPane);
		
		JButton btnNewButton = new JButton("search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			Spider url = new Spider();
			String query = textField.getText();
               	 	String item = (String)comboBox.getSelectedItem();
                
                	model.removeAllElements();
			int pen = 0;
                
                	title.clear();
                	links.clear();
                	Connection connDB = null;
				
                try
                {
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
   
                    String dataSource = "jdbc:ucanaccess://D:/report/account.accdb";

                    connDB = DriverManager.getConnection(dataSource);

                    Statement state = connDB.createStatement();
                    Statement st = connDB.createStatement();
                    Statement sta = connDB.createStatement();
                    
                    if(item.equals("url"))
                    {
                    	state.execute("SELECT * FROM page where keyword = '"+ query +"'");
                        ResultSet rs = state.getResultSet();
                        
                        while(rs.next())
                        	pen++;
                    }
                    else
                    {
                    	state.execute("SELECT * FROM image where keyword = '"+ query +"'");
                        ResultSet rs = state.getResultSet();
                        
                        while(rs.next())
                        	pen++;
                    }
  
                    if(pen == 0)
                    {
                    	 if(item.equals("url"))
                         {
                             url.search("https://www.google.com/search?q=" + query + "&num=50", query);

                             title.addAll(url.getTitle());
                             links.addAll(url.getLinks());  

                             tag = url.getLinks().size()/10;
                             
                             for(int i = 0;i < 10;i++)
                             {
                            	 model.addElement(title.get(i));
                            	 model.addElement(links.get(i));
                             }

                             for(int i= 0;i < links.size();i++)
                             {
                            	 System.out.print(title.get(i));
                            	 System.out.println(links.get(i));
                             }

                             for(int i= 0;i < links.size();i++)
                             {
                            	 st.executeUpdate("INSERT INTO page (keyword, tag, url) VALUES ( '"+query +"','"+ title.get(i) +"','"+ links.get(i) +"')");
                             }
                             
                         }
                         else
                         {
                             url.img("https://www.google.com/search?tbm=isch&q=" + query, query);

                             links = url.getLinks();  
                             
                             tag = url.getLinks().size()/10;
   
                             for(int i = 0;i < 10;i++)
                             {
                             	model.addElement(links.get(i));
                             }

                             for(String link:links)
                             {
                            	 st.executeUpdate("INSERT INTO image (keyword, url) VALUES ( '"+query +"','"+ link +"')");
                             }
                         }
                    	 
                    }
                    else
                    {
                    	if(item.equals("url"))
                    	{
                    		state.execute("SELECT * FROM page where keyword = '"+ query +"'");
                            ResultSet res = state.getResultSet();
                        	 
                        	while(res.next())
                            {
                        		title.add(res.getString("title"));
                            	links.add(res.getString("url"));
                            }
                        	
                        	
                        	for(String link:links)
                        	{
                        		System.out.println(link);
                        	}
                        	
                        	
                        	for(int i = 0;i < 10;i++)
                            {
                        		model.addElement(title.get(i));
                            	model.addElement(links.get(i));
                            }
                    	}
                    	else
                    	{
                    		state.execute("SELECT * FROM image where keyword = '"+ query +"'");
                            ResultSet res = state.getResultSet();
                        	 
                        	while(res.next())
                            {
                            	links.add(res.getString("url"));
                            }
                        	
                        	
                        	for(int i = 0;i < 10;i++)
                            {
                            	model.addElement(links.get(i));
                            }
                    	}

                    }
  
                    state.close();
                    connDB.close();
                }catch(ClassNotFoundException e)
                {
                    System.out.println("Driver loading failed!");
                }catch(SQLException e)
                {
                    System.out.println("DB linking failed!");
                }     

                comboBox_1.setVisible(true);
                
                
                list.setBounds(48, 113, 502, 255);
        		contentPane.add(list);
                list.setVisible(true);
				scrollPane.setViewportView(list);
			}
		});
		
		btnNewButton.setBounds(452, 34, 87, 23);
		contentPane.add(btnNewButton);
		
		
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String page = (String)comboBox_1.getSelectedItem();
				String item = (String)comboBox.getSelectedItem();
				int j;
				
				model.removeAllElements();
				
				
				if(page.equals("1"))
				{
					if(tag < 1)
						j = links.size();
					else
						j = 10;
					
					if(item.equals("url"))
					{
						for(int i = 0;i < j;i++)
						{
							model.addElement(title.get(i));
							model.addElement(links.get(i));
						}
					}
					else
					{
						for(int i = 0;i < j;i++)
						{
							model.addElement(links.get(i));
						}
					}
				}
				else if(page.equals("2"))
				{
					if(tag < 2)
						j = links.size();
					else
						j = 20;
					
					if(item.equals("url"))
					{
						for(int i = 10;i < j;i++)
						{
							model.addElement(title.get(i));
							model.addElement(links.get(i));
						}
					}
					else
					{
						for(int i = 10;i < j;i++)
						{
							model.addElement(links.get(i));
						}
					}
				}
				else if(page.equals("3"))
				{
					if(tag < 3)
						j = links.size();
					else
						j = 30;
					
					if(item.equals("url"))
					{
						for(int i = 20;i < j;i++)
						{
							model.addElement(title.get(i));
							model.addElement(links.get(i));
						}
					}
					else
					{
						for(int i = 20;i < j;i++)
						{
							model.addElement(links.get(i));
						}
					}
				}
				else if(page.equals("4"))
				{
					if(tag < 4)
						j = links.size();
					else
						j = 40;
					
					if(item.equals("url"))
					{
						for(int i = 30;i < j;i++)
						{
							model.addElement(title.get(i));
							model.addElement(links.get(i));
						}
					}
					else
					{
						for(int i = 30;i < j;i++)
						{
							model.addElement(links.get(i));
						}
					}
				}
				else
				{
					if(tag < 5)
						j = links.size();
					else
						j = 50;
					
					if(item.equals("url"))
					{
						for(int i = 40;i < j;i++)
						{
							model.addElement(title.get(i));
							model.addElement(links.get(i));
						}
					}
					else
					{
						for(int i = 40;i < j;i++)
						{
							model.addElement(links.get(i));
						}
					}
				}
					
			}
		});
		
		contentPane.add(comboBox_1);
	}
}
