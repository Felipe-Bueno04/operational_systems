import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        var sortingThreadsManager = new SortingThreadsManager(3,
                List.of(SortingEnum.Bubble, SortingEnum.Quick, SortingEnum.Bubble), 100);
        sortingThreadsManager.startComputing();
    }
}
