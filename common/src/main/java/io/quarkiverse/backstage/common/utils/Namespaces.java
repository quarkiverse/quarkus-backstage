package io.quarkiverse.backstage.common.utils;

public final class Namespaces {

    public static String addNamespace(String content, String namespace) {
        // Split content into lines for processing
        String[] lines = content.split("\\r?\\n");
        StringBuilder result = new StringBuilder();
        boolean inMetadataSection = false;
        boolean namespaceAdded = false;

        for (String line : lines) {
            if (line.startsWith("metadata:")) {
                inMetadataSection = true;
                result.append(line).append("\n");
                continue;
            }

            if (inMetadataSection) {
                // Check if namespace already exists
                if (line.matches("^\\s*namespace:")) {
                    // Replace the existing namespace
                    line = line.replaceAll("namespace:.*", "namespace: " + namespace);
                    result.append(line).append("\n");
                    namespaceAdded = true;
                    continue;
                }

                // Check for the end of the metadata section
                if (!line.startsWith(" ") && !line.isEmpty()) {
                    inMetadataSection = false;

                    // If namespace was not added, add it now
                    if (!namespaceAdded) {
                        result.append("  namespace: ").append(namespace).append("\n");
                        namespaceAdded = true;
                    }
                }
            }
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
