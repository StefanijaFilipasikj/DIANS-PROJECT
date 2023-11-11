public class FirstLetterUpperFilter implements Filter<String>{
    @Override
    public String execute(String input) {
        String[] split = input.split(",");

        split[split.length-1] = convert(split[split.length-1]);
        split[split.length-2] = convert(split[split.length-2]);
        split[split.length-3] = convert(split[split.length-3]);

        return String.join(",", split);
    }

    public String convert(String word){
        return Character.toString(word.charAt(0)).toUpperCase() + word.substring(1);
    }
}
