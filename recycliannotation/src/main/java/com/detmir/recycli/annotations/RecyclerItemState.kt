package com.detmir.recycli.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class RecyclerItem {
    interface Provider {

    }
}