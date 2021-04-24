package com.detmir.kkppt3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import com.detmir.recycli.adapters.*
import com.detmir.recycli.annotations.RecyclerItemState
import com.detmir.recycli.annotations.RecyclerItemStateBinder
import com.detmir.recycli.annotations.RecyclerItemViewHolder
import com.detmir.recycli.annotations.RecyclerItemViewHolderCreator
import com.detmir.shapes.circle.CircleItem
import com.detmir.shapes.line.LineItem
import com.detmir.shapes.square.SquareItem
import com.detmir.ui.avatar.AvatarItem
import com.detmir.ui.bottom.BottomLoading
import com.detmir.ui.button.ButtonItem
import com.detmir.ui.label.LabelItem
import com.detmir.ui.radio.RadioItem
import com.detmir.ui.test02.Test02Item
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var recyclerAdapter: RecyclerAdapter
    //lateinit var adapterInfinity: RecyclerAdapterInfinity
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecyclerAdapter.staticBinders = setOf(
            com.detmir.ui.RecyclerBinderImpl(),
            com.detmir.shapes.RecyclerBinderImpl(),
            com.detmir.RecyclerBinderImpl()
        )
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val case0000Button = findViewById<Button>(R.id.activity_main_0000)
        val case0100Button = findViewById<Button>(R.id.activity_main_0100)
        val case0101Button = findViewById<Button>(R.id.activity_main_0101)
        val case0200Button = findViewById<Button>(R.id.activity_main_0200)
        val case0300Button = findViewById<Button>(R.id.activity_main_0300)
        val case0301Button = findViewById<Button>(R.id.activity_main_0301)
        val case0400Button = findViewById<Button>(R.id.activity_main_0400)

        case0000Button.setOnClickListener {
            startActivity(Intent(this, Case0000Demo::class.java))
        }

        case0100Button.setOnClickListener {
            startActivity(Intent(this, Case0100SimpleActivity::class.java))
        }

        case0101Button.setOnClickListener {
            startActivity(Intent(this, Case0101SimpleVHActivity::class.java))
        }

        case0200Button.setOnClickListener {
            startActivity(Intent(this, Case0200ClickAndStateActivity::class.java))
        }

        case0300Button.setOnClickListener {
            startActivity(Intent(this, Case0300Sealed::class.java))
        }

        case0301Button.setOnClickListener {
            startActivity(Intent(this, Case0301SealedSeveralBinds::class.java))
        }

        case0400Button.setOnClickListener {
            startActivity(Intent(this, Case0400IntoView::class.java))
        }

    }


    //BINDING INTO VIEW
    fun bindingIntoView() {

//        val kClass: com.detmir.recycli.RecyclerBinder = Class.forName("com.detmir.ui.RecyclerBinder")
//            .newInstance() as com.detmir.recycli.RecyclerBinder
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(listOf(
            AvatarItem.State(
                view = AvatarItem.VIEWS.ROUNDED,
                id = "AvatarItem.AvatarItemRoundedCornersView",
                text = "me Avatar Rounded"
            ),
            AvatarItem.State(
                view = AvatarItem.VIEWS.SQAURED,
                id = "AvatarItem.AvatarItemSquaredCornersView",
                text = "me Avatar Squared"
            )
        ))
    }


    //BINDING SEALED VIEWS STYLE
    fun bindingSealedViewsStyle() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(listOf(
            LabelItem.State.Big(
                id = "LabelItem.State.Big",
                text = "me Big Label"
            ),
            LabelItem.State.Small(
                id = "LabelItem.State.Small",
                text = "me Big Small"
            )
        ))
    }


    //BINDING SEALED STYLE
    fun bindingSealedStyle() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            items = listOf(
                ButtonItem.State.Loading(
                    id = "ButtonItem.State.Loading",
                    text = "me Loading Button"
                ),
                ButtonItem.State.Error(
                    id = "ButtonItem.State.Error",
                    text = "me Error Button"
                ),
                ButtonItem.State.Colored.Green(
                    id = "ButtonItem.State.Colored.Green",
                    text = "me Colored.Green Button"
                ),
                ButtonItem.State.Colored.Orange(
                    id = "ButtonItem.State.Colored.Orange",
                    text = "me Colored.Orange Button"
                )
            )
        )
    }

    //SEVERAL MODULES
    fun bindingSeveralModules() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(listOf(
            SquareItem.State(
                id = "SquareItem.State",
                text = "me Square"
            ),
            CircleItem.State(
                id = "CircleItem.State",
                text = "me Circle"
            ),
            RadioItem.State(
                id = "RadioItem.State",
                text = "me Radio"
            )

        ))
    }


    //BINDING WHEN STYLE (CANONICAL)
    fun bindingWhenStyle() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.bindState(items = listOf(
            LineItem.State.Bezier(
                id = "LineItem.State.Bezier",
                text = "me Line Bezier"
            ),
            LineItem.State.Arc(
                id = "LineItem.State.Arc",
                text = "me Line Arc"
            )
        ))
    }


    //INFINITE
    val infiniteItems = mutableListOf<RecyclerItem>()
    var infiniteItemsErrorThrown = false
    fun bindingInfinite() {

        val callbacks = object : RecyclerAdapter.Callbacks {
            override fun loadRange(curPage: Int) {
                Log.d("mmmmd", "loadRange curPage=$curPage")
                Single.timer(2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        if (curPage == 4 && !infiniteItemsErrorThrown) {
                            infiniteItemsErrorThrown = true
                            throw Exception("error")
                        }
                        it
                    }
                    .doOnSubscribe {
                        val recyclerState = InfinityState(
                            requestState = InfinityState.Request.LOADING,
                            items = infiniteItems,
                            page = curPage,
                            endReached = curPage == 10
                        )
                        recyclerAdapter.bindState(recyclerState)
                    }
                    .doOnError {
                        val recyclerState = InfinityState(
                            requestState = InfinityState.Request.ERROR,
                            items = infiniteItems,
                            page = curPage,
                            endReached = curPage == 10
                        )
                        recyclerAdapter.bindState(recyclerState)
                    }
                    .doOnSuccess {
                        if (curPage == 0) infiniteItems.clear()

                        val newItems = (curPage * 20 until (curPage * 20 + 20)).map {
                            SquareItem.State(
                                id = "$it",
                                text = "its $it"
                            )
                        }

                        infiniteItems.addAll(newItems)


                        val recyclerState = InfinityState(
                            requestState = InfinityState.Request.IDLE,
                            items = infiniteItems,
                            page = curPage,
                            endReached = curPage == 10
                        )
                        recyclerAdapter.bindState(recyclerState)
                    }
                    .subscribe(
                        {},
                        {}
                    )
            }
        }

        recyclerAdapter = RecyclerAdapter(
            //binders = setOf(com.detmir.ui.RecyclerBinder(), com.detmir.shapes.RecyclerBinder()),
            infinityCallbacks = callbacks,
            bottomLoading = BottomLoading()
        )
        recyclerView.adapter = recyclerAdapter
        callbacks.loadRange(0)

    }


    //BINDING WHEN STYLE (CANONICAL)
    fun bindingWhenTestSealed() {
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(listOf(
            Test02Item.State.One(
                id = "Test02Item.State.One",
                text = "me Test02Item.State.One"
            ),
            Test02Item.State.Two.Two_1(
                id = "Test02Item.State.Two.Two_1",
                text = "me Test02Item.State.Two.Two_1"
            ),
            Test02Item.State.Two.Two_2(
                id = "Test02Item.State.Two.Two_2",
                text = "me Test02Item.State.Two.Two_2"
            ),
            Test02Item.State.Two.Two_3(
                id = "Test02Item.State.Two.Two_3",
                text = "me Test02Item.State.Two.Two_3"
            ),
            Test02Item.State.Three(
                id = "Test02Item.State.Three",
                text = "Test02Item.State.Three"
            )
        ))
    }


    //BINDING VIEW HOLDERS
    fun bindingVH() {
        recyclerAdapter = RecyclerAdapter()

        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(listOf(
            Footballer.Messi(
                id = "Messi111",
                name = "Messi1111",
                view = MessiVH::class.java
            ),
            Footballer.Messi(
                id = "Messi222",
                name = "Messi2222",
                view = MessiVH::class.java
            ),
            Footballer.Messi(
                id = "Messi333",
                name = "Messi333",
                view = MessiDMVH::class.java
            ),
            Footballer.Ronaldu(
                id = "Ronaldu",
                name = "Ronaldu"
            ),
            Footballer.Zidan
        ))
    }


    @RecyclerItemState
    sealed class Footballer: RecyclerItem {


        data class Messi(
            val id: String,
            val name: String,
            val view: Class<out Any>
        ): Footballer() {
            override fun provideId() = id
            override fun withView() = view
        }


        data class Ronaldu(
            val id: String,
            val name: String
        ): Footballer() {
            override fun provideId() = id
        }

        object Zidan: Footballer() {
            override fun provideId() = "zidan"
        }
    }



    @RecyclerItemViewHolder
    class MessiVH(view: View): RecyclerView.ViewHolder(view) {
        var tv: TextView
        init {
            tv = itemView as TextView
        }

        @RecyclerItemStateBinder
        fun bindView(messi: Footballer.Messi) {
            tv.text = messi.name
        }

        companion object {
            @RecyclerItemViewHolderCreator
            fun provideViewHolder(context: Context): MessiVH {
                val view = TextView(context)
                view.setBackgroundColor(Color.parseColor("#ff00ff00"))
                return MessiVH(view)
            }
        }
    }



    @RecyclerItemViewHolder
    class MessiDMVH(view: View): RecyclerView.ViewHolder(view) {
        var tv: TextView
        init {
            tv = itemView as TextView
        }

        @RecyclerItemStateBinder
        fun bindView(messi: Footballer.Messi) {
            tv.text = messi.name
        }

        companion object {
            @RecyclerItemViewHolderCreator
            fun provideViewHolder(context: Context): MessiDMVH {
                val view = TextView(context)
                view.setBackgroundColor(Color.parseColor("#ff000000"))
                return MessiDMVH(view)
            }
        }
    }


    @RecyclerItemViewHolder
    class RonalduVH(view: View): RecyclerView.ViewHolder(view) {
        var tv: TextView
        init {
            tv = itemView as TextView
        }

        @RecyclerItemStateBinder
        fun bindView(ronaldu: Footballer.Ronaldu) {
            tv.text = ronaldu.name
        }

        companion object {
            @RecyclerItemViewHolderCreator
            fun provideViewHolder(context: Context): RonalduVH {
                val view = TextView(context)
                view.setBackgroundColor(Color.parseColor("#ffff0000"))
                return RonalduVH(view)
            }
        }
    }



    @RecyclerItemViewHolder
    class TitleItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tv: TextView
        init {
            tv = itemView as TextView
        }

        @RecyclerItemStateBinder
        fun bindView(ronaldu: Footballer.Ronaldu) {
            tv.text = ronaldu.name
        }

        companion object {
            @RecyclerItemViewHolderCreator
            fun provideViewHolder(context: Context): TitleItemViewHolder {
                val view = TextView(context)
                view.setBackgroundColor(Color.parseColor("#ffff0000"))
                return TitleItemViewHolder(view)
            }
        }
    }

    @RecyclerItemViewHolder
    class ZidanVH(view: View): RecyclerView.ViewHolder(view) {
        var tv: TextView
        init {
            tv = itemView as TextView
        }

        @RecyclerItemStateBinder
        fun bindView(zidan: Footballer.Zidan) {
            tv.text = "zidan"
        }

        companion object {
            @RecyclerItemViewHolderCreator
            fun provideViewHolder(context: Context): ZidanVH {
                val view = TextView(context)
                view.setBackgroundColor(Color.parseColor("#ff0000FF"))
                return ZidanVH(view)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}