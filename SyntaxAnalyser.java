import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


/**
 * This class represents a syntax analyser.
 *
 * @Author Nikolay Pankov
 */
public class SyntaxAnalyser extends AbstractSyntaxAnalyser {

    private int statementlist_num = 0;
//    LexicalAnalyser lex;

    public SyntaxAnalyser(String filename) throws IOException {
//        System.out.println(filename);

        lex = new LexicalAnalyser(filename);
       // CompilationException ce = new CompilationException("haha");
//        System.out.println(lex.getNextToken().toString());



    }

    /**
     * This method represents the statement_part. It starts by expecting a Begin token, after which
     *
     * @throws IOException
     * @throws CompilationException
     */
    @Override
    public void _statementPart_() throws IOException, CompilationException {
 //       System.out.println("----------------------------------------------------------");
//        System.out.println("312BEGIN StatementPart");
        System.out.println("312BEGIN StatementPart");


        //Program should begin with token: Begin
        acceptTerminal(2);

        //While there are still tokens to be read
        while(nextToken.symbol != Token.eofSymbol){

            statementList();


            //Semi column at end
            if(nextToken.symbol== 30){
                acceptTerminal(30);
            }
            //if END symbol
            else if(nextToken.symbol == 8){
                acceptTerminal(8);

            }

            else {
                //report invalid token
                myGenerate.reportError(nextToken, " Invalid token");
            }




//            System.out.println("");
//            System.out.println("");

        }
        System.out.println("312END StatementPart");

//        System.out.println("----------------------------------------------------------");
    }

    /**
     * Accept Terminal method is called when we reach a terminal. The method decides if the
     * next terminal corresponds to the terminal we expect, if not report error.
     * @param symbol
     * @throws IOException
     * @throws CompilationException
     */
    @Override
    public void acceptTerminal(int symbol) throws IOException, CompilationException {
//        System.out.println("Accept method");

        if(nextToken.symbol == symbol){

//          //Insert terminal
            myGenerate.insertTerminal(nextToken);
            //move to next
            nextToken = lex.getNextToken();
        }else {

            //If token symbol is not what we expect we throw an error.
            String errorMsg = "Expected: " + Token.getName(symbol);
            myGenerate.reportError(nextToken, errorMsg);


        }

    }



    /**
     * Method representing Terminal assignment statement. Following the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void assignmentStatement() throws IOException, CompilationException {
        //System.out.println("312BEGIN AssignmentStatement");
        myGenerate.commenceNonterminal("AssignmentStatement");

        //First we need Identifier
        acceptTerminal(16);
        //Second we need ::= symbol
        acceptTerminal(1);

        //If next is stringConstant
        if(nextToken.symbol == 31){
            acceptTerminal(31);
        }
        //If not then go to <expression>
        else{
            expression();

        }



        myGenerate.finishNonterminal("AssignmentStatement");
    }

    /**
     * Method representing Terminal expression from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void expression() throws IOException, CompilationException {
//        System.out.println("312BEGIN Expression");
        myGenerate.commenceNonterminal("Expression");

        //<expression> ::= <term> | <expression> + <term> | <expression> - <term>

        //Always starts with term
        term();

        //+ AND -
        if(nextToken.symbol == 27){
            acceptTerminal(27);
            expression();
        }else if (nextToken.symbol ==24){
            acceptTerminal(24);
            expression();
        }

//        System.out.println("312END Expression");
        myGenerate.finishNonterminal("Expression");
    }



    /**
     * Method representing Terminal term from the grammar rules.
     * <term> ::= <factor> | <term> * <factor> | <term> / <factor>
     *  times symbol =33, devide symbol =6
     * @throws IOException
     * @throws CompilationException
     */
    private void term() throws IOException, CompilationException {
        //Terms
        //System.out.println("Begin TERM");
        myGenerate.commenceNonterminal("TERM");

        //starts with factor
        factor();

        //multiply
        if (nextToken.symbol ==33){
            acceptTerminal(33);
            term();
        //divide
        }else if(nextToken.symbol==6){
            acceptTerminal(6);
            term();
        }

       // System.out.println("End TERM");
        myGenerate.finishNonterminal("TERM");

    }


