package com.masabi.kotlin

import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.reflect.KParameter

class Param1Int_Builder {
    private var param1: Int? = null

    val values: MutableMap<String, Any?> = mutableMapOf()

    fun withParam1(param1: Int?): Param1Int_Builder {
        this.param1 = param1
        this.values["param1"] = param1
        return this
    }

    fun build(): Param1Int {
        val constructor = ::Param1Int
        val parametersByName = constructor.parameters.groupBy { it.name }.mapValues { it.value.first() }
        fillInMissingNullables(parametersByName)
        verifyNonNullArgumentsArePresent(parametersByName)
        verifyMandatoryArgumentsArePresent(parametersByName)
        return constructor.callBy(mapOf(*values.map { parametersByName.getValue(it.key) to values.get(it.key) }.toTypedArray()))
    }

    private fun verifyNonNullArgumentsArePresent(parametersByName: Map<String?, KParameter>) {
        parametersByName
                    .filter { !it.value.type.isMarkedNullable }
                    .filter { !it.value.isOptional }
                    .forEach { if (values.get(it.key) == null) throw IllegalStateException("'${it.key}' cannot be null") }
    }

    private fun verifyMandatoryArgumentsArePresent(parametersByName: Map<String?, KParameter>) {
        parametersByName
                   .filter { !it.value.isOptional }
                   .forEach { if (!values.containsKey(it.key)) throw IllegalStateException("'${it.key}' has no default value, you must set one") }
    }

    private fun fillInMissingNullables(parametersByName: Map<String?, KParameter>) {
        parametersByName
                    .filter { it.value.type.isMarkedNullable }
                    .filter { !it.value.isOptional }
                    .forEach { values[it.key!!] = null }
    }
}
