// https://www.hackerrank.com/challenges/sherlock-and-anagrams/problem

import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;

public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = Integer.parseInt(in.nextLine());

        while (t-- > 0) {
            int res = 0;

            HashMap<String, Integer> hashMap = new HashMap<>();

            String s = in.nextLine();
            final int LEN = s.length();

            for (int i = 0; i < LEN; i++) {
                for (int j = 1; j <= LEN - i; j++) {
                    String sub = s.substring(i, i + j);

                    char[] temp = sub.toCharArray();

                    Arrays.sort(temp);
                    String sToMap = new String(temp);

                    if (hashMap.containsKey(sToMap)) {
                        hashMap.put(sToMap, hashMap.get(sToMap) + 1);
                    } else {
                        hashMap.put(sToMap, 1);
                    }
                }
            }
            for (String key : hashMap.keySet()) {
                int count = hashMap.get(key);
                if (count > 1) {
                    res += (count * (count - 1)) / 2;
                }
            }
            System.out.println(res);
        }
    }
}
