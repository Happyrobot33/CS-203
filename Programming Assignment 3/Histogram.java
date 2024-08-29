public class Histogram {
    private int[] histogram;
    private int totalChars;

    public Histogram(String line) {
        histogram = new int[26];
        totalChars = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c >= 'a' && c <= 'z') {
                histogram[c - 'a']++;
                totalChars++;
            }
        }
    }

    public int getFrequency(char c) {
        return histogram[c - 'a'];
    }

    public int getLength() {
        return totalChars;
    }

    public int[] getHistogram() {
        return histogram;
    }

    //to string
    public String toString() {
        String result = "";
        for (int i = 0; i < histogram.length; i++) {
            result += (char) (i + 'a') + ": " + histogram[i] + "\n";
        }
        return result;
    }
}
