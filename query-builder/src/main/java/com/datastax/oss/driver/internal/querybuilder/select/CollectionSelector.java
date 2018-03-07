/*
 * Copyright DataStax, Inc.
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
package com.datastax.oss.driver.internal.querybuilder.select;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.querybuilder.select.Selector;
import com.datastax.oss.driver.internal.querybuilder.Joiners;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.Objects;

public abstract class CollectionSelector implements Selector {

  private final Iterable<Selector> elementSelectors;
  private final String opening;
  private final String closing;
  private final CqlIdentifier alias;

  protected CollectionSelector(
      Iterable<Selector> elementSelectors, String opening, String closing, CqlIdentifier alias) {
    Preconditions.checkNotNull(elementSelectors);
    Preconditions.checkArgument(
        elementSelectors.iterator().hasNext(), "Must have at least one selector");
    checkNoAlias(elementSelectors);
    Preconditions.checkNotNull(opening);
    Preconditions.checkNotNull(closing);
    this.elementSelectors = elementSelectors;
    this.opening = opening;
    this.closing = closing;
    this.alias = alias;
  }

  @Override
  public String asCql(boolean pretty) {
    return opening + Joiners.joinSelectorsWithComma(elementSelectors, pretty) + closing;
  }

  public Iterable<Selector> getElementSelectors() {
    return elementSelectors;
  }

  @Override
  public CqlIdentifier getAlias() {
    return alias;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof CollectionSelector) {
      CollectionSelector that = (CollectionSelector) other;
      return Iterables.elementsEqual(this.elementSelectors, that.elementSelectors)
          && this.opening.equals(that.opening)
          && this.closing.equals(that.closing)
          && Objects.equals(this.alias, that.alias);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementSelectors, opening, closing, alias);
  }

  private static void checkNoAlias(Iterable<Selector> elementSelectors) {
    String offendingAliases = null;
    for (Selector selector : elementSelectors) {
      CqlIdentifier alias = selector.getAlias();
      if (alias != null) {
        if (offendingAliases == null) {
          offendingAliases = alias.asCql(true);
        } else {
          offendingAliases += ", " + alias.asCql(true);
        }
      }
    }
    if (offendingAliases != null) {
      throw new IllegalArgumentException(
          "Can't use aliases in selection list, offending aliases: " + offendingAliases);
    }
  }
}
