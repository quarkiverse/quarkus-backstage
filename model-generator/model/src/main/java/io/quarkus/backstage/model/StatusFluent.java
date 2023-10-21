package io.quarkus.backstage.model;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import io.quarkus.backstage.model.builder.BaseFluent;
import io.quarkus.backstage.model.builder.Nested;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class StatusFluent<A extends StatusFluent<A>> extends BaseFluent<A> {
    public StatusFluent() {
    }

    public StatusFluent(Status instance) {
        this.copyInstance(instance);
    }

    private ArrayList<StatusItemBuilder> items = new ArrayList<StatusItemBuilder>();

    protected void copyInstance(Status instance) {
        if (instance != null) {
            this.withItems(instance.getItems());
        }
    }

    public A addToItems(int index, StatusItem item) {
        if (this.items == null) {
            this.items = new ArrayList<StatusItemBuilder>();
        }
        StatusItemBuilder builder = new StatusItemBuilder(item);
        if (index < 0 || index >= items.size()) {
            _visitables.get("items").add(builder);
            items.add(builder);
        } else {
            _visitables.get("items").add(index, builder);
            items.add(index, builder);
        }
        return (A) this;
    }

    public A setToItems(int index, StatusItem item) {
        if (this.items == null) {
            this.items = new ArrayList<StatusItemBuilder>();
        }
        StatusItemBuilder builder = new StatusItemBuilder(item);
        if (index < 0 || index >= items.size()) {
            _visitables.get("items").add(builder);
            items.add(builder);
        } else {
            _visitables.get("items").set(index, builder);
            items.set(index, builder);
        }
        return (A) this;
    }

    public A addToItems(io.quarkus.backstage.model.StatusItem... items) {
        if (this.items == null) {
            this.items = new ArrayList<StatusItemBuilder>();
        }
        for (StatusItem item : items) {
            StatusItemBuilder builder = new StatusItemBuilder(item);
            _visitables.get("items").add(builder);
            this.items.add(builder);
        }
        return (A) this;
    }

    public A addAllToItems(Collection<StatusItem> items) {
        if (this.items == null) {
            this.items = new ArrayList<StatusItemBuilder>();
        }
        for (StatusItem item : items) {
            StatusItemBuilder builder = new StatusItemBuilder(item);
            _visitables.get("items").add(builder);
            this.items.add(builder);
        }
        return (A) this;
    }

    public A removeFromItems(io.quarkus.backstage.model.StatusItem... items) {
        if (this.items == null)
            return (A) this;
        for (StatusItem item : items) {
            StatusItemBuilder builder = new StatusItemBuilder(item);
            _visitables.get("items").remove(builder);
            this.items.remove(builder);
        }
        return (A) this;
    }

    public A removeAllFromItems(Collection<StatusItem> items) {
        if (this.items == null)
            return (A) this;
        for (StatusItem item : items) {
            StatusItemBuilder builder = new StatusItemBuilder(item);
            _visitables.get("items").remove(builder);
            this.items.remove(builder);
        }
        return (A) this;
    }

    public A removeMatchingFromItems(Predicate<StatusItemBuilder> predicate) {
        if (items == null)
            return (A) this;
        final Iterator<StatusItemBuilder> each = items.iterator();
        final List visitables = _visitables.get("items");
        while (each.hasNext()) {
            StatusItemBuilder builder = each.next();
            if (predicate.test(builder)) {
                visitables.remove(builder);
                each.remove();
            }
        }
        return (A) this;
    }

    public List<StatusItem> buildItems() {
        return items != null ? build(items) : null;
    }

    public StatusItem buildItem(int index) {
        return this.items.get(index).build();
    }

    public StatusItem buildFirstItem() {
        return this.items.get(0).build();
    }

    public StatusItem buildLastItem() {
        return this.items.get(items.size() - 1).build();
    }

    public StatusItem buildMatchingItem(Predicate<StatusItemBuilder> predicate) {
        for (StatusItemBuilder item : items) {
            if (predicate.test(item)) {
                return item.build();
            }
        }
        return null;
    }

    public boolean hasMatchingItem(Predicate<StatusItemBuilder> predicate) {
        for (StatusItemBuilder item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withItems(List<StatusItem> items) {
        if (this.items != null) {
            _visitables.get("items").clear();
        }
        if (items != null) {
            this.items = new ArrayList();
            for (StatusItem item : items) {
                this.addToItems(item);
            }
        } else {
            this.items = null;
        }
        return (A) this;
    }

    public A withItems(io.quarkus.backstage.model.StatusItem... items) {
        if (this.items != null) {
            this.items.clear();
            _visitables.remove("items");
        }
        if (items != null) {
            for (StatusItem item : items) {
                this.addToItems(item);
            }
        }
        return (A) this;
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    public ItemsNested<A> addNewItem() {
        return new ItemsNested(-1, null);
    }

    public ItemsNested<A> addNewItemLike(StatusItem item) {
        return new ItemsNested(-1, item);
    }

    public ItemsNested<A> setNewItemLike(int index, StatusItem item) {
        return new ItemsNested(index, item);
    }

    public ItemsNested<A> editItem(int index) {
        if (items.size() <= index)
            throw new RuntimeException("Can't edit items. Index exceeds size.");
        return setNewItemLike(index, buildItem(index));
    }

    public ItemsNested<A> editFirstItem() {
        if (items.size() == 0)
            throw new RuntimeException("Can't edit first items. The list is empty.");
        return setNewItemLike(0, buildItem(0));
    }

    public ItemsNested<A> editLastItem() {
        int index = items.size() - 1;
        if (index < 0)
            throw new RuntimeException("Can't edit last items. The list is empty.");
        return setNewItemLike(index, buildItem(index));
    }

    public ItemsNested<A> editMatchingItem(Predicate<StatusItemBuilder> predicate) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (predicate.test(items.get(i))) {
                index = i;
                break;
            }
        }
        if (index < 0)
            throw new RuntimeException("Can't edit matching items. No match found.");
        return setNewItemLike(index, buildItem(index));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        StatusFluent that = (StatusFluent) o;
        if (!java.util.Objects.equals(items, that.items))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(items, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (items != null && !items.isEmpty()) {
            sb.append("items:");
            sb.append(items);
        }
        sb.append("}");
        return sb.toString();
    }

    public class ItemsNested<N> extends StatusItemFluent<ItemsNested<N>> implements Nested<N> {
        ItemsNested(int index, StatusItem item) {
            this.index = index;
            this.builder = new StatusItemBuilder(this, item);
        }

        StatusItemBuilder builder;
        int index;

        public N and() {
            return (N) StatusFluent.this.setToItems(index, builder.build());
        }

        public N endItem() {
            return and();
        }

    }

}
