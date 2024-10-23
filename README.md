# File Converter CLI Tool

## Summary
1. [Introduction](#introduction)
2. [Objectives](#objectives)
3. [Group Composition](#group-composition)
4. [Git Workflow](#git-workflow)
5. [Technologies Used](#technologies-used)
6. [How to Build](#how-to-build)
7. [How to Use](#how-to-use)
8. [Supported Conversions](#supported-conversions)
9. [Example Files](#example-files)
10. [Project Structure](#project-structure)
11. [Possible Improvements](#possible-improvements)
12. [License](#license)

---

## Introduction

This project is a simple **Command Line Interface (CLI)** tool built in Java. It allows users to convert files between three formats:
- **JSON**
- **XML**
- **CSV**

The purpose of the project is to practice file processing in Java, using **Picocli** to handle command-line arguments, **Jackson** for reading and writing JSON/XML, and **OpenCSV** for handling CSV files. The tool takes input and output file types and file paths as arguments and performs the file conversion. The CLI also displays messages indicating the success or failure of the process.

---

## Objectives

- **Create a CLI** that processes files using **Java IO**.
- **Practice Java, Maven, and Picocli** by developing a tool to convert files.
- **Practice Git workflows** by sharing and collaborating on code using GitHub.
  
---

## Group Composition

- **Member 1**: Evan Rothen
- **Member 2**: Jonathan Thiébaud

---

## Git Workflow

We followed a collaborative Git workflow:
- **Issues** we didn't need to use the issues for this project, because we have used some more simple ways to communicate.
- We used **2 branches** main and master, which were merged using the CLI (don't need pull request).
- **Signed commits** We weren't able to use this feature cause of a bug when signing with GPG, even if the command to get used GPG key return the right value. 
- A `.gitignore` file was used to ignore IDE-specific files and Maven build output.

---

## Technologies Used

- **Java 21**: The programming language used for this project.
- **Maven**: Used for building the project and managing dependencies.
- **Picocli**: A framework for building command-line applications.
- **Jackson**: A library for handling JSON and XML data.
- **OpenCSV**: A library for handling CSV data.

---

## How to Build

### Prerequisites

- **Java 21** or later must be installed on your system.
- **Maven** must be installed to build the project.

### How to use

```bash
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type <INPUT_TYPE> --filepath <PATH_TO_INPUT_FILE> --output_type <OUTPUT_TYPE>
```
  
### Example to convert files

```bash
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type JSON --filepath ./files/input.json --output_type CSV
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type JSON --filepath ./files/input.json --output_type XML
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type XML --filepath ./files/input.xml --output_type JSON
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type XML --filepath ./files/input.xml --output_type CSV
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type CSV --filepath ./files/input.csv --output_type JSON
java -jar target/cli-converter-1.0-SNAPSHOT.jar --input_type CSV --filepath ./files/input.csv --output_type XML 
```

### What you get 

Line outputed when conversion suceed :
```bash
Successfully converted JSON to CSV. Output saved to: ./files/json-to-csv.csv
```
```bash
json-to-csv.csv
```

### Clone the Repository

```bash
git clone https://github.com/your-username/file-converter-cli.git
cd file-converter-cli
