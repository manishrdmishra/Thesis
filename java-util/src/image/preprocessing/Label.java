package image.preprocessing;

import ij.gui.Roi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Label
{

    String[] labels = null;

    /**
     * 
     * @param labelFileName
     *            This constructor reads the name of labels from a file.
     * @throws IOException
     */

    public Label(String labelFileName)
    {
        labels = null;

        try
        {
            readLabelsFromFile(labelFileName);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Label(String[] labels)
    {
        this.labels = labels;

    }

    public void readLabelsFromFile(String labelFileName) throws IOException
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

                labels[count] = line;
                line = br.readLine();
                count = count + 1;
            }

        } finally
        {
            br.close();
        }

    }

    public String[] getLabels()
    {
        return labels;
    }

    public int getLabelForRoi(Roi roi)
    {

        int label = 0;
        String roiName = roi.getName();
        for (int index = 0; index < labels.length; index++)
        {
            if (roiName.contains(labels[index]) == true)
            {
                label = index + 1;
            }
        }
        return label;
    }

}
