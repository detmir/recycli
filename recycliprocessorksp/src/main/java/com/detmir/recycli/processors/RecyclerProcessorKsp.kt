package com.detmir.recycli.processors

import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemView
import com.detmir.recycli.annotations.RecyclerItemViewHolder
import com.detmir.recycli.annotations.RecyclerItemViewHolderCreator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement

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

        logger.info("jksps KSP process")


        //STATES
//        val symbolsStates = resolver
//            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerItemState")
//
//        val symbolsStatesClass = symbolsStates
//            .filterIsInstance<KSClassDeclaration>()
        val stateSymbols = resolver
            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerItemState")
            .filterIsInstance<KSClassDeclaration>()

        val stateSymbolsIterator = stateSymbols.iterator()

        // VIEWS
        val viewSymbols = resolver
            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerItemView")
            .filterIsInstance<KSClassDeclaration>().iterator()

        val viewSymbolsIterator = viewSymbols.iterator()


        // VIEW HOLDERS
        val viewHolderSymbols = resolver
            .getSymbolsWithAnnotation("com.detmir.recycli.annotations.RecyclerItemViewHolder")
            .filterIsInstance<KSClassDeclaration>().iterator()

        val viewHolderSymbolsIterator = viewHolderSymbols.iterator()

        // Exit from the processor in case nothing is annotated with @RecyclerItemState, @RecyclerItemView, @RecyclerItemViewHolder
        if (
            !(stateSymbolsIterator.hasNext() || viewSymbolsIterator.hasNext() || viewHolderSymbolsIterator.hasNext())
        ) return emptyList()



        while (stateSymbolsIterator.hasNext()) {
            val stateClazz = stateSymbolsIterator.next()
            allElementsInvolved.add(stateClazz)
            getTopPackage(stateClazz)
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



        while (viewSymbolsIterator.hasNext()) {
            val viewClazz = viewSymbolsIterator.next()
            allElementsInvolved.add(viewClazz)
            fillViewProps(
                viewElement = viewClazz,
                stateToIndexMap = stateToIndexMap,
                stateToViewMap = stateToViewMap,
                type = Type.VIEW
            )
        }



        while (viewHolderSymbolsIterator.hasNext()) {
            val holderClazz = viewHolderSymbolsIterator.next()
            logger.info("jksps KSP iWrap.i=${iWrap.i} holderClazz=${holderClazz.qualifiedName?.asString()}")
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



        RecyclerFileProviderKsp.generateBinderClass(
            resolver = resolver,
            codeGenerator = codeGenerator,
            packageName = packageName.joinToString("."),
            completeMap = completeMap,
            allElementsInvolved = allElementsInvolved
        )
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
    }

    private fun getTopPackage(element: KSClassDeclaration) {

//        var enclosing: Element = element.enclosingElement
//
//        while (enclosing.kind != ElementKind.PACKAGE) {
//            enclosing = enclosing.enclosingElement
//        }
//        val enclosingPackage = enclosing.toString()
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
        logger.info("jksps KSP enclosingPackage=${enclosingPackage} newPackageName=${newPackageName}")
        packageName = newPackageName
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
                //KSDeclaration

//                logger.info("jksps KSP decls.qualifiedName?.asString()=${decls.qualifiedName?.asString()}")
//                logger.info("jksps KSP decls=${decls.toString()}")

                if (decls is KSClassDeclaration && decls.classKind == ClassKind.OBJECT) {
                    //logger.info("jksps KSP IS CLASS ${decls.toString()}")
                    //val declAsClass = decls as KSClassDeclaration
                    val declIterator = decls.getAllFunctions().iterator()
                    //logger.info("jksps KSP declAsClass.classKind.type ${declAsClass.classKind.type}")
                    //logger.info("jksps KSP declAsClass.classKind.name ${declAsClass.classKind.name}")
                    while (declIterator.hasNext() && viewCreatorClassName == null) {
                        val declFunction = declIterator.next()
                        //logger.info("jksps KSP declFunction ${declFunction.annotations}")
                        //logger.info("jksps KSP declFunction.qualifiedName ${declFunction.qualifiedName?.asString()}")
                        val isRecyclerItemViewHolderCreator =
                            declFunction.annotations.firstOrNull { ksAnnotation ->
                                //logger.info("jksps KSP ksAnnotation.shortName ${ksAnnotation.shortName.asString()}")
                                ksAnnotation.shortName.asString() == "RecyclerItemViewHolderCreator"
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

        logger.info("jksps KSP viewCreatorClassName ${viewCreatorClassName}")


        if (viewCreatorClassName == null) {
            logger.error("jksps KSP RecyclerItemCreator not found for ${viewElement.qualifiedName?.asString()} ")
            return
        }

        val binderFunctionsIterator = viewElement.getAllFunctions().iterator()
        while (binderFunctionsIterator.hasNext()) {
            val binderFunction = binderFunctionsIterator.next()

            val isRecyclerItemViewBinderAnnotation =
                binderFunction.annotations.firstOrNull { ksAnnotation ->
                    //logger.info("jksps KSP ksAnnotation ${ksAnnotation.shortName.asString()}")
                    ksAnnotation.shortName.asString() == "RecyclerItemStateBinder"
                }



            if (isRecyclerItemViewBinderAnnotation != null) {
                logger.info("jksps KSP binderFunction ${binderFunction.qualifiedName?.asString()}")
                if (binderFunction.parameters.size == 1) {
                    binderFunction.parameters.forEach { binderParameter ->
                        val currentStateClass: String? =
                            binderParameter.type.resolve().declaration.qualifiedName?.asString()
                        logger.info("jksps KSP currentStateClass $currentStateClass")
                        if (currentStateClass == null) {
                            logger.error("jksps KSP binder function must be one arhument function with state ${binderFunction.qualifiedName?.asString()} in ${viewElement.qualifiedName?.asString()}")
                        }
                        val viewElementqualifiedName = viewElement.qualifiedName?.asString()
                        if (stateToIndexMap.containsKey(currentStateClass) && viewElementqualifiedName != null) {
                            logger.info("jksps KSP PUT TO stateToViewMap currentStateClass $currentStateClass")
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
            //logger.info("jksps KSP isRecyclerItemViewBinder = $isRecyclerItemViewBinder declFunction=$")
        }

        var i = 0
        stateToViewMap.forEach { (t, viewPropsMap) ->
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
