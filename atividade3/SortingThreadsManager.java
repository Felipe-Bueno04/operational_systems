import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RecursiveTask;

public class SortingThreadsManager {
    private final int threadNumber;

    private final List<SortingEnum> algorithms;

    private final int arrayLength;

    private List<Integer> slicedSortedArrays;

    private final Map<SortingEnum, SortingTask> algorithmsDictionary = new HashMap<SortingEnum, SortingTask>() {{
        put(SortingEnum.Bubble, new BubbleSortTask());
        put(SortingEnum.Quick, new QuickSortTask());
    }};

    public SortingThreadsManager(int threadNumber, List<SortingEnum> algorithms, int arrayLength) {
        this.threadNumber = threadNumber;
        this.algorithms = algorithms;
        this.arrayLength = arrayLength;
        // this.slicedSortedArrays = new int[][];
    }

    private void sortAtIndex(int index, int[] originalArray) {
        long startTime = System.nanoTime();
        SortingEnum algorithm = algorithms.get(index);
        SortingTask task = algorithmsDictionary.get(algorithm);
        int start = index * (arrayLength / threadNumber);
        int end = (index + 1) * (arrayLength / threadNumber);
        int[] slicedArray = Arrays.copyOfRange(originalArray, start, end);
        int[] sortedSlicedArray = task.compute(slicedArray);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        String stringArray = Arrays.toString(sortedSlicedArray);
        System.out.println(
            "Thread " + index +
            "\n algorithm: " + algorithm +
            "\n time:" + durationMs + "ms" +
            "\n sorted Array: " +
            stringArray
        );

        return;
    }

    public void startComputing() {
        int[] array = Helper.shuffleArray(Helper.generateArray(arrayLength));

        System.out.println(Arrays.toString(array));
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        
        for (int i = 0; i < threadNumber; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                public void run() {
                    sortAtIndex(index, array);
                }
            });
        }

        executorService.shutdown();

        while(!executorService.isTerminated()) {

        }
    }
}
