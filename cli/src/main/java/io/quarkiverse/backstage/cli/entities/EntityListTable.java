package io.quarkiverse.backstage.cli.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.quarkiverse.backstage.v1alpha1.EntityList;

public class EntityListTable {

    private static final String API_VERSION = "API Version";
    private static final String KIND = "Kind";
    private static final String NAME = "Name";
    private static final String UUID = "UUID";

    private static final String NEWLINE = "\n";

    private String indent = "";
    private boolean showHeader = true;
    private List<EntityListItem> items;

    public EntityListTable(EntityList entityList) {
        this(entityList.getItems().stream().map(EntityListItem::from).collect(Collectors.toList()));
    }

    public EntityListTable(Collection<EntityListItem> items) {
        this(new ArrayList<>(items), "", true);
    }

    public EntityListTable(List<EntityListItem> items, String indent, boolean showHeader) {
        this.items = items;
        this.indent = indent;
        this.showHeader = showHeader;
    }

    public EntityListTable() {
    }

    private void setItems(List<EntityListItem> items) {
        this.items = items;
    }

    private void setIndent(String indent) {
        this.indent = indent;
    }

    private void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public String getContent() {
        String format = getFormat(items);
        StringBuilder sb = new StringBuilder();
        if (showHeader) {
            sb.append(indent);
            sb.append(getHeader(format, items));
            sb.append(NEWLINE);
        }
        sb.append(getBody(format, items, indent));
        return sb.toString();
    }

    // Utils
    private static String[] getLabels() {
        return new String[] { API_VERSION, KIND, NAME, UUID };
    }

    private static String getHeader(String format, Collection<EntityListItem> items) {
        return String.format(format, getLabels());
    }

    private static String getBody(String format, Collection<EntityListItem> items, String indent) {
        StringBuilder sb = new StringBuilder();
        for (EntityListItem item : items) {
            sb.append(indent);
            sb.append(String.format(format, item.getFields()));
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    private static String getFormat(Collection<EntityListItem> items) {
        StringBuilder sb = new StringBuilder();

        int maxApiVersionLength = Stream.concat(Stream.of(API_VERSION),
                items.stream().map(EntityListItem::getApiVersion))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxApiVersionLength + "s ");
        sb.append("\t");

        int maxKindVersionLength = Stream.concat(Stream.of(KIND),
                items.stream().map(EntityListItem::getApiVersion))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxKindVersionLength + "s ");
        sb.append("\t");

        int maxNameLength = Stream.concat(Stream.of(NAME),
                items.stream().map(EntityListItem::getName))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxNameLength + "s ");
        sb.append("\t");

        int maxIdLength = Stream.concat(Stream.of(UUID),
                items.stream().map(EntityListItem::getId))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxIdLength + "s ");

        sb.append("\t");
        return sb.toString();
    }
}
