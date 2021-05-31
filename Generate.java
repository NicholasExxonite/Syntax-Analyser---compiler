public class Generate extends AbstractGenerate {
    public Generate(){

    }

    /**
     * Method is called when an error occurs.
     * @param token
     * @param explanatoryMessage
     * @throws CompilationException
     */
    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        //Display error message
        System.out.println("Error occurred at line: " + token.lineNumber + ", found: " +
                token.text + ". " +explanatoryMessage);



        //Throw compilation error
        throw new CompilationException("Unexpected token " + token.text + " ," + explanatoryMessage +
                " at line " + token.lineNumber);
    }
}
