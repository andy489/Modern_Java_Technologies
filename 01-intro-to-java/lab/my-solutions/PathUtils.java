public class PathUtils {
    public static String getCanonicalPath(String path) {
        StringBuilder canPath = new StringBuilder().append("/");
        String[] crackIntoPieces = path.split("/");

        for (String s : crackIntoPieces) {
            //  - handle consecutive "/"
            // - handle current dir "."
            if (s.isEmpty() || s.equals(".")) {
                continue;
            }
            // handle parent directory ".."

            if (s.equals("..")) {
                if (canPath.length() > 1) {
                    canPath.deleteCharAt(canPath.length() - 1);

                    int lastEraser = canPath.length();

                    while (canPath.charAt(lastEraser - 1) != ('/')) {
                        canPath.deleteCharAt(--lastEraser);
                    }
                } else if (canPath.length() != 1) {
                    canPath.append(s).append("/");
                }
            } else {
                canPath.append(s).append("/");
            }
        }

        if (canPath.length() > 1) {
            canPath.deleteCharAt(canPath.length() - 1);
        }

        return canPath.toString();
    }
}
