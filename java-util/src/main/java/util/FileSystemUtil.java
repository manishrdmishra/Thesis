package main.java.util;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.Vector;

public class FileSystemUtil
{

    /**
     * Read the lines of files and stores them in a vector of string.
     * 
     * @return
     */
    public static Vector<String> readFile(String fileName)
    {
        Vector<String> lines = new Vector<String>();
        try
        {

            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    fstream));

            String strLine;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                // Print the content on the console
                System.out.println(strLine);
                lines.add(strLine);

            }

            // Close the input stream
            br.close();
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
        ;
        return lines;
    }

    public static void createDirectory(Path path)
    {
        Set<PosixFilePermission> perms = PosixFilePermissions
                .fromString("rwxr-x---");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions
                .asFileAttribute(perms);
        try
        {
            Files.createDirectory(path, attr);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Vector<File> getFiles(String directoryName)
    {

        File directory = new File(directoryName);

        // get all the files from a directory

        File[] fileList = directory.listFiles();
        Vector<File> fileNames = new Vector<File>();

        for (File file : fileList)
        {

            if (file.isFile())
            {

                fileNames.add(file);
                // System.out.println(file.getName());

            }

        }
        return fileNames;

    }

    public static Vector<File> getDirectories(String directoryName)
    {

        File directory = new File(directoryName);

        // get all the files from a directory

        File[] fileList = directory.listFiles();
        Vector<File> directoryNames = new Vector<File>();

        for (File file : fileList)
        {

            if (file.isDirectory())
            {

                directoryNames.add(file);
                // System.out.println(file.getName());

            }

        }
        return directoryNames;

    }

    public static void saveImages(Path path, Vector<String> imageNames,
            Vector<ImageProcessor> imageProcessors)
    {
        for (int i = 0; i < imageProcessors.size(); i++)
        {

            ImagePlus patchImage = new ImagePlus(imageNames.get(i),
                    imageProcessors.get(i));
            Path newPath = new File(path + "/" + imageNames.get(i)).toPath();
            System.out.println("Saving image at: " + newPath);
            FileSaver fileSaver = new FileSaver(patchImage);
            fileSaver.saveAsTiff(newPath.toString());

        }
    }

}
