package io.quarkiverse.backstage.cli.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Optional;

public class SourceTransformer {

    /**
     * Copies source files from the original project to the template skeleton, replacing package names with placeholders.
     *
     * @param sourceDir The source directory (src/main/java or src/test/java).
     * @param targetDir The target directory in the template skeleton.
     * @param parameters The map of parameters for replacement.
     * @param packagePrefix The common package prefix to replace.
     * @throws IOException If an I/O error occurs.
     */
    public static void copy(Path sourceDir, Path targetDir, Map<String, String> parameters, String packagePrefix) {
        if (!Files.exists(sourceDir)) {
            System.out.println("Source directory does not exist: " + sourceDir);
            return;
        }

        try {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // Compute the relative path from the source directory
                    Path relativePath = sourceDir.relativize(dir);
                    // Replace the package prefix in the directory structure
                    Optional<Path> newRelativePath = replacePackageInPath(relativePath, packagePrefix);
                    if (newRelativePath.isEmpty()) {
                        return FileVisitResult.CONTINUE;
                    }
                    Path targetPath = targetDir.resolve(newRelativePath.get());
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path relativePath = sourceDir.relativize(file);
                    Optional<Path> newRelativePath = replacePackageInPath(relativePath, packagePrefix);
                    if (newRelativePath.isEmpty()) {
                        return FileVisitResult.CONTINUE;
                    }
                    Path targetPath = targetDir.resolve(newRelativePath.get());

                    if (file.toString().endsWith(".java")) {
                        // Read file content
                        String content = Files.readString(file);
                        // Replace package prefix with placeholder
                        String modifiedContent = content.replace(packagePrefix, "${{ values.package }}");
                        // Write to target file
                        Files.writeString(targetPath, modifiedContent, StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING);
                    } else {
                        // Copy non-Java files as-is
                        Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy source files", e);
        }
    }

    /**
     * Replaces the package prefix in the given relative path with the placeholder.
     *
     * @param relativePath The relative path from the source directory.
     * @param packagePrefix The common package prefix to replace.
     * @return The modified relative path with placeholders.
     */
    private static Optional<Path> replacePackageInPath(Path relativePath, String packagePrefix) {
        // Convert package prefix to path
        String packagePrefixWithDots = packagePrefix.replace('.', FileSystems.getDefault().getSeparator().charAt(0));
        Path packagePath = Paths.get(packagePrefixWithDots);
        if (relativePath.startsWith(packagePath)) {
            Path remainingPath = packagePath.getNameCount() == relativePath.getNameCount() ? Paths.get("")
                    : relativePath.subpath(packagePath.getNameCount(), relativePath.getNameCount());
            return Optional.of(Paths.get("${{ values.package }}").resolve(remainingPath));
        }
        return Optional.empty();
    }
}
