package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;

import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ResourceFluent<A extends ResourceFluent<A>> extends BaseFluent<A> {
    public ResourceFluent() {
    }

    public ResourceFluent(Resource instance) {
        this.copyInstance(instance);
    }

    private EntityMetaBuilder metadata;
    private ResourceSpecBuilder spec;
    private StatusBuilder status;

    protected void copyInstance(Resource instance) {
        instance = (instance != null ? instance : new Resource());

        if (instance != null) {
            this.withMetadata(instance.getMetadata());
            this.withSpec(instance.getSpec());
            this.withStatus(instance.getStatus());
            this.withMetadata(instance.getMetadata());
            this.withSpec(instance.getSpec());
            this.withStatus(instance.getStatus());
        }
    }

    public EntityMeta buildMetadata() {
        return this.metadata != null ? this.metadata.build() : null;
    }

    public A withMetadata(EntityMeta metadata) {
        _visitables.get("metadata").remove(this.metadata);
        if (metadata != null) {
            this.metadata = new EntityMetaBuilder(metadata);
            _visitables.get("metadata").add(this.metadata);
        } else {
            this.metadata = null;
            _visitables.get("metadata").remove(this.metadata);
        }
        return (A) this;
    }

    public boolean hasMetadata() {
        return this.metadata != null;
    }

    public MetadataNested<A> withNewMetadata() {
        return new MetadataNested(null);
    }

    public MetadataNested<A> withNewMetadataLike(EntityMeta item) {
        return new MetadataNested(item);
    }

    public MetadataNested<A> editMetadata() {
        return withNewMetadataLike(java.util.Optional.ofNullable(buildMetadata()).orElse(null));
    }

    public MetadataNested<A> editOrNewMetadata() {
        return withNewMetadataLike(java.util.Optional.ofNullable(buildMetadata()).orElse(new EntityMetaBuilder().build()));
    }

    public MetadataNested<A> editOrNewMetadataLike(EntityMeta item) {
        return withNewMetadataLike(java.util.Optional.ofNullable(buildMetadata()).orElse(item));
    }

    public ResourceSpec buildSpec() {
        return this.spec != null ? this.spec.build() : null;
    }

    public A withSpec(ResourceSpec spec) {
        _visitables.get("spec").remove(this.spec);
        if (spec != null) {
            this.spec = new ResourceSpecBuilder(spec);
            _visitables.get("spec").add(this.spec);
        } else {
            this.spec = null;
            _visitables.get("spec").remove(this.spec);
        }
        return (A) this;
    }

    public boolean hasSpec() {
        return this.spec != null;
    }

    public SpecNested<A> withNewSpec() {
        return new SpecNested(null);
    }

    public SpecNested<A> withNewSpecLike(ResourceSpec item) {
        return new SpecNested(item);
    }

    public SpecNested<A> editSpec() {
        return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(null));
    }

    public SpecNested<A> editOrNewSpec() {
        return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(new ResourceSpecBuilder().build()));
    }

    public SpecNested<A> editOrNewSpecLike(ResourceSpec item) {
        return withNewSpecLike(java.util.Optional.ofNullable(buildSpec()).orElse(item));
    }

    public Status buildStatus() {
        return this.status != null ? this.status.build() : null;
    }

    public A withStatus(Status status) {
        _visitables.get("status").remove(this.status);
        if (status != null) {
            this.status = new StatusBuilder(status);
            _visitables.get("status").add(this.status);
        } else {
            this.status = null;
            _visitables.get("status").remove(this.status);
        }
        return (A) this;
    }

    public boolean hasStatus() {
        return this.status != null;
    }

    public StatusNested<A> withNewStatus() {
        return new StatusNested(null);
    }

    public StatusNested<A> withNewStatusLike(Status item) {
        return new StatusNested(item);
    }

    public StatusNested<A> editStatus() {
        return withNewStatusLike(java.util.Optional.ofNullable(buildStatus()).orElse(null));
    }

    public StatusNested<A> editOrNewStatus() {
        return withNewStatusLike(java.util.Optional.ofNullable(buildStatus()).orElse(new StatusBuilder().build()));
    }

    public StatusNested<A> editOrNewStatusLike(Status item) {
        return withNewStatusLike(java.util.Optional.ofNullable(buildStatus()).orElse(item));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ResourceFluent that = (ResourceFluent) o;
        if (!java.util.Objects.equals(metadata, that.metadata))
            return false;

        if (!java.util.Objects.equals(spec, that.spec))
            return false;

        if (!java.util.Objects.equals(status, that.status))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(metadata, spec, status, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (metadata != null) {
            sb.append("metadata:");
            sb.append(metadata + ",");
        }
        if (spec != null) {
            sb.append("spec:");
            sb.append(spec + ",");
        }
        if (status != null) {
            sb.append("status:");
            sb.append(status);
        }
        sb.append("}");
        return sb.toString();
    }

    public class MetadataNested<N> extends EntityMetaFluent<MetadataNested<N>> implements Nested<N> {
        MetadataNested(EntityMeta item) {
            this.builder = new EntityMetaBuilder(this, item);
        }

        EntityMetaBuilder builder;

        public N and() {
            return (N) ResourceFluent.this.withMetadata(builder.build());
        }

        public N endMetadata() {
            return and();
        }

    }

    public class SpecNested<N> extends ResourceSpecFluent<SpecNested<N>> implements Nested<N> {
        SpecNested(ResourceSpec item) {
            this.builder = new ResourceSpecBuilder(this, item);
        }

        ResourceSpecBuilder builder;

        public N and() {
            return (N) ResourceFluent.this.withSpec(builder.build());
        }

        public N endSpec() {
            return and();
        }

    }

    public class StatusNested<N> extends StatusFluent<StatusNested<N>> implements Nested<N> {
        StatusNested(Status item) {
            this.builder = new StatusBuilder(this, item);
        }

        StatusBuilder builder;

        public N and() {
            return (N) ResourceFluent.this.withStatus(builder.build());
        }

        public N endStatus() {
            return and();
        }

    }

}
