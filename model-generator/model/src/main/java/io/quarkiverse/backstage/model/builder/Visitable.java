package io.quarkiverse.backstage.model.builder;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public interface Visitable<T> {

    default <V> T accept(Class<V> type, Visitor<V> visitor) {
        return accept(new Visitor<V>() {
            @Override
            public Class<V> getType() {
                return type;
            }

            @Override
            public void visit(V element) {
                visitor.visit(element);
            }
        });
    }

    default T accept(io.quarkiverse.backstage.model.builder.Visitor... visitors) {
        for (Visitor visitor : visitors) {
            if (visitor.canVisit(Collections.emptyList(), this)) {
                visitor.visit(this);
            }
        }
        return getTarget(this);
    }

    default T accept(List<Entry<String, Object>> path, io.quarkiverse.backstage.model.builder.Visitor... visitors) {
        return accept(path, "", visitors);
    }

    default T accept(List<Entry<String, Object>> path, String currentKey,
            io.quarkiverse.backstage.model.builder.Visitor... visitors) {
        for (Visitor visitor : visitors) {
            if (visitor.canVisit(path, this)) {
                visitor.visit(path, this);
            }
        }
        return getTarget(this);
    }

    default T getTarget(Visitable<T> visitable) {
        return (T) visitable;
    }

}
