package com.detmir.recycli.processors

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

object RecyclerProcessorGlobal {

    var ii = 0

    val binders = mutableSetOf<String>()
}