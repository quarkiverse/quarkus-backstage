package io.quarkiverse.backstage.client.dsl.templates;

import java.util.Map;

import io.quarkiverse.backstage.scaffolder.v1beta3.Template;
import io.sundr.dsl.annotations.All;
import io.sundr.dsl.annotations.Any;
import io.sundr.dsl.annotations.Dsl;
import io.sundr.dsl.annotations.EntryPoint;
import io.sundr.dsl.annotations.InterfaceName;
import io.sundr.dsl.annotations.MethodName;
import io.sundr.dsl.annotations.Terminal;

@Dsl
@InterfaceName("TemplatesDsl")
public interface TemplatesDslSpec {

    @EntryPoint
    void templates();

    @Any(methods = { "templates" })
    void withName(String name);

    @Any(methods = { "templates" })
    @All(methods = { "withName" })
    void inNamespace(String namespace);

    @Terminal
    @MethodName("get")
    @All(methods = { "templates" })
    @Any(methods = { "withName", "inNamespace" })
    Template getTemplate();

    @Terminal
    @MethodName("instantiate")
    @Any(methods = { "withName", "inNamespace" })
    String instantiate(Map<String, Object> values);

}

interface Usage {
    default void usage(TemplatesDsl client) {
        String result = client.templates().withName("my-template").instantiate(Map.of("key", "value"));
        result = client.templates().withName("my-template").inNamespace("my-namespace").instantiate(Map.of("key", "value"));
        client.templates().withName("my-template").get();
    }
}
