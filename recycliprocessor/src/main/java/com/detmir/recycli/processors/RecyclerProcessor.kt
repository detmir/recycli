package com.detmir.recycli.processors

import com.detmir.recycli.annotations.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(RecyclerProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@Suppress("unused")
internal class RecyclerProcessor : AbstractProcessor() {

    private var packageName: ArrayList<String> = ArrayList()

    override fun getSupportedAnnotationTypes(): Set<String?> {
        return setOf(
            RecyclerItemState::class.java.canonicalName,
            RecyclerItemStateBinder::class.java.canonicalName,
            RecyclerItemView::class.java.canonicalName,
            RecyclerItemViewHolder::class.java.canonicalName,
            RecyclerItemViewHolderCreator::class.java.canonicalName
        )
    }

    override fun process(
        elementsSet: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        val indexToStateMap = mutableMapOf<Int, String>()
        val stateToIndexMap = mutableMapOf<String, Int>()
        val stateToViewMap = mutableMapOf<String, MutableList<ViewProps>>()
        val stateSealedAliases = mutableMapOf<Int, Int>()
        val completeMap = mutableMapOf<String, ViewProps>()
        val iWrap = IWrap()
        val allElementsInvolved = mutableSetOf<Element>()

        //STATES
        ld("jksps process")
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemState::class.java
        )?.forEach { element ->
            allElementsInvolved.add(element)
            getTopPackage(element)
            indexToStateMap[iWrap.i] = element.toString()
            ld("jksps indexToStateMap i=${iWrap.i} element.toString()=${element.toString()}")
            stateToIndexMap[element.toString()] = iWrap.i
            stateSealedAliases[iWrap.i] = iWrap.i
            val topClassIndex = iWrap.i

            craftSealedClass(
                element = element,
                topClassIndex = topClassIndex,
                iWrap = iWrap,
                indexToStateMap = indexToStateMap,
                stateToIndexMap = stateToIndexMap,
                stateSealedAliases = stateSealedAliases,
                allElementsInvolved = allElementsInvolved
            )
            iWrap.i++
        }


        //VIEWS
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemView::class.java
        )?.forEach { viewElement ->
            allElementsInvolved.add(viewElement)
            fillViewProps(
                viewElement = viewElement,
                stateToIndexMap = stateToIndexMap,
                stateToViewMap = stateToViewMap,
                type = Type.VIEW
            )
        }

        //VIEW HOLDERS
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemViewHolder::class.java
        )?.forEach { viewElement ->
            allElementsInvolved.add(viewElement)
            fillViewProps(
                viewElement = viewElement,
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


        RecyclerFileProvider.generateBinderClass(
            processingEnv = processingEnv,
            packageName = packageName.joinToString("."),
            completeMap = completeMap,
            allElementsInvolved = allElementsInvolved,
            filer = processingEnv.filer
        )
        return true
    }

    private fun craftSealedClass(
        element: Element,
        topClassIndex: Int,
        iWrap: IWrap,
        indexToStateMap: MutableMap<Int, String>,
        stateToIndexMap: MutableMap<String, Int>,
        stateSealedAliases: MutableMap<Int, Int>,
        allElementsInvolved: MutableSet<Element>
    ) {
        element.enclosedElements.forEach { enclosedElement ->

            if (enclosedElement.enclosingElement == element && enclosedElement.kind == ElementKind.CLASS) {

                allElementsInvolved.add(element)
                iWrap.i++
                indexToStateMap[iWrap.i] = enclosedElement.toString()
                stateToIndexMap[enclosedElement.toString()] = iWrap.i
                stateSealedAliases[iWrap.i] = topClassIndex

                ld("jksps iWrap.i=${iWrap.i} element=$element enclosedElement=$enclosedElement")
                craftSealedClass(
                    enclosedElement,
                    topClassIndex,
                    iWrap,
                    indexToStateMap,
                    stateToIndexMap,
                    stateSealedAliases,
                    allElementsInvolved
                )
            }
        }
    }


    private fun getTopPackage(element: Element) {
        var enclosing: Element = element.enclosingElement
        while (enclosing.kind != ElementKind.PACKAGE) {
            enclosing = enclosing.enclosingElement
        }
        val enclosingPackage = enclosing.toString()
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


    private fun fillViewProps(
        viewElement: Element,
        stateToIndexMap: MutableMap<String, Int>,
        stateToViewMap: MutableMap<String, MutableList<ViewProps>>,
        type: Type
    ) {
        var viewCreatorClassName = ""
        if (type == Type.VIEW_HOLDER) {
            viewElement.enclosedElements.forEach { enclosedElementCreatorClass ->
                if (enclosedElementCreatorClass.enclosingElement == viewElement
                    && enclosedElementCreatorClass.kind == ElementKind.CLASS
                    && enclosedElementCreatorClass.simpleName.toString() == "Companion"
                ) {
                    enclosedElementCreatorClass.enclosedElements.forEach { enclosedElementCreatorFunc ->
                        if (enclosedElementCreatorFunc.enclosingElement == viewElement
                            && enclosedElementCreatorFunc.kind == ElementKind.METHOD ||
                            enclosedElementCreatorFunc.getAnnotation(RecyclerItemViewHolderCreator::class.java) != null
                        ) {
                            viewCreatorClassName =
                                "$enclosedElementCreatorClass.${enclosedElementCreatorFunc.simpleName}"
                        }
                    }
                }
            }
        } else {
            viewCreatorClassName = viewElement.toString()
        }

        viewElement.enclosedElements.forEach { enclosedElement ->
            if (enclosedElement.enclosingElement == viewElement
                && enclosedElement.kind == ElementKind.METHOD
                && enclosedElement.getAnnotation(RecyclerItemStateBinder::class.java) != null
            ) {
                val executableElement = enclosedElement as ExecutableElement
                if (executableElement.parameters.size == 1) {
                    executableElement.parameters.forEach { variableElement ->
                        val currentStateClass = variableElement.asType().toString()
                        if (stateToIndexMap.containsKey(currentStateClass)) {
                            val viewProps = ViewProps(
                                index = 0,
                                viewCreatorClassName = viewCreatorClassName,
                                binderFunctionName = enclosedElement.simpleName.toString(),
                                viewBinderClassName = viewElement.toString(),
                                type = type
                            )
                            val currentStateViewsList = stateToViewMap.getOrPut(
                                currentStateClass,
                                { mutableListOf() })
                            currentStateViewsList.add(viewProps)
                        }
                    }
                } else {
                    le("RecyclerStateBinder function must be one parameter function")
                }
            }
        }

        var i = 0
        stateToViewMap.forEach { t, viewPropsMap ->
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


    @Suppress("unused")
    private fun ld(string: String) {
        if (ALLOW_DEBUG_LOG) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                string + "\r"
            )
        }
    }

    @Suppress("unused")
    private fun le(string: String) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.ERROR,
            string + "\r"
        )

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
