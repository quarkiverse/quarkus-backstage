package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class EntityMetaFluent<A extends EntityMetaFluent<A>> extends BaseFluent<A> {
    public EntityMetaFluent() {
    }

    public EntityMetaFluent(EntityMeta instance) {
        this.copyInstance(instance);
    }

    private Optional<String> uid = Optional.empty();
    private Optional<String> etag = Optional.empty();
    private String name;
    private Optional<String> namespace = Optional.empty();
    private Optional<String> title = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Map<String, String> labels = new LinkedHashMap<String, String>();
    private Map<String, String> annotations = new LinkedHashMap<String, String>();
    private List<String> tags = new ArrayList<String>();
    private ArrayList<EntityLinkBuilder> links = new ArrayList<EntityLinkBuilder>();

    protected void copyInstance(EntityMeta instance) {
        instance = (instance != null ? instance : new EntityMeta());

        if (instance != null) {
            this.withUid(instance.getUid());
            this.withEtag(instance.getEtag());
            this.withName(instance.getName());
            this.withNamespace(instance.getNamespace());
            this.withTitle(instance.getTitle());
            this.withDescription(instance.getDescription());
            this.withLabels(instance.getLabels());
            this.withAnnotations(instance.getAnnotations());
            this.withTags(instance.getTags());
            this.withLinks(instance.getLinks());
            this.withUid(instance.getUid());
            this.withEtag(instance.getEtag());
            this.withName(instance.getName());
            this.withNamespace(instance.getNamespace());
            this.withTitle(instance.getTitle());
            this.withDescription(instance.getDescription());
            this.withLabels(instance.getLabels());
            this.withAnnotations(instance.getAnnotations());
            this.withTags(instance.getTags());
            this.withLinks(instance.getLinks());
        }
    }

    public A withUid(Optional<String> uid) {
        if (uid == null || !uid.isPresent()) {
            this.uid = Optional.empty();
        } else {
            this.uid = uid;
        }
        return (A) this;
    }

    public A withUid(String uid) {
        if (uid == null) {
            this.uid = Optional.empty();
        } else {
            this.uid = Optional.of(uid);
        }
        return (A) this;
    }

    public Optional<String> getUid() {
        return this.uid;
    }

    public boolean hasUid() {
        return uid != null && uid.isPresent();
    }

    public A withEtag(Optional<String> etag) {
        if (etag == null || !etag.isPresent()) {
            this.etag = Optional.empty();
        } else {
            this.etag = etag;
        }
        return (A) this;
    }

    public A withEtag(String etag) {
        if (etag == null) {
            this.etag = Optional.empty();
        } else {
            this.etag = Optional.of(etag);
        }
        return (A) this;
    }

    public Optional<String> getEtag() {
        return this.etag;
    }

    public boolean hasEtag() {
        return etag != null && etag.isPresent();
    }

    public String getName() {
        return this.name;
    }

    public A withName(String name) {
        this.name = name;
        return (A) this;
    }

    public boolean hasName() {
        return this.name != null;
    }

    public A withNamespace(Optional<String> namespace) {
        if (namespace == null || !namespace.isPresent()) {
            this.namespace = Optional.empty();
        } else {
            this.namespace = namespace;
        }
        return (A) this;
    }

    public A withNamespace(String namespace) {
        if (namespace == null) {
            this.namespace = Optional.empty();
        } else {
            this.namespace = Optional.of(namespace);
        }
        return (A) this;
    }

    public Optional<String> getNamespace() {
        return this.namespace;
    }

    public boolean hasNamespace() {
        return namespace != null && namespace.isPresent();
    }

    public A withTitle(Optional<String> title) {
        if (title == null || !title.isPresent()) {
            this.title = Optional.empty();
        } else {
            this.title = title;
        }
        return (A) this;
    }

    public A withTitle(String title) {
        if (title == null) {
            this.title = Optional.empty();
        } else {
            this.title = Optional.of(title);
        }
        return (A) this;
    }

    public Optional<String> getTitle() {
        return this.title;
    }

    public boolean hasTitle() {
        return title != null && title.isPresent();
    }

    public A withDescription(Optional<String> description) {
        if (description == null || !description.isPresent()) {
            this.description = Optional.empty();
        } else {
            this.description = description;
        }
        return (A) this;
    }

    public A withDescription(String description) {
        if (description == null) {
            this.description = Optional.empty();
        } else {
            this.description = Optional.of(description);
        }
        return (A) this;
    }

    public Optional<String> getDescription() {
        return this.description;
    }

    public boolean hasDescription() {
        return description != null && description.isPresent();
    }

    public A addToLabels(String key, String value) {
        if (this.labels == null && key != null && value != null) {
            this.labels = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.labels.put(key, value);
        }
        return (A) this;
    }

    public A addToLabels(Map<String, String> map) {
        if (this.labels == null && map != null) {
            this.labels = new LinkedHashMap();
        }
        if (map != null) {
            this.labels.putAll(map);
        }
        return (A) this;
    }

    public A removeFromLabels(String key) {
        if (this.labels == null) {
            return (A) this;
        }
        if (key != null && this.labels != null) {
            this.labels.remove(key);
        }
        return (A) this;
    }

    public A removeFromLabels(Map<String, String> map) {
        if (this.labels == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.labels != null) {
                    this.labels.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, String> getLabels() {
        return this.labels;
    }

    public <K, V> A withLabels(Map<String, String> labels) {
        if (labels == null) {
            this.labels = null;
        } else {
            this.labels = new LinkedHashMap(labels);
        }
        return (A) this;
    }

    public boolean hasLabels() {
        return this.labels != null;
    }

    public A addToAnnotations(String key, String value) {
        if (this.annotations == null && key != null && value != null) {
            this.annotations = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.annotations.put(key, value);
        }
        return (A) this;
    }

    public A addToAnnotations(Map<String, String> map) {
        if (this.annotations == null && map != null) {
            this.annotations = new LinkedHashMap();
        }
        if (map != null) {
            this.annotations.putAll(map);
        }
        return (A) this;
    }

    public A removeFromAnnotations(String key) {
        if (this.annotations == null) {
            return (A) this;
        }
        if (key != null && this.annotations != null) {
            this.annotations.remove(key);
        }
        return (A) this;
    }

    public A removeFromAnnotations(Map<String, String> map) {
        if (this.annotations == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.annotations != null) {
                    this.annotations.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, String> getAnnotations() {
        return this.annotations;
    }

    public <K, V> A withAnnotations(Map<String, String> annotations) {
        if (annotations == null) {
            this.annotations = null;
        } else {
            this.annotations = new LinkedHashMap(annotations);
        }
        return (A) this;
    }

    public boolean hasAnnotations() {
        return this.annotations != null;
    }

    public A addToTags(int index, String item) {
        if (this.tags == null) {
            this.tags = new ArrayList<String>();
        }
        this.tags.add(index, item);
        return (A) this;
    }

    public A setToTags(int index, String item) {
        if (this.tags == null) {
            this.tags = new ArrayList<String>();
        }
        this.tags.set(index, item);
        return (A) this;
    }

    public A addToTags(java.lang.String... items) {
        if (this.tags == null) {
            this.tags = new ArrayList<String>();
        }
        for (String item : items) {
            this.tags.add(item);
        }
        return (A) this;
    }

    public A addAllToTags(Collection<String> items) {
        if (this.tags == null) {
            this.tags = new ArrayList<String>();
        }
        for (String item : items) {
            this.tags.add(item);
        }
        return (A) this;
    }

    public A removeFromTags(java.lang.String... items) {
        if (this.tags == null)
            return (A) this;
        for (String item : items) {
            this.tags.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromTags(Collection<String> items) {
        if (this.tags == null)
            return (A) this;
        for (String item : items) {
            this.tags.remove(item);
        }
        return (A) this;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getTag(int index) {
        return this.tags.get(index);
    }

    public String getFirstTag() {
        return this.tags.get(0);
    }

    public String getLastTag() {
        return this.tags.get(tags.size() - 1);
    }

    public String getMatchingTag(Predicate<String> predicate) {
        for (String item : tags) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingTag(Predicate<String> predicate) {
        for (String item : tags) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withTags(List<String> tags) {
        if (tags != null) {
            this.tags = new ArrayList();
            for (String item : tags) {
                this.addToTags(item);
            }
        } else {
            this.tags = null;
        }
        return (A) this;
    }

    public A withTags(java.lang.String... tags) {
        if (this.tags != null) {
            this.tags.clear();
            _visitables.remove("tags");
        }
        if (tags != null) {
            for (String item : tags) {
                this.addToTags(item);
            }
        }
        return (A) this;
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
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

    public A addToLinks(io.quarkiverse.backstage.v1alpha1.EntityLink... items) {
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

    public A removeFromLinks(io.quarkiverse.backstage.v1alpha1.EntityLink... items) {
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

    public A withLinks(io.quarkiverse.backstage.v1alpha1.EntityLink... links) {
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
        EntityMetaFluent that = (EntityMetaFluent) o;
        if (!java.util.Objects.equals(uid, that.uid))
            return false;

        if (!java.util.Objects.equals(etag, that.etag))
            return false;

        if (!java.util.Objects.equals(name, that.name))
            return false;

        if (!java.util.Objects.equals(namespace, that.namespace))
            return false;

        if (!java.util.Objects.equals(title, that.title))
            return false;

        if (!java.util.Objects.equals(description, that.description))
            return false;

        if (!java.util.Objects.equals(labels, that.labels))
            return false;

        if (!java.util.Objects.equals(annotations, that.annotations))
            return false;

        if (!java.util.Objects.equals(tags, that.tags))
            return false;

        if (!java.util.Objects.equals(links, that.links))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(uid, etag, name, namespace, title, description, labels, annotations, tags, links,
                super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (uid != null) {
            sb.append("uid:");
            sb.append(uid + ",");
        }
        if (etag != null) {
            sb.append("etag:");
            sb.append(etag + ",");
        }
        if (name != null) {
            sb.append("name:");
            sb.append(name + ",");
        }
        if (namespace != null) {
            sb.append("namespace:");
            sb.append(namespace + ",");
        }
        if (title != null) {
            sb.append("title:");
            sb.append(title + ",");
        }
        if (description != null) {
            sb.append("description:");
            sb.append(description + ",");
        }
        if (labels != null && !labels.isEmpty()) {
            sb.append("labels:");
            sb.append(labels + ",");
        }
        if (annotations != null && !annotations.isEmpty()) {
            sb.append("annotations:");
            sb.append(annotations + ",");
        }
        if (tags != null && !tags.isEmpty()) {
            sb.append("tags:");
            sb.append(tags + ",");
        }
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
            return (N) EntityMetaFluent.this.setToLinks(index, builder.build());
        }

        public N endLink() {
            return and();
        }

    }

}
