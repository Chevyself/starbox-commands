package com.github.chevyself.starbox.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.NonNull;

/**
 * Utility class to find classes in a package.
 *
 * @param <T> the type of class to find.
 */
public final class ClassFinder<T> {

  /** The type of class to find. */
  private final Class<T> type;
  /** The package name to search in. */
  @NonNull private final String packageName;
  /** The path of the package. This is the package name but with '/' instead of '.'. */
  @NonNull private final String path;
  /** The classes found. */
  @NonNull private final List<Class<T>> classes;
  /** The predicate to check if the class is valid. */
  @NonNull private Predicate<Class<T>> predicate;
  /** Extra class loaders to check for classes. */
  @NonNull private final List<Supplier<ClassLoader>> extras;

  /** The class loader supplier to check for classes. */
  @NonNull private Supplier<ClassLoader> classLoaderSupplier;

  /** Whether to search in sub packages. */
  private boolean recursive;

  /**
   * Create the class finder.
   *
   * @param type the type of class to find.
   * @param packageName the package name to search in.
   */
  public ClassFinder(Class<T> type, @NonNull String packageName) {
    this.type = type;
    this.packageName = packageName;
    this.path = packageName.replace('.', '/');
    this.classes = new ArrayList<>();
    this.predicate = clazz -> true;
    this.extras = new ArrayList<>();
    this.classLoaderSupplier = () -> Thread.currentThread().getContextClassLoader();
    this.recursive = false;
  }

  /**
   * Create the class finder.
   *
   * @param packageName the package name to search in.
   */
  public ClassFinder(@NonNull String packageName) {
    this(null, packageName);
  }

  /**
   * Add a class loader to check for classes.
   *
   * @param loader the loader to check.
   * @return this class finder.
   */
  @NonNull
  public ClassFinder<T> checkIn(@NonNull ClassLoader loader) {
    this.extras.add(() -> loader);
    return this;
  }

  /**
   * Get a predicate that checks that a class has all the specified annotations.
   *
   * @param annotations the annotations to check.
   * @return the predicate.
   * @param <T> the type of class to find.
   */
  @SafeVarargs
  @NonNull
  public static <T> Predicate<Class<T>> checkForAllAnnotations(
      @NonNull Class<? extends Annotation>... annotations) {
    return clazz -> {
      for (Class<? extends Annotation> annotation : annotations) {
        if (!clazz.isAnnotationPresent(annotation)) {
          return false;
        }
      }
      return true;
    };
  }

  /**
   * Get a predicate that checks that a class has any of the specified annotations.
   *
   * @param annotations the annotations to check.
   * @return the predicate.
   * @param <T> the type of class to find.
   */
  @SafeVarargs
  @NonNull
  public static <T> Predicate<Class<T>> checkForAnyAnnotations(
      @NonNull Class<? extends Annotation>... annotations) {
    return clazz -> {
      for (Class<? extends Annotation> annotation : annotations) {
        if (clazz.isAnnotationPresent(annotation)) {
          return true;
        }
      }
      return false;
    };
  }

  /**
   * Set the predicate to check if the class is valid.
   *
   * @param predicate the predicate to check
   * @return this instance.
   */
  @NonNull
  public ClassFinder<T> setPredicate(@NonNull Predicate<Class<T>> predicate) {
    this.predicate = predicate;
    return this;
  }

  /**
   * Set whether to search in sub packages.
   *
   * @param recursive whether to search in sub packages.
   * @return this instance.
   */
  @NonNull
  public ClassFinder<T> setRecursive(boolean recursive) {
    this.recursive = recursive;
    return this;
  }

  /**
   * Find the classes.
   *
   * @return the classes found.
   */
  @NonNull
  public List<Class<T>> find() {
    if (this.classes.isEmpty()) {
      String classPath = System.getProperty("java.class.path");
      String[] split = classPath.split(File.pathSeparator);
      for (String entry : split) {
        this.checkEntry(entry);
      }
      // Supplied
      this.checkInLoader(this.classLoaderSupplier.get());
      // Extra
      this.extras.forEach(supplier -> this.checkInLoader(supplier.get()));
      return classes;
    }
    return this.classes;
  }

  private void checkInLoader(@NonNull ClassLoader loader) {
    if (!(loader instanceof URLClassLoader)) {
      return;
    }
    this.checkInLoader((URLClassLoader) loader);
  }

  private void checkInLoader(@NonNull URLClassLoader loader) {
    URL[] urls = loader.getURLs();
    for (URL url : urls) {
      if (url.getProtocol().equals("file")) {
        String jarPath = url.getPath();
        this.checkEntry(jarPath);
      }
    }
  }

  /**
   * Clear the classes found.
   *
   * @return this instance.
   */
  @NonNull
  public ClassFinder<T> clear() {
    this.classes.clear();
    return this;
  }

  private void checkEntry(@NonNull String entry) {
    if (!entry.endsWith(".jar")) {
      String packagePath = entry + "/" + this.path;
      File packageFile = new File(packagePath);
      if (packageFile.exists() && packageFile.isDirectory()) {
        File[] files = packageFile.listFiles();
        if (files == null) {
          return;
        }
        for (File file : files) {
          this.checkFile(file);
        }
      }
    } else {
      try (JarFile jarFile = new JarFile(entry)) {
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
          JarEntry jarEntry = enumeration.nextElement();
          String name = jarEntry.getName();
          if (!jarEntry.isDirectory()
              && name.startsWith(this.path.replace('.', '/'))
              && name.endsWith(".class")) {
            this.checkClass(name);
          }
        }
      } catch (IOException e) {
        // Ignored package does not exist
      }
    }
  }

  private void checkFile(@NonNull File file) {
    String fileName = file.getName();
    if (file.isFile() && fileName.endsWith(".class")) {
      this.checkClass(this.packageName + "." + fileName);
    } else if (this.recursive && file.isDirectory()) {
      String subPackageName = this.packageName + "." + fileName;
      this.classes.addAll(
          new ClassFinder<>(this.type, subPackageName)
              .setPredicate(this.predicate)
              .setRecursive(true)
              .setClassLoaderSupplier(this.classLoaderSupplier)
              .find());
    }
  }

  /**
   * Set the class loader supplier.
   *
   * @param classLoaderSupplier the class loader supplier.
   * @return this instance.
   */
  @NonNull
  public ClassFinder<T> setClassLoaderSupplier(@NonNull Supplier<ClassLoader> classLoaderSupplier) {
    this.classLoaderSupplier = classLoaderSupplier;
    return this;
  }

  private void checkClass(@NonNull String fileName) {
    String className = fileName.substring(0, fileName.length() - 6).replace('/', '.');
    try {
      Class<?> clazz = Class.forName(className);
      if (this.type != null && !this.type.isAssignableFrom(clazz)) {
        return;
      }
      //noinspection unchecked: we checked the type on the statement above.
      Class<T> clazzAsType = (Class<T>) clazz;
      if (!this.classes.contains(clazzAsType) && this.predicate.test(clazzAsType)) {
        this.classes.add(clazzAsType);
      }
    } catch (ClassNotFoundException e) {
      // Ignored
    }
  }
}
