public class FirstLetterUpperFilter implements Filter<String>{
    @Override
    public String execute(String input) {
        String[] split = input.split(",");
        split[split.length-1] = Character.toString(split[split.length-1].charAt(0)).toUpperCase()
                +split[split.length-1].substring(1);
        return String.join(",", split);
    }
}
