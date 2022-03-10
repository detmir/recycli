package com.detmir.recycli.processors

import java.io.File
import javax.annotation.processing.ProcessingEnvironment

internal object RecyclerFileProvider {

    fun generateBinderClass(
        packageName: String?,
        processingEnv: ProcessingEnvironment,
        completeMap: Map<String, RecyclerProcessor.ViewProps>
    ) {

        if (packageName == null) return

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

            val generatedSourcesRoot: String =
                processingEnv.options[RecyclerProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

            val file = File(
                "$generatedSourcesRoot/${
                    packageName.replace(
                        ".",
                        "/"
                    )
                }/RecyclerBinderImpl.kt"
            ).also { file ->
                file.parentFile.mkdirs()
            }

            val sb = StringBuilder("")
            sb.append("package $packageName\n")
            getImports(sb = sb)
            sb.append("public class RecyclerBinderImpl : RecyclerBinder {\n")
            getStateToIndex(sb = sb, completeMap = completeMap)
            getOnCreateView(sb = sb, completeMap = completeMap)
            getOnBindView(sb = sb, completeMap = completeMap)
            getItemViewType(sb = sb)
            sb.append("}\n")

            file.writeText(sb.toString())


        }
    }

    private fun getItemViewType(sb: StringBuilder) {
        sb.append(
            """
            public override fun getItemViewType(recyclerItemState: String): Int {
                return stateToIndexMap[recyclerItemState] ?: -1
            }
            """.trimIndent()
        )
            .append("\n")
    }

    private fun getOnBindView(
        sb: StringBuilder,
        completeMap: Map<String, RecyclerProcessor.ViewProps>
    ) {
        sb.append(
            """
            public override fun onBindViewHolder(
                holder: RecyclerView.ViewHolder,
                position: Int,
                state: String,
                item: RecyclerItem
            ): Unit {
                when (state) {
            """.trimIndent()
        ).append("\n")



        completeMap.forEach { (state, viewProps) ->
            val stateClass = state.split("#")[0]
            when (viewProps.type) {
                RecyclerProcessor.Type.VIEW -> {
                    sb.append(
                        """
                        "$state" -> {
                            (holder.itemView as ${viewProps.viewBinderClassName}).${viewProps.binderFunctionName}(item as ${stateClass})
                        }
                        """
                    )
                }
                RecyclerProcessor.Type.VIEW_HOLDER -> {
                    sb.append(
                        """
                        "$state" -> {
                            (holder as ${viewProps.viewBinderClassName}).${viewProps.binderFunctionName}(item as $stateClass)
                        }
                        """
                    )
                }
            }

        }

        sb.append(
            """
            else -> {}
                }                   
            }
            """.trimIndent()
        )
    }

    private fun getImports(
        sb: StringBuilder
    ) {
        sb.append(
            """
            import android.view.View
            import android.view.ViewGroup
            import androidx.recyclerview.widget.RecyclerView
            import androidx.recyclerview.widget.RecyclerView.ViewHolder
            import com.detmir.recycli.adapters.RecyclerBinder
            import com.detmir.recycli.adapters.RecyclerItem
            import java.util.HashMap
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit           
            """.trimIndent()
        )
    }

    private fun getStateToIndex(
        sb: StringBuilder,
        completeMap: Map<String, RecyclerProcessor.ViewProps>
    ) {
        sb.append("public override val stateToIndexMap: java.util.HashMap<String, Int> = hashMapOf<String,Int>(\n")
        var r = 0
        completeMap.forEach {
            val state = it.key
            val viewProps = it.value
            if (r != 0) {
                sb.append(",\n")
            }
            sb.append("\"$state\" to ${viewProps.index}")
            r++
        }
        sb.append(")\n")
    }

    private fun getOnCreateView(
        completeMap: Map<String, RecyclerProcessor.ViewProps>,
        sb: StringBuilder
    ) {
        sb.append(
            """
            public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                RecyclerView.ViewHolder {
                var v: View? = null
                var vh: RecyclerView.ViewHolder? = null
                when (viewType) {
            """.trimIndent()
        )
        sb.append("\n")

        val reversedMap = mutableMapOf<RecyclerProcessor.ViewProps, ArrayList<String>>()

        completeMap.forEach { (state, viewProp) ->
            val states = reversedMap.getOrPut(viewProp) {
                ArrayList()
            }

            states.add(state)
        }

        reversedMap.forEach { (viewProps, _) ->
            when (viewProps.type) {
                RecyclerProcessor.Type.VIEW_HOLDER -> {
                    sb.append(
                        """
                        ${viewProps.index} -> {vh = ${viewProps.viewCreatorClassName}(parent.context)}
                        """.trimIndent()
                    ).append("\n")
                }
                RecyclerProcessor.Type.VIEW -> {
                    sb.append(
                        """
                        ${viewProps.index} -> {v = ${viewProps.viewCreatorClassName}(parent.context)}
                        """.trimIndent()
                    ).append("\n")
                }
            }
        }

        sb.append("else -> throw Exception(\"Recyclii can't find view for a RecyclerItem\")\n")
        sb.append("}\n")

        sb.append(
            """
            return when {
                vh != null -> vh
                v != null -> object : RecyclerView.ViewHolder(v) {}
                else -> object : RecyclerView.ViewHolder(View(parent.context)) {}
                }
            }
            """.trimIndent()
        )
        sb.append("\n")

    }

}
