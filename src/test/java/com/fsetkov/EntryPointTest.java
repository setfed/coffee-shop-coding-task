package com.fsetkov;

import com.fsetkov.exception.ExitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryPointTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void cleanUp(@TempDir Path tempDir) {
        // Reset System.in and System.out to their original streams
        System.setOut(originalOut);
        System.setIn(originalIn);

        // Explicitly delete the temp directory
        try {
            Files.walk(tempDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();
    }

    @Test
    void testMainMethod(@TempDir Path tempDir) throws Exception {
        // Simulate user input:
        // - large coffee with extra milk
        // - small coffee with special roast
        // - bacon roll
        // - orange juice
        String input = """
                John
                3
                1
                1
                3
                5
                4
                end
                exit
                """;

        try (InputStream in = new ByteArrayInputStream(input.getBytes());
             PrintStream out = new PrintStream(tempDir.resolve("output.txt").toFile())) {
            System.setIn(in);
            System.setOut(out);

            // when
            try {
                EntryPoint.main(null);
            } catch (ExitException e) {
                // Ignore the exception
            }

            // then
            try (Scanner scanner = new Scanner(new FileInputStream(tempDir.resolve("output.txt").toFile()))) {
                StringBuilder actualOutput = new StringBuilder();
                while (scanner.hasNextLine()) {
                    actualOutput.append(scanner.nextLine()).append(System.lineSeparator());
                }

                String expectedOutput = String.join(System.lineSeparator(),
                        "Hello at our coffee shop, what is your name?",
                        "Hello John",
                        "Write your choice number,",
                        "when you finish write 'end'",
                        " 1. Small coffee                   | 2.55 CHF",
                        " 2. Medium coffee                  | 3.05 CHF",
                        " 3. Big coffee                     | 3.55 CHF",
                        " 4. Freshly squeezed orange juice  | 3.95 CHF",
                        " 5. Bacon roll                     | 4.53 CHF",
                        "",
                        "You chose Big coffee",
                        "You can choose additional extra for your choice",
                        " 1. Extra milk                     | 0.32 CHF",
                        " 2. Foamed milk                    | 0.51 CHF",
                        " 3. Special roast coffee           | 0.95 CHF",
                        " 4. No extra                       | 0.00 CHF",
                        "",
                        "You chose Extra milk",
                        "Write your choice number,",
                        "when you finish write 'end'",
                        " 1. Small coffee                   | 2.55 CHF",
                        " 2. Medium coffee                  | 3.05 CHF",
                        " 3. Big coffee                     | 3.55 CHF",
                        " 4. Freshly squeezed orange juice  | 3.95 CHF",
                        " 5. Bacon roll                     | 4.53 CHF",
                        "",
                        "You chose Small coffee",
                        "You can choose additional extra for your choice",
                        " 1. Extra milk                     | 0.32 CHF",
                        " 2. Foamed milk                    | 0.51 CHF",
                        " 3. Special roast coffee           | 0.95 CHF",
                        " 4. No extra                       | 0.00 CHF",
                        "",
                        "You chose Special roast coffee",
                        "Write your choice number,",
                        "when you finish write 'end'",
                        " 1. Small coffee                   | 2.55 CHF",
                        " 2. Medium coffee                  | 3.05 CHF",
                        " 3. Big coffee                     | 3.55 CHF",
                        " 4. Freshly squeezed orange juice  | 3.95 CHF",
                        " 5. Bacon roll                     | 4.53 CHF",
                        "",
                        "You chose Bacon roll",
                        "Write your choice number,",
                        "when you finish write 'end'",
                        " 1. Small coffee                   | 2.55 CHF",
                        " 2. Medium coffee                  | 3.05 CHF",
                        " 3. Big coffee                     | 3.55 CHF",
                        " 4. Freshly squeezed orange juice  | 3.95 CHF",
                        " 5. Bacon roll                     | 4.53 CHF",
                        "",
                        "You chose Freshly squeezed orange juice",
                        "Write your choice number,",
                        "when you finish write 'end'",
                        " 1. Small coffee                   | 2.55 CHF",
                        " 2. Medium coffee                  | 3.05 CHF",
                        " 3. Big coffee                     | 3.55 CHF",
                        " 4. Freshly squeezed orange juice  | 3.95 CHF",
                        " 5. Bacon roll                     | 4.53 CHF",
                        "",
                        "-----------------------------------------",
                        "Description of Goods:",
                        " 1. Freshly squeezed orange juice  | 3.95 CHF",
                        " 2. Big coffee                     | 3.55 CHF",
                        " 3. Small coffee                   | 2.55 CHF",
                        " 4. Bacon roll                     | 4.53 CHF",
                        " 5. Special roast coffee           | 0.00 CHF",
                        " 6. Extra milk                     | 0.32 CHF",
                        "",
                        "-----------------------------------------",
                        "Total: 14.90 CHF",
                        "-----------------------------------------",
                        "Do you want to end program? If yes print 'exit', if no press ENTER",
                        ""
                );
                assertEquals(expectedOutput, actualOutput.toString());
            }
        } finally {
            // Reset System.in and System.out to their original streams
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    private static String ls() {
        return lineSeparator();
    }
}