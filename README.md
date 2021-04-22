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
    implementation 'com.detmir.recycli:adapters:1.0.3'
    compileOnly 'com.detmir.recycli:annotations:1.0.3'
    kapt 'com.detmir.recycli:processors:1.0.3'
}

```

<a name="first_steps"/>

## First steps

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
