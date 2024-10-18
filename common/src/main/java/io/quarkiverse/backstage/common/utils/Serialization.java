package io.quarkiverse.backstage.common.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.dekorate.utils.Streams;
import io.dekorate.utils.Strings;
import io.quarkiverse.backstage.v1alpha1.Entity;
import io.quarkiverse.backstage.v1alpha1.EntityList;
import io.quarkiverse.backstage.v1alpha1.EntityListBuilder;

public class Serialization {

    private static final String DOCUMENT_DELIMITER = "---";

    private static final String TAG_PATTERN = "!<.*>";
    private static final String BLANK = "";

    private static JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper(JSON_FACTORY).registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);

    private static YAMLFactory YAML_FACTORY = new YAMLFactory()
            .enable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper(YAML_FACTORY).registerModule(new Jdk8Module())
            .setSerializationInclusion(Include.NON_EMPTY);

    static {
        JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper jsonMapper() {
        return JSON_MAPPER;
    }

    public static ObjectMapper yamlMapper() {
        return YAML_MAPPER;
    }

    public static <T> String asJson(T object) {
        try {
            if (object instanceof EntityList) {
                EntityList list = (EntityList) object;
                return list.getItems().stream()
                        .map(Serialization::writeValueAsJsonSafe)
                        .collect(Collectors.joining(",", "[", "]"));
            }
            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String asYaml(T object) {
        try {
            if (object instanceof EntityList) {
                EntityList list = (EntityList) object;
                if (list.getItems().size() == 1) {
                    return YAML_MAPPER.writeValueAsString(list.getItems().get(0)).replaceAll(TAG_PATTERN, BLANK)
                            .replaceAll(DOCUMENT_DELIMITER, BLANK);
                }

                return list.getItems().stream()
                        .map(Serialization::writeValueAsYamlSafe)
                        .map(s -> s.replaceAll(TAG_PATTERN, BLANK))
                        .collect(Collectors.joining());
            }
            return YAML_MAPPER.writeValueAsString(object).replaceAll(TAG_PATTERN, BLANK).replaceAll(DOCUMENT_DELIMITER, BLANK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals a file into a list of maps.
     *
     * @param file The {@link Path}.
     * @return
     */
    public static List<Map<Object, Object>> unmarshalAsListOfMaps(Path file) throws IOException {
        return unmarshalAsListOfMaps(Files.readString(file));
    }

    /**
     * Unmarshals a file into a list of maps.
     *
     * @param content The YAML content.
     * @return
     */
    public static List<Map<Object, Object>> unmarshalAsListOfMaps(String content) throws IOException {
        String[] parts = Serialization.splitDocument(content);

        List<Map<Object, Object>> list = new ArrayList<>();
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                continue;
            }

            list.add(Serialization.yamlMapper().readValue(part, new TypeReference<Map<Object, Object>>() {
            }));
        }

        return list;
    }

    /**
     * Unmarshals a stream.
     *
     * @param is The {@link InputStream}.
     * @return
     */
    public static EntityList unmarshalAsList(InputStream is) {
        String content = Strings.read(is);
        String[] parts = splitDocument(content);
        List<Entity> items = new ArrayList<>();
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                continue;
            }
            Object resource = unmarshal(part);
            if (resource instanceof EntityList) {
                items.addAll(((EntityList) resource).getItems());
            } else if (resource instanceof Entity) {
                items.add((Entity) resource);
            } else if (resource instanceof Entity[]) {
                Arrays.stream((Entity[]) resource).forEach(r -> items.add(r));
            }
        }
        return new EntityListBuilder().withItems(items).build();
    }

    /**
     * Unmarshals a stream.
     *
     * @param is The {@link InputStream}.
     * @param <T> The target type.
     * @return
     */
    public static <T> T unmarshal(InputStream is) {
        return unmarshal(is, JSON_MAPPER);
    }

    /**
     * Unmarshals a stream.
     *
     * @param is The {@link InputStream}.
     * @param mapper The {@link ObjectMapper} to use.
     * @param <T> The target type.
     * @return
     */
    public static <T> T unmarshal(InputStream is, ObjectMapper mapper) {
        return unmarshal(is, mapper, Collections.emptyMap());
    }

    /**
     * Unmarshals a stream optionally performing placeholder substitution to the stream.
     *
     * @param is The {@link InputStream}.
     * @param mapper The {@link ObjectMapper} to use.
     * @param parameters A {@link Map} with parameters for placeholder substitution.
     * @param <T> The target type.
     * @return
     */
    public static <T> T unmarshal(InputStream is, ObjectMapper mapper, Map<String, String> parameters) {
        try (FileInputStream fis = Streams.crateTempFileInputStream(is);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            long readLimit = fis.getChannel().size();
            try {
                bis.mark(Math.toIntExact(readLimit));
            } catch (ArithmeticException e) {
                bis.mark(Integer.MAX_VALUE);
            }
            int intch;
            do {
                intch = bis.read();
            } while (intch > -1 && Character.isWhitespace(intch));
            bis.reset();

            if (intch != '{') {
                mapper = YAML_MAPPER;
            }
            return mapper.readerFor(Entity.class).readValue(bis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals a {@link String} optionally performing placeholder substitution to the String.
     *
     * @param str The {@link String}.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(String str) {
        try (InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
            return unmarshal(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals a {@link String} optionally performing placeholder substitution to the String.
     *
     * @param str The {@link String}.
     * @param type The target type.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(String str, final Class<T> type) {
        try (InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
            return unmarshal(is, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals an {@link String} optionally performing placeholder substitution to the stream.
     *
     * @param str The {@link String}.
     * @param type The {@link TypeReference}.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(String str, TypeReference<T> type) {
        try (InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
            return unmarshal(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals an {@link InputStream} optionally performing placeholder substitution to the stream.
     *
     * @param is The {@link InputStream}.
     * @param type The type.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(InputStream is, final Class<T> type) {
        return unmarshal(is, new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }

    /**
     * Unmarshals an {@link File} optionally performing placeholder substitution to the stream.
     *
     * @param f The {@link File}.
     * @param type The {@link TypeReference}.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(File f, TypeReference<T> type) {
        try (InputStream is = new FileInputStream(f)) {
            return unmarshal(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals a {@link File} optionally performing placeholder substitution to the stream.
     *
     * @param f The {@link File}.
     * @param type The type.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(File f, final Class<T> type) {
        try (FileInputStream is = new FileInputStream(f)) {
            return unmarshal(is, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals a {@link URL} optionally performing placeholder substitution to the stream.
     *
     * @param u The {@link URL}.
     * @param type The type.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(URL u, final Class<T> type) {
        try (InputStream is = u.openStream()) {
            return unmarshal(is, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshals an {@link InputStream} optionally performing placeholder substitution to the stream.
     *
     * @param is The {@link InputStream}.
     * @param type The {@link TypeReference}.
     * @param <T>
     * @return
     */
    public static <T> T unmarshal(InputStream is, TypeReference<T> type) {
        try (BufferedInputStream bis = new BufferedInputStream(is)) {
            bis.mark(-1);
            int intch;
            do {
                intch = bis.read();
            } while (intch > -1 && Character.isWhitespace(intch));
            bis.reset();

            ObjectMapper mapper = JSON_MAPPER;
            if (intch != '{') {
                mapper = YAML_MAPPER;
            }
            return mapper.readValue(bis, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] splitDocument(String aSpecFile) {
        List<String> documents = new ArrayList<>();
        String[] lines = aSpecFile.split("\\r?\\n");
        int nLine = 0;
        StringBuilder builder = new StringBuilder();

        while (nLine < lines.length) {
            if ((lines[nLine].length() >= DOCUMENT_DELIMITER.length()
                    && !lines[nLine].substring(0, DOCUMENT_DELIMITER.length()).equals(DOCUMENT_DELIMITER))
                    || (lines[nLine].length() < DOCUMENT_DELIMITER.length())) {
                builder.append(lines[nLine] + System.lineSeparator());
            } else {
                documents.add(builder.toString());
                builder.setLength(0);
                //To have meaningful line numbers, in jackson error messages, we need each resource
                //to retain its original position in the document.
                for (int i = 0; i <= nLine; i++) {
                    builder.append(System.lineSeparator());
                }
            }
            nLine++;
        }

        if (!builder.toString().isEmpty())
            documents.add(builder.toString());
        return documents.toArray(new String[documents.size()]);
    }

    private static <T> String writeValueAsYamlSafe(T object) {
        try {
            return YAML_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> String writeValueAsJsonSafe(T object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
