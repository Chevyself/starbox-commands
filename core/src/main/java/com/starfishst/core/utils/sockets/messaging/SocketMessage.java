package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.utils.Maps;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * A socket message is send between sockets. It contains data, which can be requested or just send
 */
public class SocketMessage {

  /** The data of the message */
  @NotNull protected final HashMap<String, String> data;

  /**
   * Create the message
   *
   * @param data the data to send in the message
   */
  public SocketMessage(@NotNull HashMap<String, String> data) {
    this.data = data;
    checkType();
  }

  /**
   * Checks if the data in the message contains the type, if it does not have a type this method
   * adds it automatically
   */
  private void checkType() {
    if (!this.data.containsKey("type")) {
      this.data.put("type", getType().toString());
    }
  }

  /**
   * Puts all the data in a single string that can be send
   *
   * @return the built data
   */
  @NotNull
  public String build() {
    return Maps.toString(getData());
  }

  /**
   * Get the data that this socket has
   *
   * @return the data
   */
  @NotNull
  public HashMap<String, String> getData() {
    return data;
  }

  /**
   * Get the type of the message
   *
   * @return the type
   */
  @NotNull
  public SocketMessageType getType() {
    return SocketMessageType.MESSAGE;
  }
}