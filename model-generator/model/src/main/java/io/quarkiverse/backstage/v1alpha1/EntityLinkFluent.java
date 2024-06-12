package io.quarkiverse.backstage.v1alpha1;

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

    private String url;
    private Optional<String> title = Optional.empty();
    private Optional<String> icon = Optional.empty();
    private Optional<String> type = Optional.empty();

    protected void copyInstance(EntityLink instance) {
        if (instance != null) {
            this.withUrl(instance.getUrl());
            this.withTitle(instance.getTitle());
            this.withIcon(instance.getIcon());
            this.withType(instance.getType());
        }
    }

    public String getUrl() {
        return this.url;
    }

    public A withUrl(String url) {
        this.url = url;
        return (A) this;
    }

    public boolean hasUrl() {
        return this.url != null;
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
        if (!java.util.Objects.equals(url, that.url))
            return false;

        if (!java.util.Objects.equals(title, that.title))
            return false;

        if (!java.util.Objects.equals(icon, that.icon))
            return false;

        if (!java.util.Objects.equals(type, that.type))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(url, title, icon, type, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (url != null) {
            sb.append("url:");
            sb.append(url + ",");
        }
        if (title != null) {
            sb.append("title:");
            sb.append(title + ",");
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
