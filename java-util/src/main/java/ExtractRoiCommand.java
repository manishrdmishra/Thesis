package main.java;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.image.postprocessing.ExtractRoi.ExtractRoi;

public class ExtractRoiCommand implements Command
{

    String name;

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        name = args[0];

    }

    @Override
    public void execute()
    {
        ExtractRoi extractRoi = new ExtractRoi();
        extractRoi.setImageName(name);
        extractRoi.getRoi();

    }

}
