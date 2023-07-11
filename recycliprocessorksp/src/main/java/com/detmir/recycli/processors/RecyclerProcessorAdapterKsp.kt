package com.detmir.recycli.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class RecyclerProcessorAdapterKsp(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        logger.info("jksps KSP process ADAPTER")

        val recyclerBinderAdapters = resolver
            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerBinderAdapter")
            .filterIsInstance<KSClassDeclaration>().iterator()

        recyclerBinderAdapters.forEach {
            logger.info("jksps KSP recyclerBinderAdapters=${it.qualifiedName?.asString()}")
        }
        return emptyList()
    }
}
