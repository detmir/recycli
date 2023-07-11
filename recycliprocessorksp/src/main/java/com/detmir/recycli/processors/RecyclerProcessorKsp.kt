package com.detmir.recycli.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

class RecyclerProcessorKsp(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val indexToStateMap = mutableMapOf<Int, String>()
        val stateToIndexMap = mutableMapOf<String, Int>()
        val stateToViewMap = mutableMapOf<String, MutableList<ViewProps>>()
        val stateSealedAliases = mutableMapOf<Int, Int>()
        val completeMap = mutableMapOf<String, ViewProps>()
        val iWrap = IWrap()
        val allElementsInvolved = mutableSetOf<KSClassDeclaration>()

        logger.info("jksps KSP process")


        val symbolsStates = resolver
            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerItemState")

        val symbolsStatesClass = symbolsStates
            .filterIsInstance<KSClassDeclaration>()
        val i = symbolsStatesClass.iterator()
        while (i.hasNext()) {
            val stateClazz = i.next()
            allElementsInvolved.add(stateClazz)
            //getTopPackage(element)
            val stateClazzName = stateClazz.qualifiedName?.asString()
            if (stateClazzName != null) {
                indexToStateMap[iWrap.i] = stateClazzName
                logger.info("jksps KSP iWrap.i=${iWrap.i} klass.qualifiedName=${stateClazz.qualifiedName?.asString()}")
                stateToIndexMap[stateClazzName] = iWrap.i
                stateSealedAliases[iWrap.i] = iWrap.i
                val topClassIndex = iWrap.i

                craftSealedClass(
                    logger = logger,
                    element = stateClazz,
                    topClassIndex = topClassIndex,
                    iWrap = iWrap,
                    indexToStateMap = indexToStateMap,
                    stateToIndexMap = stateToIndexMap,
                    stateSealedAliases = stateSealedAliases,
                    allElementsInvolved = allElementsInvolved
                )
                iWrap.i++

            }
        }

        return symbolsStates.toList()//emptyList()

    }



    private fun craftSealedClass(
        logger: KSPLogger,
        element: KSClassDeclaration,
        topClassIndex: Int,
        iWrap: IWrap,
        indexToStateMap: MutableMap<Int, String>,
        stateToIndexMap: MutableMap<String, Int>,
        stateSealedAliases: MutableMap<Int, Int>,
        allElementsInvolved: MutableSet<KSClassDeclaration>
    ) {

        element.getSealedSubclasses().forEach { enclosedElement ->
            val enclosedElementQualifiedName = enclosedElement.qualifiedName?.asString()
            if (enclosedElementQualifiedName != null) {

                allElementsInvolved.add(enclosedElement)
                iWrap.i++
                indexToStateMap[iWrap.i] = enclosedElementQualifiedName
                stateToIndexMap[enclosedElementQualifiedName] = iWrap.i
                stateSealedAliases[iWrap.i] = topClassIndex

                logger.info("jksps KSP iWrap.i=${iWrap.i} element=${element.qualifiedName?.asString()} enclosedElementQualifiedName=${enclosedElementQualifiedName}")

                craftSealedClass(
                    logger = logger,
                    element = enclosedElement,
                    topClassIndex = topClassIndex,
                    iWrap = iWrap,
                    indexToStateMap = indexToStateMap,
                    stateToIndexMap = stateToIndexMap,
                    stateSealedAliases = stateSealedAliases,
                    allElementsInvolved = allElementsInvolved
                )
            }
        }

//        element.enclosedElements.forEach { enclosedElement ->
//            if (enclosedElement.enclosingElement == element && enclosedElement.kind == ElementKind.CLASS) {
//                allElementsInvolved.add(element)
//                iWrap.i++
//                indexToStateMap[iWrap.i] = enclosedElement.toString()
//                stateToIndexMap[enclosedElement.toString()] = iWrap.i
//                stateSealedAliases[iWrap.i] = topClassIndex
//
//                craftSealedClass(
//                    enclosedElement,
//                    topClassIndex,
//                    iWrap,
//                    indexToStateMap,
//                    stateToIndexMap,
//                    stateSealedAliases,
//                    allElementsInvolved
//                )
//            }
//        }
    }



    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        const val ALLOW_DEBUG_LOG = true
    }

    enum class Type {
        VIEW, VIEW_HOLDER
    }

    data class ViewProps(
        var index: Int,
        var type: Type,
        val viewCreatorClassName: String,
        val viewBinderClassName: String,
        val binderFunctionName: String
    )

    data class IWrap(
        var i: Int = 0
    )
}
