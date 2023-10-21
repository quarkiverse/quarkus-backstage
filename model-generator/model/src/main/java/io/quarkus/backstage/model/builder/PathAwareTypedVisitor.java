package io.quarkus.backstage.model.builder;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map.Entry;

public class PathAwareTypedVisitor<V, P> extends TypedVisitor<V> {
    PathAwareTypedVisitor() {
        List<Class> args = Visitors.getTypeArguments(PathAwareTypedVisitor.class, getClass());
        if (args == null || args.isEmpty()) {
            throw new IllegalStateException("Could not determine type arguments for path aware typed visitor.");
        }
        this.type = (Class<V>) args.get(0);
        this.parentType = (Class<P>) args.get(1);
    }

    private final Class<V> type;
    private final Class<P> parentType;

    public void visit(V element) {

    }

    public void visit(List<Entry<String, Object>> path, V element) {
        visit(element);
    }

    public P getParent(List<Entry<String, Object>> path) {
        return path.size() - 1 >= 0 ? (P) path.get(path.size() - 1) : null;
    }

    public Class<P> getParentType() {
        return parentType;
    }

}
