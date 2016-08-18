package main.java.exception;

public class NumberOfArgumentsMismatchException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NumberOfArgumentsMismatchException(String message)
    {
        super(message);
    }

    public String getMessage()
    {
        return super.getMessage();
    }

}
