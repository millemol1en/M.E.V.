package Info;

public class KMP {

    public int[] failure;

    public boolean initKMP (String text, String pat) {
        this.failure = new int[pat.length()];
        fail(pat);
        int pos = calculateMatch(text, pat);
        if (pos == -1) {
            //System.out.println("no match: " + text);
            return false;
        } else {
            //System.out.println("MATCH: " + text);
            return true;
        }
    }

    public void fail(String pat) {
        int n = pat.length();
        this.failure[0] = -1;

        //...
        for (int j = 1; j < n ; j++) {
            int i = failure[j - 1];
            while((pat.charAt(j) != pat.charAt(i + 1) && i >= 0)) {
                i = failure[i];
            }
            if (pat.charAt(j) == pat.charAt(i+1)) {
                failure[j] = i + 1;
            } else {
                failure[j] = -1;
            }


        }
    }

    public int calculateMatch(String text, String pat) {
        int i = 0, j = 0; //ints start at 0.
        int textLength = text.length();
        int patLength = pat.length();

        while (i < textLength && j < patLength) {
            if (text.charAt(i) == (pat.charAt(j))) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = failure[j - 1] + 1;
            }
        }
        return ((j == patLength) ? (i - patLength) : -1);
    }
}
