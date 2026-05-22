package com.github.chevyself.starbox.util.objects;

import com.github.chevyself.starbox.arguments.SingleArgument;
import lombok.NonNull;

/**
 * This class represents an object which can be identified with a name and given a description. It
 * is currently used for {@link SingleArgument} and the commands in JDA
 */
public interface Mappable {

  /**
   * Get the name of the object.
   *
   * @return the name of the object as a string
   */
  @NonNull
  String getName();

  /**
   * Get the description of the object.
   *
   * @return the description of the object as a string
   */
  @NonNull
  String getDescription();
}
