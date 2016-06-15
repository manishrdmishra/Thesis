package main.java.util;

import java.io.IOException;
import java.util.Vector;

public interface ReadProperties
{
    public Vector<String> readPropertyFile(String fileName) throws IOException;

}
