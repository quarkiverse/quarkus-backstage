package io.quarkiverse.backstage.v1alpha1;

import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import io.quarkiverse.backstage.model.builder.BaseFluent;
import io.quarkiverse.backstage.model.builder.Nested;
import io.quarkiverse.backstage.model.builder.VisitableBuilder;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class EntityListFluent<A extends EntityListFluent<A>> extends BaseFluent<A> {
    public EntityListFluent() {
    }

    public EntityListFluent(EntityList instance) {
        this.copyInstance(instance);
    }

    private ArrayList<VisitableBuilder<? extends Entity, ?>> items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();

    protected void copyInstance(EntityList instance) {
        instance = (instance != null ? instance : new EntityList());

        if (instance != null) {
            this.withItems(instance.getItems());
            this.withItems(instance.getItems());
        }
    }

    public A addToItems(VisitableBuilder<? extends Entity, ?> builder) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        _visitables.get("items").add(builder);
        this.items.add(builder);
        return (A) this;
    }

    public A addToItems(int index, VisitableBuilder<? extends Entity, ?> builder) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        if (index < 0 || index >= items.size()) {
            _visitables.get("items").add(builder);
            items.add(builder);
        } else {
            _visitables.get("items").add(index, builder);
            items.add(index, builder);
        }
        return (A) this;
    }

    public A addToItems(int index, Entity item) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        VisitableBuilder<? extends Entity, ?> builder = builder(item);
        if (index < 0 || index >= items.size()) {
            _visitables.get("items").add(builder);
            items.add(builder);
        } else {
            _visitables.get("items").add(index, builder);
            items.add(index, builder);
        }
        return (A) this;
    }

    public A setToItems(int index, Entity item) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        VisitableBuilder<? extends Entity, ?> builder = builder(item);
        if (index < 0 || index >= items.size()) {
            _visitables.get("items").add(builder);
            items.add(builder);
        } else {
            _visitables.get("items").set(index, builder);
            items.set(index, builder);
        }
        return (A) this;
    }

    public A addToItems(io.quarkiverse.backstage.v1alpha1.Entity... items) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        for (Entity item : items) {
            VisitableBuilder<? extends Entity, ?> builder = builder(item);
            _visitables.get("items").add(builder);
            this.items.add(builder);
        }
        return (A) this;
    }

    public A addAllToItems(Collection<Entity> items) {
        if (this.items == null) {
            this.items = new ArrayList<VisitableBuilder<? extends Entity, ?>>();
        }
        for (Entity item : items) {
            VisitableBuilder<? extends Entity, ?> builder = builder(item);
            _visitables.get("items").add(builder);
            this.items.add(builder);
        }
        return (A) this;
    }

    public A removeFromItems(VisitableBuilder<? extends Entity, ?> builder) {
        if (this.items == null)
            return (A) this;
        _visitables.get("items").remove(builder);
        this.items.remove(builder);
        return (A) this;
    }

    public A removeFromItems(io.quarkiverse.backstage.v1alpha1.Entity... items) {
        if (this.items == null)
            return (A) this;
        for (Entity item : items) {
            VisitableBuilder<? extends Entity, ?> builder = builder(item);
            _visitables.get("items").remove(builder);
            this.items.remove(builder);
        }
        return (A) this;
    }

    public A removeAllFromItems(Collection<Entity> items) {
        if (this.items == null)
            return (A) this;
        for (Entity item : items) {
            VisitableBuilder<? extends Entity, ?> builder = builder(item);
            _visitables.get("items").remove(builder);
            this.items.remove(builder);
        }
        return (A) this;
    }

    public A removeMatchingFromItems(Predicate<VisitableBuilder<? extends Entity, ?>> predicate) {
        if (items == null)
            return (A) this;
        final Iterator<VisitableBuilder<? extends Entity, ?>> each = items.iterator();
        final List visitables = _visitables.get("items");
        while (each.hasNext()) {
            VisitableBuilder<? extends Entity, ?> builder = each.next();
            if (predicate.test(builder)) {
                visitables.remove(builder);
                each.remove();
            }
        }
        return (A) this;
    }

    public List<Entity> buildItems() {
        return build(items);
    }

    public Entity buildItem(int index) {
        return this.items.get(index).build();
    }

    public Entity buildFirstItem() {
        return this.items.get(0).build();
    }

    public Entity buildLastItem() {
        return this.items.get(items.size() - 1).build();
    }

    public Entity buildMatchingItem(Predicate<VisitableBuilder<? extends Entity, ?>> predicate) {
        for (VisitableBuilder<? extends Entity, ?> item : items) {
            if (predicate.test(item)) {
                return item.build();
            }
        }
        return null;
    }

    public boolean hasMatchingItem(Predicate<VisitableBuilder<? extends Entity, ?>> predicate) {
        for (VisitableBuilder<? extends Entity, ?> item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withItems(List<Entity> items) {
        if (items != null) {
            this.items = new ArrayList();
            for (Entity item : items) {
                this.addToItems(item);
            }
        } else {
            this.items = null;
        }
        return (A) this;
    }

    public A withItems(io.quarkiverse.backstage.v1alpha1.Entity... items) {
        if (this.items != null) {
            this.items.clear();
            _visitables.remove("items");
        }
        if (items != null) {
            for (Entity item : items) {
                this.addToItems(item);
            }
        }
        return (A) this;
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    public ApiItemsNested<A> addNewApiItem() {
        return new ApiItemsNested(-1, null);
    }

    public ApiItemsNested<A> addNewApiItemLike(Api item) {
        return new ApiItemsNested(-1, item);
    }

    public ApiItemsNested<A> setNewApiItemLike(int index, Api item) {
        return new ApiItemsNested(index, item);
    }

    public DomainItemsNested<A> addNewDomainItem() {
        return new DomainItemsNested(-1, null);
    }

    public DomainItemsNested<A> addNewDomainItemLike(Domain item) {
        return new DomainItemsNested(-1, item);
    }

    public DomainItemsNested<A> setNewDomainItemLike(int index, Domain item) {
        return new DomainItemsNested(index, item);
    }

    public GroupItemsNested<A> addNewGroupItem() {
        return new GroupItemsNested(-1, null);
    }

    public GroupItemsNested<A> addNewGroupItemLike(Group item) {
        return new GroupItemsNested(-1, item);
    }

    public GroupItemsNested<A> setNewGroupItemLike(int index, Group item) {
        return new GroupItemsNested(index, item);
    }

    public TemplateItemsNested<A> addNewTemplateItem() {
        return new TemplateItemsNested(-1, null);
    }

    public TemplateItemsNested<A> addNewTemplateItemLike(Template item) {
        return new TemplateItemsNested(-1, item);
    }

    public TemplateItemsNested<A> setNewTemplateItemLike(int index, Template item) {
        return new TemplateItemsNested(index, item);
    }

    public ComponentItemsNested<A> addNewComponentItem() {
        return new ComponentItemsNested(-1, null);
    }

    public ComponentItemsNested<A> addNewComponentItemLike(Component item) {
        return new ComponentItemsNested(-1, item);
    }

    public ComponentItemsNested<A> setNewComponentItemLike(int index, Component item) {
        return new ComponentItemsNested(index, item);
    }

    public LocationItemsNested<A> addNewLocationItem() {
        return new LocationItemsNested(-1, null);
    }

    public LocationItemsNested<A> addNewLocationItemLike(Location item) {
        return new LocationItemsNested(-1, item);
    }

    public LocationItemsNested<A> setNewLocationItemLike(int index, Location item) {
        return new LocationItemsNested(index, item);
    }

    public SystemItemsNested<A> addNewSystemItem() {
        return new SystemItemsNested(-1, null);
    }

    public SystemItemsNested<A> addNewSystemItemLike(System item) {
        return new SystemItemsNested(-1, item);
    }

    public SystemItemsNested<A> setNewSystemItemLike(int index, System item) {
        return new SystemItemsNested(index, item);
    }

    public ResourceItemsNested<A> addNewResourceItem() {
        return new ResourceItemsNested(-1, null);
    }

    public ResourceItemsNested<A> addNewResourceItemLike(Resource item) {
        return new ResourceItemsNested(-1, item);
    }

    public ResourceItemsNested<A> setNewResourceItemLike(int index, Resource item) {
        return new ResourceItemsNested(index, item);
    }

    public UserItemsNested<A> addNewUserItem() {
        return new UserItemsNested(-1, null);
    }

    public UserItemsNested<A> addNewUserItemLike(User item) {
        return new UserItemsNested(-1, item);
    }

    public UserItemsNested<A> setNewUserItemLike(int index, User item) {
        return new UserItemsNested(index, item);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        EntityListFluent that = (EntityListFluent) o;
        if (!java.util.Objects.equals(items, that.items))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(items, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (items != null && !items.isEmpty()) {
            sb.append("items:");
            sb.append(items);
        }
        sb.append("}");
        return sb.toString();
    }

    protected static <T> VisitableBuilder<T, ?> builder(Object item) {
        switch (item.getClass().getName()) {
            case "io.quarkiverse.backstage.v1alpha1." + "Api":
                return (VisitableBuilder<T, ?>) new ApiBuilder((Api) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Domain":
                return (VisitableBuilder<T, ?>) new DomainBuilder((Domain) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Group":
                return (VisitableBuilder<T, ?>) new GroupBuilder((Group) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Template":
                return (VisitableBuilder<T, ?>) new TemplateBuilder((Template) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Component":
                return (VisitableBuilder<T, ?>) new ComponentBuilder((Component) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Location":
                return (VisitableBuilder<T, ?>) new LocationBuilder((Location) item);
            case "io.quarkiverse.backstage.v1alpha1." + "System":
                return (VisitableBuilder<T, ?>) new SystemBuilder((System) item);
            case "io.quarkiverse.backstage.v1alpha1." + "Resource":
                return (VisitableBuilder<T, ?>) new ResourceBuilder((Resource) item);
            case "io.quarkiverse.backstage.v1alpha1." + "User":
                return (VisitableBuilder<T, ?>) new UserBuilder((User) item);
        }
        return (VisitableBuilder<T, ?>) builderOf(item);
    }

    public class ApiItemsNested<N> extends ApiFluent<ApiItemsNested<N>> implements Nested<N> {
        ApiItemsNested(int index, Api item) {
            this.index = index;
            this.builder = new ApiBuilder(this, item);
        }

        ApiBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endApiItem() {
            return and();
        }

    }

    public class DomainItemsNested<N> extends DomainFluent<DomainItemsNested<N>> implements Nested<N> {
        DomainItemsNested(int index, Domain item) {
            this.index = index;
            this.builder = new DomainBuilder(this, item);
        }

        DomainBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endDomainItem() {
            return and();
        }

    }

    public class GroupItemsNested<N> extends GroupFluent<GroupItemsNested<N>> implements Nested<N> {
        GroupItemsNested(int index, Group item) {
            this.index = index;
            this.builder = new GroupBuilder(this, item);
        }

        GroupBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endGroupItem() {
            return and();
        }

    }

    public class TemplateItemsNested<N> extends TemplateFluent<TemplateItemsNested<N>> implements Nested<N> {
        TemplateItemsNested(int index, Template item) {
            this.index = index;
            this.builder = new TemplateBuilder(this, item);
        }

        TemplateBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endTemplateItem() {
            return and();
        }

    }

    public class ComponentItemsNested<N> extends ComponentFluent<ComponentItemsNested<N>> implements Nested<N> {
        ComponentItemsNested(int index, Component item) {
            this.index = index;
            this.builder = new ComponentBuilder(this, item);
        }

        ComponentBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endComponentItem() {
            return and();
        }

    }

    public class LocationItemsNested<N> extends LocationFluent<LocationItemsNested<N>> implements Nested<N> {
        LocationItemsNested(int index, Location item) {
            this.index = index;
            this.builder = new LocationBuilder(this, item);
        }

        LocationBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endLocationItem() {
            return and();
        }

    }

    public class SystemItemsNested<N> extends SystemFluent<SystemItemsNested<N>> implements Nested<N> {
        SystemItemsNested(int index, System item) {
            this.index = index;
            this.builder = new SystemBuilder(this, item);
        }

        SystemBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endSystemItem() {
            return and();
        }

    }

    public class ResourceItemsNested<N> extends ResourceFluent<ResourceItemsNested<N>> implements Nested<N> {
        ResourceItemsNested(int index, Resource item) {
            this.index = index;
            this.builder = new ResourceBuilder(this, item);
        }

        ResourceBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endResourceItem() {
            return and();
        }

    }

    public class UserItemsNested<N> extends UserFluent<UserItemsNested<N>> implements Nested<N> {
        UserItemsNested(int index, User item) {
            this.index = index;
            this.builder = new UserBuilder(this, item);
        }

        UserBuilder builder;
        int index;

        public N and() {
            return (N) EntityListFluent.this.setToItems(index, builder.build());
        }

        public N endUserItem() {
            return and();
        }

    }

}
