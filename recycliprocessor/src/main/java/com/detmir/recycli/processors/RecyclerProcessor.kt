package com.detmir.recycli.processors

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.detmir.recycli.annotations.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(RecyclerProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@Suppress("unused")
class RecyclerProcessor : AbstractProcessor() {

    private var packageName: String? = null

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


        //STATES
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemState::class.java
        )?.forEach { element ->
            getTopPackageOnce(element)
            indexToStateMap[iWrap.i] = element.toString()
            stateToIndexMap[element.toString()] = iWrap.i
            stateSealedAliases[iWrap.i] = iWrap.i
            val topClassIndex = iWrap.i


            craftSealedClass(
                element = element,
                topClassIndex = topClassIndex,
                iWrap = iWrap,
                indexToStateMap = indexToStateMap,
                stateToIndexMap = stateToIndexMap,
                stateSealedAliases = stateSealedAliases
            )
            iWrap.i++
        }


        //VIEWS
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemView::class.java
        )?.forEach { viewElement ->
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





        generateBinderClass(
            completeMap = completeMap
        )
        return true
    }

    private fun craftSealedClass(
        element: Element,
        topClassIndex: Int,
        iWrap: IWrap,
        indexToStateMap: MutableMap<Int, String>,
        stateToIndexMap: MutableMap<String, Int>,
        stateSealedAliases: MutableMap<Int, Int>
    ) {
        element.enclosedElements.forEach { enclosedElement ->
            if (enclosedElement.enclosingElement == element && enclosedElement.kind == ElementKind.CLASS) {
                iWrap.i++
                indexToStateMap[iWrap.i] = enclosedElement.toString()
                stateToIndexMap[enclosedElement.toString()] = iWrap.i
                stateSealedAliases[iWrap.i] = topClassIndex

                craftSealedClass(
                    enclosedElement,
                    topClassIndex,
                    iWrap,
                    indexToStateMap,
                    stateToIndexMap,
                    stateSealedAliases
                )
            }
        }
    }


    private fun getTopPackageOnce(element: Element) {
        if (packageName == null) {
            var enclosing: Element = element.enclosingElement
            while (enclosing.kind != ElementKind.PACKAGE) {
                enclosing = enclosing.enclosingElement
            }
            val enclosingPackage = enclosing.toString()
            val arr = enclosingPackage.split(".")
            packageName = arr.dropLast(1).joinToString(".")
        }
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


    private fun generateBinderClass(completeMap: Map<String, ViewProps>) {
        if (completeMap.isNotEmpty()) {

            val sbStateToIndexMap = StringBuilder("")
            var r = 0
            completeMap.forEach {
                val state = it.key
                val viewProps = it.value
                if (r != 0) {
                    sbStateToIndexMap.append(",\n")
                }
                sbStateToIndexMap.append("\"$state\" to ${viewProps.index}")
                r++
            }


            val classMy = TypeSpec.classBuilder("RecyclerBinderImpl")
                .addSuperinterface(ClassName("com.detmir.recycli.adapters", "RecyclerBinder"))
                .addProperty(
                    PropertySpec
                        .builder(
                            "stateToIndexMap",
                            HashMap::class.asClassName()
                                .parameterizedBy(
                                    String::class.asClassName(),
                                    Int::class.asClassName()
                                )
                        )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("hashMapOf<String, Int>($sbStateToIndexMap)").build()
                )
                .addFunction(
                    getOnCreateView(
                        completeMap = completeMap
                    )
                )
                .addFunction(
                    getOnBindView(
                        stateToViewMap = completeMap

                    )
                )
                .addFunction(
                    getItemViewType()
                )
                .build()


            val generatedSourcesRoot: String =
                processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

            val file2 = File(generatedSourcesRoot).apply { mkdir() }

            val fileBuilder = FileSpec.builder(packageName ?: "", "RecyclerBinderImpl")
                .addImport("androidx.recyclerview.widget", "RecyclerView")
                .addImport("android.view", "View")
                .addImport("com.detmir.recycli.adapters", "RecyclerItem")
                .addType(classMy)

            fileBuilder.build().writeTo(file2)


        }
    }


    private fun getItemViewType(): FunSpec {
        return FunSpec.builder("getItemViewType")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("recyclerItemState", String()::class)
            .addCode(
                CodeBlock.of(
                    """
                    return stateToIndexMap[recyclerItemState] ?: -1
                    """
                )
            )
            .returns(Int::class)
            .build()
    }


    private fun getOnBindView(
        stateToViewMap: Map<String, ViewProps>
    ): FunSpec {
        val builder = FunSpec.builder("onBindViewHolder")
        builder.addModifiers(KModifier.OVERRIDE)
        builder.addParameter(
            "holder",
            ClassName("androidx.recyclerview.widget", "RecyclerView.ViewHolder")
        )
        builder.addParameter("position", Int::class)
        builder.addParameter("state", String::class)
        builder.addParameter("item", ClassName("com.detmir.recycli.adapters", "RecyclerItem"))


        builder.addCode(
            CodeBlock.of(
                """
                when (state) {
                """
            )
        )

        stateToViewMap.forEach { (state, viewProps) ->
            val stateClass = state.split("#")[0]
            when (viewProps.type) {
                Type.VIEW -> {
                    builder.addCode(
                        CodeBlock.of(
                            """
                    "%L" -> {
                        (holder.itemView as %L).%L(item as %L)
                    }
                    """,
                            state,
                            viewProps.viewBinderClassName,
                            viewProps.binderFunctionName,
                            stateClass
                        )
                    )
                }
                Type.VIEW_HOLDER -> {
                    builder.addCode(
                        CodeBlock.of(
                            """
                    "%L" -> {
                        (holder as %L).%L(item as %L)
                    }
                    """,
                            state,
                            viewProps.viewBinderClassName,
                            viewProps.binderFunctionName,
                            stateClass
                        )
                    )
                }
            }

        }

        builder.addCode(
            """
                    else -> {
                        
                    }
                }
                """
        )


        return builder.build()
    }


    private fun getOnCreateView(
        completeMap: Map<String, ViewProps>
    ): FunSpec {
        val builder = FunSpec.builder("onCreateViewHolder")
        builder.addModifiers(KModifier.OVERRIDE)
        builder.addParameter("parent", ClassName("android.view", "ViewGroup"))
        builder.addParameter("viewType", Int::class)
        builder.returns(
            ClassName(
                "androidx.recyclerview.widget",
                "RecyclerView.ViewHolder"
            )
        )

        builder.addCode(
            CodeBlock.of(
                """
                var v: View? = null
                var vh: RecyclerView.ViewHolder? = null
                when (viewType) {
                """
            )
        )


        val reversedMap = mutableMapOf<ViewProps, ArrayList<String>>()

        completeMap.forEach { state, viewProp ->
            val states = reversedMap.getOrPut(viewProp) {
                ArrayList()
            }

            states.add(state)
        }

        reversedMap.forEach { (viewProps, states) ->
            when (viewProps.type) {
                Type.VIEW_HOLDER -> {
                    builder.addCode(
                        CodeBlock.of(
                            """
                    %L -> {
                        vh = %L(parent.context)
                    }
                    """, viewProps.index, viewProps.viewCreatorClassName
                        )
                    )
                }
                Type.VIEW -> {
                    builder.addCode(
                        CodeBlock.of(
                            """
                    %L -> {
                        v = %L(parent.context)
                    }
                    """, viewProps.index, viewProps.viewCreatorClassName
                        )
                    )
                }
            }

        }

        builder.addCode(
            """
                    else -> throw Exception("Recyclii can't find view for a RecyclerItem")
                }      
                         return when {
                            vh != null -> vh
                            v != null -> object : RecyclerView.ViewHolder(v) {}
                            else -> object : RecyclerView.ViewHolder(View(parent.context)) {}
                         }
                
                """
        )

        return builder.build()
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
        const val ALLOW_DEBUG_LOG = false
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