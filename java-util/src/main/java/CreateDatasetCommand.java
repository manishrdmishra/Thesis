package main.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import main.java.exception.NumberOfArgumentsMismatchException;

/**
 * The arguments required for this  command are
 * 1) Path of source directory from where we will sample files
 * 2) Path of destination directory where we will copy the randomly sampled 
 * files from source directory.
 * 3) Number of files to sample from source 
 */
public class CreateDatasetCommand implements Command
{

    private Path sourceDirName;
    private Path destinationDirNmae;

    // Number of files to be randomly sampled from source directory
    private int numberOfFiles;

    @Override
    
    
    public void parseArguments(String[] args)
            throws NumberOfArgumentsMismatchException
    {
        // TODO Auto-generated method stub
        if (args.length != 3)
        {
            String errorMessage = "This command expects three arguments";
            throw new NumberOfArgumentsMismatchException(errorMessage);
        }

        // The 0th argument is the name of the program, so starting from 1st
        // index.
        sourceDirName = Paths.get(args[0]);
        destinationDirNmae = Paths.get(args[1]);
        numberOfFiles = Integer.parseInt(args[2]);

    }

    @Override
    public void execute()
    {
        // TODO Auto-generated method stub
        List<Path> filesPath = null;
        try
        {
            filesPath = readFilesFromSourceDir();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Vector<Path> randomFiles = null;
        try
        {
            randomFiles = sampleRandomFilesFromSourceDir(filesPath);
        } catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try
        {

            copyRandomFilesToSourceDir(randomFiles);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private List<Path> readFilesFromSourceDir() throws IOException
    {

        List<Path> result = new LinkedList<>();
        try (DirectoryStream<Path> stream = Files
                .newDirectoryStream(sourceDirName))
        {
            for (Path entry : stream)
            {
                result.add(entry);
            }
        } catch (DirectoryIteratorException ex)
        {
            // I/O error encounted during the iteration, the cause is an
            // IOException
            throw ex.getCause();
        }

        return result;

    }

    private Vector<Path> sampleRandomFilesFromSourceDir(List<Path> filesPaths)
            throws Exception
    {
        Vector<Path> randomFiles = new Vector<Path>();

        if (filesPaths.size() < numberOfFiles)
        {
            String errorMessage = "Number of files in source dir is less than the number of you want to sample";
            throw new Exception(errorMessage);
        }

        Random rand = new Random();
        for (int i = 0; i < numberOfFiles; i++)
        {

            int size = filesPaths.size();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt(size);
            Path file = filesPaths.get(randomNum);
            System.out.println(file.getFileName());
            randomFiles.add(file);
            filesPaths.remove(randomNum);

        }
        return randomFiles;

    }

    public void copyRandomFilesToSourceDir(Vector<Path> randomFiles)
            throws IOException
    {

        for (Path file : randomFiles)
        {

            Path newPath = new File(destinationDirNmae.toString()
                    +"/"+ file.getFileName()).toPath();
            System.out.println(newPath.toString());
            Files.copy(file, newPath);
        }
    }

}
