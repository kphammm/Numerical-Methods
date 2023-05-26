package edu.cpp.cs3010;
import java.util.Scanner;

public class Dec2IEE {
	
	public static void main(String[] args) {
		
	    Scanner scanned = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("Enter Decimal Number\n");

	    String decNum = scanned.nextLine();  // Reads user input
	    
	    int indexOfDecimal = decNum.indexOf(".");
	    
	    int intDecNum = Integer.parseInt(decNum.substring(0, indexOfDecimal));
	    double decDecNum = Double.parseDouble(decNum.substring(indexOfDecimal)); 
	    
	    //checks sign of input and removes sign from input
	    int sign = 0;
	    if(decNum.substring(0,1).equals("-")) {
	    	decNum = decNum.substring(1);
	    	sign = 1;
	    }
	    //for the case that there is no numerical value after the decimal point
		if(decDecNum==0) {
			System.out.println("IEEE 754: " + sign +" "+ expMantissa(binConverter(intDecNum) + ".0"));
		}
		//for all other cases
		if(decDecNum!=0) {
			System.out.println("IEEE 754: " + sign + " " + expMantissa(binConverter(intDecNum) + "." + reverseBinConverter(decDecNum)) );
		}
	}
	
	//Turns Integer part before decimal point into binary
	public static String binConverter(int number) {
		String result = "";
		while(number > 0) {
			int adder = number - ((number/2)*2);
			number = number/2;
			result = adder + result;
		}
		return result;
	}
	//Turns Decimal part after decimal point into binary
	public static String reverseBinConverter(double number) {
		String result = "";
		while(number<1) {
			
			int adder = (int) ((number*2)-(number*2)%1);
			
			number = number*2;
			if(number>1) {
				number = number-1;
			}
			result = result+adder;
		}
		return result;
	}
	//returns exponent and mantissa
	public static String expMantissa(String before) {
		
		String result = "";
		String additional = "";
		int indexOfDecimal = before.indexOf(".");
		double beforeAsDouble = Double.parseDouble(before);
		double afterDecimal = Double.parseDouble(before.substring(indexOfDecimal)); 
		int lengthOfAfterDecimal = before.substring(indexOfDecimal).length()-1;
	
		if(beforeAsDouble>1) {
			String exponent = binConverter(indexOfDecimal-1+127);
			String mantissa = before.substring(1, indexOfDecimal)+before.substring(indexOfDecimal+1, before.length());
			
			//adds zero infront of exponent binary that is less than 8
			if(exponent.length()<8) {
				for(int i = exponent.length(); i<8; i++) {
					exponent = "0"+ exponent;
				}
			}
			//adds 0's up to 23 for mantissas that are less than 23
			for(int i = mantissa.length(); i<23; i++) {
				additional = additional + "0";
			}
			
			if(afterDecimal ==0) {
				result = (exponent+" "+before.substring(1, indexOfDecimal)+additional +0);
			}
			if(lengthOfAfterDecimal<24 && afterDecimal!=0) {
				result = ( exponent+" "+mantissa + additional);
			}
			if(lengthOfAfterDecimal>23 && afterDecimal!=0){
				result = ( exponent+" "+before.substring(1, indexOfDecimal)+before.substring(indexOfDecimal+1, 25));
			}
		}
		if(beforeAsDouble<1) {
			int index = 0;
			//finds index of the first 1
			for(int i = before.length()-1; i>=0; i--) {
				if(before.charAt(i)=='1') {
					index = i;
				}
			}
			
			//adds zero infront of exponent binary that is less than 8
			String exponent = binConverter(127-index);
			if(exponent.length()<8) {
				for(int i = exponent.length(); i<8; i++) {
					exponent = "0"+ exponent;
				}
			}
			//adds 0's up to 23 for mantissas that are less than 23
			if(lengthOfAfterDecimal<23) {
				String man =  before.substring(index+1);
				
				for(int i = man.length(); i<23; i++) {
					additional = additional + "0";
				}
				result = (exponent+" "+man+additional);
			}
			if(lengthOfAfterDecimal>23) {
				result = (exponent+" "+ before.substring(index+1, index+1+23));
			}
		}
	return result;	
	}
}
