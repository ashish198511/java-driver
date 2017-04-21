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

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.type.DataType;
import com.datastax.oss.driver.api.type.UserDefinedType;
import com.google.common.collect.ImmutableMap;

/**
 * Helper class to build {@link UserDefinedType} instances.
 *
 * <p>This is not part of the public API, because building user defined types manually can be
 * tricky: the fields must be defined in the exact same order as the database definition, otherwise
 * you will insert corrupt data in your database. If you decide to use this class anyway, make sure
 * that you define fields in the correct order, and that the database schema never changes.
 */
public class UserDefinedTypeBuilder {

  private final CqlIdentifier keyspaceName;
  private final CqlIdentifier typeName;
  private final ImmutableMap.Builder<CqlIdentifier, DataType> fieldTypesBuilder;

  public UserDefinedTypeBuilder(CqlIdentifier keyspaceName, CqlIdentifier typeName) {
    this.keyspaceName = keyspaceName;
    this.typeName = typeName;
    this.fieldTypesBuilder = ImmutableMap.builder();
  }

  /**
   * Adds a new field. The fields in the resulting type will be in the order of the calls to this
   * method.
   */
  public UserDefinedTypeBuilder withField(CqlIdentifier name, DataType dataType) {
    fieldTypesBuilder.put(name, dataType);
    return this;
  }

  public UserDefinedType build() {
    return new DefaultUserDefinedType(keyspaceName, typeName, fieldTypesBuilder.build());
  }
}
