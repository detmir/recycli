![recycli_logo](https://user-images.githubusercontent.com/1109620/115422301-7405b780-a205-11eb-9372-1411ff17168d.png)

Recycli is a Kotlin library for Android RecyclerView that simplifies complex multiple view types lists creation. With DiffUtils inside, annotation based adapter generator and MVI pattern as philosophy it provides both simple and powerful tool for fast RecyclerView based screens development.

![ezgif-6-e9d7bd416187](https://user-images.githubusercontent.com/1109620/115579256-a7f8df80-a2ce-11eb-9bc2-ac79d3905b89.gif)

## Table of Contents  
[Installation](#installation)  
[First steps](#first_steps)  
[License](#license)  

<a name="installation"/>

## Installation
Add Maven Central to you repositories at project or module level build.gradle:

```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```
and add Kapt plugin and Recycli dependencies to your build.gradle at module level:
```gradle
plugins {
    id 'kotlin-kapt'
}

dependencies {
    implementation 'com.detmir.recycli:adapters:1.0.4'
    compileOnly 'com.detmir.recycli:annotations:1.0.4'
    kapt 'com.detmir.recycli:processors:1.0.4'
}

```

<a name="first_steps"/>

## First steps
First create Kotlin data classes annotated with `@RecyclerItemState` and extends `RecyclerItem`. Unique (for this adapter) String `id` must be provided.
Those classes describes recycler items states. Let's create 2 data classes - Header and User items.
```java
@RecyclerItemState
data class HeaderItem(
    val id: String,
    val title: String
) : RecyclerItem {
    override fun provideId() = id
}
```

```java
@RecyclerItemState
data class UserItem(
    val id: String,
    val firstName: String,
    val online: Boolean
) : RecyclerItem {
    override fun provideId() = id
}
```

And add two view classes `HeaderItemView` and `UserItemView` that extends any `View` or `ViewGroup` container. Annotate those classes with `@RecyclerItemView` annotation. And add method with items state as parameter and annotate it with `@RecyclerItemStateBinder`.

```java
@RecyclerItemView
class HeaderItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val title: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.header_view, this)
        title = findViewById(R.id.header_view_title)
    }

    @RecyclerItemStateBinder
    fun bindState(headerItem: HeaderItem) {
        title.text = headerItem.title
    }
}
```

```java
@RecyclerItemView
class UserItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val firstName: TextView
    private val status: FrameLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.user_view, this)
        firstName = findViewById(R.id.user_view_first_name)
        status = findViewById(R.id.user_view_status)
    }

    @RecyclerItemStateBinder
    fun bindState(userItem: UserItem) {
        firstName.text = userItem.firstName
        @ColorRes val color = if (userItem.online) R.color.recycliGreen else R.color.recycliRed
        status.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }
}
```

This views will be used at `onCreateViewHolder` functions at `RecyclerView.Adapter` for corresponding states. `bindState` will be called when `onBindViewHolder` called at adapter.

And now you can create `RecyclerView`, set `RecyclerAdapter` as adapter and bind list of `RecyclerItems` to it:

```java
class Case0100SimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_0100)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_case_0100_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerAdapter = RecyclerAdapter(setOf(RecyclerBinderImpl()))
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.bindState(
            listOf(
                HeaderItem(
                    id = "HEADER_USERS",
                    title = "Tasks"
                ),
                UserItem(
                    id = "USER_ANDREW",
                    firstName = "Andrew",
                    online = true
                ),
                UserItem(
                    id = "USER_MAX",
                    firstName = "Max",
                    online = true
                )
            )
        )
    }
} 
```
And RecyclerView will display:

![Screenshot_20210423-135440_KKppt3](https://user-images.githubusercontent.com/1109620/115862192-80278a00-a43c-11eb-8a06-4552ea95001b.png)



<a name="license"/>

## License

```
Copyright 2021 Detsky Mir Group

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
