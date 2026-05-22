package com.github.chevyself.starbox.velocity.providers.type;

import com.github.chevyself.starbox.providers.StarboxExtraArgumentProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;

/**
 * An extension for velocity extra arguments.
 *
 * @param <O> the type of the object to provide
 */
public interface VelocityExtraArgumentProvider<O>
    extends StarboxExtraArgumentProvider<O, CommandContext> {}
