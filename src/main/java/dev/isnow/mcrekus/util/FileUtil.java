package dev.isnow.mcrekus.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Comparator;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

    public Optional<File> getOldestFile(final File filePath) {
        final Path path = filePath.toPath();

        Optional<File> file = Optional.empty();

        try {
            final long fileCount = Files.list(path).count();

            if (fileCount > 1) {
                Optional<Path> oldestFilePath = Files.list(path)
                        .filter(f -> !Files.isDirectory(f))
                        .min(Comparator.comparingLong(f -> f.toFile().lastModified()));

                if (oldestFilePath.isPresent()) {
                    file = Optional.of(oldestFilePath.get().toFile());
                }
            }
        } catch (IOException e) {
            RekusLogger.warn("Failed to get latest file from " + filePath.getAbsolutePath() + ". Error: " + e);
        }

        return file;
    }

    public String decompressB64GZIP(final File file) throws Exception {
        final String content = new String(Files.readAllBytes(file.toPath()));

        final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(
                Base64.getDecoder().decode(content)));

        final BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        final StringBuilder outStr = new StringBuilder();

        String line;
        while ((line=bf.readLine())!=null) {
            outStr.append(line);
        }

        return outStr.toString();
    }
}