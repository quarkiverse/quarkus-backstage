package io.quarkiverse.backstage.scaffolder.v1beta3;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import io.quarkiverse.backstage.EntityLink;
import io.quarkiverse.backstage.EntityLinkBuilder;
import io.quarkiverse.backstage.EntityLinkFluent;
import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class OutputFluent<A extends OutputFluent<A>> extends BaseFluent<A> {
    public OutputFluent() {
    }

    public OutputFluent(Output instance) {
        this.copyInstance(instance);
    }

    private ArrayList<EntityLinkBuilder> links = new ArrayList<EntityLinkBuilder>();

    protected void copyInstance(Output instance) {
        instance = (instance != null ? instance : new Output());

        if (instance != null) {
            this.withLinks(instance.getLinks());
            this.withLinks(instance.getLinks());
        }
    }

    public A addToLinks(int index, EntityLink item) {
        if (this.links == null) {
            this.links = new ArrayList<EntityLinkBuilder>();
        }
        EntityLinkBuilder builder = new EntityLinkBuilder(item);
        if (index < 0 || index >= links.size()) {
            _visitables.get("links").add(builder);
            links.add(builder);
        } else {
            _visitables.get("links").add(index, builder);
            links.add(index, builder);
        }
        return (A) this;
    }

    public A setToLinks(int index, EntityLink item) {
        if (this.links == null) {
            this.links = new ArrayList<EntityLinkBuilder>();
        }
        EntityLinkBuilder builder = new EntityLinkBuilder(item);
        if (index < 0 || index >= links.size()) {
            _visitables.get("links").add(builder);
            links.add(builder);
        } else {
            _visitables.get("links").set(index, builder);
            links.set(index, builder);
        }
        return (A) this;
    }

    public A addToLinks(io.quarkiverse.backstage.EntityLink... items) {
        if (this.links == null) {
            this.links = new ArrayList<EntityLinkBuilder>();
        }
        for (EntityLink item : items) {
            EntityLinkBuilder builder = new EntityLinkBuilder(item);
            _visitables.get("links").add(builder);
            this.links.add(builder);
        }
        return (A) this;
    }

    public A addAllToLinks(Collection<EntityLink> items) {
        if (this.links == null) {
            this.links = new ArrayList<EntityLinkBuilder>();
        }
        for (EntityLink item : items) {
            EntityLinkBuilder builder = new EntityLinkBuilder(item);
            _visitables.get("links").add(builder);
            this.links.add(builder);
        }
        return (A) this;
    }

    public A removeFromLinks(io.quarkiverse.backstage.EntityLink... items) {
        if (this.links == null)
            return (A) this;
        for (EntityLink item : items) {
            EntityLinkBuilder builder = new EntityLinkBuilder(item);
            _visitables.get("links").remove(builder);
            this.links.remove(builder);
        }
        return (A) this;
    }

    public A removeAllFromLinks(Collection<EntityLink> items) {
        if (this.links == null)
            return (A) this;
        for (EntityLink item : items) {
            EntityLinkBuilder builder = new EntityLinkBuilder(item);
            _visitables.get("links").remove(builder);
            this.links.remove(builder);
        }
        return (A) this;
    }

    public A removeMatchingFromLinks(Predicate<EntityLinkBuilder> predicate) {
        if (links == null)
            return (A) this;
        final Iterator<EntityLinkBuilder> each = links.iterator();
        final List visitables = _visitables.get("links");
        while (each.hasNext()) {
            EntityLinkBuilder builder = each.next();
            if (predicate.test(builder)) {
                visitables.remove(builder);
                each.remove();
            }
        }
        return (A) this;
    }

    public List<EntityLink> buildLinks() {
        return links != null ? build(links) : null;
    }

    public EntityLink buildLink(int index) {
        return this.links.get(index).build();
    }

    public EntityLink buildFirstLink() {
        return this.links.get(0).build();
    }

    public EntityLink buildLastLink() {
        return this.links.get(links.size() - 1).build();
    }

    public EntityLink buildMatchingLink(Predicate<EntityLinkBuilder> predicate) {
        for (EntityLinkBuilder item : links) {
            if (predicate.test(item)) {
                return item.build();
            }
        }
        return null;
    }

    public boolean hasMatchingLink(Predicate<EntityLinkBuilder> predicate) {
        for (EntityLinkBuilder item : links) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withLinks(List<EntityLink> links) {
        if (this.links != null) {
            _visitables.get("links").clear();
        }
        if (links != null) {
            this.links = new ArrayList();
            for (EntityLink item : links) {
                this.addToLinks(item);
            }
        } else {
            this.links = null;
        }
        return (A) this;
    }

    public A withLinks(io.quarkiverse.backstage.EntityLink... links) {
        if (this.links != null) {
            this.links.clear();
            _visitables.remove("links");
        }
        if (links != null) {
            for (EntityLink item : links) {
                this.addToLinks(item);
            }
        }
        return (A) this;
    }

    public boolean hasLinks() {
        return links != null && !links.isEmpty();
    }

    public LinksNested<A> addNewLink() {
        return new LinksNested(-1, null);
    }

    public LinksNested<A> addNewLinkLike(EntityLink item) {
        return new LinksNested(-1, item);
    }

    public LinksNested<A> setNewLinkLike(int index, EntityLink item) {
        return new LinksNested(index, item);
    }

    public LinksNested<A> editLink(int index) {
        if (links.size() <= index)
            throw new RuntimeException("Can't edit links. Index exceeds size.");
        return setNewLinkLike(index, buildLink(index));
    }

    public LinksNested<A> editFirstLink() {
        if (links.size() == 0)
            throw new RuntimeException("Can't edit first links. The list is empty.");
        return setNewLinkLike(0, buildLink(0));
    }

    public LinksNested<A> editLastLink() {
        int index = links.size() - 1;
        if (index < 0)
            throw new RuntimeException("Can't edit last links. The list is empty.");
        return setNewLinkLike(index, buildLink(index));
    }

    public LinksNested<A> editMatchingLink(Predicate<EntityLinkBuilder> predicate) {
        int index = -1;
        for (int i = 0; i < links.size(); i++) {
            if (predicate.test(links.get(i))) {
                index = i;
                break;
            }
        }
        if (index < 0)
            throw new RuntimeException("Can't edit matching links. No match found.");
        return setNewLinkLike(index, buildLink(index));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        OutputFluent that = (OutputFluent) o;
        if (!java.util.Objects.equals(links, that.links))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(links, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (links != null && !links.isEmpty()) {
            sb.append("links:");
            sb.append(links);
        }
        sb.append("}");
        return sb.toString();
    }

    public class LinksNested<N> extends EntityLinkFluent<LinksNested<N>> implements Nested<N> {
        LinksNested(int index, EntityLink item) {
            this.index = index;
            this.builder = new EntityLinkBuilder(this, item);
        }

        EntityLinkBuilder builder;
        int index;

        public N and() {
            return (N) OutputFluent.this.setToLinks(index, builder.build());
        }

        public N endLink() {
            return and();
        }

    }

}
