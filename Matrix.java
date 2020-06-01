import java.util.*;
import java.io.*;

public class Matrix{
	
	public static void main(String[] args){

		int n = Integer.parseInt(args[0]);

		int[] arr = new int[n]; 

		for(int i = 0; i < n; i++){
			if(i < 10){
				arr[i] = 10 * i + 10;
			}
			else{
				arr[i] = (100 * i) - 100;
			}
		}


		try
        {
           	for(int c : arr){
           		System.out.println(c + "x" + c + ":");
           		Process process = Runtime.getRuntime().exec("java MyMatrixMultiply "+c+" "+c+" "+c+" "+c+" "+4);

           		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		        String line = null;

		        while ((line = input.readLine()) != null)
		        {
		           System.out.println("   " + line);
		        }
			}
        } 
        catch (IOException e){
        	e.printStackTrace();
        }

	}
}