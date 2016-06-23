package main.java.image.preprocessing.ExtractPatch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import main.java.util.ReadProperties;

public class ReadPatchExtractionProperties implements ReadProperties
{
    Vector<String> extractionProperties = null;
    InputStream inputStream;
    Properties prop = new Properties();

    @Override
    public Vector<String> readPropertyFile(String propertyFileName)
            throws IOException
    {

        loadPropertyFile(propertyFileName);
        extractionProperties = new Vector<String>();
        extractionProperties.add(prop.getProperty("patchWidth"));
        extractionProperties.add(prop.getProperty("patchHeight"));
        extractionProperties.add(prop.getProperty("padding"));
        extractionProperties.add(prop.getProperty("dataPath"));
        extractionProperties.add(prop.getProperty("labelPath"));
        extractionProperties.add(prop.getProperty("dataPatchPath"));
        extractionProperties.add(prop.getProperty("labelPatchPath"));

        return extractionProperties;
    }

    private void loadPropertyFile(String propertyFileName) throws IOException
    {

        inputStream = getClass().getClassLoader().getResourceAsStream(
                propertyFileName);

        if (inputStream != null)
        {
            prop.load(inputStream);
        } else
        {
            inputStream.close();
            throw new FileNotFoundException("property file '"
                    + propertyFileName + "' not found in the classpath");
        }
    }
}
