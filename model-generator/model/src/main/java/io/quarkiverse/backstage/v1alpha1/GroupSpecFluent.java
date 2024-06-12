package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class GroupSpecFluent<A extends GroupSpecFluent<A>> extends BaseFluent<A> {
    public GroupSpecFluent() {
    }

    public GroupSpecFluent(GroupSpec instance) {
        this.copyInstance(instance);
    }

    private String type;
    private ProfileBuilder profile;
    private String parent;
    private List<String> children = new ArrayList<String>();
    private List<String> members = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(GroupSpec instance) {
        instance = (instance != null ? instance : new GroupSpec());

        if (instance != null) {
            this.withType(instance.getType());
            this.withProfile(instance.getProfile());
            this.withParent(instance.getParent());
            this.withChildren(instance.getChildren());
            this.withMembers(instance.getMembers());
            this.withAdditionalProperties(instance.getAdditionalProperties());
            this.withType(instance.getType());
            this.withProfile(instance.getProfile());
            this.withParent(instance.getParent());
            this.withChildren(instance.getChildren());
            this.withMembers(instance.getMembers());
            this.withAdditionalProperties(instance.getAdditionalProperties());
        }
    }

    public String getType() {
        return this.type;
    }

    public A withType(String type) {
        this.type = type;
        return (A) this;
    }

    public boolean hasType() {
        return this.type != null;
    }

    public Profile buildProfile() {
        return this.profile != null ? this.profile.build() : null;
    }

    public A withProfile(Profile profile) {
        _visitables.get("profile").remove(this.profile);
        if (profile != null) {
            this.profile = new ProfileBuilder(profile);
            _visitables.get("profile").add(this.profile);
        } else {
            this.profile = null;
            _visitables.get("profile").remove(this.profile);
        }
        return (A) this;
    }

    public boolean hasProfile() {
        return this.profile != null;
    }

    public ProfileNested<A> withNewProfile() {
        return new ProfileNested(null);
    }

    public ProfileNested<A> withNewProfileLike(Profile item) {
        return new ProfileNested(item);
    }

    public ProfileNested<A> editProfile() {
        return withNewProfileLike(java.util.Optional.ofNullable(buildProfile()).orElse(null));
    }

    public ProfileNested<A> editOrNewProfile() {
        return withNewProfileLike(java.util.Optional.ofNullable(buildProfile()).orElse(new ProfileBuilder().build()));
    }

    public ProfileNested<A> editOrNewProfileLike(Profile item) {
        return withNewProfileLike(java.util.Optional.ofNullable(buildProfile()).orElse(item));
    }

    public String getParent() {
        return this.parent;
    }

    public A withParent(String parent) {
        this.parent = parent;
        return (A) this;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public A addToChildren(int index, String item) {
        if (this.children == null) {
            this.children = new ArrayList<String>();
        }
        this.children.add(index, item);
        return (A) this;
    }

    public A setToChildren(int index, String item) {
        if (this.children == null) {
            this.children = new ArrayList<String>();
        }
        this.children.set(index, item);
        return (A) this;
    }

    public A addToChildren(java.lang.String... items) {
        if (this.children == null) {
            this.children = new ArrayList<String>();
        }
        for (String item : items) {
            this.children.add(item);
        }
        return (A) this;
    }

    public A addAllToChildren(Collection<String> items) {
        if (this.children == null) {
            this.children = new ArrayList<String>();
        }
        for (String item : items) {
            this.children.add(item);
        }
        return (A) this;
    }

    public A removeFromChildren(java.lang.String... items) {
        if (this.children == null)
            return (A) this;
        for (String item : items) {
            this.children.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromChildren(Collection<String> items) {
        if (this.children == null)
            return (A) this;
        for (String item : items) {
            this.children.remove(item);
        }
        return (A) this;
    }

    public List<String> getChildren() {
        return this.children;
    }

    public String getChild(int index) {
        return this.children.get(index);
    }

    public String getFirstChild() {
        return this.children.get(0);
    }

    public String getLastChild() {
        return this.children.get(children.size() - 1);
    }

    public String getMatchingChild(Predicate<String> predicate) {
        for (String item : children) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingChild(Predicate<String> predicate) {
        for (String item : children) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withChildren(List<String> children) {
        if (children != null) {
            this.children = new ArrayList();
            for (String item : children) {
                this.addToChildren(item);
            }
        } else {
            this.children = null;
        }
        return (A) this;
    }

    public A withChildren(java.lang.String... children) {
        if (this.children != null) {
            this.children.clear();
            _visitables.remove("children");
        }
        if (children != null) {
            for (String item : children) {
                this.addToChildren(item);
            }
        }
        return (A) this;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public A addToMembers(int index, String item) {
        if (this.members == null) {
            this.members = new ArrayList<String>();
        }
        this.members.add(index, item);
        return (A) this;
    }

    public A setToMembers(int index, String item) {
        if (this.members == null) {
            this.members = new ArrayList<String>();
        }
        this.members.set(index, item);
        return (A) this;
    }

    public A addToMembers(java.lang.String... items) {
        if (this.members == null) {
            this.members = new ArrayList<String>();
        }
        for (String item : items) {
            this.members.add(item);
        }
        return (A) this;
    }

    public A addAllToMembers(Collection<String> items) {
        if (this.members == null) {
            this.members = new ArrayList<String>();
        }
        for (String item : items) {
            this.members.add(item);
        }
        return (A) this;
    }

    public A removeFromMembers(java.lang.String... items) {
        if (this.members == null)
            return (A) this;
        for (String item : items) {
            this.members.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromMembers(Collection<String> items) {
        if (this.members == null)
            return (A) this;
        for (String item : items) {
            this.members.remove(item);
        }
        return (A) this;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public String getMember(int index) {
        return this.members.get(index);
    }

    public String getFirstMember() {
        return this.members.get(0);
    }

    public String getLastMember() {
        return this.members.get(members.size() - 1);
    }

    public String getMatchingMember(Predicate<String> predicate) {
        for (String item : members) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingMember(Predicate<String> predicate) {
        for (String item : members) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withMembers(List<String> members) {
        if (members != null) {
            this.members = new ArrayList();
            for (String item : members) {
                this.addToMembers(item);
            }
        } else {
            this.members = null;
        }
        return (A) this;
    }

    public A withMembers(java.lang.String... members) {
        if (this.members != null) {
            this.members.clear();
            _visitables.remove("members");
        }
        if (members != null) {
            for (String item : members) {
                this.addToMembers(item);
            }
        }
        return (A) this;
    }

    public boolean hasMembers() {
        return members != null && !members.isEmpty();
    }

    public A addToAdditionalProperties(String key, Object value) {
        if (this.additionalProperties == null && key != null && value != null) {
            this.additionalProperties = new LinkedHashMap();
        }
        if (key != null && value != null) {
            this.additionalProperties.put(key, value);
        }
        return (A) this;
    }

    public A addToAdditionalProperties(Map<String, Object> map) {
        if (this.additionalProperties == null && map != null) {
            this.additionalProperties = new LinkedHashMap();
        }
        if (map != null) {
            this.additionalProperties.putAll(map);
        }
        return (A) this;
    }

    public A removeFromAdditionalProperties(String key) {
        if (this.additionalProperties == null) {
            return (A) this;
        }
        if (key != null && this.additionalProperties != null) {
            this.additionalProperties.remove(key);
        }
        return (A) this;
    }

    public A removeFromAdditionalProperties(Map<String, Object> map) {
        if (this.additionalProperties == null) {
            return (A) this;
        }
        if (map != null) {
            for (Object key : map.keySet()) {
                if (this.additionalProperties != null) {
                    this.additionalProperties.remove(key);
                }
            }
        }
        return (A) this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public <K, V> A withAdditionalProperties(Map<String, Object> additionalProperties) {
        if (additionalProperties == null) {
            this.additionalProperties = null;
        } else {
            this.additionalProperties = new LinkedHashMap(additionalProperties);
        }
        return (A) this;
    }

    public boolean hasAdditionalProperties() {
        return this.additionalProperties != null;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        GroupSpecFluent that = (GroupSpecFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(profile, that.profile))
            return false;

        if (!java.util.Objects.equals(parent, that.parent))
            return false;

        if (!java.util.Objects.equals(children, that.children))
            return false;

        if (!java.util.Objects.equals(members, that.members))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, profile, parent, children, members, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (profile != null) {
            sb.append("profile:");
            sb.append(profile + ",");
        }
        if (parent != null) {
            sb.append("parent:");
            sb.append(parent + ",");
        }
        if (children != null && !children.isEmpty()) {
            sb.append("children:");
            sb.append(children + ",");
        }
        if (members != null && !members.isEmpty()) {
            sb.append("members:");
            sb.append(members + ",");
        }
        if (additionalProperties != null && !additionalProperties.isEmpty()) {
            sb.append("additionalProperties:");
            sb.append(additionalProperties);
        }
        sb.append("}");
        return sb.toString();
    }

    public class ProfileNested<N> extends ProfileFluent<ProfileNested<N>> implements Nested<N> {
        ProfileNested(Profile item) {
            this.builder = new ProfileBuilder(this, item);
        }

        ProfileBuilder builder;

        public N and() {
            return (N) GroupSpecFluent.this.withProfile(builder.build());
        }

        public N endProfile() {
            return and();
        }

    }

}
