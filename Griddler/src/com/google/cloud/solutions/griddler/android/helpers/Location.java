/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.solutions.griddler.android.helpers;

/**
 * Represents a location in a matrix. Used primarily with the BoundedGrid
 */
public class Location implements Comparable<Location> {
  private int row;
  private int col;

  /**
   * The turn angle for turning 90 degrees to the left.
   */
  public static final int LEFT = -90;
  /**
   * The turn angle for turning 90 degrees to the right.
   */
  public static final int RIGHT = 90;
  /**
   * The turn angle for turning 45 degrees to the left.
   */
  public static final int HALF_LEFT = -45;
  /**
   * The turn angle for turning 45 degrees to the right.
   */
  public static final int HALF_RIGHT = 45;
  /**
   * The turn angle for turning a full circle.
   */
  public static final int FULL_CIRCLE = 360;
  /**
   * The turn angle for turning a half circle.
   */
  public static final int HALF_CIRCLE = 180;
  /**
   * The turn angle for making no turn.
   */
  public static final int AHEAD = 0;

  /**
   * The compass direction for north.
   */
  public static final int NORTH = 0;
  /**
   * The compass direction for northeast.
   */
  public static final int NORTHEAST = 45;
  /**
   * The compass direction for east.
   */
  public static final int EAST = 90;
  /**
   * The compass direction for southeast.
   */
  public static final int SOUTHEAST = 135;
  /**
   * The compass direction for south.
   */
  public static final int SOUTH = 180;
  /**
   * The compass direction for southwest.
   */
  public static final int SOUTHWEST = 225;
  /**
   * The compass direction for west.
   */
  public static final int WEST = 270;
  /**
   * The compass direction for northwest.
   */
  public static final int NORTHWEST = 315;

  /**
   * Constructor
   *
   * @param r The row
   * @param c The column
   */
  public Location(int r, int c) {
    row = r;
    col = c;
  }

  /**
   * Get the row
   *
   * @return The row
   */
  public int getRow() {
    return row;
  }

  /**
   * Get the column
   *
   * @return The column
   */
  public int getCol() {
    return col;
  }

  /**
   * Returns the location of the adjacent tile based upon the direction
   *
   * @param direction The direction of the tile
   * @return {@link Location}
   */
  public Location getAdjacentLocation(int direction) {
    // reduce mod 360 and round to closest multiple of 45
    int adjustedDirection = (direction + HALF_RIGHT / 2) % FULL_CIRCLE;
    if (adjustedDirection < 0) {
      adjustedDirection += FULL_CIRCLE;
    }

    adjustedDirection = (adjustedDirection / HALF_RIGHT) * HALF_RIGHT;
    int dc = 0;
    int dr = 0;
    if (adjustedDirection == EAST) {
      dc = 1;
    } else if (adjustedDirection == SOUTHEAST) {
      dc = 1;
      dr = 1;
    } else if (adjustedDirection == SOUTH) {
      dr = 1;
    } else if (adjustedDirection == SOUTHWEST) {
      dc = -1;
      dr = 1;
    } else if (adjustedDirection == WEST) {
      dc = -1;
    } else if (adjustedDirection == NORTHWEST) {
      dc = -1;
      dr = -1;
    } else if (adjustedDirection == NORTH) {
      dr = -1;
    } else if (adjustedDirection == NORTHEAST) {
      dc = 1;
      dr = -1;
    }
    return new Location(getRow() + dr, getCol() + dc);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Location)) {
      return false;
    }

    Location otherLoc = (Location) other;
    return getRow() == otherLoc.getRow() && getCol() == otherLoc.getCol();
  }

  @Override
  public int hashCode() {
    return getRow() * 3737 + getCol();
  }

  @Override
  public int compareTo(Location other) {
    Location otherLoc = other;
    if (getRow() < otherLoc.getRow()) {
      return -1;
    }
    if (getRow() > otherLoc.getRow()) {
      return 1;
    }
    if (getCol() < otherLoc.getCol()) {
      return -1;
    }
    if (getCol() > otherLoc.getCol()) {
      return 1;
    }
    return 0;
  }

  @Override
  public String toString() {
    return "(" + getRow() + ", " + getCol() + ")";
  }
}
