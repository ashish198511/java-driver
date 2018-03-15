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
package com.datastax.oss.driver.internal.querybuilder.term;

import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.querybuilder.term.Term;

public class LiteralTerm<T> implements Term {

  private final T value;
  private final TypeCodec<T> codec;

  public LiteralTerm(T value, TypeCodec<T> codec) {
    this.value = value;
    this.codec = codec;
  }

  @Override
  public void appendTo(StringBuilder builder) {
    if (value == null) {
      builder.append("NULL");
    } else {
      TypeCodec<T> actualCodec = (codec == null) ? CodecRegistry.DEFAULT.codecFor(value) : codec;
      builder.append(actualCodec.format(value));
    }
  }

  public T getValue() {
    return value;
  }

  public TypeCodec<T> getCodec() {
    return codec;
  }
}
