package adammistal.sourcebrowser.utils;

import android.text.TextUtils;

public class Validation {

    private static final String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";

    public static boolean isUrl(String url) {
        return !TextUtils.isEmpty(url) && url.matches(urlPattern);
    }
}
