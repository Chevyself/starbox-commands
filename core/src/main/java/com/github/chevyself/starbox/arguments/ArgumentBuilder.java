package com.github.chevyself.starbox.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

/**
 * Builder for {@link Argument}.
 *
 * @param <O> the type of the argument
 */
public class ArgumentBuilder<O> {

  @NonNull private final Class<O> clazz;
  @NonNull private String name;
  @NonNull private String description;
  @NonNull private List<String> suggestions;
  @NonNull private ArgumentBehaviour behaviour;
  private boolean required;
  private boolean extra;

  /**
   * Create a new argument builder.
   *
   * @param clazz the type of the argument
   */
  public ArgumentBuilder(@NonNull Class<O> clazz) {
    this.clazz = clazz;
    this.name = clazz.getSimpleName();
    this.description = "No description provided";
    this.suggestions = new ArrayList<>();
    this.behaviour = ArgumentBehaviour.NORMAL;
    this.required = false;
    this.extra = false;
  }

  /**
   * Set the name of the argument.
   *
   * @param name the name of the argument
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Set the description of the argument.
   *
   * @param description the description of the argument
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setDescription(@NonNull String description) {
    this.description = description;
    return this;
  }

  /**
   * Set the suggestions of the argument.
   *
   * @param suggestions the suggestions of the argument
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setSuggestions(@NonNull Collection<String> suggestions) {
    this.suggestions = new ArrayList<>(suggestions);
    return this;
  }

  /**
   * Set the suggestions of the argument.
   *
   * @param suggestions the suggestions of the argument
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setSuggestions(@NonNull String... suggestions) {
    this.suggestions = new ArrayList<>(Arrays.asList(suggestions));
    return this;
  }

  /**
   * Set the behaviour to obtain the argument.
   *
   * @param behaviour the behaviour to obtain the argument
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setBehaviour(@NonNull ArgumentBehaviour behaviour) {
    this.behaviour = behaviour;
    return this;
  }

  /**
   * Set whether the argument is required.
   *
   * @param required whether the argument is required
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setRequired(boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Set whether this argument is an {@link ExtraArgument}.
   *
   * @param extra whether this argument is an {@link ExtraArgument}
   * @return this builder
   */
  @NonNull
  public ArgumentBuilder<O> setExtra(boolean extra) {
    this.extra = extra;
    return this;
  }

  /**
   * Build the argument.
   *
   * @param index the index of the argument
   * @return the built argument
   */
  @NonNull
  public Argument<O> build(int index) {
    if (this.extra) {
      return new ExtraArgument<>(this.clazz);
    }
    return new SingleArgument<>(
        this.name,
        this.description,
        this.suggestions,
        this.behaviour,
        this.clazz,
        this.required,
        index);
  }
}
