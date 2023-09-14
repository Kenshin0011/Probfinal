import java.util.*;

public class Member extends Probfinal{
	protected int number;
	protected static ArrayList<Member_Struct> mem = new ArrayList<Member_Struct>(); //List to store member information
	
    int add_member(String name, int count) { //Method to add member information
		Member_Struct m = new Member_Struct();
		m.state = new ArrayList<Rental>(); 
		number = count; //Membership numbers are in ascending order from 1
		m.number = number;
		m.name = name;
		mem.add(m);
		count++;
		System.out.println("Added \"" + name + "\" to membership");
		return count;
    }
    
    void remove_member(int number) { //Method to delete member information
		int flag = 1;
		for (int i = 0; i < mem.size(); i++) {
			if (mem.get(i).number == number) {
				if (mem.get(i).state.size() != 0) { //Registration of members renting furniture cannot be deleted
					System.out.println("The member with membership number \"" + number + "\" rents furniture, so he can't remove his registration");
				} else {
					mem.remove(i);
					System.out.println("The member with membership number \"" + number + "\" has been deleted");
					break;
				}
				flag = 0;
				break;
			}
		}
        if (flag == 1) { //There is no member with the entered number
        	System.out.println("The member with membership number \"" + number + "\" does not exist");
        }
    }
    
    void rental(int mem_number, int fur_number, int num) { //Method for renting furniture
    	Furniture fu = new Furniture();
        boolean flag = false;
        int j,k;
        if (fu.furniture_exisitence(fur_number, num) == true) { //Whether furniture can be prepared for the number of inputs
        	for (int i = 0; i < mem.size(); i++) {
            	if (mem.get(i).number == mem_number) {
            		for (k = 0; k < mem.get(i).state.size(); k++) {
            			if (mem.get(i).state.get(k).number == fur_number) {
            				flag = true;
            				break;
            			}
            		}
            		
            		if (!flag) { //When renting new furniture that you have not rented at the moment
            			Rental fr = new Rental();
                		fr.number = mem_number;
                		fr.num = num;
                		fr.name = mem.get(i).name;
                		
            			Rental mr = new Rental();
                		mr.number = fur_number;
                		mr.num = num;
                		for (j = 0; j < fu.fur.size(); j++) {
                        	if (fu.fur.get(j).number == fur_number) {
                        		mr.name = fu.fur.get(j).name;
                        		 fu.fur.get(j).rental.add(fr); //Updated both furniture rental list and member rental list
                        		break;
                        	}		
                        }
                		mem.get(i).state.add(mr); //Updated both furniture rental list and member rental list
            		} else { //When renting additional furniture that is currently rented
            			mem.get(i).state.get(k).num += num;
            			for (j = 0; j < fu.fur.size(); j++) {
                        	if (fu.fur.get(j).number == fur_number) {
                        		for (int l = 0; l < fu.fur.get(j).rental.size(); l++) {
                        			if (fu.fur.get(j).rental.get(l).number == mem_number) {
                        				fu.fur.get(j).rental.get(l).num += num;
                        				break;
                        			}
                        		}
                        		break;
                        	}		
                        }
            		}
            		fu.reduce_Furniture(fur_number, num);
            		break;
            	}
        	}
        } 
    }
    
    void return_fur(int mem_number, int fur_number, int num) { //Method for returning furniture
    	Furniture fu = new Furniture();
    	int k,l;
    	for (int i = 0; i < mem.size(); i++) {
        	if (mem.get(i).number == mem_number) {
        		for (k = 0; k < mem.get(i).state.size(); k++) {
        			if (mem.get(i).state.get(k).number == fur_number) {
        				if (mem.get(i).state.get(k).num >= num) {
        					mem.get(i).state.get(k).num -= num; //Reduce the number of furniture rentals
            				for (int j = 0; j < fu.fur.size(); j++) {
                            	if (fu.fur.get(j).number == fur_number) {
                            		fu.fur.get(j).num += num;
                            		for (l = 0; l < fu.fur.get(j).rental.size(); l++) {
                            			if (fu.fur.get(j).rental.get(l).number == mem_number) {
                            				fu.fur.get(j).rental.get(l).num -= num; //Reduce the number of furniture rentals
                            				break;
                            			}
                            		}
                            		if (fu.fur.get(j).rental.get(l).num == 0) { //When the number of furniture rentals reaches 0, delete it from the rental list.
                            			fu.fur.get(j).rental.remove(l);
                    				}
                            		break; 
                            	}		
                            }
            				if (mem.get(i).state.get(k).num == 0) { //When the number of furniture rentals reaches 0, delete it from the rental list.
            					mem.get(i).state.remove(k);
            				}
        				} else { //When the number of inputs exceeds the number of rentals
        					System.out.println("The number entered exceeds the number that can be returned");
        				}
        				break;
        			}
        		}
        		break;
        	}
        }

    }
    
    void rename(int mem_number, String s) { //Method to change member name
    	int flag = 1;
        for (int i = 0; i < mem.size(); i++) {
        	if (mem.get(i).number == mem_number) {
        		search_member(mem_number).name =s;
        		System.out.println("Changed the name of the member with membership number \"" + mem_number + "\" to \"" + s + "\"");
        		flag = 0;
        		break;
        	}
        }
        if (flag == 1) { //When the member with the entered number is not registered
        	System.out.println("The member with membership number \"" + number + "\" does not exist");
        }
    }
    
    Member_Struct search_member(int number) { //Returns an element of the list of member information
    	Member_Struct dummy = new Member_Struct();
    	dummy.number = 0;
    	dummy.name = "NULL";

    	for (int i = 0; i < mem.size(); i++) {
        	if (mem.get(i).number == number) {
                return mem.get(i);
        	}		
        }return dummy;
    }
    
    boolean member_exisitence(int number) { //Method to check if a member is registered
    	for (int i = 0; i < mem.size(); i++) {
        	if (mem.get(i).number == number) {
                return true;
        	}		
        }return false;
    }
    
    String member_name(int number) { //Method to find out the name from the membership number
    	for (int i = 0; i < mem.size(); i++) {
        	if (mem.get(i).number == number) {
                return mem.get(i).name;
        	}		
        }
    	return "";
    }
    
    void print_member() { //Method to output member information
    	for (int i = 0; i < mem.size(); i++) {
    		System.out.print("number: " + mem.get(i).number + ", name: " + mem.get(i).name + ", Rental list:");
    		for (int j = 0; j < mem.get(i).state.size(); j++) {
    			System.out.print("[number:" + mem.get(i).state.get(j).number + ", name:" + mem.get(i).state.get(j).name + ", num:" + mem.get(i).state.get(j).num + "]");
    			if (j != mem.get(i).state.size() - 1) {
    				System.out.print(", ");
    			}
    		}   
    		System.out.println(); 	
        }
    }
}
