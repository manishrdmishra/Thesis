package main.java;

import main.java.exception.NumberOfArgumentsMismatchException;

public interface Command
{
    public void parseArguments(String[] args) throws NumberOfArgumentsMismatchException;

    public void execute();
}
