package com.detmir.recycli.processors

import com.detmir.recycli.annotations.RecyclerItemState
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import com.detmir.recycli.annotations.RecyclerItemViewHolder
import com.detmir.recycli.annotations.RecyclerItemViewHolderCreator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class RecyclerProcessorKsp(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private var packageName: ArrayList<String> = ArrayList()

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val indexToStateMap = mutableMapOf<Int, String>()
        val stateToIndexMap = mutableMapOf<String, Int>()
        val stateToViewMap = mutableMapOf<String, MutableList<ViewProps>>()
        val stateSealedAliases = mutableMapOf<Int, Int>()
        val completeMap = mutableMapOf<String, ViewProps>()
        val iWrap = IWrap()
        val allElementsInvolved = mutableSetOf<KSClassDeclaration>()
        val filesInvolved = mutableListOf<KSFile>()

        logRelease("Recycli start processing", logger)

        // STATE CLASSES
        val stateSymbols = resolver
            .getSymbolsWithAnnotation(RecyclerItemState::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()


        val stateSymbolsIterator = stateSymbols.iterator()

        // VIEWS CLASSES
        val viewSymbols = resolver
            .getSymbolsWithAnnotation(RecyclerItemView::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>().iterator()

        val viewSymbolsIterator = viewSymbols.iterator()


        // VIEWHOLDER CLASSES
        val viewHolderSymbols = resolver
            .getSymbolsWithAnnotation(RecyclerItemViewHolder::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>().iterator()

        val viewHolderSymbolsIterator = viewHolderSymbols.iterator()

        // Exit from the processor in case nothing is annotated with @RecyclerItemState, @RecyclerItemView, @RecyclerItemViewHolder
        if (
            !(stateSymbolsIterator.hasNext() || viewSymbolsIterator.hasNext() || viewHolderSymbolsIterator.hasNext())
        )  {
            logRelease("Recycli end processing, nothing found", logger)
            return emptyList()
        }



        // PROCESS STATE CLASSES
        while (stateSymbolsIterator.hasNext()) {
            val stateClazz = stateSymbolsIterator.next()
            allElementsInvolved.add(stateClazz)
            stateClazz.containingFile?.let(filesInvolved::add)
            fillStates(
                stateElement = stateClazz,
                stateToIndexMap = stateToIndexMap,
                indexToStateMap = indexToStateMap,
                iWrap = iWrap,
                stateSealedAliases = stateSealedAliases,
                allElementsInvolved = allElementsInvolved,
            )
        }


        // PROCESS VIEW CLASSES
        while (viewSymbolsIterator.hasNext()) {
            val viewClazz = viewSymbolsIterator.next()
            viewClazz.containingFile?.let(filesInvolved::add)
            allElementsInvolved.add(viewClazz)
            fillViewProps(
                viewElement = viewClazz,
                stateToIndexMap = stateToIndexMap,
                stateToViewMap = stateToViewMap,
                type = Type.VIEW
            )
        }


        // PROCESS VIEWHOLDER CLASSES
        while (viewHolderSymbolsIterator.hasNext()) {
            val holderClazz = viewHolderSymbolsIterator.next()
            holderClazz.containingFile?.let(filesInvolved::add)
            allElementsInvolved.add(holderClazz)
            fillViewProps(
                viewElement = holderClazz,
                stateToIndexMap = stateToIndexMap,
                stateToViewMap = stateToViewMap,
                type = Type.VIEW_HOLDER
            )
        }


        //HANDLE SEALED CLASSES
        fillWithSealedClasses(
            stateToIndexMap = stateToIndexMap,
            indexToStateMap = indexToStateMap,
            stateToViewMap = stateToViewMap,
            stateSealedAliases = stateSealedAliases,
            completeMap = completeMap
        )

        // CREATE RecyclerBinderImpl
        RecyclerFileProviderKsp.generateBinderClass(
            filesInvolved = filesInvolved,
            codeGenerator = codeGenerator,
            packageName = packageName.joinToString("."),
            completeMap = completeMap,
        )

        logRelease("Recycli end processing, ${filesInvolved.size} files found", logger)

        // Don't need any multi round processing
        return emptyList()
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
    }

    private fun getTopPackage(element: KSClassDeclaration) {

        val enclosingPackage = element.packageName.asString()
        val arr = enclosingPackage.split(".")

        if (packageName.isEmpty()) {
            packageName = ArrayList(arr)
        }
        val newPackageName = ArrayList<String>()
        arr.forEachIndexed { index, s ->
            val curAtIndex = packageName.getOrNull(index)
            if (curAtIndex == s) {
                newPackageName.add(s)
            }
        }
        packageName = newPackageName
    }


    private fun fillStates(
        stateElement: KSClassDeclaration,
        stateToIndexMap: MutableMap<String, Int>,
        indexToStateMap: MutableMap<Int, String>,
        iWrap: IWrap,
        stateSealedAliases: MutableMap<Int, Int>,
        allElementsInvolved: MutableSet<KSClassDeclaration>,
    ) {
        getTopPackage(stateElement)
        val stateClazzName = stateElement.qualifiedName?.asString()
        if (stateClazzName != null) {

            indexToStateMap[iWrap.i] = stateClazzName
            stateToIndexMap[stateClazzName] = iWrap.i
            stateSealedAliases[iWrap.i] = iWrap.i
            val topClassIndex = iWrap.i

            craftSealedClass(
                logger = logger,
                element = stateElement,
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

    private fun fillViewProps(
        viewElement: KSClassDeclaration,
        stateToIndexMap: MutableMap<String, Int>,
        stateToViewMap: MutableMap<String, MutableList<ViewProps>>,
        type: Type
    ) {
        var viewCreatorClassName: String? = null

        if (type == Type.VIEW_HOLDER) {
            val decl = viewElement.declarations.iterator()
            while (decl.hasNext() && viewCreatorClassName == null) {
                val decls = decl.next()
                if (decls is KSClassDeclaration && decls.classKind == ClassKind.OBJECT) {
                    val declIterator = decls.getAllFunctions().iterator()
                    while (declIterator.hasNext() && viewCreatorClassName == null) {
                        val declFunction = declIterator.next()
                        val isRecyclerItemViewHolderCreator =
                            declFunction.annotations.firstOrNull { ksAnnotation ->
                                ksAnnotation.shortName.asString() == RecyclerItemViewHolderCreator::class.java.simpleName //"RecyclerItemViewHolderCreator"
                            }
                        if (isRecyclerItemViewHolderCreator != null) {
                            viewCreatorClassName = declFunction.qualifiedName?.asString()
                        }
                    }
                }
            }
        } else {
            viewCreatorClassName = viewElement.qualifiedName?.asString()
        }


        if (viewCreatorClassName == null) {
            logger.error("jksps KSP RecyclerItemCreator not found for ${viewElement.qualifiedName?.asString()} ")
            return
        }

        val binderFunctionsIterator = viewElement.getAllFunctions().iterator()
        while (binderFunctionsIterator.hasNext()) {
            val binderFunction = binderFunctionsIterator.next()

            val isRecyclerItemViewBinderAnnotation =
                binderFunction.annotations.firstOrNull { ksAnnotation ->
                    ksAnnotation.shortName.asString() == RecyclerItemStateBinder::class.java.simpleName //"RecyclerItemStateBinder"
                }



            if (isRecyclerItemViewBinderAnnotation != null) {
                if (binderFunction.parameters.size == 1) {
                    binderFunction.parameters.forEach { binderParameter ->
                        val currentStateClass: String? =
                            binderParameter.type.resolve().declaration.qualifiedName?.asString()
                        if (currentStateClass == null) {
                            logger.error("jksps KSP binder function must be one arhument function with state ${binderFunction.qualifiedName?.asString()} in ${viewElement.qualifiedName?.asString()}")
                        }
                        val viewElementqualifiedName = viewElement.qualifiedName?.asString()
                        if (stateToIndexMap.containsKey(currentStateClass) && viewElementqualifiedName != null) {
                            val viewProps = ViewProps(
                                index = 0,
                                viewCreatorClassName = viewCreatorClassName,
                                binderFunctionName = binderFunction.simpleName.asString(),
                                viewBinderClassName = viewElementqualifiedName,
                                type = type
                            )
                            if (currentStateClass != null) {
                                val currentStateViewsList = stateToViewMap.getOrPut(
                                    currentStateClass,
                                    { mutableListOf() })
                                currentStateViewsList.add(viewProps)
                            }
                        }
                    }
                } else {
                    logger.error("jksps KSP RecyclerStateBinder function must be one parameter function")
                }

            }
        }

        var i = 0
        stateToViewMap.forEach { (_, viewPropsMap) ->
            viewPropsMap.forEach { viewProps ->
                viewProps.index = i
                i++
            }
        }
    }


    private fun fillWithSealedClasses(
        stateToIndexMap: MutableMap<String, Int>,
        stateToViewMap: MutableMap<String, MutableList<ViewProps>>,
        stateSealedAliases: MutableMap<Int, Int>,
        indexToStateMap: MutableMap<Int, String>,
        completeMap: MutableMap<String, ViewProps>
    ) {
        //SEALED STATES VIEWS
        val addSealed = mutableMapOf<String, MutableList<ViewProps>>()
        stateToViewMap.forEach { stateToViewMapEntry ->
            val currentStateClass = stateToViewMapEntry.key
            val currentStateClassIndex = stateToIndexMap[currentStateClass]
            stateSealedAliases.filter { stateSealedAliasesEntry ->
                stateSealedAliasesEntry.value == currentStateClassIndex && currentStateClassIndex != stateSealedAliasesEntry.key
            }.forEach {
                val sealedStateClass = indexToStateMap[it.key]
                if (!stateToViewMap.contains(sealedStateClass)) {
                    val currentViewProps = stateToViewMap[currentStateClass]
                    if (sealedStateClass != null && currentViewProps != null) {
                        addSealed[sealedStateClass] = currentViewProps
                    }
                }
            }
        }

        var completeIndex = 0

        stateToViewMap.putAll(addSealed)

        stateToViewMap.forEach {
            val state = it.key

            it.value.forEach { viewProps ->
                if (!completeMap.containsKey("$state#default")) {
                    completeMap.putIfAbsent(
                        "$state#default",
                        viewProps
                    )
                    completeIndex++
                }

                completeMap[state + "#" + viewProps.viewBinderClassName] = viewProps
                completeIndex++
            }
        }
    }

    fun logRelease(message: String, logger: KSPLogger) {
        if (ALLOW_RELEASE_LOG) {
            logger.info(message)
        }
    }

    fun logDebug(message: String, logger: KSPLogger) {
        if (ALLOW_DEBUG_LOG) {
            logger.info(message)
        }
    }

    companion object {
        const val ALLOW_DEBUG_LOG = false
        const val ALLOW_RELEASE_LOG = true
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
