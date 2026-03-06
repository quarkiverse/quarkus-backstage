package io.quarkiverse.backstage.cli.locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.quarkiverse.backstage.v1alpha1.Location;

public class LocationListTable {

    private static final String API_VERSION = "API Version";
    private static final String KIND = "Kind";
    private static final String NAME = "Name";
    private static final String TARGETS = "Targets";
    private static final String UUID = "UUID";

    private static final String NEWLINE = "\n";

    private String indent = "";
    private boolean showHeader = true;
    private List<LocationListItem> items;

    public LocationListTable(List<Location> locations) {
        this(locations.stream().map(LocationListItem::from).collect(Collectors.toList()));
    }

    public LocationListTable(Collection<LocationListItem> items) {
        this(new ArrayList<>(items), "", true);
    }

    public LocationListTable(List<LocationListItem> items, String indent, boolean showHeader) {
        this.items = items;
        this.indent = indent;
        this.showHeader = showHeader;
    }

    public LocationListTable() {
    }

    private void setItems(List<LocationListItem> items) {
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
        return new String[] { UUID, NAME, TARGETS };
    }

    private static String getHeader(String format, Collection<LocationListItem> items) {
        return String.format(format, getLabels());
    }

    private static String getBody(String format, Collection<LocationListItem> items, String indent) {
        StringBuilder sb = new StringBuilder();
        for (LocationListItem item : items) {
            sb.append(indent);
            sb.append(String.format(format, item.getFields()));
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    private static String getFormat(Collection<LocationListItem> items) {
        StringBuilder sb = new StringBuilder();

        /*
         * int maxApiVersionLength = Stream.concat(Stream.of(API_VERSION),
         * items.stream().map(LocationListItem::getApiVersion))
         * .filter(Objects::nonNull)
         * .map(String::length)
         * .max(Comparator.naturalOrder())
         * .orElse(0);
         * sb.append(" %-" + maxApiVersionLength + "s ");
         * sb.append("\t");
         *
         * int maxKindVersionLength = Stream.concat(Stream.of(KIND),
         * items.stream().map(LocationListItem::getApiVersion))
         * .filter(Objects::nonNull)
         * .map(String::length)
         * .max(Comparator.naturalOrder())
         * .orElse(0);
         * sb.append(" %-" + maxKindVersionLength + "s ");
         * sb.append("\t");
         */

        int maxIdLength = Stream.concat(Stream.of(UUID),
                items.stream().map(LocationListItem::getId))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxIdLength + "s ");

        int maxNameLength = Stream.concat(Stream.of(NAME),
                items.stream().map(LocationListItem::getName))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxNameLength + "s ");
        sb.append("\t");

        int maxTargetsLength = Stream.concat(Stream.of(TARGETS),
                items.stream().map(LocationListItem::getTargets))
                .filter(Objects::nonNull)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        sb.append(" %-" + maxTargetsLength + "s ");
        sb.append("\t");

        sb.append("\t");
        return sb.toString();
    }
}
