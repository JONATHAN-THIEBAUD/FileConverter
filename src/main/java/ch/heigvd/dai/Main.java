package ch.heigvd.dai;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import java.nio.file.Path;

@CommandLine.Command(name = "file-converter", description = "A CLI tool for file format conversion.")
public class FileConverter implements Runnable {

    // Option for input file type (e.g., JSON, XML, CSV)
    @Option(names = "--input_type", required = true, description = "Input file type (e.g., JSON, XML, CSV)")
    private String inputType;

    // Option for file path to the input file
    @Option(names = "--filepath", required = true, description = "Path to the input file")
    private Path filePath;

    // Option for output file type (e.g., JSON, XML, CSV)
    @Option(names = "--output_type", required = true, description = "Output file type (e.g., JSON, XML, CSV)")
    private String outputType;

    @Override
    public void run() {
        // For now, just print the values provided by the user
        System.out.println("Input Type: " + inputType);
        System.out.println("File Path: " + filePath);
        System.out.println("Output Type: " + outputType);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileConverter()).execute(args);
        System.exit(exitCode);
    }
}
