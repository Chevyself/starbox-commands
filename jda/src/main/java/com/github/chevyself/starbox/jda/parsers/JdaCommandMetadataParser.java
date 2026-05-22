package com.github.chevyself.starbox.jda.parsers;

import com.github.chevyself.starbox.jda.annotations.CommandPermission;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import java.lang.reflect.AnnotatedElement;
import lombok.NonNull;

/**
 * Implementation of {@link CommandMetadataParser} for JDA. This parses the {@link
 * CommandPermission} and puts it in the metadata.
 */
public class JdaCommandMetadataParser implements CommandMetadataParser {

  @Override
  public @NonNull CommandMetadata parse(@NonNull AnnotatedElement element) {
    CommandMetadata metadata = new CommandMetadata();
    if (element.isAnnotationPresent(CommandPermission.class)) {
      metadata.put("permission", element.getAnnotation(CommandPermission.class).value());
    }
    return metadata;
  }

  @Override
  public void close() {}
}
