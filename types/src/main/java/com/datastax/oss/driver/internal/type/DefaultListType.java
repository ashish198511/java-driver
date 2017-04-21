/*
 * Copyright (C) 2017-2017 DataStax Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.type;

import com.datastax.oss.driver.api.type.DataType;
import com.datastax.oss.driver.api.type.ListType;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DefaultListType implements ListType {

  private static final long serialVersionUID = 1;

  /** @serial */
  private final DataType elementType;
  /** @serial */
  private final boolean frozen;

  public DefaultListType(DataType elementType, boolean frozen) {
    Preconditions.checkNotNull(elementType);
    this.elementType = elementType;
    this.frozen = frozen;
  }

  @Override
  public DataType getElementType() {
    return elementType;
  }

  @Override
  public boolean isFrozen() {
    return frozen;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof ListType) {
      ListType that = (ListType) other;
      // frozen is not taken into account
      return this.elementType.equals(that.getElementType());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.elementType.hashCode();
  }

  @Override
  public String toString() {
    return "List(" + elementType + ", " + (frozen ? "" : "not ") + "frozen)";
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    Preconditions.checkNotNull(elementType);
  }
}
