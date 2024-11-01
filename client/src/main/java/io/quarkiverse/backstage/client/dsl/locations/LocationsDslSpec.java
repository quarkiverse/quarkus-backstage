package io.quarkiverse.backstage.client.dsl.locations;

import java.util.List;

import io.quarkiverse.backstage.client.model.AnalyzeLocationRequest;
import io.quarkiverse.backstage.client.model.AnalyzeLocationResponse;
import io.quarkiverse.backstage.client.model.CreateLocationResponse;
import io.quarkiverse.backstage.client.model.LocationEntry;
import io.quarkiverse.backstage.v1alpha1.Location;
import io.sundr.dsl.annotations.All;
import io.sundr.dsl.annotations.Any;
import io.sundr.dsl.annotations.Dsl;
import io.sundr.dsl.annotations.EntryPoint;
import io.sundr.dsl.annotations.InterfaceName;
import io.sundr.dsl.annotations.MethodName;
import io.sundr.dsl.annotations.None;
import io.sundr.dsl.annotations.Terminal;

@Dsl
@InterfaceName("LocationsDsl")
public interface LocationsDslSpec {

    @EntryPoint
    void locations();

    @Any(methods = { "locations" })
    @None(methods = { "withId" })
    void withKind(String kind);

    @Any(methods = { "locations" })
    @All(methods = { "withKind" })
    void withName(String name);

    @Any(methods = { "locations" })
    @All(methods = { "withKind", "withName" })
    void inNamespace(String namespace);

    @Any(methods = { "locations" })
    @None(methods = { "withKind", "withName", "inNamespace" })
    void withId(String name);

    @Terminal
    @Any(methods = { "locations" })
    @None(methods = { "withId", "withKind", "withName", "inNamespace" })
    CreateLocationResponse create(Location location);

    @Terminal
    @Any(methods = { "locations" })
    @None(methods = { "withId", "withKind", "withName", "inNamespace" })
    CreateLocationResponse createFromUrl(String url);

    @Terminal
    @Any(methods = { "locations" })
    @None(methods = { "withId", "withKind", "withName", "inNamespace" })
    CreateLocationResponse createFromFile(String file);

    @Terminal
    @MethodName("list")
    @Any(methods = { "locations" })
    @None(methods = { "withId", "withKind", "withName", "inNamespace" })
    List<LocationEntry> listLocations();

    @Terminal
    @InterfaceName("GetByEntityInterface")
    @MethodName("get")
    @Any(methods = { "locations" })
    @All(methods = { "withKind", "withName", "inNamespace" })
    LocationEntry getEntity();

    @Terminal
    @InterfaceName("GetByIdInterface")
    @MethodName("get")
    @Any(methods = { "locations" })
    @All(methods = { "withId" })
    LocationEntry getEntityById();

    @Terminal
    @MethodName("delete")
    @All(methods = { "locations", "withId" })
    Boolean deleteLocation();

    @Terminal
    @MethodName("refresh")
    @Any(methods = { "locations" })
    @All(methods = { "withKind", "withName", "inNamespace" })
    Boolean refresh();

    @Terminal
    @MethodName("analyze")
    @Any(methods = { "locations" })
    @None(methods = { "withKind", "withName", "inNamespace", "withId" })
    AnalyzeLocationResponse analyze(AnalyzeLocationRequest request);
}

interface Usage {
    default void usage(LocationsDsl client) {
        List<LocationEntry> locations = client.locations().list();
        LocationEntry byId = client.locations().withId("id").get();
        LocationEntry byKindNameAndNamespace = client.locations().withKind("kind").withName("name").inNamespace("namespace")
                .get();
        client.locations().withId("id").delete();

    }
}
