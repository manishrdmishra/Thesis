package main.java.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

}
