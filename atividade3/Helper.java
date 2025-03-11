public class Helper {
    public static int[] shuffleArray(int[] array) {
        int currentIndex = array.length;
      
        // While there remain elements to shuffle...
        while (currentIndex != 0) {
            int randomIndex = ((int) Math.floor(Math.random() * currentIndex));
            currentIndex--;
      
            // And swap it with the current element.
            int bucketRandomIndex = array[randomIndex];
            array[randomIndex] = array[currentIndex];
            array[currentIndex] = bucketRandomIndex;
        }

        return array;
    }

    public static int[] generateArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        return array;
    }
}
