package bg.sofia.uni.fmi.mjt.markdown.converter;

import bg.sofia.uni.fmi.mjt.markdown.MarkdownConverter;
import bg.sofia.uni.fmi.mjt.markdown.MarkdownConverterAPI;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarkdownConverterTest {
    final String inputBold = "I just love **Java**.";
    final String expectedBold = """
            <html>
            <body>
            I just love <strong>Java</strong>.
            </body>
            </html>
            """;

    final String inputItalic = "I just love *Java*.";
    final String expectedItalic = """
            <html>
            <body>
            I just love <em>Java</em>.
            </body>
            </html>
            """;

    final String inputCode = "I just love `Java`.";
    final String expectedCode = """
            <html>
            <body>
            I just love <code>Java</code>.
            </body>
            </html>
            """;

    final String inputBoldAndItalic = "I just love * **Java** *.";
    final String expectedBoldAndItalic = """
            <html>
            <body>
            I just love <em> <strong>Java</strong> </em>.
            </body>
            </html>
            """;
    final String inputHeading = "# Heading level 1";
    final String expectedHeading = """
            <html>
            <body>
            <h1>Heading level 1</h1>
            </body>
            </html>
            """;

    final String firstMarkdown = """
            # Heading level 1
            **Lorem** ipsum dolor sit amet, 
            *consectetur* adipiscing `elit`, 
            ** *sed* ** do eiusmod `tempor` **incididunt**
            ## Heading level 2
            `ut` labore et **dolore** magna *aliqua*.
            Ut enim ad minim veniam.
            """;
    final String secondMarkdown = """
            # Heading level 1
            ## Heading level 2
                            
            **Lorem** ipsum dolor sit amet.
                            
            ###### Heading level 6
                            
            *consectetur* **adipiscing** `elit`.
            """;
    final String firstHtml = """
            <html>
            <body>
            <h1>Heading level 1</h1>
            <strong>Lorem</strong> ipsum dolor sit amet,
            <em>consectetur</em> adipiscing <code>elit</code>,
            <strong> <em>sed* ** do eiusmod <code>tempor</code> *</em>incididunt</strong>
            <h2>Heading level 2</h2>
            <code>ut</code> labore et <strong>dolore</strong> magna <em>aliqua</em>.
            Ut enim ad minim veniam.
            </body>
            </html>
            """;
    final String secondHtml = """
            <html>
            <body>
            <h1>Heading level 1</h1>
            <h2>Heading level 2</h2>
                        
            <strong>Lorem</strong> ipsum dolor sit amet.
                        
            <h6>Heading level 6</h6>
                        
            <em>consectetur</em> <strong>adipiscing</strong> <code>elit</code>.
            </body>
            </html>
            """;

    private final MarkdownConverterAPI converter = new MarkdownConverter();

    @Test
    void testConvertMarkdownReaderBold() {
        StringReader sourceReader = new StringReader(inputBold);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(expectedBold, outputWriter.toString(),
                "Expected well formed html text with bolt tag");
    }

    @Test
    void testConvertMarkdownReaderItalic() {
        StringReader sourceReader = new StringReader(inputItalic);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(expectedItalic, outputWriter.toString(),
                "Expected well formed html text with italic tag");
    }

    @Test
    void testConvertMarkdownReaderCode() {
        StringReader sourceReader = new StringReader(inputCode);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(expectedCode, outputWriter.toString(),
                "Expected well formed html text with code tag");
    }


    @Test
    void testConvertMarkdownReaderBoldAndItalic() {
        StringReader sourceReader = new StringReader(inputBoldAndItalic);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(expectedBoldAndItalic, outputWriter.toString(),
                "Expected well formed html text with bold and italic tags");
    }

    @Test
    void testConvertMarkdownReaderHeader() {
        StringReader sourceReader = new StringReader(inputHeading);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(expectedHeading, outputWriter.toString(),
                "Expected well formed html text with header tag 1");
    }

    @Test
    void testConverter() {
        StringReader sourceReader = new StringReader(firstMarkdown);
        StringWriter outputWriter = new StringWriter();

        converter.convertMarkdown(sourceReader, outputWriter);

        assertEquals(firstHtml, outputWriter.toString(),
                "Expected well formed html text with all supported tags");
    }

    @Test
    void testConvertMarkdownWithPaths() throws IOException {
        final String parentDirName = "temp";

        File inputFile = new File(parentDirName + File.separator + "tempTestInputMarkdown.md");
        File outputFile = new File(parentDirName + File.separator + "tempTestOutputHtml.html");

        inputFile.getParentFile().mkdirs();

        try {
            inputFile.createNewFile();
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = Files.newBufferedWriter(inputFile.toPath())) {
            bw.write(secondMarkdown);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        converter.convertMarkdown(inputFile.toPath(), outputFile.toPath());

        long l = compareFileContentWithStringContentLineByLine(new FileReader(outputFile), new StringReader(secondHtml));

        deleteLogsRecursively(new File(parentDirName));

        assertEquals(-1, l, "Expected well formed html in file");
    }

    @Test
    void testConvertAllMarkdownFiles() throws IOException {
        final String sourceDirName = "markdownFiles";
        final String destinationDirName = "htmlFiles";

        File inputFile1 = new File(sourceDirName + File.separator + "first.md");
        File inputFile2 = new File(sourceDirName + File.separator + "second.md");


        inputFile1.getParentFile().mkdirs();
        new File(destinationDirName).mkdir();

        try {
            inputFile1.createNewFile();
            inputFile2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                BufferedWriter bw1 = Files.newBufferedWriter(inputFile1.toPath());
                BufferedWriter bw2 = Files.newBufferedWriter(inputFile2.toPath())
        ) {
            bw1.write(firstMarkdown);
            bw2.write(secondMarkdown);

            bw1.flush();
            bw2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        converter.convertAllMarkdownFiles(Path.of(sourceDirName), Path.of(destinationDirName));

       assertTrue(compareHtmlFilesContentWithExpectedContent(
                        Path.of(destinationDirName),
                        Map.of("firstHtml", firstHtml, "secondHtml", secondHtml)
       ),"Expected well formed html files");

        deleteLogsRecursively(new File(sourceDirName));
        deleteLogsRecursively(new File(destinationDirName));
    }


    /*
        In the while loop, instead of reading bytes, we read a line of each file and check for equality.
        If all the lines are identical for both files, then we return -1L, but if there's a discrepancy,
        we return the line number where the first mismatch is found.

        If the files are of different sizes but the smaller file matches the corresponding lines of the larger file,
        then it returns the number of lines of the smaller file.
     */
    public long compareFileContentWithStringContentLineByLine(Reader r1, Reader r2) throws IOException {
        try (BufferedReader bf1 = new BufferedReader(r1);
             BufferedReader bf2 = new BufferedReader(r2)) {

            long lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();

                if (line2 == null || !line1.equals(line2)) {
                    return lineNumber;
                }

                lineNumber++;
            }
            if (bf2.readLine() == null) {
                return -1;
            } else {
                return lineNumber;
            }
        }
    }

    private void deleteLogsRecursively(File dir) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (final File f : files) {
                deleteLogsRecursively(f);
            }
        }

        dir.delete();
    }

    public boolean compareHtmlFilesContentWithExpectedContent(Path htmlDir, Map<String, String> actualExpected) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(htmlDir, "*.html")) {

            for (Path file : stream) {
                String sourceFileName = file.getFileName().toString();
                String sourceFileNameWithoutExtension = sourceFileName.substring(0, sourceFileName.indexOf("."));

                FileReader fileReader = new FileReader(file.toString());
                StringReader stringReader = new StringReader(actualExpected.get(sourceFileNameWithoutExtension+"Html"));

                long l = compareFileContentWithStringContentLineByLine(fileReader, stringReader);
                if (l != -1) {
                    return false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while comparing file contents from dir: " + htmlDir, e);
        }
        return true;
    }
}
