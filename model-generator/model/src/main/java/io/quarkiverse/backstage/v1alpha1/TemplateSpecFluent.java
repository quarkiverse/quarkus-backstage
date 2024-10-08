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

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class TemplateSpecFluent<A extends TemplateSpecFluent<A>> extends BaseFluent<A> {
    public TemplateSpecFluent() {
    }

    public TemplateSpecFluent(TemplateSpec instance) {
        this.copyInstance(instance);
    }

    private String type;
    private ArrayList<ParameterBuilder> parameters = new ArrayList<ParameterBuilder>();
    private ArrayList<StepBuilder> steps = new ArrayList<StepBuilder>();
    private OutputBuilder output;

    protected void copyInstance(TemplateSpec instance) {
        instance = (instance != null ? instance : new TemplateSpec());

        if (instance != null) {
            this.withType(instance.getType());
            this.withParameters(instance.getParameters());
            this.withSteps(instance.getSteps());
            this.withOutput(instance.getOutput());
            this.withType(instance.getType());
            this.withParameters(instance.getParameters());
            this.withSteps(instance.getSteps());
            this.withOutput(instance.getOutput());
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

    public A addToParameters(int index, Parameter item) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<ParameterBuilder>();
        }
        ParameterBuilder builder = new ParameterBuilder(item);
        if (index < 0 || index >= parameters.size()) {
            _visitables.get("parameters").add(builder);
            parameters.add(builder);
        } else {
            _visitables.get("parameters").add(index, builder);
            parameters.add(index, builder);
        }
        return (A) this;
    }

    public A setToParameters(int index, Parameter item) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<ParameterBuilder>();
        }
        ParameterBuilder builder = new ParameterBuilder(item);
        if (index < 0 || index >= parameters.size()) {
            _visitables.get("parameters").add(builder);
            parameters.add(builder);
        } else {
            _visitables.get("parameters").set(index, builder);
            parameters.set(index, builder);
        }
        return (A) this;
    }

    public A addToParameters(io.quarkiverse.backstage.v1alpha1.Parameter... items) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<ParameterBuilder>();
        }
        for (Parameter item : items) {
            ParameterBuilder builder = new ParameterBuilder(item);
            _visitables.get("parameters").add(builder);
            this.parameters.add(builder);
        }
        return (A) this;
    }

    public A addAllToParameters(Collection<Parameter> items) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<ParameterBuilder>();
        }
        for (Parameter item : items) {
            ParameterBuilder builder = new ParameterBuilder(item);
            _visitables.get("parameters").add(builder);
            this.parameters.add(builder);
        }
        return (A) this;
    }

    public A removeFromParameters(io.quarkiverse.backstage.v1alpha1.Parameter... items) {
        if (this.parameters == null)
            return (A) this;
        for (Parameter item : items) {
            ParameterBuilder builder = new ParameterBuilder(item);
            _visitables.get("parameters").remove(builder);
            this.parameters.remove(builder);
        }
        return (A) this;
    }

    public A removeAllFromParameters(Collection<Parameter> items) {
        if (this.parameters == null)
            return (A) this;
        for (Parameter item : items) {
            ParameterBuilder builder = new ParameterBuilder(item);
            _visitables.get("parameters").remove(builder);
            this.parameters.remove(builder);
        }
        return (A) this;
    }

    public A removeMatchingFromParameters(Predicate<ParameterBuilder> predicate) {
        if (parameters == null)
            return (A) this;
        final Iterator<ParameterBuilder> each = parameters.iterator();
        final List visitables = _visitables.get("parameters");
        while (each.hasNext()) {
            ParameterBuilder builder = each.next();
            if (predicate.test(builder)) {
                visitables.remove(builder);
                each.remove();
            }
        }
        return (A) this;
    }

    public List<Parameter> buildParameters() {
        return parameters != null ? build(parameters) : null;
    }

    public Parameter buildParameter(int index) {
        return this.parameters.get(index).build();
    }

    public Parameter buildFirstParameter() {
        return this.parameters.get(0).build();
    }

    public Parameter buildLastParameter() {
        return this.parameters.get(parameters.size() - 1).build();
    }

    public Parameter buildMatchingParameter(Predicate<ParameterBuilder> predicate) {
        for (ParameterBuilder item : parameters) {
            if (predicate.test(item)) {
                return item.build();
            }
        }
        return null;
    }

    public boolean hasMatchingParameter(Predicate<ParameterBuilder> predicate) {
        for (ParameterBuilder item : parameters) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withParameters(List<Parameter> parameters) {
        if (this.parameters != null) {
            _visitables.get("parameters").clear();
        }
        if (parameters != null) {
            this.parameters = new ArrayList();
            for (Parameter item : parameters) {
                this.addToParameters(item);
            }
        } else {
            this.parameters = null;
        }
        return (A) this;
    }

    public A withParameters(io.quarkiverse.backstage.v1alpha1.Parameter... parameters) {
        if (this.parameters != null) {
            this.parameters.clear();
            _visitables.remove("parameters");
        }
        if (parameters != null) {
            for (Parameter item : parameters) {
                this.addToParameters(item);
            }
        }
        return (A) this;
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public A addNewParameter(String name, String description, String type, String defaultValue) {
        return (A) addToParameters(new Parameter(name, description, type, defaultValue));
    }

    public ParametersNested<A> addNewParameter() {
        return new ParametersNested(-1, null);
    }

    public ParametersNested<A> addNewParameterLike(Parameter item) {
        return new ParametersNested(-1, item);
    }

    public ParametersNested<A> setNewParameterLike(int index, Parameter item) {
        return new ParametersNested(index, item);
    }

    public ParametersNested<A> editParameter(int index) {
        if (parameters.size() <= index)
            throw new RuntimeException("Can't edit parameters. Index exceeds size.");
        return setNewParameterLike(index, buildParameter(index));
    }

    public ParametersNested<A> editFirstParameter() {
        if (parameters.size() == 0)
            throw new RuntimeException("Can't edit first parameters. The list is empty.");
        return setNewParameterLike(0, buildParameter(0));
    }

    public ParametersNested<A> editLastParameter() {
        int index = parameters.size() - 1;
        if (index < 0)
            throw new RuntimeException("Can't edit last parameters. The list is empty.");
        return setNewParameterLike(index, buildParameter(index));
    }

    public ParametersNested<A> editMatchingParameter(Predicate<ParameterBuilder> predicate) {
        int index = -1;
        for (int i = 0; i < parameters.size(); i++) {
            if (predicate.test(parameters.get(i))) {
                index = i;
                break;
            }
        }
        if (index < 0)
            throw new RuntimeException("Can't edit matching parameters. No match found.");
        return setNewParameterLike(index, buildParameter(index));
    }

    public A addToSteps(int index, Step item) {
        if (this.steps == null) {
            this.steps = new ArrayList<StepBuilder>();
        }
        StepBuilder builder = new StepBuilder(item);
        if (index < 0 || index >= steps.size()) {
            _visitables.get("steps").add(builder);
            steps.add(builder);
        } else {
            _visitables.get("steps").add(index, builder);
            steps.add(index, builder);
        }
        return (A) this;
    }

    public A setToSteps(int index, Step item) {
        if (this.steps == null) {
            this.steps = new ArrayList<StepBuilder>();
        }
        StepBuilder builder = new StepBuilder(item);
        if (index < 0 || index >= steps.size()) {
            _visitables.get("steps").add(builder);
            steps.add(builder);
        } else {
            _visitables.get("steps").set(index, builder);
            steps.set(index, builder);
        }
        return (A) this;
    }

    public A addToSteps(io.quarkiverse.backstage.v1alpha1.Step... items) {
        if (this.steps == null) {
            this.steps = new ArrayList<StepBuilder>();
        }
        for (Step item : items) {
            StepBuilder builder = new StepBuilder(item);
            _visitables.get("steps").add(builder);
            this.steps.add(builder);
        }
        return (A) this;
    }

    public A addAllToSteps(Collection<Step> items) {
        if (this.steps == null) {
            this.steps = new ArrayList<StepBuilder>();
        }
        for (Step item : items) {
            StepBuilder builder = new StepBuilder(item);
            _visitables.get("steps").add(builder);
            this.steps.add(builder);
        }
        return (A) this;
    }

    public A removeFromSteps(io.quarkiverse.backstage.v1alpha1.Step... items) {
        if (this.steps == null)
            return (A) this;
        for (Step item : items) {
            StepBuilder builder = new StepBuilder(item);
            _visitables.get("steps").remove(builder);
            this.steps.remove(builder);
        }
        return (A) this;
    }

    public A removeAllFromSteps(Collection<Step> items) {
        if (this.steps == null)
            return (A) this;
        for (Step item : items) {
            StepBuilder builder = new StepBuilder(item);
            _visitables.get("steps").remove(builder);
            this.steps.remove(builder);
        }
        return (A) this;
    }

    public A removeMatchingFromSteps(Predicate<StepBuilder> predicate) {
        if (steps == null)
            return (A) this;
        final Iterator<StepBuilder> each = steps.iterator();
        final List visitables = _visitables.get("steps");
        while (each.hasNext()) {
            StepBuilder builder = each.next();
            if (predicate.test(builder)) {
                visitables.remove(builder);
                each.remove();
            }
        }
        return (A) this;
    }

    public List<Step> buildSteps() {
        return steps != null ? build(steps) : null;
    }

    public Step buildStep(int index) {
        return this.steps.get(index).build();
    }

    public Step buildFirstStep() {
        return this.steps.get(0).build();
    }

    public Step buildLastStep() {
        return this.steps.get(steps.size() - 1).build();
    }

    public Step buildMatchingStep(Predicate<StepBuilder> predicate) {
        for (StepBuilder item : steps) {
            if (predicate.test(item)) {
                return item.build();
            }
        }
        return null;
    }

    public boolean hasMatchingStep(Predicate<StepBuilder> predicate) {
        for (StepBuilder item : steps) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    public A withSteps(List<Step> steps) {
        if (this.steps != null) {
            _visitables.get("steps").clear();
        }
        if (steps != null) {
            this.steps = new ArrayList();
            for (Step item : steps) {
                this.addToSteps(item);
            }
        } else {
            this.steps = null;
        }
        return (A) this;
    }

    public A withSteps(io.quarkiverse.backstage.v1alpha1.Step... steps) {
        if (this.steps != null) {
            this.steps.clear();
            _visitables.remove("steps");
        }
        if (steps != null) {
            for (Step item : steps) {
                this.addToSteps(item);
            }
        }
        return (A) this;
    }

    public boolean hasSteps() {
        return steps != null && !steps.isEmpty();
    }

    public StepsNested<A> addNewStep() {
        return new StepsNested(-1, null);
    }

    public StepsNested<A> addNewStepLike(Step item) {
        return new StepsNested(-1, item);
    }

    public StepsNested<A> setNewStepLike(int index, Step item) {
        return new StepsNested(index, item);
    }

    public StepsNested<A> editStep(int index) {
        if (steps.size() <= index)
            throw new RuntimeException("Can't edit steps. Index exceeds size.");
        return setNewStepLike(index, buildStep(index));
    }

    public StepsNested<A> editFirstStep() {
        if (steps.size() == 0)
            throw new RuntimeException("Can't edit first steps. The list is empty.");
        return setNewStepLike(0, buildStep(0));
    }

    public StepsNested<A> editLastStep() {
        int index = steps.size() - 1;
        if (index < 0)
            throw new RuntimeException("Can't edit last steps. The list is empty.");
        return setNewStepLike(index, buildStep(index));
    }

    public StepsNested<A> editMatchingStep(Predicate<StepBuilder> predicate) {
        int index = -1;
        for (int i = 0; i < steps.size(); i++) {
            if (predicate.test(steps.get(i))) {
                index = i;
                break;
            }
        }
        if (index < 0)
            throw new RuntimeException("Can't edit matching steps. No match found.");
        return setNewStepLike(index, buildStep(index));
    }

    public Output buildOutput() {
        return this.output != null ? this.output.build() : null;
    }

    public A withOutput(Output output) {
        _visitables.get("output").remove(this.output);
        if (output != null) {
            this.output = new OutputBuilder(output);
            _visitables.get("output").add(this.output);
        } else {
            this.output = null;
            _visitables.get("output").remove(this.output);
        }
        return (A) this;
    }

    public boolean hasOutput() {
        return this.output != null;
    }

    public A withNewOutput(String name, String description) {
        return (A) withOutput(new Output(name, description));
    }

    public OutputNested<A> withNewOutput() {
        return new OutputNested(null);
    }

    public OutputNested<A> withNewOutputLike(Output item) {
        return new OutputNested(item);
    }

    public OutputNested<A> editOutput() {
        return withNewOutputLike(java.util.Optional.ofNullable(buildOutput()).orElse(null));
    }

    public OutputNested<A> editOrNewOutput() {
        return withNewOutputLike(java.util.Optional.ofNullable(buildOutput()).orElse(new OutputBuilder().build()));
    }

    public OutputNested<A> editOrNewOutputLike(Output item) {
        return withNewOutputLike(java.util.Optional.ofNullable(buildOutput()).orElse(item));
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        TemplateSpecFluent that = (TemplateSpecFluent) o;
        if (!java.util.Objects.equals(type, that.type))
            return false;

        if (!java.util.Objects.equals(parameters, that.parameters))
            return false;

        if (!java.util.Objects.equals(steps, that.steps))
            return false;

        if (!java.util.Objects.equals(output, that.output))
            return false;

        return true;
    }

    public int hashCode() {
        return java.util.Objects.hash(type, parameters, steps, output, super.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (type != null) {
            sb.append("type:");
            sb.append(type + ",");
        }
        if (parameters != null && !parameters.isEmpty()) {
            sb.append("parameters:");
            sb.append(parameters + ",");
        }
        if (steps != null && !steps.isEmpty()) {
            sb.append("steps:");
            sb.append(steps + ",");
        }
        if (output != null) {
            sb.append("output:");
            sb.append(output);
        }
        sb.append("}");
        return sb.toString();
    }

    public class ParametersNested<N> extends ParameterFluent<ParametersNested<N>> implements Nested<N> {
        ParametersNested(int index, Parameter item) {
            this.index = index;
            this.builder = new ParameterBuilder(this, item);
        }

        ParameterBuilder builder;
        int index;

        public N and() {
            return (N) TemplateSpecFluent.this.setToParameters(index, builder.build());
        }

        public N endParameter() {
            return and();
        }

    }

    public class StepsNested<N> extends StepFluent<StepsNested<N>> implements Nested<N> {
        StepsNested(int index, Step item) {
            this.index = index;
            this.builder = new StepBuilder(this, item);
        }

        StepBuilder builder;
        int index;

        public N and() {
            return (N) TemplateSpecFluent.this.setToSteps(index, builder.build());
        }

        public N endStep() {
            return and();
        }

    }

    public class OutputNested<N> extends OutputFluent<OutputNested<N>> implements Nested<N> {
        OutputNested(Output item) {
            this.builder = new OutputBuilder(this, item);
        }

        OutputBuilder builder;

        public N and() {
            return (N) TemplateSpecFluent.this.withOutput(builder.build());
        }

        public N endOutput() {
            return and();
        }

    }

}
