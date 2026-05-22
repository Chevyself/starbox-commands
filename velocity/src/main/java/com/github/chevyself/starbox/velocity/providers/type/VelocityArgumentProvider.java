package com.github.chevyself.starbox.velocity.providers.type;

import com.github.chevyself.starbox.common.tab.SuggestionsArgumentProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;

/** An extension for providers made for velocity commands. */
public interface VelocityArgumentProvider<O>
    extends SuggestionsArgumentProvider<O, CommandContext> {}
