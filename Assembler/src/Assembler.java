// Erkay GÜNAY 150114059
// Aydin DUYGU 150118981
// Mehmet Soykan MUTLU 150114006
// Erdi Türkay 150119853

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Assembler {

	public static void main(String[] args) {
		
		//Header needed for logisim
		fileWrite("v2.0 raw\n");
		
		//Reading instructions file line by line until no line left
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("/Users/Erkay/eclipse-workspace/DLD_Assembler/src/instructions.txt"));
			String line = reader.readLine();
			while (line != null) {
				switchAndSend(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Divide instructions into two part
	//First part is opcode and second part is operands
		
	public static void switchAndSend(String line) {
		String part [] = line.split(" ");
		
		String binaryCode = "";
		
        switch (part[0]) {
	        case "AND":
	        		 binaryCode += "0000";
	        		 binaryCode += threeRegister(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
	                 break;
	        
	        case "ADD":
		       		 binaryCode += "0001";
		       		 binaryCode += threeRegister(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;
	        
	        case "LD":
	        		binaryCode += "0010";
	        		binaryCode += oneRegisterOneAddr(part[1]);
	        		fileWrite(binaryToHex(binaryCode) + "\n");
	       		 break;	
               
	        case "ST":
		       		 binaryCode += "0011";
		       		 binaryCode += oneRegisterOneAddr(part[1]);
		       		 fileWrite(binaryToHex(binaryCode) + "\n");
	       		 break;	
		        
	        case "ANDI":
		       		 binaryCode += "0100";
		       		 binaryCode += twoRegisterOneImm(part[1]);
		       		 fileWrite(binaryToHex(binaryCode) + "\n");
	       		 break;
	       		 
	        case "ADDI":
		       		 binaryCode += "0101";
		       		 binaryCode += twoRegisterOneImm(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;		
	        
	        case "CMP":
		       		 binaryCode += "0110";
		       	     binaryCode += twoRegister(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;	
       
	        case "JUMP":
		       		 binaryCode += "0111";
		       		 binaryCode += jump(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break; 
	                
	        case "JE":
		       		 binaryCode += "1000";
		       		 binaryCode += jump(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;	
	                
	        case "JA":
		       		 binaryCode += "1001";
		       		 binaryCode += jump(part[1]);
	        		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;	
	        case "JB":
		       		 binaryCode += "1010";
		       		 binaryCode += jump(part[1]);
		       		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;
	        case "JBE":
		       		 binaryCode += "1011";
		       		 binaryCode += jump(part[1]);
		       		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;
	        case "JAE":
		       		 binaryCode += "1100";
		       		 binaryCode += jump(part[1]);
		       		 fileWrite(binaryToHex(binaryCode) + "\n");
		       		 break;
	        default: 
	        		 System.out.println("Invalid");
	                 break;
	    }

	}
	
	// Divide arguments into three register
	public static String threeRegister(String registers) {
		
		String binaryCode = "";
		String register [] = registers.split(",");
		
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(register[0])));
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(register[1])));
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(register[2])));

		return binaryCode;
	}
	
	// Divide arguments into two register
	public static String twoRegister(String registers) {
		
		String binaryCode = "";
		String register [] = registers.split(",");
		
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(register[0])));
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(register[1])));

		return binaryCode;
	}
	
	// Divide arguments into two register and one immediate value
	public static String twoRegisterOneImm(String values) {
		
		String binaryCode = "";
		String value [] = values.split(",");
		
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(value[0])));
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(value[1])));
		binaryCode += immBinary(Integer.toBinaryString(Integer.parseInt(value[2])));
		
		return binaryCode;
	}

	// Divide arguments into one register and one address value
	public static String oneRegisterOneAddr(String values) {
		
		String binaryCode = "";
		String value [] = values.split(",");
		
		binaryCode += registerBinary(Integer.toBinaryString(splitRegisterNumber(value[0])));
		binaryCode += addr9Binary(Integer.toBinaryString(Integer.parseInt(value[1])));
		
		return binaryCode;
	}
	
	
	// Divide arguments for jump instructions
	public static String jump(String value) {
		String binaryCode = "";
		binaryCode += addr12Binary(Integer.toBinaryString(Integer.parseInt(value)));
		
		return binaryCode;
	}
	
	//Split registers into R and an integer
	public static int splitRegisterNumber(String register) {
		
		register = register.substring(1,register.length());
		return Integer.parseInt(register);
	}
	
	//Convert binary value to hex value
	public static String binaryToHex(String binary) {
		
		int decimal = Integer.parseInt(binary,2);
   		String hexStr = Integer.toString(decimal,16);
   		
   		int length = hexStr.length();
   		
   		if(length == 1) {
   			hexStr = "000" + hexStr;
   		}
   		else if(length == 2) {
   			hexStr = "00" + hexStr;
   		}
   		else if(length == 3) {
   			hexStr = "0" + hexStr;
   		}
		
		return hexStr;
	}
	
	//Register to binary.
	public static String registerBinary(String s) {
		
		int length = s.length();
		String binary = "";
		
		if(length == 1) {
			binary += "00" + s;
		}
		else if(length == 2) {
			binary += "0" + s;
		}
		else {
			binary += s;

		}
		return binary;
	}
	
	
	//Immediate value to binary.
	public static String immBinary(String s) {
		
		int length = s.length();
		String binary = "";
		
		if(length > 6) {
			binary += s.substring(length-6, length);
		}
		else if(length == 1) {
			binary += "00000" + s;
		}
		else if(length == 2) {
			binary += "0000" + s;
		}
		else if(length == 3) {
			binary += "000" + s;
		}
		else if(length == 4) {
			binary += "00" + s;
		}
		else if(length == 5) {
			binary += "0" + s;
		}
		else {
			binary += s;

		}
		
		return binary;
	}
	
	public static String addr9Binary(String s) {
		
		int length = s.length();
		String binary = "";
		
		if(length > 9) {
			binary += s.substring(length-9, length);
		}
		else if(length == 1) {
			binary += "00000000" + s;
		}
		else if(length == 2) {
			binary += "0000000" + s;
		}
		else if(length == 3) {
			binary += "000000" + s;
		}
		else if(length == 4) {
			binary += "00000" + s;
		}
		else if(length == 5) {
			binary += "0000" + s;
		}
		else if(length == 6) {
			binary += "000" + s;
		}
		else if(length == 7) {
			binary += "00" + s;
		}
		else if(length == 8) {
			binary += "0" + s;
		}
		else {
			binary += s;

		}
		
		return binary;
	}
	
	public static String addr12Binary(String s) {
		
		int length = s.length();
		String binary = "";
		
		if(length > 12) {
			binary += s.substring(length-12, length);
		}
		else if(length == 1) {
			binary += "00000000000" + s;
		}
		else if(length == 2) {
			binary += "0000000000" + s;
		}
		else if(length == 3) {
			binary += "000000000" + s;
		}
		else if(length == 4) {
			binary += "00000000" + s;
		}
		else if(length == 5) {
			binary += "0000000" + s;
		}
		else if(length == 6) {
			binary += "000000" + s;
		}
		else if(length == 7) {
			binary += "00000" + s;
		}
		else if(length == 8) {
			binary += "0000" + s;
		}
		else if(length == 9) {
			binary += "000" + s;
		}
		else if(length == 10) {
			binary += "00" + s;
		}
		else if(length == 11) {
			binary += "0" + s;
		}		
		else {
			binary += s;

		}
		
		return binary;
	}
	
	public static void fileWrite(String str) {
       try { 
    	   
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter("instruction_memory.txt", true)); 
            out.write(str); 
            out.close(); 
        } 
        catch (IOException e) { 
            System.err.println("Exception occoured" + e); 
        } 
	}
		
}
