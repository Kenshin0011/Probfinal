import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Probfinal {
	private static int memcount=1, furcount=1;  //Membership number and furniture number
	private final static int width = 1000, height = 600, MaxFur = 30, MemMax=30; //Size regulation
	private static JFrame frame; //main frame
	private static JFrame f; //Confirmation frame
	private static JFrame fr = new JFrame(); //Frame for view
	private JLabel label = new JLabel("", JLabel.CENTER); //main label
	private JTextField text_string = new JTextField(""); //String type text input
	private JTextField text_int = new JTextField(""); //Primary key text input
	private JTextField text_num = new JTextField(""); //Where to enter the number of texts
	private JTextField text_int2 = new JTextField("");
	private JTextField text_num2 = new JTextField("");
	private DefaultListModel<String> listModel;
	private JList<String> list;

	class QuitButtonAction implements ActionListener { //Button to shut down the system
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	class CloseButtonAction implements ActionListener { //Button to close view frame
		public void actionPerformed(ActionEvent e) {
			fr.dispose();
		}
	}
	
	class OKButtonAction implements ActionListener {  //Button to confirm that you have entered your membership number
		Member mem = new Member();
		public void actionPerformed(ActionEvent e) {
			try {
				int n = Integer.valueOf(text_int.getText());
				if (mem.member_exisitence(n)) {
					frameinit();
					frame.setTitle("Rental");
					Component contents = create_MEMComponents(); 
					frame.getContentPane().add(contents, BorderLayout.CENTER);
					
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
					frame.setResizable(false);
					frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(list, "The number entered does not exist", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception er) {
				JOptionPane.showMessageDialog(frame, "please input Membership number!", "ERROR", JOptionPane.ERROR_MESSAGE);
			} 
		}
	}
	
	class RentButtonAction implements ActionListener { //Button to press when you want to rent
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			f = new JFrame("Are you really sure?");
			Component contents = create_checkrentComponents(); 
			f.getContentPane().add(contents, BorderLayout.CENTER);
			
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			f.setSize(400, 250);
			f.setResizable(false); //サイズ固定
			f.setLocationRelativeTo(null);
			f.setVisible(true); 
		}
	}
	
	class YESRentButtonAction implements ActionListener { //Button to accept to rent
		Member mem = new Member();
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			try {
				f.dispose();
				int index = list.getSelectedIndex();
				int n = Integer.valueOf(text_int.getText());
				int num = Integer.valueOf(text_num.getText());
				text_num.setText("");
				text_num.setFont(new Font("Arial", Font.PLAIN, 24));
				if (num < 0) {
					throw new Exception();
				}
				if (index != -1) {
					if (num >= 1) {
						if (!fur.furniture_exisitence(fur.fur.get(index).number, num)) {
							label.setText("Sorry, You can't borrow due to lack of furniture");
						} else {
							mem.rental(n, fur.fur.get(index).number, num);
							frameinit();
							frame.setTitle("Rental");
							Component contents = create_MEMComponents();
                            frame.getContentPane().add(contents, BorderLayout.CENTER);
							
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
							frame.setResizable(false);
							frame.setVisible(true);
							//The display is divided into singular and plural
							if (num == 1) {
								label.setText("You rented \"" + fur.fur.get(index).name + "\"");
							} else {
								label.setText("You rented " + num + " " + fur.fur.get(index).name + "s");
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(list, "None selected!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception er) {
				System.err.println(er.getMessage());
				JOptionPane.showMessageDialog(frame, "please input num!", "ERROR", JOptionPane.ERROR_MESSAGE);
			} 
		}
	}
	
	class NORentButtonAction implements ActionListener { //Reject button when confirming rental
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			frameinit();
			Component contents = create_MEMComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class ReturnButtonAction implements ActionListener { //Button to return furniture
		public void actionPerformed(ActionEvent e) {
			f = new JFrame("Are you really sure?");
			Component contents = create_checkReturnComponents(); 
			f.getContentPane().add(contents, BorderLayout.CENTER);
			
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			f.setSize(400, 250);
			f.setResizable(false); //サイズ固定
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		}
	}
	
	class YESReturnButtonAction implements ActionListener { //Button to press when agreeing to return furniture
		Member mem = new Member();
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			try {
				f.dispose();
				int index = list.getSelectedIndex();
				int n = Integer.valueOf(text_int2.getText());
				int num = Integer.valueOf(text_num2.getText());
				if (num < 0) {
					throw new Exception();
				}
				int flag = 0;
				if (index != -1) {
					for (int j = 0; j < fur.fur.size(); j++) {
                    	if (fur.fur.get(j).number == n) {
                    		flag = 1;
                    	}
    				}
					
					if (num >= 1 && flag != 0) {
						for (int i = 0; i < mem.mem.size(); i++) {
				        	if (mem.mem.get(i).number == mem.mem.get(index).number) {
				        		for (int k = 0; k < mem.mem.get(i).state.size(); k++) {
				        			if (mem.mem.get(i).state.get(k).number == n) {
				        				flag = 2;
				        				if (mem.mem.get(i).state.get(k).num >= num) {
				        					flag = 3;
				        					break;
				        				}
				        			}
				        		}
				        		break;
				        	}
						}
					}
					
	                if (flag == 0) { //When furniture does not exist
	                	JOptionPane.showMessageDialog(list, "Furniture with item number \"" + n + "\" does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			    	} else if (flag == 1) { //When not renting furniture
			    		JOptionPane.showMessageDialog(list, "The person with membership number　\"" + mem.mem.get(index).number + "\"　does not rent the furniture", "Error", JOptionPane.ERROR_MESSAGE);
			    	} else if (flag == 2) { //When the number of borrowed items is exceeded
			    		JOptionPane.showMessageDialog(list, "The number entered exceeds the number that can be returned", "Error", JOptionPane.ERROR_MESSAGE);
				    } else {
			    		mem.return_fur(mem.mem.get(index).number, n, num);
			    		frinit(1);
			    		label.setText("Furniture number \"" + n + "\" has been returned");
			    		
			    	}
				} else {
					JOptionPane.showMessageDialog(list, "None selected!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception er) {
				JOptionPane.showMessageDialog(frame, "please input num!", "ERROR", JOptionPane.ERROR_MESSAGE);
			} 
		}
	}
	
	class NOReturnButtonAction implements ActionListener { //Reject button when confirming return
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			frameinit();
			Component contents = create_memComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}

	public void textinit() { //Text initialization
		text_string.setText("");
		text_int.setText("");
		text_num.setText("");
		text_string.setFont(new Font("Arial", Font.PLAIN, 24));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		text_num.setFont(new Font("Arial", Font.PLAIN, 24));
	}
	
	public void frameinit() { //main frame initialization
		frame.getContentPane().removeAll();
		frame.repaint();
	}
	
	public void frinit(int m) { //view frame initialization
		fr.getContentPane().removeAll();
		fr.repaint();
		
		Component contents;
		if (m == 1) {
			fr.setTitle("Member information list");
			contents = create_viewmemComponents(); 
		} else {
			fr.setTitle("Furniture information list");
			contents = create_viewfurComponents(); 
		} 
		fr.getContentPane().add(contents, BorderLayout.CENTER);
		
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		fr.setSize(width - 250, height - 200);
		fr.setResizable(false); //サイズ固定
		fr.setVisible(true);
	}
	
	class YESButtonAction implements ActionListener { //Button to agree to delete membership registration
		Member mem = new Member();
		public void actionPerformed(ActionEvent e) {
			try {
				f.dispose();
				int flag = 1;
				int n = Integer.valueOf(text_int.getText());
				textinit();
				for (int i = 0; i < mem.mem.size(); i++) {
					if (mem.mem.get(i).number == n) {
		        		if (mem.mem.get(i).state.size() != 0) {
		        			JOptionPane.showMessageDialog(frame, "The member with membership number \"" + n + "\" rents furniture, so he can't remove his registration", "ERROR", JOptionPane.ERROR_MESSAGE);
		        		} else { //Registration of members renting furniture cannot be deleted
		        			mem.mem.remove(i);
		        			label.setText("The member with membership number \"" + n + "\" has been deleted");
		        			frinit(1);
		        		}
		        		flag = 0;
		        		break;
		        	}
		        }
				if (flag == 1) { //There is no member with the entered number
		        	label.setText("The member with membership number \"" + n + "\" does not exist");
		        }
			} catch (Exception er) {
				JOptionPane.showMessageDialog(frame, "please input num!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
    
	class NOButtonAction implements ActionListener { //Button to disagree to delete membership registration
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			frameinit();
			Component contents = create_removememComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class YESfurrmButtonAction implements ActionListener { //Button to agree to delete furniture registration
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			try {
				f.dispose();
				int flag = 1;
				int n = Integer.valueOf(text_int.getText());
				textinit();
				
				for (int i = 0; i < fur.fur.size(); i++) {
					if (fur.fur.get(i).number == n) {
		        		if (fur.fur.get(i).rental.size() != 0) {
		        			JOptionPane.showMessageDialog(frame, "Furniture with item number \"" + n + "\" is rented, so registration cannot be deleted", "ERROR", JOptionPane.ERROR_MESSAGE);
		        		} else {
		        			fur.fur.remove(i);
		        			frinit(2);
		        			label.setText("Furniture with item number \"" + n + "\" has been deleted");
		        		}
		        		flag = 0;
		        		break;
		        	}
		        }
				if (flag == 1) {
		        	label.setText("Furniture with item number \"" + n + "\" does not exist");
		        }
			} catch (Exception er) {
				JOptionPane.showMessageDialog(frame, "please input number!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		
		}
	}
	    
	class NOfurrmButtonAction implements ActionListener { //Button to disagree to delete furniture registration
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			frameinit();
			Component contents = create_removefurComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class HomeButtonAction implements ActionListener { //Button to return to the top page
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Top");
			Component contents = create_initComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class BackempButtonAction implements ActionListener { //Button to return to "Employee" page
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Employee");
			Component contents = create_empComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
	
	class BackmeminfoButtonAction implements ActionListener { //Button to return to "Member information" page
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Member information");
			Component contents = create_memComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}

	class BackfurinfoButtonAction implements ActionListener { //Button to return to "Furniture information" page
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Furniture information");
			Component contents = create_furComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}
		
	class memButtonAction implements ActionListener { //If you select "Member"
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Member");
			Component contents = create_memnumberComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class empButtonAction implements ActionListener { //If you select "Employee"
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Employee");
			Component contents = create_empComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class meminfoButtonAction implements ActionListener { //If you want to manipulate member information
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Member information");
			
			Component contents = create_memComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class memaddinfoButtonAction implements ActionListener { //If you want to add members
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Add member");
			
			Component contents = create_addmemComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class memremoveinfoButtonAction implements ActionListener { //If you want to remove members
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Remove member");
			
			Component contents = create_removememComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class memrenameinfoButtonAction implements ActionListener { //If you want to rename members
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Rename member");
			
			Component contents = create_renamememComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class memviewButtonAction implements ActionListener { //If you want to view member information
		public void actionPerformed(ActionEvent e) {
			frinit(1);
		}
	}
	
	class Addmem_ButtonAction implements ActionListener { //Button to actually add members
		Member mem = new Member();
		public void actionPerformed(ActionEvent e) {
			try {
				String m = text_string.getText();
				text_string.setText("");
				if (!m.equals("")) {
					memcount = mem.add_member(m, memcount);
					label.setText("Added \"" + m + "\" to membership");
					frinit(1);
				} else {
					throw new NullPointerException();
				}
			} catch (NullPointerException er) {
				JOptionPane.showMessageDialog(frame, "please input String!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	class Removemem_ButtonAction implements ActionListener { //Button to actually remove members
		public void actionPerformed(ActionEvent e) {
			f = new JFrame("Are you really sure?");
			Component contents = create_checkComponents(); 
			f.getContentPane().add(contents, BorderLayout.CENTER);
			
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			f.setSize(400, 250);
			f.setResizable(false);
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		}
	}
	
	class Renamemem_ButtonAction implements ActionListener { //Button to actually rename members
		Member mem = new Member();
		public void actionPerformed(ActionEvent e) {
			try {
				int flag = 1;
				String m = text_string.getText();
				int n = Integer.valueOf(text_int.getText());
				textinit();
				if (!m.equals("")) {
					for (int i = 0; i < mem.mem.size(); i++) {
			        	if (mem.mem.get(i).number == n) {
			        		mem.rename(n, m);
			    		    label.setText("Changed the name of the member with membership number \"" + n + "\" to \"" + m + "\"");
			    		    frinit(1);
			        		flag =0;
			        		break;
			        	}
			        }
					
					if (flag == 1) {
			        	label.setText("The member with membership number \"" + n + "\" does not exist");
			        }
				} else {
					throw new Exception();
				}
			} catch (Exception er) {
				textinit();
				JOptionPane.showMessageDialog(frame, "please input both num and String!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	class furinfoButtonAction implements ActionListener { //If you want to manipulate furniture information
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Furinture information");
			
			Component contents = create_furComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
	
	class furaddinfoButtonAction implements ActionListener { //If you want to add furniture
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Add furniture");
			
			Component contents = create_addfurComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
		
	class furremoveinfoButtonAction implements ActionListener { //If you want to remove furniture
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Remove furniture");
			
			Component contents = create_removefurComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
		
	class furreduceinfoButtonAction implements ActionListener { //If you want to reduce furniture
		public void actionPerformed(ActionEvent e) {
			frameinit();
			frame.setTitle("Reduce furniture");
			
			Component contents = create_reducefurComponents(); 
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
			frame.setVisible(true);
		}
	}
		
	class furviewButtonAction implements ActionListener { //If you want to view furniture information
		public void actionPerformed(ActionEvent e) {
			frinit(2);
		}
	}
		
	class Addfur_ButtonAction implements ActionListener { //Button to actually add furniture
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			try {
				boolean flag = false;
				String m = text_string.getText();
			    int n = Integer.valueOf(text_num.getText());
			    textinit();
			    if (n < 0) {
					throw new Exception();
				}
				label.setFont(new Font("SansSerif",Font.BOLD,24)); //Label font settings
    	        Color c = Color.red;  //Specify the color name of the color
    	        label.setForeground(c);  //Label foreground
    	        
			    if (!m.equals("")) {
			    	for (int i = 0; i < fur.fur.size(); i++) {
						if (fur.fur.get(i).name.equals(m) == true) {
							furcount = fur.add_furniture(m, n, furcount);
							label.setText("Added " + n + " existing \"" + m + "\"!");
							frinit(2);
			                flag = true;
			                break;
			        	}
			        }
			    	if (flag == false) { //When registering new furniture
			    		furcount = fur.add_furniture(m, n, furcount);
			    		frinit(2);
			    	    label.setText("Added " + n + " new \"" + m + "\"!");
					}	
			    } else {
					throw new Exception();
				}   
			} catch (Exception er) {
				textinit();
				JOptionPane.showMessageDialog(frame, "please input both String and num!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	class RemovefurButtonAction implements ActionListener { //Button to actually remove furniture
		public void actionPerformed(ActionEvent e) {
			f = new JFrame("Are you really sure?");
			Component contents = create_checkfurrmComponents(); 
			f.getContentPane().add(contents, BorderLayout.CENTER);
			
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			f.setSize(400, 250);
			f.setResizable(false); //Fixed size
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		}
	}
		
	class ReducefurButtonAction implements ActionListener { //Button to actually reduce furniture
		Furniture fur = new Furniture();
		public void actionPerformed(ActionEvent e) {
			try {
				int flag = 1;
				int n = Integer.valueOf(text_int.getText());
				int num = Integer.valueOf(text_num.getText());
				textinit();
				label.setFont(new Font("SansSerif",Font.BOLD,24));
    	        Color c = Color.red;  
    	        label.setForeground(c); 
    	        if (num < 0) {
					throw new Exception();
				}
    	        
				for (int i = 0; i < fur.fur.size(); i++) {
		        	if (fur.fur.get(i).number == n) {
		        		if (fur.fur.get(i).num >= num) {
		        			fur.reduce_Furniture(n, num);
			    		    label.setText("Reduced the number of furniture (item number: \"" + n  + "\", furniture name: \"" + fur.fur.get(i).name + "\") by " + num);
			        		flag =0;
			        		frinit(2);
		        		} else { //When the current number is less than the entered number
		        			label.setText(fur.fur.get(i).name + " is missimg.");
		        			flag = 0;
		        		}
		        	}
		        }
				
				if (flag == 1) {
		        	label.setText("Furniture with item number \"" + n + "\" does not exist");
		        }
			} catch (Exception er) {
				textinit();
				JOptionPane.showMessageDialog(frame, "please input both number and num!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	public Component create_initComponents() { //Initial state
		label = new JLabel("Please choose whether you are a employee or a member", JLabel.CENTER); 
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue; 
        label.setForeground(c); 
		JButton membutton = new JButton("Member");
		membutton.setFont(new Font("Arial", Font.PLAIN, 24));
		memButtonAction buttonListener1 = new memButtonAction();
		membutton.addActionListener( buttonListener1 );
		
		JButton empbutton = new JButton("Employee");
		empbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		empButtonAction buttonListener2 = new empButtonAction();
		empbutton.addActionListener( buttonListener2 );
		
		JButton quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Arial", Font.PLAIN, 24));
		QuitButtonAction quitButtonListener = new QuitButtonAction();
		quitButton.addActionListener( quitButtonListener );
		quitButton.setBounds(width/2 - 50, height - 180, 100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(null); //Disable layout manager
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 50, 30, 50, 30 ));
		pane1.setLayout(new GridLayout(1, 0));
		pane1.add(empbutton);
		pane1.add(membutton);
		label.setBounds(0, -250, width, height);
		pane1.setBounds(200, 150, 600, 250);
		pane.add(label);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(quitButton);
		return pane;
	}
	
	public Component create_memnumberComponents() { //Waiting for membership number to be entered
		text_int = new JTextField(7);
		text_int.setPreferredSize(new Dimension(20, 20));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JLabel l = new JLabel("Please input your Membership number", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;
        l.setForeground(c); 
        
		JButton yesbutton = new JButton("OK");
		OKButtonAction buttonListener0 = new OKButtonAction();
		yesbutton.addActionListener( buttonListener0 );
		yesbutton.setFont(new Font("Arial", Font.PLAIN, 28));
		c = Color.red; 
        yesbutton.setForeground(c);
		
		JButton nobutton = new JButton("CANCEL");
		HomeButtonAction buttonListener = new HomeButtonAction();
		nobutton.addActionListener( buttonListener );
		nobutton.setFont(new Font("Arial", Font.PLAIN, 28));
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 30, 30 ));
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 50, 10, 50 ));
		pane1.setLayout(new GridLayout(1, 0));
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane1.add(yesbutton);
		pane1.add(Box.createRigidArea(new Dimension(40, 30)));
		pane1.add(nobutton);
		pane.add(Box.createRigidArea(new Dimension(50, 30)));
		pane.add(l);
		pane.add(Box.createRigidArea(new Dimension(50, 160)));
		pane.add(text_int);
		pane.add(Box.createRigidArea(new Dimension(50, 100)));
		pane.add(pane1);
		pane.add(Box.createRigidArea(new Dimension(50, 70)));
		return pane;
	}
	
	public Component create_MEMComponents() { //The state where the member operates the rental
		Furniture fur = new Furniture();
		Member mem = new Member();
		int n = Integer.valueOf(text_int.getText());
		JLabel l1 = new JLabel("Hello " + mem.member_name(n), JLabel.CENTER);
		l1.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.red; 
        l1.setForeground(c); 
        
        JLabel l2 = new JLabel("Please input the number of furniture you want to rent", JLabel.CENTER);
		l2.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.blue;
        l2.setForeground(c);
        
        label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red;
        label.setForeground(c); 
        
        JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener = new HomeButtonAction();
		homebutton.addActionListener( buttonListener );
	    homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    homebutton.setBounds(60, 450, 100, 100);
	    
	    JButton backbutton = new JButton("Back");
	    HomeButtonAction buttonListener0 = new HomeButtonAction();
	    backbutton.addActionListener( buttonListener0 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
	    
	    text_num = new JTextField("");
		text_num.setFont(new Font("Arial", Font.PLAIN, 24));

		listModel = new DefaultListModel<String>();

		list = new JList<String>(listModel);
		list.setVisibleRowCount(MemMax);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.createVerticalScrollBar();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(width/2 - 250, 240, 500, 200);
		String s1, s2, s3, s4, s;
		
		for (int i = 0; i < fur.fur.size(); i++) {
			s = "";
			s1 = String.valueOf(fur.fur.get(i).number);
			s2 = String.valueOf(fur.fur.get(i).name);
			s3 = String.valueOf(fur.fur.get(i).num); //Number of things you can borrow
			s4 = "0";
			
			for (int j = 0; j < mem.search_member(n).state.size(); j++) {
				if (mem.search_member(n).state.get(j).number == fur.fur.get(i).number) {
					s4 = String.valueOf(mem.search_member(n).state.get(j).num); //The number of furniture renting
					break;
				}
    		}  

			s += "number: " + s1 + ", name: " + s2 + ", num:" + s3 + ", The number of furniture you are renting:" + s4; 
    		listModel.addElement(s); //Enter all at once in the scroll pane
        }
		
		if (fur.fur.size() == 0) {
			label.setText("There is no registered furniture");
		}
		
		JButton rentButton = new JButton("Rent");
		RentButtonAction rentButtonListener = new RentButtonAction();
		rentButton.addActionListener( rentButtonListener );
		rentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rentButton.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red;
        rentButton.setForeground(c);
		rentButton.setBounds(width/2 - 50, 450, 100, 100);
		
	    JPanel pane = new JPanel();
		pane.setLayout(null);
		label.setBounds(0, -200, width, height);
		l1.setBounds(-400, -280, width, height);
		l2.setBounds(0, -250, width, height);
		text_num.setBounds(width/2 - 75, 150, 200, 50);
		pane.add(text_num);
		pane.add(label);
		pane.add(l2);
		pane.add(l1);
		pane.add(scrollPane);
		pane.add(homebutton);
		pane.add(backbutton);
		pane.add(rentButton);
		return pane;
	}
	
	public Component create_empComponents() {//Employee operation screen
		label = new JLabel("Please select the information you want to view", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue; 
        label.setForeground(c); 
        
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener = new HomeButtonAction();
		homebutton.addActionListener( buttonListener );
	    homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    homebutton.setBounds(60, 450, 100, 100);
	    
	    JButton backbutton = new JButton("Back");
	    HomeButtonAction buttonListener0 = new HomeButtonAction();
	    backbutton.addActionListener( buttonListener0 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
	    
		JButton membutton = new JButton("Member information");
		meminfoButtonAction buttonListener1 = new meminfoButtonAction();
		membutton.addActionListener( buttonListener1 );
		membutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton furbutton = new JButton("Furniture information");
		furinfoButtonAction buttonListener2 = new furinfoButtonAction();
		furbutton.addActionListener( buttonListener2 );
		furbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(1, 0));
		label.setBounds(0, -250, width, height);
		pane1.setBounds(200, 250, 600, 120);
		pane.add(label);
		pane1.add(membutton);
		pane1.add(furbutton);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		return pane;
	}
	
	public Component create_memComponents() {//Change member information
		JLabel l = new JLabel("Please select an operation", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;    
        l.setForeground(c);   
        
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener = new HomeButtonAction();
		homebutton.addActionListener( buttonListener );
	    homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    homebutton.setBounds(60, 450, 100, 100);
	    
	    JButton backbutton = new JButton("Back");
	    BackempButtonAction buttonListener0 = new BackempButtonAction();
	    backbutton.addActionListener( buttonListener0 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton addmembutton = new JButton("Add Member");
		memaddinfoButtonAction buttonListener1 = new memaddinfoButtonAction();
		addmembutton.addActionListener( buttonListener1 );
		addmembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton removemembutton = new JButton("Remove Member");
		memremoveinfoButtonAction buttonListener2 = new memremoveinfoButtonAction();
		removemembutton.addActionListener( buttonListener2 );
		removemembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton renamemembutton = new JButton("Rename Member");
		memrenameinfoButtonAction buttonListener3 = new memrenameinfoButtonAction();
		renamemembutton.addActionListener( buttonListener3 );
		renamemembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton viewmembutton = new JButton("View Member and return furniture");
		memviewButtonAction buttonListener4 = new memviewButtonAction();
		viewmembutton.addActionListener( buttonListener4 );
		viewmembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		l.setBounds(0, -250, width, height);
		pane1.setBounds(200, 100, 600, 300);
		pane.add(l);
		pane1.add(addmembutton);
		pane1.add(removemembutton);
		pane1.add(renamemembutton);
		pane1.add(viewmembutton);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);

		return pane;
	}
	
	public Component create_furComponents() { //Change furniture information
		JLabel l = new JLabel("Please select an operation", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;    
        l.setForeground(c);      
        
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener1 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener1 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
	    BackempButtonAction buttonListener0 = new BackempButtonAction();
	    backbutton.addActionListener( buttonListener0 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton addfurbutton = new JButton("Add Furniture");
		furaddinfoButtonAction buttonListener2 = new furaddinfoButtonAction();
		addfurbutton.addActionListener( buttonListener2 );
		addfurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton removefurbutton = new JButton("Remove Furniture");
		furremoveinfoButtonAction buttonListener3= new furremoveinfoButtonAction();
		removefurbutton.addActionListener( buttonListener3 );
		removefurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton reducefurbutton = new JButton("Reduce Furniture");
		furreduceinfoButtonAction buttonListener4= new furreduceinfoButtonAction();
		reducefurbutton.addActionListener( buttonListener4 );
		reducefurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JButton viewfurbutton = new JButton("View Furniture");
		furviewButtonAction buttonListener5 = new furviewButtonAction();
		viewfurbutton.addActionListener( buttonListener5 );
		viewfurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		l.setBounds(0, -250, width, height);
		pane1.setBounds(200, 100, 600, 300);
		pane.add(l);
		pane1.add(addfurbutton);
		pane1.add(removefurbutton);
		pane1.add(reducefurbutton);
		pane1.add(viewfurbutton);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
	
	public Component create_addmemComponents() { //member add operation screen
		JLabel l = new JLabel("Please enter the name of the person you want to add", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue; 
        l.setForeground(c);    
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red;  
        label.setForeground(c); 
		text_string = new JTextField(20);
		text_string.setPreferredSize(new Dimension(100, 50));
		text_string.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel l_st = new JLabel("Member name:");
		l_st.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton addmembutton = new JButton("Add");
		Addmem_ButtonAction buttonListener = new Addmem_ButtonAction();
		addmembutton.addActionListener( buttonListener );
		addmembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
		addmembutton.setForeground(c);
		addmembutton.setBounds(450, 400, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackmeminfoButtonAction buttonListener1 = new BackmeminfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
	    JPanel pane = new JPanel();
		pane.setLayout(null);
		
		JPanel pane_int = new JPanel();
		pane_int.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_int.setLayout(new GridLayout(1, 0));
		
		pane_int.add(l_st);
		pane_int.add(text_string);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane_int.setBounds(200, 220, 600, 100);
		pane.add(addmembutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane_int, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
	
	public Component create_removememComponents() { //member remove operation screen
		JLabel l = new JLabel("Please enter the membership number of the person you want to delete", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue; 
        l.setForeground(c); 
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red; 
        label.setForeground(c); 
		text_int = new JTextField(7);
		text_int.setPreferredSize(new Dimension(20, 20));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel l_int = new JLabel("Memeber number:");
		l_int.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackmeminfoButtonAction buttonListener1 = new BackmeminfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton removemembutton = new JButton("Remove");
		Removemem_ButtonAction buttonListener = new Removemem_ButtonAction();
		removemembutton.addActionListener( buttonListener );
		removemembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red; 
		removemembutton.setForeground(c);
		removemembutton.setBounds(450, 400, 100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		
		JPanel pane_int = new JPanel();
		pane_int.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_int.setLayout(new GridLayout(1, 0));
		
		pane_int.add(l_int);
		pane_int.add(text_int);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane_int.setBounds(200, 220, 600, 100);
		pane.add(removemembutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane_int, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
	
	public Component create_renamememComponents() { //member rename operation screen
		JLabel l = new JLabel("Please enter the membership number of the person whose name you want to correct and the corrected name.", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,15));
        Color c = Color.blue;
        l.setForeground(c); 
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red; 
        label.setForeground(c); 
        text_int = new JTextField(7);
		text_int.setPreferredSize(new Dimension(20, 20));
		text_string = new JTextField(20);
		text_string.setPreferredSize(new Dimension(100, 50));
		text_string.setFont(new Font("Arial", Font.PLAIN, 24));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JLabel l_int = new JLabel("Memeber number:");
		l_int.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JLabel l_st = new JLabel("Member name:");
		l_st.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackmeminfoButtonAction buttonListener1 = new BackmeminfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton renamemembutton = new JButton("Rename");
		Renamemem_ButtonAction buttonListener = new Renamemem_ButtonAction();
		renamemembutton.addActionListener( buttonListener );
		renamemembutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red; 
		renamemembutton.setForeground(c);
		renamemembutton.setBounds(450, 400, 100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		
		JPanel pane_int = new JPanel();
		pane_int.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_int.setLayout(new GridLayout(3, 1));
		
		JPanel pane_st = new JPanel();
		pane_st.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_st.setLayout(new GridLayout(2, 1));
		
		pane_int.add(l_int);
		pane_int.add(text_int);
		pane_st.add(l_st);
		pane_st.add(text_string);
		pane1.add(pane_int);
		pane1.add(pane_st);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane1.setBounds(200, 100, 600, 300);
		pane.add(renamemembutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
	
	public Component create_viewmemComponents() { //Member browsing and furniture return operation screen
		Member mem = new Member();
		JLabel l = new JLabel("Please input Furniture number and num", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;
        l.setForeground(c);
		
        
        JLabel l1 = new JLabel("Furniture number:");
		l1.setFont(new Font("SansSerif",Font.BOLD,14));
		JLabel l2 = new JLabel("num:");
		l2.setFont(new Font("SansSerif",Font.BOLD,14));
		
        text_int2 = new JTextField(7);
		text_int2.setPreferredSize(new Dimension(20, 20));
		text_int2.setFont(new Font("Arial", Font.PLAIN, 20));
		text_num2 = new JTextField(7);
		text_num2.setPreferredSize(new Dimension(20, 20));
		text_num2.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		
		l.setBounds(0, -180, width - 250, height - 200);
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		l1.setBounds(80, 85, 140, 50);
		text_int2.setBounds(220, 85, 100, 50);
		l2.setBounds(430, 85, 50, 50);
		text_num2.setBounds(480, 85, 100, 50);
		
        
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setVisibleRowCount(MaxFur);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.createVerticalScrollBar();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds((width - 250)/2-200, 150, 400, 180);
		String s1, s2, s3, s;
		
		for (int i = 0; i < mem.mem.size(); i++) {
			s = "";
			s1 = String.valueOf(mem.mem.get(i).number);
			s2 = String.valueOf(mem.mem.get(i).name);

			s += "number: " + s1 + ", name: " + s2 + ", Rental list:";
    		for (int j = 0; j < mem.mem.get(i).state.size(); j++) {
    			s1 = String.valueOf(mem.mem.get(i).state.get(j).number);
				s2 = String.valueOf(mem.mem.get(i).state.get(j).name);
				s3 = String.valueOf(mem.mem.get(i).state.get(j).num);
				s += "[number:" + s1 + ", name:" + s2 + ", num:" + s3 + "]";

    			if (j != mem.mem.get(i).state.size() - 1) {
    				s += ", ";
    			}
    		}   
    		listModel.addElement(s);
        }
		
		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Arial", Font.PLAIN, 24));
		CloseButtonAction quitButtonListener = new CloseButtonAction();
		closeButton.addActionListener( quitButtonListener );
		closeButton.setBounds(20, height - 280, 150, 40);
		
		JButton returnfurbutton = new JButton("Return");
		ReturnButtonAction buttonListener6 = new ReturnButtonAction();
		returnfurbutton.addActionListener( buttonListener6 );
		returnfurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		returnfurbutton.setBounds(width - 390, 180, 100, 100);
		c = Color.red;  
		returnfurbutton.setForeground(c); 
		
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red;  
        label.setForeground(c);
        label.setBounds(0, -140, width - 250, height - 200);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		pane.add(l);
		pane.add(label);
		pane.add(l1);
		pane.add(text_int2);
		pane.add(l2);
		pane.add(text_num2);
		pane.add(scrollPane);
		pane.add(closeButton);
		pane.add(returnfurbutton);
		
		return pane;
	}
	
	public Component create_checkComponents() { //Member deletion confirmation screen
		JLabel l = new JLabel("Are you really sure?", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.red; 
        l.setForeground(c);
        
		JButton yesbutton = new JButton("YES");
		YESButtonAction buttonListener0 = new YESButtonAction();
		yesbutton.addActionListener( buttonListener0 );
		yesbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red; 
        yesbutton.setForeground(c);
		
		JButton nobutton = new JButton("NO");
		NOButtonAction buttonListener = new NOButtonAction();
		nobutton.addActionListener( buttonListener );
		nobutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane.setLayout(new GridLayout(0, 1));
		
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane1.setLayout(new GridLayout(1, 0));
		
		pane1.add(yesbutton);
		pane1.add(nobutton);
		pane.add(l);
		pane.add(pane1);
		return pane;
	}
	
	public Component create_addfurComponents() {  //furniture add operation screen
		JLabel l = new JLabel("Please enter the name of furniture you want to add", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;
        l.setForeground(c);  
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,24));
        c = Color.red;
        label.setForeground(c);
		text_string = new JTextField(20);
		text_string.setPreferredSize(new Dimension(100, 20));
		text_num = new JTextField(7);
		text_num.setPreferredSize(new Dimension(20, 20));
		text_string.setFont(new Font("Arial", Font.PLAIN, 24));
		text_num.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel l_st = new JLabel("Furniture name:");
		l_st.setFont(new Font("SansSerif",Font.BOLD,24));
		JLabel l_num = new JLabel("Number of furniture:");
		l_num.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackfurinfoButtonAction buttonListener1 = new BackfurinfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton addfurbutton = new JButton("Add");
		Addfur_ButtonAction buttonListener = new Addfur_ButtonAction();
		addfurbutton.addActionListener( buttonListener );
		addfurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		addfurbutton.setBounds(450, 430, 100, 100);
		c = Color.red; 
		addfurbutton.setForeground(c); 
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		
		JPanel pane_st = new JPanel();
		pane_st.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_st.setLayout(new GridLayout(2, 1));
		
		JPanel pane_num = new JPanel();
		pane_num.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_num.setLayout(new GridLayout(3, 1));
		
		pane_st.add(l_st);
		pane_st.add(text_string);
		pane_num.add(l_num);
		pane_num.add(text_num);
		pane1.add(pane_st);
		pane1.add(pane_num);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane1.setBounds(200, 100, 600, 300);
		pane.add(addfurbutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
		

	public Component create_removefurComponents() {  //furniture remove operation screen
		JLabel l = new JLabel("Please enter the item number of furniture you want to delete", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.blue;
        l.setForeground(c);
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red; 
        label.setForeground(c); 
		text_int = new JTextField(10);
		text_int.setPreferredSize(new Dimension(20, 20));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel l_int = new JLabel("Furniture number:");
		l_int.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackfurinfoButtonAction buttonListener1 = new BackfurinfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
	    
		JButton removefurbutton = new JButton("Remove");
		RemovefurButtonAction buttonListener = new RemovefurButtonAction();
		removefurbutton.addActionListener( buttonListener );
		removefurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
		removefurbutton.setForeground(c); 
		removefurbutton.setBounds(450, 400, 100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		
		JPanel pane_int = new JPanel();
		pane_int.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_int.setLayout(new GridLayout(1, 0));
		
		pane_int.add(l_int);
		pane_int.add(text_int);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane_int.setBounds(200, 220, 600, 100);
		pane.add(removefurbutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane_int, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
		
	public Component create_reducefurComponents() { //furniture reduce operation screen
		JLabel l = new JLabel("Please enter the furniture number you want to reduce and the number of furniture.", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,20));
        Color c = Color.blue;
        l.setForeground(c);
        
		label = new JLabel("", JLabel.CENTER);
		label.setFont(new Font("SansSerif",Font.BOLD,25));
        c = Color.red;
        label.setForeground(c);
		text_int = new JTextField(10);
		text_int.setPreferredSize(new Dimension(20, 20));
		text_num = new JTextField(7);
		text_num.setPreferredSize(new Dimension(20, 20));
		text_int.setFont(new Font("Arial", Font.PLAIN, 24));
		text_num.setFont(new Font("Arial", Font.PLAIN, 24));
		JLabel l_int = new JLabel("Furniture number:");
		l_int.setFont(new Font("SansSerif",Font.BOLD,24));
		JLabel l_num = new JLabel("Number of furniture:");
		l_num.setFont(new Font("SansSerif",Font.BOLD,24));
		
		JButton homebutton = new JButton("Top");
		HomeButtonAction buttonListener0 = new HomeButtonAction();
		homebutton.addActionListener( buttonListener0 );
		homebutton.setFont(new Font("Arial", Font.PLAIN, 24));
		homebutton.setBounds(60, 450, 100, 100);
		
		JButton backbutton = new JButton("Back");
		BackfurinfoButtonAction buttonListener1 = new BackfurinfoButtonAction();
	    backbutton.addActionListener( buttonListener1 );
	    backbutton.setFont(new Font("Arial", Font.PLAIN, 24));
	    backbutton.setBounds(width - 200, height - 100, 100, 50);
		
		JButton reducefurbutton = new JButton("Reduce");
		ReducefurButtonAction buttonListener = new ReducefurButtonAction();
		reducefurbutton.addActionListener( buttonListener );
		reducefurbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
		reducefurbutton.setForeground(c);
		reducefurbutton.setBounds(450, 430, 100, 100);
		
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		
		JPanel pane_int = new JPanel();
		pane_int.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_int.setLayout(new GridLayout(2, 1));
		
		JPanel pane_num = new JPanel();
		pane_num.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane_num.setLayout(new GridLayout(2, 1));
		
		pane_int.add(l_int);
		pane_int.add(text_int);
		pane_num.add(l_num);
		pane_num.add(text_num);
		pane1.add(pane_int);
		pane1.add(pane_num);
		l.setBounds(0, -250, width, height);
		label.setBounds(0, -200, width, height);
		pane1.setBounds(200, 100, 600, 300);
		pane.add(reducefurbutton);
		pane.add(l);
		pane.add(label);
		pane.add(pane1, BorderLayout.CENTER);
		pane.add(homebutton);
		pane.add(backbutton);
		
		return pane;
	}
	
	public Component create_viewfurComponents() { // //furniture browsing operation screen
		Furniture fur = new Furniture();
		JPanel pane = new JPanel();
		pane.setLayout(null);
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		pane1.setLayout(new GridLayout(0, 1));
		
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setVisibleRowCount(MaxFur);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.createVerticalScrollBar();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		String s1, s2, s3, s4, s;
		
		for (int i = 0; i < fur.fur.size(); i++) {
			s = "";
			s1 = String.valueOf(fur.fur.get(i).number);
			s2 = String.valueOf(fur.fur.get(i).name);
			s3 = String.valueOf(fur.fur.get(i).num);
			s4 = String.valueOf(fur.total_rental(fur.fur.get(i).number));

			s += "number: " + s1 + ", name: " + s2 + ", Number of possessions:" + s3 + ", rental num:" + s4 + ", Rental list:";
    		for (int j = 0; j < fur.fur.get(i).rental.size(); j++) {
    			s1 = String.valueOf(fur.fur.get(i).rental.get(j).number);
				s2 = String.valueOf(fur.fur.get(i).rental.get(j).name);
				s3 = String.valueOf(fur.fur.get(i).rental.get(j).num);
				s += "[number:" + s1 + ", name:" + s2 + ", num:" + s3 + "]";

    			if (j != fur.fur.get(i).rental.size() - 1) {
    				s += ", ";
    			}
    		}   
    		listModel.addElement(s);
        }
		
		if (fur.fur.size() == 0) {
			JLabel l = new JLabel("There is no registered furniture", JLabel.CENTER);
			l.setFont(new Font("SansSerif",Font.BOLD,25));
	        Color c = Color.red;
	        l.setForeground(c);
			pane1.add(l);
		}
		
		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Arial", Font.PLAIN, 24));
		CloseButtonAction quitButtonListener = new CloseButtonAction();
		closeButton.addActionListener( quitButtonListener );
		closeButton.setBounds((width - 250)/2 - 75, height - 280, 150, 40);
		
		pane1.setBounds(30, 10, 700, 300);
		pane1.add(scrollPane);
		pane.add(pane1);
		pane.add(closeButton);
		
		return pane;
	}
		

	public Component create_checkfurrmComponents() { //Furniture deletion confirmation screen
		JLabel l = new JLabel("Are you really sure?", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.red;
        l.setForeground(c);
        
		JButton yesbutton = new JButton("YES");
		YESfurrmButtonAction buttonListener0 = new YESfurrmButtonAction();
		yesbutton.addActionListener( buttonListener0 );
		yesbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
        yesbutton.setForeground(c);
		
		JButton nobutton = new JButton("NO");
		NOfurrmButtonAction buttonListener = new NOfurrmButtonAction();
		nobutton.addActionListener( buttonListener );
		nobutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane.setLayout(new GridLayout(0, 1));
		
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane1.setLayout(new GridLayout(1, 0));
		
		pane1.add(yesbutton);
		pane1.add(nobutton);
		pane.add(l);
		pane.add(pane1);
		return pane;
	}
		
	public Component create_checkrentComponents() { //Rental confirmation screen
		JLabel l = new JLabel("Are you really sure?", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.red;
        l.setForeground(c);
        
		JButton yesbutton = new JButton("YES");
		YESRentButtonAction buttonListener0 = new YESRentButtonAction();
		yesbutton.addActionListener( buttonListener0 );
		yesbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
        yesbutton.setForeground(c);
		
		JButton nobutton = new JButton("NO");
		NORentButtonAction buttonListener = new NORentButtonAction();
		nobutton.addActionListener( buttonListener );
		nobutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane.setLayout(new GridLayout(0, 1));
		
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane1.setLayout(new GridLayout(1, 0));
		
		pane1.add(yesbutton);
		pane1.add(nobutton);
		pane.add(l);
		pane.add(pane1);
		return pane;
	}
	
	public Component create_checkReturnComponents() { //Return confirmation screen
		JLabel l = new JLabel("Are you really sure?", JLabel.CENTER);
		l.setFont(new Font("SansSerif",Font.BOLD,25));
        Color c = Color.red;
        l.setForeground(c);
        
		JButton yesbutton = new JButton("YES");
		YESReturnButtonAction buttonListener0 = new YESReturnButtonAction();
		yesbutton.addActionListener( buttonListener0 );
		yesbutton.setFont(new Font("Arial", Font.PLAIN, 24));
		c = Color.red;
        yesbutton.setForeground(c);
		
		JButton nobutton = new JButton("NO");
		NOReturnButtonAction buttonListener = new NOReturnButtonAction();
		nobutton.addActionListener( buttonListener );
		nobutton.setFont(new Font("Arial", Font.PLAIN, 24));
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane.setLayout(new GridLayout(0, 1));
		
		JPanel pane1 = new JPanel();
		pane1.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
		pane1.setLayout(new GridLayout(1, 0));
		
		pane1.add(yesbutton);
		pane1.add(nobutton);
		pane.add(l);
		pane.add(pane1);
		return pane;
	}
	
	public static void main (String[] args) {
		Probfinal app = new Probfinal();
		
		frame = new JFrame(); 
		frame.setTitle("Top");
		
		Component contents = app.create_initComponents(); 
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(width, height);
		frame.setResizable(false); //Fixed size
		frame.setLocationRelativeTo(null); //Fixed in the center
		frame.setVisible(true);
	}
}
