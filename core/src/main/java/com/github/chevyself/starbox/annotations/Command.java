package com.github.chevyself.starbox.annotations;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.util.Strings;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

/** This is used to represent a class or method as a command. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Command {

  /**
   * Get the aliases of the command.
   *
   * @return the aliases of the command
   */
  String[] aliases();

  /**
   * Get the description of the command.
   *
   * @return the description of the command
   */
  String description() default "";

  /**
   * Get the usage of the command.
   *
   * @return the usage of the command
   */
  String usage() default "";

  /**
   * Get the flags of the command.
   *
   * @return the flags of the command
   */
  Flag[] flags() default {};

  /**
   * Get the global middlewares to exclude from the command.
   *
   * @return the global middlewares to exclude from the command
   */
  @NonNull
  Class<? extends Middleware<?>>[] exclude() default {};

  /**
   * Get the middlewares to include in the command.
   *
   * @return the middlewares to include in the command
   */
  @NonNull
  Class<? extends Middleware<?>>[] include() default {};

  /** Utility class to help the parsing of the {@link Command} annotation. */
  class Supplier {

    /**
     * Get the usage.
     *
     * @param element the annotated element
     * @param arguments the list of arguments
     * @param aliases the aliases of the command
     * @return the usage
     */
    @NonNull
    public static String getUsage(
        @NonNull AnnotatedElement element,
        @NonNull List<Argument<?>> arguments,
        @NonNull String... aliases) {
      String usage = null;
      if (element.isAnnotationPresent(Command.class)) {
        usage = element.getAnnotation(Command.class).usage();
      }
      if (usage == null || usage.isEmpty()) {
        usage = "/" + Strings.buildUsageAliases(aliases) + " " + Argument.generateUsage(arguments);
      }
      return usage;
    }

    /**
     * Get the usage. This will not generate arguments.
     *
     * @param element the annotated element
     * @param aliases the aliases of the command
     * @return the usage
     */
    @NonNull
    public static String getUsage(@NonNull AnnotatedElement element, @NonNull String... aliases) {
      return Supplier.getUsage(element, new ArrayList<>(), aliases);
    }

    /**
     * Get the description of the command. If it is not present, it will return "No description
     * provided".
     *
     * @param element the annotated element
     * @return the description of the command
     */
    @NonNull
    public static String getDescription(@NonNull AnnotatedElement element) {
      String description = null;
      if (element.isAnnotationPresent(Command.class)) {
        description = element.getAnnotation(Command.class).description();
      }
      return description == null || description.isEmpty() ? "No description provided" : description;
    }
  }
}
