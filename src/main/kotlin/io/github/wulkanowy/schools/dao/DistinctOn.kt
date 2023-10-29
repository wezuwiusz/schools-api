package io.github.wulkanowy.schools.dao

import org.jetbrains.exposed.sql.BooleanColumnType
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.QueryBuilder

fun customDistinctOn(vararg expressions: Expression<*>): CustomFunction<Boolean?> = customBooleanFunction(
    functionName = "DISTINCT ON",
    postfix = " TRUE",
    params = expressions
)

fun customBooleanFunction(
    functionName: String, postfix: String = "", vararg params: Expression<*>
): CustomFunction<Boolean?> =
    object : CustomFunction<Boolean?>(functionName, BooleanColumnType(), *params) {
        override fun toQueryBuilder(queryBuilder: QueryBuilder) {
            super.toQueryBuilder(queryBuilder)
            if (postfix.isNotEmpty()) {
                queryBuilder.append(postfix)
            }
        }
    }
