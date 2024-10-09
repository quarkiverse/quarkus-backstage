package io.quarkiverse.backstage.cli.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.quarkiverse.backstage.v1alpha1.EntityList;

public class TemplateListTable {

    private static final String API_VERSION = "API Version";
    private static final String KIND = "Kind";
    private static final String NAME = "Name";
    private static final String UUID = "UUID";

    private static final String NEWLINE = "\n";

    private String indent = "";
    private boolean showHeader = true;
    private List<TemplateListItem> items;

    public TemplateListTable(EntityList entityList) {
        this(entityList.getItems().stream().map(TemplateListItem::from).collect(Collectors.toList()));
    }

    public TemplateListTable(Collection<TemplateListItem> items) {
        this(new ArrayList<>(items), "", true);
    }

    public TemplateListTable(List<TemplateListItem> items, String indent, boolean showHeader) {
        this.items = items;
        this.indent = indent;
        this.showHeader = showHeader;
    }

    public TemplateListTable() {
    }

    private void setItems(List<TemplateListItem> items) {
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

    private static String getHeader(String format, Collection<TemplateListItem> items) {
        return String.format(format, getLabels());
    }

    private static String getBody(String format, Collection<TemplateListItem> items, String indent) {
        StringBuilder sb = new StringBuilder();
        for (TemplateListItem item : items) {
            sb.append(indent);
            sb.append(String.format(format, item.getFields()));
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    private static String getFormat(Collection<TemplateListItem> items) {
        StringBuilder sb = new StringBuilder();

        int maxApiVersionLength = Stream.concat(Stream.of(API_VERSION),
                items.stream().map(TemplateListItem::getApiVersion))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxApiVersionLength + "s ");
        sb.append("\t");

        int maxKindVersionLength = Stream.concat(Stream.of(KIND),
                items.stream().map(TemplateListItem::getApiVersion))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxKindVersionLength + "s ");
        sb.append("\t");

        int maxNameLength = Stream.concat(Stream.of(NAME),
                items.stream().map(TemplateListItem::getName))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxNameLength + "s ");
        sb.append("\t");

        int maxIdLength = Stream.concat(Stream.of(UUID),
                items.stream().map(TemplateListItem::getId))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxIdLength + "s ");

        sb.append("\t");
        return sb.toString();
    }
}
