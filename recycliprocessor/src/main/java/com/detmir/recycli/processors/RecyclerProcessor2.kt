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
internal class RecyclerProcessor2 : AbstractProcessor() {

    private var packageName: ArrayList<String> = ArrayList()

    override fun getSupportedAnnotationTypes(): Set<String?> {
        return setOf(
            RecyclerItemState::class.java.canonicalName,
            RecyclerBackedAdapter::class.java.canonicalName,
        )
    }

    override fun process(
        elementsSet: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        //ADAPTERS
        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerBackedAdapter::class.java
        )?.forEach { element ->
            ld("dasdasda2  RecyclerBackedAdapter ${element.toString()}")
        }

        roundEnvironment?.getElementsAnnotatedWith(
            RecyclerItemState::class.java
        )?.forEach { element ->
            ld("dasdasda2  RecyclerItemState ${element.toString()}")
        }

        return false

    }

    @Suppress("unused")
    private fun ld(string: String) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.NOTE,
            string + "\r"
        )
    }
}
