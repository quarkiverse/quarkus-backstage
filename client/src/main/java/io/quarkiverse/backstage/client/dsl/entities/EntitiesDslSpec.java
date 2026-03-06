package io.quarkiverse.backstage.client.dsl.entities;

import java.util.Collection;
import java.util.List;

import io.quarkiverse.backstage.v1alpha1.Entity;
import io.sundr.dsl.annotations.All;
import io.sundr.dsl.annotations.Any;
import io.sundr.dsl.annotations.Dsl;
import io.sundr.dsl.annotations.EntryPoint;
import io.sundr.dsl.annotations.InterfaceName;
import io.sundr.dsl.annotations.MethodName;
import io.sundr.dsl.annotations.None;
import io.sundr.dsl.annotations.Terminal;

@Dsl
@InterfaceName("EntitiesDsl")
public interface EntitiesDslSpec {

    @EntryPoint
    void entities();

    @Any(methods = { "entities" })
    @None(methods = { "withUID" })
    void withKind(String kind);

    @Any(methods = { "entities" })
    @All(methods = { "withKind" })
    void withName(String name);

    @Any(methods = { "entities" })
    @All(methods = { "withKind", "withName" })
    void inNamespace(String namespace);

    @Any(methods = { "entities" })
    @None(methods = { "withKind", "withName", "inNamespace" })
    void withUID(String name);

    @Terminal
    @MethodName("create")
    @Any(methods = { "entities" })
    @None(methods = { "withUID", "withKind", "withName", "inNamespace" })
    List<Entity> create(Collection<Entity> entities);

    @Terminal
    @MethodName("list")
    @Any(methods = { "entities" })
    @None(methods = { "withUID", "withKind", "withName", "inNamespace" })
    List<Entity> listEntities();

    @Terminal
    @MethodName("list")
    @Any(methods = { "entities" })
    @None(methods = { "withUID", "withKind", "withName", "inNamespace" })
    List<Entity> listEntities(String filter);

    @Terminal
    @MethodName("get")
    @Any(methods = { "entities" })
    @All(methods = { "withKind", "withName", "inNamespace" })
    Entity getEntity();

    @Terminal
    @MethodName("delete")
    @All(methods = { "entities", "withUID" })
    Boolean deleteEntity();

    @Terminal
    @MethodName("refresh")
    @Any(methods = { "entities" })
    @All(methods = { "withKind", "withName", "inNamespace" })
    Boolean refresh();
}

interface Usage {

    default void usage(EntitiesDsl client) {
        List<Entity> entities = client.entities().list();
        List<Entity> filtered = client.entities().list("filter");
        Entity entity = client.entities().withKind("Component").withName("my-component").inNamespace("default").get();
        client.entities().withKind("Component").withName("my-component").inNamespace("default").refresh();
        client.entities().withKind("Component").withName("my-component").inNamespace("default");
        client.entities().withUID("1234").delete();
        client.entities().create(entities);
    }
}
