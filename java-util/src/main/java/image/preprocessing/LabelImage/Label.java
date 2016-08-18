package image.preprocessing.LabelImage;

import ij.gui.Roi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Label
{

    HashMap<String, Integer> labels;

    /**
     * 
     * @param labelFileName
     *            This constructor reads the name of labelName from a file.
     * @throws IOException
     */

    public Label(String labelFileName)
    {
        labels = new HashMap<String, Integer>();

        try
        {
            readlabelNameFromFile(labelFileName);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Label(HashMap<String, Integer> labels)
    {
        this.labels = labels;

    }

    public void readlabelNameFromFile(String labelFileName) throws IOException
    {
        BufferedReader br = null;
        int count = 0;
        try
        {
            br = new BufferedReader(new FileReader(labelFileName));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {

            String line = br.readLine();

            while (line != null)
            {
                String[] labelLine = line.split("\\s+");
                labels.put(labelLine[0], Integer.parseInt(labelLine[1]));
                line = br.readLine();
                count = count + 1;
            }

        } finally
        {
            br.close();
        }

    }

    public HashMap<String, Integer> getlabelName()
    {
        return labels;
    }

    public int getLabelForRoi(Roi roi)
    {

        String roiName = roi.getName();
        Iterator<Entry<String, Integer>> labelIterator = labels.entrySet()
                .iterator();
        while (labelIterator.hasNext())
        {

            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) labelIterator
                    .next();
            if (roiName.contains(pair.getKey().toString()) == true)
            {

                return pair.getValue();
            }

        }

        return 0;

    }

}
