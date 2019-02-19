/*
 * Name - Sushmitha Nagarajan
 * ID - 1001556348
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
//import java.util.*;

//class to initialise all variables and checks on the input
public class CheckTrueFalse {
 
	// initialise all the variables here
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final int UNKNOWN = -1;
    public static final int NO_OF_COS = 5;
    public static final int NO_OF_ROS = 5;
    public static final int NO_OF_ROW_COLS = 5;
    public static final String UNDERSCORE = "_";
    public static final String RESULT_FILE = "resultfile.txt";
    public static final int KNOWN_ME = 0;
    public static final String res_file = "res1.txt";
    public static final int KNOWNME = 0;
    public static final int KNOWNS_ME = 0;
    public static final String res_file1 = "res1file.txt";
    
    // create the class objects here
    LogicalExpression entails_statement = new LogicalExpression();
	LogicalExpression enatils_negstatement = new LogicalExpression();
	LogicalExpression entity = new LogicalExpression();
	
    public static int MONS[][] = new int[5][5];
    public static int PITS[][] = new int[5][5];
    public static int STENCH[][] = new int[5][5];
    public static int BREEZE[][] = new int[5][5];
    public static int counter = 0;
    public static int count = 0;
    public static int klb_value = 0;
    public static int vallofwumpus = 0;
    public static int valofwumpus = 0;
    
    static Set<String> symbols = new HashSet<String>();
    static Set<String> sym = new HashSet<String>();
 
    public static void main(String[] args) {
   	
        if (args.length != 3) {
            System.out.println("Usage: " + args[0] + " [wumpus-rules-file] [additional-knowledge-file] [input_file]\n");
            exit_function(0);
        }
    	
        String buffer;
        String buff;
        BufferedReader inputStream;
        BufferedWriter outputStream;

        LogicalExpression enatils_statement = new LogicalExpression();
    	LogicalExpression enatils_negstatement = new LogicalExpression();
    	LogicalExpression entity = new LogicalExpression();
    	Hashmapmod model = new Hashmapmod();
        
  
        LogicalExpression knowledge_base = new LogicalExpression();

        // wumpus rules function definition
        //Defining a wumpus rules.txt file
        try {
            inputStream = new BufferedReader(new FileReader(args[0]));
            // load the wumpus rules in the system
            System.out.println("Loading..............................");
            System.out.println("Loading..............................");
            System.out.println("Loading..............................");
            System.out.println("Loading..............................");
            System.out.println("Loading..............................");
            knowledge_base.setConnective("and");

            while ((buffer = inputStream.readLine()) != null) {
                if (!(buffer.startsWith("#") || (buffer.equals( "" )))) {
                    LogicalExpression sube = readExpression(buffer);
                    knowledge_base.setSubexpression(sube);
                }
            }

            // input file close
            inputStream.close();

        } catch (Exception e) {// check the failure situations
        	
            System.out.println("Failed to open the file" + args[0]);
            e.printStackTrace();
            exit_function(0);// completed reading the wumpus rules
        } 
       
        try {
        	
            inputStream = new BufferedReader(new FileReader(args[1]));
         // read KB file (Knowledge base)
           
            System.out.println("loading the knowledge...");
// load tghe KB here

            while ((buffer = inputStream.readLine()) != null) {
                if (!(buffer.startsWith("#") || (buffer.equals("")))) {
                	String tmp=buffer;
                	if (tmp.contains("not")) {
						String split[] = tmp.split(" ");
						split[1] = split[1].substring(0, split[1].length() - 1);
						model.obj.put(split[1], false);
					} else {
						tmp = tmp.trim();
						model.obj.put(tmp, true);
					}
                	LogicalExpression sube = readExpression(buffer);
                    knowledge_base.setSubexpression(sube);
                  // knowledge_base.print_expression("");
                }
            }

            // close the input file
            inputStream.close();

        } catch (Exception e) {// condition handled if file could not be opened
        	
            System.out.println("failed to open the file " + args[1]);
            e.printStackTrace();
            exit_function(0);// end reading additional knowledge
        }
        

        
        if (!valid_expression(knowledge_base)) {// check for a valid knowledge_base
            System.out.println("invalid knowledge base file is given");
            exit_function(0);
        }

        // print the knowledge_base
        knowledge_base.print_expression("\n");

        // read the statement file
        String alpha1 = "";
		String alpha2 = "";
		String alpha3 = "";
		try {
			inputStream = new BufferedReader(new FileReader(args[2]));
			get_Symbol(knowledge_base);
			Set<String> uniqueSymbolset = symbols;
			while ((buffer = inputStream.readLine()) != null) {
				if (!buffer.startsWith("#")) {
					alpha1 = buffer;
					if (buffer.contains("not")) {
						String split[] = buffer.split(" ");
						alpha2 = split[1].substring(split[1].length() - 1);
					} else {
						alpha2 = "(not " + buffer + ")";
					}

					enatils_statement = readExpression(alpha1);
					enatils_negstatement = readExpression(alpha2);
					if (valid_expression(enatils_statement)&& !isValidInput(alpha1, uniqueSymbolset)) {
						System.out.println("invalid statement given in the file");
						return;
					}
					break;
				} 
			}
			inputStream.close();

		} catch (Exception e) {
			System.out.println("failed to open the file" + args[2]);
			e.printStackTrace();
			exit_function(0);
		}
		// end reading the statement file

        // check for a valid statement
        if (!valid_expression(enatils_statement)) {
            System.out.println("invalid statement given the file");
            exit_function(0);
        }

        // print the statement
        enatils_statement.print_expression("");
        // print a new line
        System.out.println("\n\n");
        
        System.out.println("\n\nProcessing... the details\n\n");
        boolean tt_entails_alpha = TT_Entails(knowledge_base, enatils_statement, model);
        System.out.println(tt_entails_alpha);
        boolean tt_entails_negation_alpha = TT_Entails(knowledge_base, enatils_negstatement, model);
        
        System.out.println(tt_entails_negation_alpha);

        findresultfinal(tt_entails_alpha, tt_entails_negation_alpha);

    } // end of main

    

    private static boolean TT_Entails(LogicalExpression knowledge_base, LogicalExpression alpha_val, Hashmapmod model2) {
        // TODO Auto-generated method stub
    	List<String> symbolList =loadsymbols(knowledge_base, alpha_val);
		symbolList = removefirstsymbol(model2,symbolList);
        return TT_Check_All(knowledge_base, alpha_val, symbolList, model2);
    }

   
    private static boolean TT_Check_All(LogicalExpression knowledge_base, LogicalExpression alpha_val, List<String> symbols, Hashmapmod model) {
        // TODO Auto-generated method stub
    	if (symbols.isEmpty()) {			
			boolean pl_true = PL_TRUE(knowledge_base, model);			
			if(pl_true){
				return PL_TRUE(alpha_val, model);				
			}
			else{
				return true;
			}			
		} else {
			String P = (String) symbols.get(0);			
			List<String> rest = symbols.subList(1, symbols.size());			
			Hashmapmod trueModel = EXTENDS(P, true, model);
			Hashmapmod falseModel = EXTENDS(P,false, model);
			return (TT_Check_All(knowledge_base, alpha_val, rest, trueModel) && (TT_Check_All(knowledge_base, alpha_val, rest, falseModel)));
    }
    }
  
    private static Hashmapmod EXTENDS(String P, boolean value, Hashmapmod model ) {
        // TODO Auto-generated method stub
        model.obj.put(P, value);
        return model;
    }

   
    private static boolean PL_TRUE(LogicalExpression logical_statement, Hashmapmod model) {
        // TODO Auto-generated method stub
    	if(terminal_symbol(logical_statement)){						
			return model.obj.get(logical_statement.getUniqueSymbol());			
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("not")){			
			return !(PL_TRUE(logical_statement.getNextSubexpression(),model));
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("or")){			
			Vector<LogicalExpression> vector = logical_statement.getSubexpressions();
			Boolean b = false;
			for(int i=0;i<vector.size();i++){
				b = b || PL_TRUE(vector.get(i),model);
			}
			return b;		
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("if")){		
			
			Vector<LogicalExpression> vector = logical_statement.getSubexpressions();
			Boolean b = PL_TRUE(vector.get(0),model);			
			b = !(b && !(PL_TRUE(vector.get(1),model)));
			return b;			
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("iff")){			
			Vector<LogicalExpression> vector = logical_statement.getSubexpressions();
			Boolean b = PL_TRUE(vector.get(0),model);			
			return b == PL_TRUE(vector.get(1),model);
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("and")){			
			Vector<LogicalExpression> vector = logical_statement.getSubexpressions();
			
			Boolean b = true;
			for(int i=0;i<vector.size();i++){				
				b = b && PL_TRUE(vector.get(i),model);
				if(b==false){	
					return b;
				}
			}
			return b;
		}
		else if(logical_statement.getConnective()!=null && logical_statement.getConnective().equalsIgnoreCase("xor")){			
			Vector<LogicalExpression> vector = logical_statement.getSubexpressions();
			Boolean b = false;
			int truthCounter=0;
			for(int i=0;i<vector.size();i++){
				boolean retrieved = PL_TRUE(vector.get(i),model);
				if(retrieved==true)truthCounter++;
				if(truthCounter>1)return false;
				b = ((b||retrieved) && !(b && retrieved));
			}
			return b;
		}
		return true;
       
    }
    static boolean terminal_symbol(LogicalExpression knowlb){
		return knowlb.getConnective()==null;
	}
   
    private static void print_symbol_values() {

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("M_" + i + "_" + j + " = " + MONS[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("P_" + i + "_" + j + " = " + PITS[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("S_" + i + "_" + j + " = " + STENCH[i][j]);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println("B_" + i + "_" + j + " = " + BREEZE[i][j]);
            }
        }
    }
  
    public static boolean getValueFromArray(String symbo) {
        // TODO Auto-generated method stub

        String symbol_initials = null;
        String[] symbol_literals = new String[3];

        symbol_literals = symbo.split(UNDERSCORE);
        symbol_initials = symbol_literals[0];
        int pos_x = Integer.parseInt(symbol_literals[1]);
        int pos_y = Integer.parseInt(symbol_literals[2]);

        if (symbol_initials.equals("M")) {
            if (MONS[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("P")) {
            if (PITS[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("S")) {
            if (STENCH[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("B")) {
            if (BREEZE[pos_x][pos_y] == TRUE) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Incorrect Symbol format!!");
        }
        return false;
    }

  
    public static LogicalExpression readExpression(String input_string) {
        LogicalExpression result = new LogicalExpression();

        // trim the whitespace off
        input_string = input_string.trim();

        if (input_string.startsWith("(")) {
            // its a subexpression

            String symbolString = "";

            // remove the '(' from the input string
            symbolString = input_string.substring(1);

            if (!symbolString.endsWith(")")) {
                // missing the closing paren - invalid expression
                System.out.println("missing ')' !!! - invalid expression! - readExpression():-" + symbolString);
                exit_function(0);

            } else {
                // remove the last ')'
                // it should be at the end
                symbolString = symbolString.substring(0, (symbolString.length() - 1));
                symbolString.trim();

                // read the connective into the result LogicalExpression object
                symbolString = result.setConnective(symbolString);
            }

            // read the subexpressions into a vector and call setSubExpressions( Vector );
            result.setSubexpressions(read_subexpressions(symbolString));

        } else {
            // the next symbo must be a unique symbo
            // if the unique symbo is not valid, the setUniqueSymbol will tell us.
            result.setUniqueSymbol(input_string);
        }

        return result;
    }

 

    public static Vector<LogicalExpression> read_subexpressions(String input_string) {

        Vector<LogicalExpression> symbolList = new Vector<LogicalExpression>();
        LogicalExpression newExpression;// = new LogicalExpression();
        String newSymbol = new String();

        input_string.trim();

        while (input_string.length() > 0) {

            newExpression = new LogicalExpression();

            if (input_string.startsWith("(")) {
                // its a subexpression.
                // have readExpression parse it into a LogicalExpression object
                // find the matching ')'
                int parenCounter = 1;
                int matchingIndex = 1;
                while ((parenCounter > 0) && (matchingIndex < input_string.length())) {
                    if (input_string.charAt(matchingIndex) == '(') {
                        parenCounter++;
                    } else if (input_string.charAt(matchingIndex) == ')') {
                        parenCounter--;
                    }
                    matchingIndex++;
                }

                // read untill the matching ')' into a new string
                newSymbol = input_string.substring(0, matchingIndex);

                // pass that string to readExpression,
                newExpression = readExpression(newSymbol);

                // add the LogicalExpression that it returns to the vector symbolList
                symbolList.add(newExpression);

                // trim the logicalExpression from the input_string for further processing
                input_string = input_string.substring(newSymbol.length(), input_string.length());

            } else {
                

                if (input_string.contains(" ")) {
                    // remove the first string from the string
                    newSymbol = input_string.substring(0, input_string.indexOf(" "));
                    input_string = input_string.substring((newSymbol.length() + 1), input_string.length());
                } else {
                    newSymbol = input_string;
                    input_string = "";
                }
                newExpression.setUniqueSymbol(newSymbol);
                symbolList.add(newExpression);
            }

            input_string.trim();

            if (input_string.startsWith(" ")) {
                // remove the leading whitespace
                input_string = input_string.substring(1);
            }
        }
        return symbolList;
    }

   
    public static boolean valid_expression(LogicalExpression expression) {

        

        if (!(expression.getUniqueSymbol() == null) && (expression.getConnective() == null)) {
            // we have a unique symbo, check to see if its valid
            return checkvalid_symbol(expression.getUniqueSymbol());
        }

     
        if ((expression.getConnective().equalsIgnoreCase("if")) || (expression.getConnective().equalsIgnoreCase("iff"))) {

            // the connective is either 'if' or 'iff' - so check the number of connectives
            if (expression.getSubexpressions().size() != 2) {
                System.out.println("error: connective \"" + expression.getConnective() + "\" with "
                    + expression.getSubexpressions().size() + " arguments\n");
                return false;
            }
        }
      
        else if (expression.getConnective().equalsIgnoreCase("not")) {
            // the connective is NOT - there can be only one symbo / subexpression
            if (expression.getSubexpressions().size() != 1) {
                System.out.println("error: connective \"" + expression.getConnective() + "\" with "
                    + expression.getSubexpressions().size() + " arguments\n");
                return false;
            }
        }
        // end check for 'not'

        // check for 'and / or / xor'
        else if ((!expression.getConnective().equalsIgnoreCase("and")) && (!expression.getConnective().equalsIgnoreCase("or"))
            && (!expression.getConnective().equalsIgnoreCase("xor"))) {
            System.out.println("error: unknown connective " + expression.getConnective() + "\n");
            return false;
        } // end check for 'and / or / not'
      
       

        // checks validity of the logical_expression with the connective with this loop
        // enumerate the values of symbols in a for loop
        for (Enumeration e = expression.getSubexpressions().elements(); e.hasMoreElements();) {
            LogicalExpression testExpression = (LogicalExpression) e.nextElement();

            // for each sube in expression, check if oits valid or not
            
            if (!valid_expression(testExpression)) {
                return false;
            }
        }
        // if the method made it here, the expression must be valid
        return true;
    }

    // find if they are a avalid symbo or not
    public static boolean checkvalid_symbol(String symbo) {
        if (symbo == null || (symbo.length() == 0)) {
            return false;
        }
// check the iterator over these values
        for (int counter = 0; counter < symbo.length(); counter++) {
            if ((symbo.charAt(counter) != '_') && (!Character.isLetterOrDigit(symbo.charAt(counter)))) {

                System.out.println("this String: " + symbo + " is invalid! Offend the  character:---" + symbo.charAt(counter)
                    + "---\n");

                return false;
            }
        }

        // check if the symbols are valid or not
       
        return true;
    }

  // find the final result and send them to the write to file
    private static void findresultfinal(boolean tt_entails_alpha, boolean tt_entails_negation_alpha) {
       
        String decision = "If statement is definitely true or definitely false is unknown.";
        if (tt_entails_alpha && !tt_entails_negation_alpha) {
            decision = "definitely true";
        } else if (!tt_entails_alpha && tt_entails_negation_alpha) {
            decision = "definitely false";
        } else if (!tt_entails_alpha && !tt_entails_negation_alpha) {
            decision = "possibly true, possibly false";
        } else if (tt_entails_alpha && tt_entails_negation_alpha) {
            decision = "both true and false";
        }        
        System.out.println("RESULT = " + decision + " ");
        System.out.println("\n\n");
        writeoutput(decision, RESULT_FILE);

    }

  // print decisons to the file
    public static void writeoutput(String decision, String resultFile) {
        try {
        	//write the output into a file using buffredwriter java function
            BufferedWriter output = new BufferedWriter(new FileWriter(resultFile));

            //write the decision variable which implies the current turn of who plays the game
            output.write(decision + "\r\n");
            output.close();//close the output file

        } catch (IOException e) {
            System.out.println("\n Problem writing the result to file!\n" + "Try again!!!! Better luck next time.");
            e.printStackTrace();// catch if a problem occurs in writing the output
        }
        
    }
// these are the entryfunction to check the path of the program
    public static void entry_function(int value)
    {
    	System.out.println("enter from checkTrueFalse program");
        System.exit(value);
    }
    
 // these are the exit function to check the path of the program
    private static void exit_function(int value) {
        System.out.println("exiting from checkTrueFalse");
        System.exit(value);
    }
    // bring or get the symbols
    private static void get_Symbol(LogicalExpression st)
    {
    	if (st.getUniqueSymbol() != null) {
    		symbols.add(st.getUniqueSymbol());
        } 
    	else
    	{
    		for (int i = 0; i < st.getSubexpressions().size(); i++) {
    			LogicalExpression lle = (LogicalExpression) st.getSubexpressions().get(i);
				get_Symbol(lle);
    			if(st.getUniqueSymbol()!=null)
    				symbols.add(st.getUniqueSymbol());
    		}
    	}
    }
    
    //bring the symbols here for the coressponding expressions
    private static void bringSymbols(LogicalExpression st)
    {
    	if (st.getUniqueSymbol() != null) {
    		symbols.add(st.getUniqueSymbol());
        } 
    	else
    	{
    		for (int i = 0; i < st.getSubexpressions().size(); i++) {
    			LogicalExpression lle = (LogicalExpression) st.getSubexpressions().get(i);
				get_Symbol(lle);
    			if(st.getUniqueSymbol()!=null)
    				symbols.add(st.getUniqueSymbol());
    		}
    	}
    }
    
    static boolean isValidInput(String s1, Set<String> s2) {
// check if we have received valid inputs in the file
		Iterator<String> it = s2.iterator();
		boolean b = false;
		while (it.hasNext()) {
			if (it.next().equals(s1))
				b = true;
		}
		if (s1.contains("(or") || s1.contains("(and") || s1.contains("(xor")	|| s1.contains("(not") || s1.contains("(if")	|| s1.contains("(iff"))
			b = true;
		    b = true;
		return b;
	}
  //load all the symbols from the KB file to the program here
   static ArrayList<String> loadsymbols(LogicalExpression knowlb, LogicalExpression alpha_val){	
	   
		get_Symbol(knowlb);
		get_Symbol(alpha_val);		
		ArrayList<String> returnList = new ArrayList<String>(symbols);
		 
		return returnList;//returns an array of list of symbols
		
	}
    
 //this method helps remove the first symbo
   // gives an iterator over a list of symbols
   static List<String> removefirstsymbol(Hashmapmod mod, List<String> symlist2) {
		
	   Iterator<Entry<String,Boolean>> it = mod.obj.entrySet().iterator();
	    while (it.hasNext()) {	    	
	        Entry<String,Boolean> p = (Entry<String,Boolean>)it.next();
	        symlist2.remove(p.getKey());	       
	    }
	    // return the symbo list 2
		return symlist2;
	}
} 
class Hashmapmod{
	//define a main class to instantiate this process
	// this class used to get the symbols 
	public HashMap<String,Boolean> obj = new HashMap<String,Boolean>();

		public Hashmapmod extend(String symbo, boolean b) {
		Hashmapmod mod = new Hashmapmod();
		mod.obj.putAll(this.obj);
		mod.obj.put(symbo, b);
		return mod;
	}
}
//this would help the whole wumpus world inference rom the given statement, knowledge base , wumpus rules text files 
//The result would give a propositional logic answer to the statement