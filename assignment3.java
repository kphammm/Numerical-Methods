package edu.cpp.cs3010;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class assignment3 {
	public static void main(String[] args) throws IOException {
			
		    Scanner scanned = new Scanner(System.in);  // Create a Scanner object
		    System.out.println("Enter the input\n");
		    String input = scanned.nextLine();  // Reads user input
		    scanned.close();
		    String fileName = input.substring(input.lastIndexOf(" ")+1);
		  
		    
		    String[] line= input.split(" ");
		    double[] equation = methodReturningArray(fileName);
		    Scanner x = new Scanner(new File(fileName));
		    while (x.hasNextLine())
		    {
		       System.out.println(x.nextLine());
		    }
		    
		 // Create a stream to hold the output
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    PrintStream ps = new PrintStream(baos);
		    // IMPORTANT: Save the old System.out!
		    PrintStream old = System.out;
		    // Tell Java to use your special stream
		    System.setOut(ps);
		    
		    double root = 0;
		    if(input.contains("-newt")) {
		    	if(line.length==5) {
		    		 root = Newton(Double.parseDouble(line[3]),10000, 0.000001,0.000001,equation);
		    	}
		    	if(line.length==7) {
		    		 root = Newton(Double.parseDouble(line[5]),Integer.parseInt(line[4]), 0.000001,0.000001,equation);
		    	}
		    }
		    if(input.contains("-sec")) {
		    	if(line.length==6) {
		    		 root = Secant(Double.parseDouble(line[3]),Double.parseDouble(line[4]),10000,0.000001,equation);
		    	}
		    	if(line.length==8) {
		    		 root = Secant(Double.parseDouble(line[5]),Double.parseDouble(line[6]),Integer.parseInt(line[4]),0.000001,equation);
		    	}
		    }
		    if(input.contains("-hybrid")) {
		    	if(line.length==6) {
		    		double a = Bisection(Double.parseDouble(line[3]),Double.parseDouble(line[4]),100, 0.000001,equation);
		    		root = Newton(a ,9900, 0.000001,0.000001,equation);
		    	}
		    	if(line.length==8) {
		    		double b = Bisection(Double.parseDouble(line[5]),Double.parseDouble(line[6]),100, 0.000001,equation);
		    		 root = Newton(b,Integer.parseInt(line[4])-100, 0.000001,0.000001,equation);
		    	}
		    }
		    if(input.contains("-newt")==false && input.contains("-sec")==false && input.contains("-hybrid")==false) {
		    	//if there is no maxiterations
		    	if(line.length==5) {
		    		root = Bisection(Double.parseDouble(line[2]),Double.parseDouble(line[3]),10000, 0.000001,equation);
		    	}
		    	if(line.length==7) {
		    		 root = Bisection(Double.parseDouble(line[4]),Double.parseDouble(line[5]),Integer.parseInt(line[3]), 0.000001,equation);
		    	}
		    }
		    //makes the console output into a string
		    System.out.flush();
		    System.setOut(old);
		    // Show what happened
		    System.out.print(baos.toString());
		    System.out.println(root);
		    
		   String console = baos.toString();
		   String iterations = "10000";
		   if(console.contains("Algorithm has converged after")) {
			   iterations = (console.substring(console.indexOf("after ")+6, console.indexOf("iterations")));
		   }
		   String outcome = "converged";
		   if(console.contains("converged")==false) {
			   outcome = "failure";
		   }
		   String solution = root + " " + iterations + " " + outcome;
		   //writes to the file
		   try {
		        BufferedWriter writer = new BufferedWriter(new FileWriter("fun1.sol"));
		        writer.write(solution);
		        writer.close();
		    }
		    catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		      }
	}
	public static double[] methodReturningArray(String name) throws IOException {
		FileReader file = new FileReader(name);
	    BufferedReader buffer = new BufferedReader(file);
	    
	    Scanner sc = new Scanner(buffer);
	    String firstLine = buffer.readLine();
	    
	      int n = Integer.parseInt(firstLine);
	      double [] myArray = new double[n+1];
	      
	      String line = sc.nextLine();
	      
	      String[] parts = line.split(" ");
	      
	      for (int i=0; i<myArray.length; i++) {
	    	  myArray[i] = Double.parseDouble(parts[i]);
	      }
	      return myArray;
    }
	public static double Polynomial(double a, double[] equation) {
		double sum = 0;
		int power = equation.length-1;
		for(int i = 0; i<equation.length; i++,power--) {
			sum+=(equation[i]*Math.pow(a,power));
		}
		return sum;
	}
	public static double DerivePolynomial(double a, double[] equation) {
		double sum = 0;
		int power = equation.length-1;
		for(int i = 0; i<equation.length; i++,power--) {
			sum+=(power*equation[i]*Math.pow(a,power-1));
		}
		return sum;
	}
	public static void swap(double a, double b) {
		double temp = a;
		a=b;
		b=temp;
	}	
	
	public static double Bisection(double a, double b, int maxIter, double eps, double[] equation){
		double fa = Polynomial(a, equation);
		double fb = Polynomial(b, equation);
		double c = 0;
		
		if(fa*fb>=0) {
			System.out.println("Inadequate values for a and b.");
			return -1.0;
		}
		double error = b-a;
		for(int i = 1; i<maxIter; i++) {
			error = error/2;
			c = a+error;
			double fc = Polynomial(c,equation);
			
			if(Math.abs(error)<eps || fc == 0) {
				System.out.print("Algorithm has converged after "+ i + " iterations!\n");
				return c;
			}
			if(fa*fc<0) {
				b = c;
				fb = fc;
			}
			else {
				a = c;
				fa = fc;
			}
		}
		  System.out.println("Max iterations reached without convergence...");
		  return c;
	}
	public static double Newton(double x, int maxIter, double eps, double delta, double[] equation){
		double fx = Polynomial(x, equation);
		
		for(int i = 1; i<maxIter; i++) {
			double fd = DerivePolynomial(x, equation);
			
			if(Math.abs(fd)<delta) {
				System.out.println("Small Slope!");
				return x;
			}
			double d = fx/fd;
			x= x-d;
			fx = Polynomial(x, equation);
			 if(Math.abs(d)<eps) {
				 System.out.print("Algorithm has converged after "+ i + " iterations!\n");
				 return x;
			 }
		}
		System.out.println("Max iterations reached without convergence...");
		  return x;
	}
	public static double Secant(double a, double b, int maxIter, double eps, double[] equation){
		double fa = Polynomial(a, equation);
		double fb = Polynomial(b, equation);
		
		if(Math.abs(fa)>Math.abs(fb)) {
			swap(a,b);
			swap(fa,fb);
		}
		for(int it = 1; it<maxIter; it++) {
			if(Math.abs(fa)>Math.abs(fb)) {
				swap(a,b);
				swap(fa,fb);
			}
			double d = (b - a) / (fb - fa);
			b = a;
			fb = fa;
			d = d*fa;
			if(Math.abs(d)<eps) {
				System.out.print("Algorithm has converged after "+ it + " iterations!\n");
			      return a;
			}
			a = a-d;
			fa = Polynomial(a,equation);
		}
		System.out.println("Maximum number of iterations reached!");
		return a;
	}
	
}