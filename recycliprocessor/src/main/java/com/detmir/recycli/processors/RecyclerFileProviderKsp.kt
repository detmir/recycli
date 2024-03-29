package com.detmir.recycli.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import java.io.OutputStream

internal object RecyclerFileProviderKsp {

    operator fun OutputStream.plusAssign(str: String) {
        this.write(str.toByteArray())
    }

    fun generateBinderClass(
        filesInvolved: List<KSFile>,
        packageName: String?,
        codeGenerator: CodeGenerator,
        completeMap: Map<String, RecyclerProcessorKsp.ViewProps>,
    ) {
        if (packageName == null) return

        if (completeMap.isNotEmpty()) {

            val packageNameCameled = packageName.replace(".", "_")
            val assetsFile = codeGenerator.createNewFileByPath(Dependencies(false), "assets/recycli/$packageNameCameled", "")
            assetsFile.flush()
            assetsFile.close()

            val file: OutputStream = codeGenerator.createNewFile(
                // Make sure to associate the generated file with sources to keep/maintain it across incremental builds.
                // Learn more about incremental processing in KSP from the official docs:
                // https://kotlinlang.org/docs/ksp-incremental.html
                dependencies = Dependencies(true, *filesInvolved.toTypedArray()),
                packageName = packageName,
                fileName = "RecyclerBinderImpl"
            )


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

            val sb = StringBuilder("")
            sb.append("package $packageName\n")
            getImports(sb = sb)
            sb.append("\n@Keep")
            sb.append("\n@RecyclerBinderAdapter")
            sb.append("\npublic class RecyclerBinderImpl : RecyclerBinder {\n")
            getStateToIndex(sb = sb, completeMap = completeMap)
            getOnCreateView(sb = sb, completeMap = completeMap)
            getOnBindView(sb = sb, completeMap = completeMap)
            getItemViewType(sb = sb)
            sb.append("}\n")


            file += sb.toString()
            file.flush()
            file.close()
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
        completeMap: Map<String, RecyclerProcessorKsp.ViewProps>
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
                RecyclerProcessorKsp.Type.VIEW -> {
                    sb.append(
                        """
                        "$state" -> {
                            (holder.itemView as ${viewProps.viewBinderClassName}).${viewProps.binderFunctionName}(item as ${stateClass})
                        }
                        """
                    )
                }
                RecyclerProcessorKsp.Type.VIEW_HOLDER -> {
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
            import com.detmir.recycli.annotations.RecyclerBinderAdapter
            import java.util.HashMap
            import androidx.annotation.Keep
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit           
            """.trimIndent()
        )
    }

    private fun getStateToIndex(
        sb: StringBuilder,
        completeMap: Map<String, RecyclerProcessorKsp.ViewProps>
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
        completeMap: Map<String, RecyclerProcessorKsp.ViewProps>,
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

        val reversedMap = mutableMapOf<RecyclerProcessorKsp.ViewProps, ArrayList<String>>()

        completeMap.forEach { (state, viewProp) ->
            val states = reversedMap.getOrPut(viewProp) {
                ArrayList()
            }

            states.add(state)
        }

        reversedMap.forEach { (viewProps, _) ->
            when (viewProps.type) {
                RecyclerProcessorKsp.Type.VIEW_HOLDER -> {
                    sb.append(
                        """
                        ${viewProps.index} -> {vh = ${viewProps.viewCreatorClassName}(parent.context)}
                        """.trimIndent()
                    ).append("\n")
                }
                RecyclerProcessorKsp.Type.VIEW -> {
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
