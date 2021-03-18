package ar.edu.itba;


import org.apache.commons.cli.*;

public class CommandParser {

    public static double cellLength;

    public static String inFilename;

    public static String outFilename;

    private static Options createOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Show help menu");
        options.addOption("c", "cellLength", true, "Cell length");
        options.addOption("o", "outfile", true, "Output file to write nodes to");
        options.addOption("i","infile", true, "Input file to read segments from");
        return options;
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Graph Drawer", options);
        System.exit(0);
    }

    static void parseCommandLine(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("h")) printHelp(options);

            if(cmd.hasOption("c")) {
                cellLength = Double.parseDouble(cmd.getOptionValue("c"));
            }
            else {
                System.out.println("You must specify cell length value");
                printHelp(options);
            }

            if (cmd.hasOption("i")) {
                inFilename = cmd.getOptionValue("i");
            }
            else {
                System.out.println("You must specify an input file");
                printHelp(options);
            }

            if (cmd.hasOption("o")) {
                outFilename = cmd.getOptionValue("o");
            }
            else {
                outFilename = "nodes.xyz";
            }

        }catch (Exception e) {
            System.out.println("Invalid command format");
            printHelp(options);
        }
    }


}
