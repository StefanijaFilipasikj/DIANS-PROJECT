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
        String[] split = input.split(",");

        String name = split[split.length-3];
        String address = split[split.length-2];
        String place = split[split.length-1];

        split[split.length-3] = convert(name);
        split[split.length-2] = convert(address);
        split[split.length-1] = convert(place);

        return String.join(",", split);
    }

    public String convert(String word){
        String result = "";
        for(int i=0;i<word.length();i++){
            if(mapping.containsKey(Character.toString(word.charAt(i)))){
                result += mapping.get(Character.toString(word.charAt(i)));
            }else{
                result += Character.toString(word.charAt(i));
            }
        }
        return result;
    }
}
