package main.java;

import java.util.Arrays;

import main.java.exception.NumberOfArgumentsMismatchException;

public class Main
{

    // check the number of arguments
    String[] validCommands = { "labelImages", "sampleImages", "extractPatches",
            "createInputFile", "help" };
    Command command = null;

    public static void main(String[] args)
    {
        System.out.println("Number of arguments : " + args.length);
        int numberOfArguments = args.length;
        Main main = new Main();

        int COMMAND_NAME_INDEX = 0;
        if (numberOfArguments > 0)
        {
            main.printCommandLineArguments(args);
            main.createCommandInstance(args[COMMAND_NAME_INDEX]);
            // pass the remaining argument to command
            String[] remainingArgs = Arrays.copyOfRange(args,
                    COMMAND_NAME_INDEX + 1, args.length);
            main.executeCommand(remainingArgs);
        } else
        {
            main.printValidCommands();
        }

    }

    public void printCommandLineArguments(String[] args)
    {
        System.out.println("argumenst are : ");
        for (String arg : args)
        {
            System.out.print(arg + "  ");
        }
    }

    public void createCommandInstance(String commandName)
    {

        switch (commandName)
        {

        case "labelImages":

            command = new LabelImagesCommand();
            break;

        case "sampleImages":
            command = new CreateDatasetCommand();
            break;

        case "extractPatches":
            command = new ExtractPatchesCommand();
            break;
        case "createInputFile":
            command = new CreateInputFileCommand();
            break;
        case "help":
            printValidCommands();
            break;
        default:
            System.out.println("No such command exist, the valid commands are");
            printValidCommands();

            break;

        }

    }

    public void printValidCommands()
    {
        System.out.print("The valid commands are: ");
        for (String commands : validCommands)
        {
            System.out.print(commands + "  ");
        }
        System.out.println();

    }

    public void executeCommand(String[] args)
    {
        try
        {
            command.parseArguments(args);
        } catch (NumberOfArgumentsMismatchException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        command.execute();
    }
}