    /**
     * Facotr method representing Terminal factor from the grammar rules.
     * <Factor> can be identifier | numberConst or | <expression>
     * @throws IOException
     * @throws CompilationException
     */
    private void factor() throws IOException, CompilationException {
//        System.out.println("BEGIN FACTOR");
        myGenerate.commenceNonterminal("FACTOR");
        if(nextToken.symbol== 16){
            acceptTerminal(16);
        }else if(nextToken.symbol ==26){
            acceptTerminal(26);
        }else {
//            System.out.println("Go to expression again");
            System.out.println("Factor can be either Identifier or NumberConst");
            myGenerate.reportError(nextToken, " Expected identifier or number const");
//            expression();
        }
//        System.out.println("END FACTOR");
        myGenerate.finishNonterminal("FACTOR");

    }

    /**
     * Method representing the Terminal argument_list from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void argumentList() throws IOException, CompilationException {
//        System.out.println("312BEGIN ArgumentList");
        myGenerate.commenceNonterminal("ArgumentList");


        if(nextToken.symbol == 29){
            return;
        }
        acceptTerminal(16);
        if(nextToken.symbol == 5){
            acceptTerminal(5);
            argumentList();
        }



//        System.out.println("312END ArgumentList");
        myGenerate.finishNonterminal("ArgumentList");
    }

    //Procedure

    /**
     * Method representing the Terminal <procedure statement> in the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void callStatement() throws IOException, CompilationException {
//        System.out.println("312BEGIN ProcedureStatement");
        myGenerate.commenceNonterminal("ProcedureStatement");
        //accept call
        acceptTerminal(3);

        //identifier
        acceptTerminal(16);

        //Need (
        acceptTerminal(20);

        //Argument list
        argumentList();

        //Need )
        acceptTerminal(29);


//        System.out.println("312END ProcedureStatement");
        myGenerate.finishNonterminal("ProcedureStatement");
    }
    //While symbol = 36
    //While statement ::= while -> <condition> -> loop -> <statement_list> end loop.

    /**
     * Method representing the Terminal <while statement> from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void whileStatement() throws IOException, CompilationException {
//        System.out.println("312BEGIN WhileStatement");

        myGenerate.commenceNonterminal("WhileStatement");

        //While
        acceptTerminal(36);
        //condition
        condition();
//        if(nextToken.symbol ==)

        //start of loop, accept LOOP symbol
        acceptTerminal(23);

        //go to statement list
        statementList();


        //if it's column do another statement
        while (nextToken.symbol == 30){
            acceptTerminal(30);

            //if next symbol is END, exit while
            if(nextToken.symbol == 8){
                break;
            }
            statementList();
        }

        //end of loop

        acceptTerminal(8);
        acceptTerminal(23);



        //statemenlist

//        System.out.println("312END WhileStatement");
        myGenerate.finishNonterminal("WhileStatement");

    }
    //Condition has 3 options ::= identifier -> <conditional operator> -> identifier/numberConst/stringConst

    /**
     * Method representing the Terminal condition from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void condition() throws IOException, CompilationException {
//        System.out.println("312BEGIN Condition");
        //First one has to be identifier
        myGenerate.commenceNonterminal("Condition");
        acceptTerminal(16);


        conditionOperator();

        //Identifier
        if(nextToken.symbol == 16){
            acceptTerminal(16);
        }
        //numberConst
        else if(nextToken.symbol == 26){
            acceptTerminal(26);
        }
        //stringConst
        else if(nextToken.symbol == 31){
            acceptTerminal(31);
        }else System.out.println("ERROR, expected (identifier / numbconst / stringConst) " +
                "but found: " + nextToken.toString());


//        System.out.println("312END Condition");
        myGenerate.finishNonterminal("Condition");
    }

    //Conditional operators
    //equal = 11, greaterEqual = 14, greaterThan = 15, lessEqual = 21
    //lessThan = 22, notEqual = 25,

    /**
     * Method representing the Terminal conditionOperator from the grammar rules
     * @throws IOException
     * @throws CompilationException
     */
    private void conditionOperator() throws IOException, CompilationException {
//        System.out.println("312BEGIN ConditionalOperator");
        myGenerate.commenceNonterminal("ConditionalOperator");
        switch (nextToken.symbol){
            //equal
            case 11:
                acceptTerminal(11);
                break;
            case 14:
                acceptTerminal(14);
                break;
            case 15:
                acceptTerminal(15);
                break;
            case 21:
                acceptTerminal(21);
                break;
            case 22:
                acceptTerminal(22);
                break;
            case 25:
                acceptTerminal(25);
                break;
        }
//        System.out.println("312END ConditionalOperator");
        myGenerate.finishNonterminal("ConditionalOperator");
    }

