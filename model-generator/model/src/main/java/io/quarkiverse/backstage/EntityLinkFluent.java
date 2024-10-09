package io.quarkiverse.backstage;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Optional;

import io.quarkiverse.backstage.model.builder.BaseFluent;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class EntityLinkFluent<A extends EntityLinkFluent<A>> extends BaseFluent<A> {
    public EntityLinkFluent() {
    }

    public EntityLinkFluent(EntityLink instance) {
        this.copyInstance(instance);
    }

    private String title;
    private Optional<String> url = Optional.empty();
    private Optional<String> entityRef = Optional.empty();
    private Optional<String> icon = Optional.empty();
    private Optional<String> type = Optional.empty();

    protected void copyInstance(EntityLink instance) {
        instance = (instance != null ? instance : new EntityLink());

        if (instance != null) {
            this.withTitle(instance.getTitle());
            this.withUrl(instance.getUrl());
            this.withEntityRef(instance.getEntityRef());
            this.withIcon(instance.getIcon());
            this.withType(instance.getType());
            this.withTitle(instance.getTitle());
            this.withUrl(instance.getUrl());
            this.withEntityRef(instance.getEntityRef());
            this.withIcon(instance.getIcon());
            this.withType(instance.getType());
        }
    }

    public String getTitle() {
        return this.title;
    }

    public A withTitle(String title) {
        this.title = title;
        return (A) this;
    }

    public boolean hasTitle() {
        return this.title != null;
    }

    public A withUrl(Optional<String> url) {
        if (url == null || !url.isPresent()) {
            this.url = Optional.empty();
        } else {
            this.url = url;
        }
        return (A) this;
    }

    public A withUrl(String url) {
        if (url == null) {
            this.url = Optional.empty();
        } else {
            this.url = Optional.of(url);
        }
        return (A) this;
    }

    public Optional<String> getUrl() {
        return this.url;
    }

    public boolean hasUrl() {
        return url != null && url.isPresent();
    }

    public A withEntityRef(Optional<String> entityRef) {
        if (entityRef == null || !entityRef.isPresent()) {
            this.entityRef = Optional.empty();
        } else {
            this.entityRef = entityRef;
        }
        return (A) this;
    }

    public A withEntityRef(String entityRef) {
        if (entityRef == null) {
            this.entityRef = Optional.empty();
        } else {
            this.entityRef = Optional.of(entityRef);
        }
        return (A) this;
    }

    public Optional<String> getEntityRef() {
        return this.entityRef;
    }

    public boolean hasEntityRef() {
        return entityRef != null && entityRef.isPresent();
    }

    public A withIcon(Optional<String> icon) {
        if (icon == null || !icon.isPresent()) {
            this.icon = Optional.empty();
        } else {
            this.icon = icon;
        }
        return (A) this;
    }

    public A withIcon(String icon) {
        if (icon == null) {
            this.icon = Optional.empty();
        } else {
            this.icon = Optional.of(icon);
        }
        return (A) this;
    }

    public Optional<String> getIcon() {
        return this.icon;
    }

    public boolean hasIcon() {
        return icon != null && icon.isPresent();
    }

    public A withType(Optional<String> type) {
        if (type == null || !type.isPresent()) {
            this.type = Optional.empty();
        } else {
            this.type = type;
        }
        return (A) this;
    }

    public A withType(String type) {
        if (type == null) {
            this.type = Optional.empty();
        } else {
            this.type = Optional.of(type);
        }
        return (A) this;
    }

    public Optional<String> getType() {
        return this.type;
    }

    public boolean hasType() {
        return type != null && type.isPresent();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        EntityLinkFluent that = (EntityLinkFluent) o;
        if (!java.util.Objects.equals(title, that.title))
            return false;

        if (!java.util.Objects.equals(url, that.url))
            return false;

        if (!java.util.Objects.equals(entityRef, that.entityRef))
            return false;

        if (!java.util.Objects.equals(icon, that.icon))
            return false;

        if (!java.util.Objects.equals(type, that.type))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(title, url, entityRef, icon, type, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (title != null) {
            sb.append("title:");
            sb.append(title + ",");
        }
        if (url != null) {
            sb.append("url:");
            sb.append(url + ",");
        }
        if (entityRef != null) {
            sb.append("entityRef:");
            sb.append(entityRef + ",");
        }
        if (icon != null) {
            sb.append("icon:");
            sb.append(icon + ",");
        }
        if (type != null) {
            sb.append("type:");
            sb.append(type);
        }
        sb.append("}");
        return sb.toString();
    }

}
