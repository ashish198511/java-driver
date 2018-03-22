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
package com.datastax.oss.driver.api.querybuilder.delete;

import static com.datastax.oss.driver.api.querybuilder.Assertions.assertThat;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilderDsl.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilderDsl.deleteFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilderDsl.literal;

import org.junit.Test;

public class DeleteSelectorTest {

  @Test
  public void should_generate_column_deletion() {
    assertThat(deleteFrom("foo").column("v").whereColumn("k").eq(bindMarker()))
        .hasCql("DELETE v FROM foo WHERE k=?");
  }

  @Test
  public void should_generate_field_deletion() {
    assertThat(deleteFrom("foo").field("address", "street").whereColumn("k").eq(bindMarker()))
        .hasCql("DELETE address.street FROM foo WHERE k=?");
  }

  @Test
  public void should_generate_element_deletion() {
    assertThat(deleteFrom("foo").element("m", literal(1)).whereColumn("k").eq(bindMarker()))
        .hasCql("DELETE m[1] FROM foo WHERE k=?");
  }
}
