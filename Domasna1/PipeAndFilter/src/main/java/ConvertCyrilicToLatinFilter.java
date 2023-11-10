import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConvertCyrilicToLatinFilter implements Filter<String>{

    Map<String, String> mapping;

    public ConvertCyrilicToLatinFilter() throws FileNotFoundException {
        mapping = new HashMap<>();
        ClassLoader loader = PipeAndFilterMain.class.getClassLoader();
        File dataFile = new File(loader.getResource("cyrilicToLatin.csv").getFile());
        Scanner scanner = new Scanner(dataFile, "UTF-8");
        while (scanner.hasNextLine()){
            String[] parts = scanner.nextLine().split(",");
            mapping.put(parts[0], parts[1]);
        }
    }

    @Override
    public String execute(String input) {
        //TODO implement logic
        String[] split = input.split(",");
        String name = split[split.length-1];
        String newName = "";
        for(int i=0;i<name.length();i++){
            if(mapping.containsKey(Character.toString(name.charAt(i)))){
                newName += mapping.get(Character.toString(name.charAt(i)));
            }else{
                newName += Character.toString(name.charAt(i));
            }
        }
        split[split.length-1] = newName;
        return String.join(",", split);
    }
}
