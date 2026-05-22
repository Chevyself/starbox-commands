package com.github.chevyself.starbox.arguments;

/** This is the behaviour of the argument. */
public enum ArgumentBehaviour {
  /**
   * The argument can be represented as a single or many words. In case that this needs more than
   * one word it requires quotation marks:
   */
  NORMAL,
  /**
   * This is an argument that accepts all the strings that come after its position.
   *
   * <p>Example: -numbers [number]... [another number] Usage: -numbers 1 2 3 4 5 6 7 8 9
   *
   * <ul>
   *   <li>number: 1 2 3 4 5 6 7 8 9
   *   <li>another number: 2
   * </ul>
   *
   * This can be more useful at the end of the command
   */
  CONTINUOUS;
}
