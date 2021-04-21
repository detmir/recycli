package com.detmir.recycli.adapters

data class ScrollKeeper(
    var pos: Int? = null,
    var offset: Int? = null
) {
    interface Provider {
        fun scrollKeeperFor(id: String): ScrollKeeper
    }

    class SimpleProvider : Provider {
        private val ids = hashMapOf<String, ScrollKeeper>()
        override fun scrollKeeperFor(id: String): ScrollKeeper {
            return ids.getOrPut(id, { ScrollKeeper() })
        }
    }
}
