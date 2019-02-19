//Name - Sushmitha Nagarajan
//ID - 1001556348
//Wumpus world main program
import java.util.*;
import java.io.*;
// import all the packages
public class LogicalExpression {

    public String uniqueSymbol = null; //set uniquesymbol to  null if a system has a unique or wrong expression
    private String connective = null; // set connective to null  if a system has a vague symbol
    private Vector<LogicalExpression> subexpressions = null; // basically a vector of unique symbols and connectives
                                                             
    private final String TRUE = "T";
    private final String FALSE = "F";
    private final String False = "False";
    private final String True = "True";
    // set these final variables for determining true or false
    
// creates a likley stack datastructure to store the symbols
    public static Stack<String> symbol_stack = new Stack<String>();
    private static boolean final_result;

    // constructors involed in addition ti init method
    public LogicalExpression() {
        this.subexpressions = new Vector<LogicalExpression>();
    }

   
    // a new logical Expression, essentially making a copy of the logical expression
    public LogicalExpression(LogicalExpression oldExpression) {

        if (oldExpression.getUniqueSymbol() == null) {
            this.uniqueSymbol = oldExpression.getUniqueSymbol();
        } else {
            // create a new logical expression from the one passed to it
            this.connective = oldExpression.getConnective();

            // now get all of the subExpressions
            // hint, enumerate over the subexpression vector of newExpression
            for (Enumeration e = oldExpression.getSubexpressions().elements(); e.hasMoreElements();) {
                LogicalExpression nextExpression = (LogicalExpression) e.nextElement();

                this.subexpressions.add(nextExpression);
            }
        }

    }

   
    public void setUniqueSymbol(String newSymbol) {
        boolean valid = true;

        // remove the leading whitespace
        newSymbol.trim();

        if (this.uniqueSymbol != null) {
            System.out.println("setUniqueSymbol(): - this LE already has a unique symbol!!!" + "\nswapping :->"
                + this.uniqueSymbol + "<- for ->" + newSymbol + "<-\n");
        } else if (valid) {
            this.uniqueSymbol = newSymbol;
        }
    }

  
    public String setConnective(String inputString) {

        String connect;
        
        // trim the whitespace at the beginning of the string if there is any
        // there shouldn't be
        inputString.trim();

        // if the first character of the inputString is a '('
        // - remove the ')' and the ')' and any whitespace after it.
        if (inputString.startsWith("(")) {
            inputString = inputString.substring(inputString.indexOf('('), inputString.length());

            // trim the whitespace
            inputString.trim();
        }

        if (inputString.contains(" ")) {
            // remove the connective out of the string
            connect = inputString.substring(0, inputString.indexOf(" "));
            inputString = inputString.substring((connect.length() + 1), inputString.length());

        } else {
            // just set to get checked and empty the inputString
            // huh?
            connect = inputString;
            inputString = "";
        }

        // if connect is a proper connective
        if (connect.equalsIgnoreCase("if") || connect.equalsIgnoreCase("iff") || connect.equalsIgnoreCase("and")
            || connect.equalsIgnoreCase("or") || connect.equalsIgnoreCase("xor") || connect.equalsIgnoreCase("not")) {
            // ok, first word in the string is a valid connective

            // set the connective
            this.connective = connect;

            return inputString;

        } else {
            System.out.println("unexpected character!!! : invalid connective!! - setConnective():-" + inputString);
            this.exit_function(0);
        }

        // invalid connective - no clue who it would get here
        System.out.println(" invalid connective! : setConnective:-" + inputString);
        return inputString;
    }

    public void setSubexpression(LogicalExpression newSub) {
        this.subexpressions.add(newSub);
    }

    public void setSubexpressions(Vector<LogicalExpression> symbols) {
        this.subexpressions = symbols;

    }

    public String getUniqueSymbol() {
        return this.uniqueSymbol;
    }

    public String getConnective() {
        return this.connective;
    }

    public LogicalExpression getNextSubexpression() {
        return this.subexpressions.lastElement();
    }

    public Vector getSubexpressions() {
        return this.subexpressions;
    }

   // function to print expressions

    public void print_expression(String separator) {

        if (this.uniqueSymbol != null) {
            System.out.print(this.uniqueSymbol.toUpperCase());
        } else {
            // else the symbol is a nested logical expression not a unique symbol
            LogicalExpression nextExpression;

            // print the connective
            System.out.print("(" + this.connective.toUpperCase());

            // enumerate over the 'symbols' ( LogicalExpression objects ) and print them
            for (Enumeration e = this.subexpressions.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();

                System.out.print(" ");
                nextExpression.print_expression("");
                System.out.print(separator);
            }

            System.out.print(")");
        }
    }
// this function is used to get the expressions
    public  ArrayList<String> get_expression() {
// get the symbols from the KB file
    	ArrayList<String> Kb=new ArrayList<String>();
        if (this.uniqueSymbol != null) {
            Kb.add(this.uniqueSymbol.toUpperCase());
        } else {
            
            LogicalExpression nextExpression;
// enumerate the elemnets in the expression
            for (Enumeration e = this.subexpressions.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();
                // the KB expression 
                Kb=nextExpression.get_expression();
            }

         
        }
        return Kb;
    }
   
