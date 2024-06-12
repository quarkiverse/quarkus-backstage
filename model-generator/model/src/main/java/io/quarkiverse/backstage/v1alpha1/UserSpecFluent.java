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
public class UserSpecFluent<A extends UserSpecFluent<A>> extends BaseFluent<A> {
    public UserSpecFluent() {
    }

    public UserSpecFluent(UserSpec instance) {
        this.copyInstance(instance);
    }

    private ProfileBuilder profile;
    private List<String> memberOf = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    protected void copyInstance(UserSpec instance) {
        instance = (instance != null ? instance : new UserSpec());

        if (instance != null) {
            this.withProfile(instance.getProfile());
            this.withMemberOf(instance.getMemberOf());
            this.withAdditionalProperties(instance.getAdditionalProperties());
            this.withProfile(instance.getProfile());
            this.withMemberOf(instance.getMemberOf());
            this.withAdditionalProperties(instance.getAdditionalProperties());
        }
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

    public A addToMemberOf(int index, String item) {
        if (this.memberOf == null) {
            this.memberOf = new ArrayList<String>();
        }
        this.memberOf.add(index, item);
        return (A) this;
    }

    public A setToMemberOf(int index, String item) {
        if (this.memberOf == null) {
            this.memberOf = new ArrayList<String>();
        }
        this.memberOf.set(index, item);
        return (A) this;
    }

    public A addToMemberOf(java.lang.String... items) {
        if (this.memberOf == null) {
            this.memberOf = new ArrayList<String>();
        }
        for (String item : items) {
            this.memberOf.add(item);
        }
        return (A) this;
    }

    public A addAllToMemberOf(Collection<String> items) {
        if (this.memberOf == null) {
            this.memberOf = new ArrayList<String>();
        }
        for (String item : items) {
            this.memberOf.add(item);
        }
        return (A) this;
    }

    public A removeFromMemberOf(java.lang.String... items) {
        if (this.memberOf == null)
            return (A) this;
        for (String item : items) {
            this.memberOf.remove(item);
        }
        return (A) this;
    }

    public A removeAllFromMemberOf(Collection<String> items) {
        if (this.memberOf == null)
            return (A) this;
        for (String item : items) {
            this.memberOf.remove(item);
        }
        return (A) this;
    }

    public List<String> getMemberOf() {
        return this.memberOf;
    }

    public String getMemberOf(int index) {
        return this.memberOf.get(index);
    }

    public String getFirstMemberOf() {
        return this.memberOf.get(0);
    }

    public String getLastMemberOf() {
        return this.memberOf.get(memberOf.size() - 1);
    }

    public String getMatchingMemberOf(Predicate<String> predicate) {
        for (String item : memberOf) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasMatchingMemberOf(Predicate<String> predicate) {
        for (String item : memberOf) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withMemberOf(List<String> memberOf) {
        if (memberOf != null) {
            this.memberOf = new ArrayList();
            for (String item : memberOf) {
                this.addToMemberOf(item);
            }
        } else {
            this.memberOf = null;
        }
        return (A) this;
    }

    public A withMemberOf(java.lang.String... memberOf) {
        if (this.memberOf != null) {
            this.memberOf.clear();
            _visitables.remove("memberOf");
        }
        if (memberOf != null) {
            for (String item : memberOf) {
                this.addToMemberOf(item);
            }
        }
        return (A) this;
    }

    public boolean hasMemberOf() {
        return memberOf != null && !memberOf.isEmpty();
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
        UserSpecFluent that = (UserSpecFluent) o;
        if (!java.util.Objects.equals(profile, that.profile))
            return false;

        if (!java.util.Objects.equals(memberOf, that.memberOf))
            return false;

        if (!java.util.Objects.equals(additionalProperties, that.additionalProperties))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(profile, memberOf, additionalProperties, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (profile != null) {
            sb.append("profile:");
            sb.append(profile + ",");
        }
        if (memberOf != null && !memberOf.isEmpty()) {
            sb.append("memberOf:");
            sb.append(memberOf + ",");
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
            return (N) UserSpecFluent.this.withProfile(builder.build());
        }

        public N endProfile() {
            return and();
        }

    }

}
