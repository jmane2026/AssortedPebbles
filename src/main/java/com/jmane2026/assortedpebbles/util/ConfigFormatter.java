package com.jmane2026.assortedpebbles.util;

import com.jmane2026.assortedpebbles.AssortedPebbles;
import net.neoforged.fml.loading.FMLPaths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigFormatter {

    public static void formatConfigLines() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("assortedpebbles-common.toml");

        if (!Files.exists(configPath)) {
            return;
        }

        try {
            String content = Files.readString(configPath);

            int startIndex = content.indexOf("lootPools =");
            if (startIndex == -1 || content.contains("\"\"\"\n")) {
                return;
            }

            String header = content.substring(0, startIndex);
            String arrayData = content.substring(startIndex);

            arrayData = arrayData.replace("lootPools = [ [", "lootPools = [\n[");
            arrayData = arrayData.replace("lootPools = [[", "lootPools = [\n[");

            arrayData = arrayData.replace("\", \"", "\",\n       \"\"\"\\\n       ");

            arrayData = arrayData.replaceAll(",(?=[a-zA-Z0-9_#:])", ", \\\n       ");

            arrayData = arrayData.replace("\"], [\"", "\n       \"\"\"\n],\n[\"");
            arrayData = arrayData.replace("\"],[\"", "\n       \"\"\"\n],\n[\"");

            arrayData = arrayData.replace("\"] ] ]", "\n       \"\"\"\n]]");
            arrayData = arrayData.replace("\"]]]", "\n       \"\"\"\n]]");
            arrayData = arrayData.replace("\"] ]", "\n       \"\"\"\n]]");
            arrayData = arrayData.replace("\"]]", "\n       \"\"\"\n]]");

            Files.writeString(configPath, header + arrayData);

        } catch (IOException e) {
            AssortedPebbles.LOGGER.error("Failed to accurately format the isolated multiline config layout!", e);
        }
    }
}