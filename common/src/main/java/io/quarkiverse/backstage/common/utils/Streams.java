package io.quarkiverse.backstage.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Streams {

    private static final String QUARKUS_BACKSTAGE = "quarkus-backstage";
    private static final String _TMP = ".tmp";

    private Streams() {
        //Utility
    }

    public static File createTemporaryFile(InputStream is) {
        try {
            Path tmp = Files.createTempFile(QUARKUS_BACKSTAGE, _TMP);
            File f = tmp.toFile();
            try (BufferedInputStream bis = new BufferedInputStream(is); FileOutputStream fos = new FileOutputStream(f)) {
                byte[] buffer = new byte[8 * 1024];
                int size = 0;
                while ((size = bis.read(buffer)) > 0) {
                    fos.write(buffer, 0, size);
                }
            }
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileInputStream crateTempFileInputStream(InputStream is) {
        try {
            final File tmpFile = createTemporaryFile(is);
            return new FileInputStream(tmpFile) {
                @Override
                public void close() throws IOException {
                    super.close();
                    tmpFile.delete();
                }
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
