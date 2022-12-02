public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words) {
        if (words == null || words.length == 0 || words[0].isEmpty()) {
            return "";
        }

        if (words.length == 1) {
            return words[0];
        }

        int minLen = Integer.MAX_VALUE;

        for (String currWord : words) {
            int currLen = currWord.length();
            minLen = Math.min(currLen, minLen);
        }

        int i = 0;
        for (; i < minLen; i++) {
            char currChar = words[0].charAt(i);

            for (String currWord : words) {
                if (currWord.charAt(i) != currChar) {
                    return words[0].substring(0, i);
                }
            }
        }

        return words[0].substring(0, minLen);
    }

    public static void main(String... args) {
        String[] strings1 = new String[]{"flower", "flow", "flight"};
        String[] strings2 = new String[]{""};
        String[] strings3 = new String[]{"cat"};
        String[] strings4 = new String[]{"protein", "protest", "protozoa"};

        System.out.println(PrefixExtractor.getLongestCommonPrefix(strings1)); // "fl"
        System.out.println(PrefixExtractor.getLongestCommonPrefix(strings2)); // ""
        System.out.println(PrefixExtractor.getLongestCommonPrefix(strings3)); // "cat"
        System.out.println(PrefixExtractor.getLongestCommonPrefix(strings4)); // "prot"

    }
}
