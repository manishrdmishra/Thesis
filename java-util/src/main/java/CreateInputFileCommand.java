package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;
import main.java.util.FileSystemUtil;

public class CreateInputFileCommand implements Command
{

    private String dataDirName;
    private String labelDirName;
    private String newFileDirName;

    @Override
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        try
        {
            dataDirName = args[0];
            labelDirName = args[1];
            newFileDirName = args[2];
        } catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    public void execute()
    {

        Vector<File> dataFileNames = FileSystemUtil.getFiles(dataDirName);
        Vector<File> labelFileNames = FileSystemUtil.getFiles(labelDirName);
        String empty = "  ";
        String newLine ="\n";
     
        try
        {
            FileOutputStream out = new FileOutputStream(newFileDirName);
            for (int i = 0; i < dataFileNames.size(); i++)
            {

                out.write(dataFileNames.get(i).toString().getBytes());
                out.write(empty.getBytes());
                out.write(labelFileNames.get(i).toString().getBytes());
                out.write(newLine.getBytes());
            }

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