    //If statement can be two options::=
    // if <condition> then <statementlist> end if
    // if <condition> then <statement list> else <statementlist> end if

    /**
     * Method representing the Terminal if statement from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void if_statement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("IfStatement");

        //has to start with ' if '
        acceptTerminal(17);
        condition();
        //after condition is ' then '
        acceptTerminal(34 );

        //statementlist
        statementList();

        //if has else
        if(nextToken.symbol == 9){
            acceptTerminal(9);

            statementList();
        }

        //end
        acceptTerminal(8);
        //if
        acceptTerminal(17 );


        myGenerate.finishNonterminal("IfStatement");
    }

    //Method for Until statements ::=
    //do <statement_list> until <condition>

    /**
     * Method representing the Terminal until statement from the grammar rules
     * @throws IOException
     * @throws CompilationException
     */
    private void untilStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("UntilStatement");
        //Accept do
        acceptTerminal(7);
        statementList();
        //accept Until
        acceptTerminal(35);
        condition();

        myGenerate.finishNonterminal("UntilStatement");

    }

    //Method for ForStatement
    //<for statement> ::=
    //for (<assignment statement> ; <condition> ; <assignment statement>) do <statement list> end loop

    /**
     * Method representing the Terminal for statement from the grammar rules.
     * @throws IOException
     * @throws CompilationException
     */
    private void forStatement() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("ForStatement");
        //First accept For
        acceptTerminal(37);

        //Accept left parenth
        acceptTerminal(20);

        assignmentStatement();

        //accept semi column
        acceptTerminal(30);
        condition();
        acceptTerminal(30 );
        assignmentStatement();

        //Accept right parenthesis
        acceptTerminal(29);

        //accept DO
        acceptTerminal(7 );

        statementList();

        // should it have more than one statements?
//        System.out.println("TEEEEEST ------- " + nextToken.toString());

        //if it's column do another statement
        while (nextToken.symbol == 30){
            acceptTerminal(30);
            statementList();
        }

        //END
        acceptTerminal(8);
        //LOOP
        acceptTerminal(23);

        myGenerate.finishNonterminal("ForStatement");

    }

    //Method containing the statement list

    /**
     * This method represents the Terminal statement list from the grammar rules.
     * It contains all possible statements.
     * @throws IOException
     * @throws CompilationException
     */
    private void statementList() throws IOException, CompilationException {
//        System.out.println("312BEGIN StatementList");
        myGenerate.commenceNonterminal("StatementList");
        statementlist_num++;


        switch (nextToken.symbol){
            //Call statement - procedure - symbol 3
            case 3:
//                    acceptTerminal(3);
                myGenerate.commenceNonterminal("Statement");
                callStatement();
                myGenerate.finishNonterminal("Statement");

                break;
            //If statement
            case 17:
                //code
                //myGenerate.commenceNonterminal();
                if_statement();
//                System.out.println("If statement***");
                break;
            //While statement
            case 36:
                //code
                myGenerate.commenceNonterminal("Statement");
                whileStatement();
                myGenerate.finishNonterminal("Statement");
                break;
            //Until statement
            case 35:
                //code
                untilStatement();
                // System.out.println("Until statement***");
                break;
            //For statement
            case 37:
                //code
                //System.out.println("For statement***");
                forStatement();
                break;
            //Assignment statement  - starts with identifier
            case 16:
                myGenerate.commenceNonterminal("Statement");
                assignmentStatement();
                myGenerate.finishNonterminal("Statement");

                break;

        }
//        System.out.println("312END StatementList");
        myGenerate.finishNonterminal("StatementList");
    }

}
