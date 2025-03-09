/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.repository.jooq;

import org.jooq.*;

import java.util.Arrays;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class CommonTableExpressionForList<T> {

    final CommonTableExpression<? extends org.jooq.Record> cte;
    final Field<T> joinField;
    final SelectField<?> listField;

    public CommonTableExpressionForList(
            String cteName, String listFieldName, Table<? extends org.jooq.Record> parent, TableField<?, T> parentJoinField, TableField<?, T> parentOrderField) {

        final Name groupByTableName = name("group_by_table");

        final Name joinName = name(parentJoinField.getName());
        final Field<T> joinField = field(joinName, parentJoinField.getDataType());

        final Name groupByKeyName = groupByTableName.append(parentJoinField.getName());
        final Field<T> groupByKeyField = field(groupByKeyName, parentJoinField.getDataType());

        final Field<?> positionField = field(groupByTableName.append(parentOrderField.getUnqualifiedName()), parentOrderField.getDataType());

        final List<? extends Field<?>> groupByFields =
                Arrays.stream(parent.fields())
                        .filter(f -> !f.equals(parentJoinField))
                        .map(f -> field(f.getUnqualifiedName(), f.getDataType()))
                        .toList();

        final String CHANNEL_SECTION_SID_NAME = "channel_sid";

        // final Name CHANNEL_SECTION_LIST_SID_NAME = name(CHANNEL_SECTION_CTE_NAME, CHANNEL_SECTION_SID_NAME);
        final Name CHANNEL_SECTION_LIST_SID_NAME = name(cteName, CHANNEL_SECTION_SID_NAME);

        this.joinField = field(CHANNEL_SECTION_LIST_SID_NAME, parentJoinField.getDataType());

        // implementation note: we must use Table<>, not TableLike<>, for the 'as()' method to work.

        // ? extends ResultQuery<Record>
        this.cte = name(cteName)
                .fields(CHANNEL_SECTION_SID_NAME, listFieldName)
                .as(
                        selectDistinct(parentJoinField.as(joinName),
                                multiset(
                                        // selectFrom(CHANNEL_SECTION.as(GROUP_BY_TABLE_NAME))
                                        select(groupByFields).from(parent.as(groupByTableName))
                                                .where(groupByKeyField.eq(joinField))
                                                .groupBy(groupByFields)
                                                .orderBy(positionField))
                                        .as(listFieldName))
                                .from(parent)
                );

        // SelectField<?> CHANNEL_SECTION_LIST_FIELD = selectFrom(CHANNEL_SECTION).asMultiset().as(name(CHANNEL_SECTION_FIELD_NAME));
        this.listField = selectFrom(cte).asMultiset().as(name(cteName, listFieldName));
    }

    public CommonTableExpression<?> getCte() {
        return cte;
    }

    public Field<T> getJoinField() {
        return joinField;
    }

    public SelectField<?> getListField() {
        return listField;
    }
}
