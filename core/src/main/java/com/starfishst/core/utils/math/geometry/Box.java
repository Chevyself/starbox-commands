package com.starfishst.core.utils.math.geometry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A box */
public class Box implements Shape {

  /** The id of the shape */
  @Nullable private final String id;
  /** The minimum position of the cube */
  @NotNull private Point minimum;
  /** The maximum position of the cube */
  @NotNull private Point maximum;

  /**
   * Create the box
   *
   * @param minimum the minimum position of the box
   * @param maximum the maximum position of the box
   * @param id the id of the cube
   */
  public Box(@NotNull Point minimum, @NotNull Point maximum, @Nullable String id) {
    this.minimum =
        new Point(
            Math.min(minimum.getX(), maximum.getX()),
            Math.min(minimum.getY(), maximum.getY()),
            Math.min(minimum.getZ(), maximum.getZ()));
    this.maximum =
        new Point(
            Math.max(minimum.getX(), maximum.getX()),
            Math.max(minimum.getY(), maximum.getY()),
            Math.max(minimum.getZ(), maximum.getZ()));
    this.id = id;
  }

  /**
   * Set the minimum position
   *
   * @param minimum the new minimum position
   */
  public void setMinimum(@NotNull Point minimum) {
    this.minimum = minimum;
  }

  /**
   * Set the maximum position
   *
   * @param maximum the maximum position
   */
  public void setMaximum(@NotNull Point maximum) {
    this.maximum = maximum;
  }

  /**
   * Get the height of the box
   *
   * @return the height
   */
  public double getHeight() {
    return getMaximum().getY() - getMinimum().getY();
  }

  /**
   * Get the width of the box
   *
   * @return the width
   */
  public double getWidth() {
    return getMaximum().getX() - getMinimum().getX();
  }

  /**
   * Get the length of the box
   *
   * @return the length
   */
  public double getLength() {
    return getMaximum().getZ() - getMinimum().getZ();
  }

  /**
   * Get the minimum point of the box
   *
   * @return the minimum point
   */
  @NotNull
  @Override
  public Point getMinimum() {
    return minimum;
  }

  /**
   * Get the maximum point of the box
   *
   * @return the maximum point
   */
  @NotNull
  @Override
  public Point getMaximum() {
    return maximum;
  }

  @Override
  public double getVolume() {
    return getWidth() * getLength() * getHeight();
  }

  @Nullable
  public String getId() {
    return this.id;
  }

  @NotNull
  @Override
  public List<Point> getPointsInside() {
    List<Point> points = new ArrayList<>();
    for (double x = getMinimum().getX(); x < getMaximum().getX(); x++) {
      for (double z = getMinimum().getZ(); z < getMaximum().getZ(); z++) {
        for (double y = getMinimum().getY(); y < getMaximum().getY(); y++) {
          points.add(new Point(x, y, z));
        }
      }
    }
    return points;
  }

  @Override
  public String toString() {
    return "Box{" + "minimum=" + minimum + ", maximum=" + maximum + ", id='" + id + '\'' + '}';
  }
}