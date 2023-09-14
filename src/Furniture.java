import java.util.*;

public class Furniture extends Member{
	protected int number;
	protected static ArrayList<Furniture_Struct> fur = new ArrayList<Furniture_Struct>();
	
	int add_furniture(String name, int num, int count) { //Method to register furniture information
		boolean flag = false;
		
		for (int i = 0; i < fur.size(); i++) {
			if (fur.get(i).name.equals(name) == true) { //When furniture with the same furniture number is already registered
                System.out.println("Added " + fur.get(i).num + " existing \"" + name + "\"!");
                fur.get(i).num += num;
                flag = true;
                break;
        	}
        }
		
		if (flag == false) { //When registering new furniture
			Furniture_Struct f = new Furniture_Struct();
			f.rental = new ArrayList<Rental>();
    	    number = count;
    	    f.number = number;
    	    f.name = name;
    	    f.num = num;
    	    fur.add(f);
    	    count++;
    	    System.out.println("Added " + num + " new \"" + name + "\"!");
		}	
		
		return count;
    }
	
	void remove_furniture(int number) { //Method to delete furniture information
		int flag = 1;
        for (int i = 0; i < fur.size(); i++) {
        	if (fur.get(i).number == number) {
        		if (fur.get(i).rental.size() != 0) {
        			System.out.println("Furniture with item number \"" + number + "\" is rented, so registration cannot be deleted");
        		} else {
        			fur.remove(i);
            		System.out.println("Furniture with item number \"" + number + "\" has been deleted");
        		}
        		
        		flag = 0;
        		break;
        	}
        }
        if (flag == 1) {
        	System.out.println("Furniture with item number \"" + number + "\" does not exist");
        }
    }
	
	void reduce_Furniture(int number, int num) { //Method to reduce the number of furniture
		int flag = 1;
		for (int i = 0; i < fur.size(); i++) {
        	if (fur.get(i).number == number) {
        		if (fur.get(i).num >= num) {
        			fur.get(i).num -= num; 
        			flag = 0;
        		} else { //When the current number is less than the entered number
        			System.out.println(fur.get(i).name + " is missimg.");
        			flag = 0;
        		}	     
        	}
        }
		
		if (flag == 1) {
			System.out.println("Furniture with item number \"" + number + "\" does not exist");
		}
	}
	
	
	boolean furniture_exisitence(int fur_number, int num) { //Method to check if any number of furniture is present
		for (int i = 0; i < fur.size(); i++) {
        	if (fur.get(i).number == fur_number) {
        		 if (fur.get(i).num < num) {
        			 System.out.println("Sorry, You can't borrow due to lack of furniture");
        			 return false;
        		 } else {
        			 return true;
        		 }
        	}		
        }

        return false;
	}
    
	int total_rental(int fur_number) { //Total number of furniture rented
		int num = 0;
		for (int i = 0; i < fur.size(); i++) {
        	if (fur.get(i).number == fur_number) {
        		if (fur.get(i).rental.size() != 0) {
        			for (int j = 0; j < fur.get(i).rental.size(); j++) {
        				num += fur.get(i).rental.get(j).num;
        			}
        		}
        	}
		}
		return num;
	}
	
	void print_furniture() { //Method to output furniture information
    	for (int i = 0; i < fur.size(); i++) {
    		System.out.print("num: " + fur.get(i).number + ", name: "
    		    	+ fur.get(i).name + ", num:" + fur.get(i).num + ", Rental list:");
    		for (int j = 0; j < fur.get(i).rental.size(); j++) {
    			System.out.print("[number:" + fur.get(i).rental.get(j).number + ", name:" + fur.get(i).rental.get(j).name + ", num:" + fur.get(i).rental.get(j).num + "]");
    			if (j != fur.get(i).rental.size() - 1) {
    				System.out.print(", ");
    			}
    		}   
    		System.out.println(); 	
        }
    }

}
