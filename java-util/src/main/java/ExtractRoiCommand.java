package main.java;

import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.image.postprocessing.ExtractRoi.ExtractRoi;
import main.java.util.FileSystemUtil;

public class ExtractRoiCommand implements Command
{

    // This file contains the name of segmented images
    // which comes out of usk-net.
    private String fileName;
    private String pathToSaveRois;

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        fileName = args[0];
        pathToSaveRois = args[1];

    }

    @Override
    public void execute()
    {
        Vector<String> files = FileSystemUtil.readFile(fileName);

        ExtractRoi extractRoi = new ExtractRoi(pathToSaveRois);
        for (int i = 0; i < files.size(); i++)
        {
            System.out.println(files.get(i));
            extractRoi.setImageName(files.get(i));
            extractRoi.getRoi();

        }
    }

}
