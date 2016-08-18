package main.java.util;

import java.util.HashMap;
import java.util.Vector;

public class StringUtil
{
    /**
     * This function splits lines into tokens and stores them into a vector. We
     * assume that the tokens are space separated.
     * 
     * @return
     */
    public static Vector<String> splitLines(Vector<String> lines)
    {
        Vector<String> tokens = new Vector<String>();
        for (String line : lines)
        {
            String[] splited = line.split("\\s+");
            for (String string : splited)
            {
                tokens.add(string);
            }
        }
        return tokens;
    }

    public static HashMap<String, String> convertTokensInPairs(
            Vector<String> tokens)
    {
        HashMap<String, String> imageRoiPairs = new HashMap<String, String>();
        if (tokens.size() % 2 != 0)
        {
            String errorMessage = "The number of tokens should be even";
            System.out.print(errorMessage);

            // throw new Exception(errorMessage);
        }
        for (int i = 0; i < tokens.size(); i = i + 2)
        {
            System.out.println("key: " + tokens.get(i) + "  value: "
                    + tokens.get(i + 1));
            imageRoiPairs.put(tokens.get(i), tokens.get(i + 1));

        }
        return imageRoiPairs;

    }
}