    // solve the values by solving the expressions 
    public boolean solve_expressions(HashMap<String, Boolean> model) {
        
       

        if (this.getUniqueSymbol() != null) {
            symbol_stack.push(this.getUniqueSymbol());
        } else {
            LogicalExpression nextExpression;

            symbol_stack.push(this.getConnective());
// push the symbols onto the stack
            
            for (Enumeration e = this.subexpressions.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();
// enumerate them along the line of the previous values
                nextExpression.solve_expressions(model);
            }
// pop the unique symbol out of the stack using this function
            final_result = popUniqueSymbolsAndEvaluateResult(model);
        }
        return final_result;
    }

   // this function uses the stack and pops the value 
    private boolean popUniqueSymbolsAndEvaluateResult(HashMap<String, Boolean> model) {
       //identify the symbols

        ArrayList<String> uniqueSymbole = new ArrayList<String>();
        String symbol,connective; //check
        boolean result = false;

        do {
            symbol = symbol_stack.pop();
            uniqueSymbole.add(symbol);
        } while (!isConnective(symbol));

        uniqueSymbole.remove(symbol);
        connective = symbol;
// if function which acts like switch function against every single connective in the symbols
        if (connective.equalsIgnoreCase("or")) { 
            result = false;
            while (!uniqueSymbole.isEmpty() && !result) {
                result = result || getValue(uniqueSymbole.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("and")) { 
            result = true;
            while (!uniqueSymbole.isEmpty() && result) {
                result = result && getValue(uniqueSymbole.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("not")) {
            //result = true;			//check
            result = !getValue(uniqueSymbole.remove(0), model);
        } else if (connective.equalsIgnoreCase("xor")) { 
                                                         
            result = false;
            int no_of_true_symbol = 0;
            while (!uniqueSymbole.isEmpty()) {
                if (getValue(uniqueSymbole.remove(0), model)) {
                    no_of_true_symbol++;
                }
            }
            if (no_of_true_symbol == 1) {
                result = true;
            }
        } else if (connective.equalsIgnoreCase("if")) { 
            result = true;
            if (uniqueSymbole.size() == 2) {
                if (getValue(uniqueSymbole.get(1), model) && !getValue(uniqueSymbole.get(0), model)) {
                    result = false;
                }
            }
        } else if (connective.equalsIgnoreCase("iff")) { 
            result = false;
            if (uniqueSymbole.size() == 2) {
                boolean symbol1 = getValue(uniqueSymbole.get(1), model);
                boolean symbol2 = getValue(uniqueSymbole.get(0), model);
                
                if (symbol1==symbol2) {
                    result = true;
                }
            }
        } else {
            System.out.println("incorrect connective found here ........................");
        }

        if (result) { 
            symbol_stack.push(TRUE);
        } else {
            symbol_stack.push(FALSE);
        }

        return result;
    }

    // check if the connective exisits
    private boolean isConnective(String symbol) {
        // TODO Auto-generated method stub
        return (symbol.equalsIgnoreCase("if") || symbol.equalsIgnoreCase("iff") || symbol.equalsIgnoreCase("and")
            || symbol.equalsIgnoreCase("or") || symbol.equalsIgnoreCase("xor") || symbol.equalsIgnoreCase("not"));
    }

  
    // this function gets the symbol and assigns the coressponding value for them
    private boolean getValue(String symbol, HashMap<String, Boolean> model) {
        
        if (symbol.equalsIgnoreCase(TRUE)) {
            return true;
        } else if (symbol.equalsIgnoreCase(FALSE)) {
            return false;
        } else if (model.get(symbol) == null) {
            return CheckTrueFalse.getValueFromArray(symbol);
        } else {
            return model.get(symbol);
        }
    }

    // the values or symbols stored in the stack are cleared
    public static void clearStack() {
        if (symbol_stack != null) {
            symbol_stack.clear();
        }
    }

    // function to exit the logical expression 
    private static void exit_function(int value) {
        System.out.println("Exit the LogicalExpression");
        System.exit(value);
    }
    
    // function to entry the logical expression 
    private static void entry_function(int value) {
        System.out.println("Enter the LogicalExpression");
        System.exit(value);
    }   
    
    // function to entry & exit the logical expression 
    private static void entrexit_function(int value) {
        System.out.println(" Enter & Exit the LogicalExpression");
        System.exit(value);
    }
}
