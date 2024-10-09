package io.quarkiverse.backstage.v1alpha1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.quarkiverse.backstage.EntityMeta;
import io.quarkiverse.backstage.Status;
import io.quarkiverse.backstage.scaffolder.v1beta3.Template;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Api.class, name = "API"),
        @JsonSubTypes.Type(value = Component.class, name = "Component"),
        @JsonSubTypes.Type(value = Domain.class, name = "Domain"),
        @JsonSubTypes.Type(value = Group.class, name = "Group"),
        @JsonSubTypes.Type(value = Location.class, name = "Location"),
        @JsonSubTypes.Type(value = Resource.class, name = "Resource"),
        @JsonSubTypes.Type(value = System.class, name = "System"),
        @JsonSubTypes.Type(value = Template.class, name = "Template"),
        @JsonSubTypes.Type(value = User.class, name = "User"),
})
public interface Entity {

    String BACKSTAGE_IO_V1ALPHA1 = "backstage.io/v1alpha1";

    default String getKind() {
        throw new UnsupportedOperationException();
    }

    default String getApiVersion() {
        return BACKSTAGE_IO_V1ALPHA1;
    }

    default EntityMeta getMetadata() {
        throw new UnsupportedOperationException();
    }

    default Status getStatus() {
        throw new UnsupportedOperationException();
    }
}
