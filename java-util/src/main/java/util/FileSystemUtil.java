package main.java.util;

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

    public static void createDirecotry(Path path)
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

        File[] fList = directory.listFiles();
        Vector<File> fileNames = new Vector<File>();

        for (File file : fList)
        {

            if (file.isFile())
            {

                fileNames.add(file);
                // System.out.println(file.getName());

            }

        }
        return fileNames;

    }

}
