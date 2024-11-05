package io.quarkiverse.backstage.client.dsl;

import io.quarkiverse.backstage.client.dsl.entities.EntitiesDsl;
import io.quarkiverse.backstage.client.dsl.events.EventsDsl;
import io.quarkiverse.backstage.client.dsl.locations.LocationsDsl;
import io.quarkiverse.backstage.client.dsl.templates.TemplatesDsl;

public interface BackstageDSL extends EntitiesDsl, LocationsDsl, TemplatesDsl, EventsDsl {

}
