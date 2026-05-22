package com.github.chevyself.starbox.parsers;

import com.github.chevyself.starbox.metadata.CommandMetadata;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

/**
 * Parses the command metadata from an annotated member. This is used for custom middlewares that
 * might require to add some extra piece of information to the command. This can be implemented by
 * adding a custom annotation and getting it from the {@link AnnotatedElement} and add it to the
 * {@link CommandMetadata} using {@link CommandMetadata#put(String, Object)}
 */
public interface CommandMetadataParser {

  /**
   * Parse the metadata from an annotated member.
   *
   * @param element the annotated member
   * @return the command metadata
   */
  @NonNull
  CommandMetadata parse(@NonNull AnnotatedElement element);

  /** Close the parser. */
  void close();
}
