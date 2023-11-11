import com.sun.tools.jconsole.JConsoleContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PipeAndFilterMain {

    public static void main(String[] args) throws IOException {

        Filter<String> convertCyrilicToLatinFilter = new ConvertCyrilicToLatinFilter();
        Filter<String> removeIdColumnFilter = new RemoveIdColumnFilter();
        Filter<String> firstLetterUpperFilter = new FirstLetterUpperFilter();

        Pipe<String> pipe = new Pipe<>();

        pipe.addFilter(convertCyrilicToLatinFilter);
        pipe.addFilter(firstLetterUpperFilter);
        pipe.addFilter(removeIdColumnFilter);

        ClassLoader loader = PipeAndFilterMain.class.getClassLoader();
        File dataFile = new File(loader.getResource("updated_data.csv").getFile());
        Scanner scanner = new Scanner(dataFile, "UTF-8");

        BufferedWriter writer = new BufferedWriter(new FileWriter("filteredData.csv"), 8192);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String result = pipe.runFilters(line);
            writer.write(result);
            writer.newLine();
        }

        scanner.close();
        writer.close();
    }
}
