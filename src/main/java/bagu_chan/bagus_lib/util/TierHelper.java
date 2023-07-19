package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.BagusLib;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TierHelper {
    private static final String SUPPORTER_URL = "https://raw.githubusercontent.com/baguchan/BagusLib/1.20/src/main/resources/assets/bagus_lib/patreon.txt";
    private static final String SUPPORTER_PATH = "assets/bagus_lib/patreon.txt";
    public static final List<User> SUPPORTER = Lists.newArrayList();

    @Nullable
    public static BufferedReader getSuporterContents() {
        BagusLib.LOGGER.info("Load Bagus Lib Supporter");
        try {
            URL url = new URL(SUPPORTER_URL);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);

            return new BufferedReader(reader);
        } catch (Exception e) { // Malformed URL, Offline, etc.
            e.printStackTrace();
        }

        try { // Backup
            BagusLib.LOGGER.info("Load Bagus Lib Supporter failed. trying Load Backup file");
            return new BufferedReader(new InputStreamReader(TierHelper.class.getClass().getClassLoader().getResourceAsStream(SUPPORTER_PATH), StandardCharsets.UTF_8));
        } catch (NullPointerException e) { // Can't parse backupFileLoc
            e.printStackTrace();
        }

        return null;
    }

    public static void addSuporterContents() {
        BufferedReader urlContents = TierHelper.getSuporterContents();
        if (urlContents != null) {
            List<String> lines = new ArrayList<>();
            try {
                String line;
                while ((line = urlContents.readLine()) != null) {
                    lines.add(line);
                }

            } catch (IOException e) {
                BagusLib.LOGGER.warn("Failed to load perks");
            }
            lines.stream().filter(s -> !(s.isEmpty() || s.contains("#"))).forEach(s -> {
                String[] values = s.split(",");
                SUPPORTER.add(new User(User.getTier(values[0]), UUID.fromString(values[1])));

            });

        } else {
            BagusLib.LOGGER.warn("Failed to load perks");
        }
    }

    @Nullable
    public static User get(Player player) {
        Optional<User> optional = SUPPORTER.stream().filter(user -> {
            return user.getUuid().toString().contains(player.getUUID().toString().toLowerCase(Locale.ROOT));
        }).findFirst();
        return optional.orElse(null);
    }

    public static User.Tier getTier(Player player) {
        User user = get(player);
        return user != null ? user.getTier() : User.Tier.UNKNOWN;
    }

}
