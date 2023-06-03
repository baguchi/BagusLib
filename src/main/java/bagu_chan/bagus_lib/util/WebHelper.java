package bagu_chan.bagus_lib.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/*
 * I borrow from this. Thanks Alex
 * https://github.com/AlexModGuy/Citadel/blob/master/src/main/java/com/github/alexthe666/citadel/web/WebHelper.java
 */
public class WebHelper {

    @Nullable
    public static BufferedReader getURLContents(@Nonnull String urlString, @Nonnull String backupFileLoc) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);

            return new BufferedReader(reader);
        } catch (Exception e) { // Malformed URL, Offline, etc.
            e.printStackTrace();
        }

        try { // Backup
            return new BufferedReader(new InputStreamReader(WebHelper.class.getClass().getClassLoader().getResourceAsStream(backupFileLoc), StandardCharsets.UTF_8));
        } catch (NullPointerException e) { // Can't parse backupFileLoc
            e.printStackTrace();
        }

        return null;
    }

}