package ch.heigvd.dai;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(name = "file-converter", description = "A CLI tool for file format conversion.")
public class FileConverter implements Runnable {

    ///Set all user parameters
    @Option(names = "--input_type", required = true, description = "Input file type (e.g., JSON, XML, CSV)")
    private String inputType;

    @Option(names = "--filepath", required = true, description = "Path to the input file")
    private Path filePath;

    @Option(names = "--output_type", required = true, description = "Output file type (e.g., JSON, XML, CSV)")
    private String outputType;

    /// Call each conversion methods depending on the output format
    @Override
    public void run() {
        try {
            switch (inputType.toUpperCase()) {
                case "JSON":
                    if (outputType.equalsIgnoreCase("XML")) {
                        convertJsonToXml(filePath);
                    } else if (outputType.equalsIgnoreCase("CSV")) {
                        convertJsonToCsv(filePath);
                    } else {
                        unsupportedConversion();
                    }
                    break;

                case "CSV":
                    if (outputType.equalsIgnoreCase("JSON")) {
                        convertCsvToJson(filePath);
                    } else if (outputType.equalsIgnoreCase("XML")) {
                        convertCsvToXml(filePath);
                    } else {
                        unsupportedConversion();
                    }
                    break;

                case "XML":
                    if (outputType.equalsIgnoreCase("JSON")) {
                        convertXmlToJson(filePath);
                    } else if (outputType.equalsIgnoreCase("CSV")) {
                        convertXmlToCsv(filePath);
                    } else {
                        unsupportedConversion();
                    }
                    break;

                default:
                    unsupportedConversion();
            }
        } catch (IOException | CsvException e) {
            System.err.println("Error during file processing: " + e.getMessage());
        }
    }

    private void unsupportedConversion() {
        System.out.println("Conversion from " + inputType + " to " + outputType + " is not supported.");
    }

    //Create a new file for each successfull conversion
    private Path generateOutputFilePath(Path inputFilePath, String inputType, String outputType) {
        String originalFileName = inputFilePath.getFileName().toString();
        String newFileName = inputType.toLowerCase() + "-to-" + outputType.toLowerCase() + "." + outputType.toLowerCase();
        return inputFilePath.getParent().resolve(newFileName);
    }

    ///Conversion methods
    private void convertJsonToXml(Path inputFilePath) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();

        //Use the Jackson lib to read and parse JSON data
        JsonNode jsonNode = jsonMapper.readTree(new File(inputFilePath.toString()));

        //Convert the path to a string that represent the file path
        String xmlContent = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                Map.of("root", jsonNode)
        );

        //Create a new output file
        Path outputFilePath = generateOutputFilePath(inputFilePath, "json", "xml");
        Files.writeString(outputFilePath, xmlContent);

        System.out.println("Successfully converted JSON to XML. Output saved to: " + outputFilePath);
    }

    private void convertCsvToJson(Path inputFilePath) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(new FileReader(inputFilePath.toString()));
        List<String[]> csvData = csvReader.readAll(); //Use a list to map character
        csvReader.close();

        if (csvData.isEmpty()) {
            System.err.println("Error: CSV file is empty.");
            return;
        }

        String[] headers = csvData.get(0);
        List<Map<String, String>> jsonRecords = new ArrayList<>();

        for (int i = 1; i < csvData.size(); i++) {
            Map<String, String> jsonRecord = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                jsonRecord.put(headers[j], csvData.get(i)[j]);
            }
            jsonRecords.add(jsonRecord);
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonContent = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonRecords);

        Path outputFilePath = generateOutputFilePath(inputFilePath, "csv", "json");
        Files.writeString(outputFilePath, jsonContent);

        System.out.println("Successfully converted CSV to JSON. Output saved to: " + outputFilePath);
    }

    private void convertJsonToCsv(Path inputFilePath) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsonNode = jsonMapper.readTree(new File(inputFilePath.toString()));

        List<String[]> csvData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<String> values = new ArrayList<>();

        //Create csv columns and convert/add data to it
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                if (headers.isEmpty()) {
                    node.fieldNames().forEachRemaining(headers::add);
                    csvData.add(headers.toArray(new String[0]));
                }
                values.clear();
                headers.forEach(field -> values.add(node.path(field).asText()));
                csvData.add(values.toArray(new String[0]));
            }
        }

        Path outputFilePath = generateOutputFilePath(inputFilePath, "json", "csv");
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFilePath.toString()));
        csvWriter.writeAll(csvData);
        csvWriter.close();

        System.out.println("Successfully converted JSON to CSV. Output saved to: " + outputFilePath);
    }

    private void convertXmlToJson(Path inputFilePath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode = xmlMapper.readTree(new File(inputFilePath.toString()));

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonContent = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        Path outputFilePath = generateOutputFilePath(inputFilePath, "xml", "json");
        Files.writeString(outputFilePath, jsonContent);

        System.out.println("Successfully converted XML to JSON. Output saved to: " + outputFilePath);
    }

    private void convertXmlToCsv(Path inputFilePath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode = xmlMapper.readTree(new File(inputFilePath.toString()));

        List<String[]> csvData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<String> values = new ArrayList<>();

        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                if (headers.isEmpty()) {
                    node.fieldNames().forEachRemaining(headers::add);
                    csvData.add(headers.toArray(new String[0]));
                }
                values.clear();
                headers.forEach(field -> values.add(node.path(field).asText()));
                csvData.add(values.toArray(new String[0]));
            }
        }

        Path outputFilePath = generateOutputFilePath(inputFilePath, "xml", "csv");
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFilePath.toString()));
        csvWriter.writeAll(csvData);
        csvWriter.close();

        System.out.println("Successfully converted XML to CSV. Output saved to: " + outputFilePath);
    }

    private void convertCsvToXml(Path inputFilePath) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(new FileReader(inputFilePath.toString()));
        List<String[]> csvData = csvReader.readAll();
        csvReader.close();

        if (csvData.isEmpty()) {
            System.err.println("Error: CSV file is empty.");
            return;
        }

        String[] headers = csvData.get(0);
        List<Map<String, String>> records = new ArrayList<>();

        for (int i = 1; i < csvData.size(); i++) {
            Map<String, String> record = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                record.put(headers[j], csvData.get(i)[j]);
            }
            records.add(record);
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonContent = jsonMapper.writeValueAsString(records);

        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode = jsonMapper.readTree(jsonContent);

        String xmlContent = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of("root", jsonNode));

        Path outputFilePath = generateOutputFilePath(inputFilePath, "csv", "xml");
        Files.writeString(outputFilePath, xmlContent);

        System.out.println("Successfully converted CSV to XML. Output saved to: " + outputFilePath);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileConverter()).execute(args);
        System.exit(exitCode);
    }
}
