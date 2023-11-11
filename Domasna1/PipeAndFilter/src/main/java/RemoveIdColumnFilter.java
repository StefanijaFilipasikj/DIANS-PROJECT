import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveIdColumnFilter implements Filter<String>{
    @Override
    public String execute(String input) {
        String[] split = input.split(",");
        List<String> list = new ArrayList<>(Arrays.stream(split).toList());
        list.remove(0);
        return String.join(",", list);
    }
}
