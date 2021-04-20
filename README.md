![recycli_logo](https://user-images.githubusercontent.com/1109620/115422301-7405b780-a205-11eb-9372-1411ff17168d.png)

Recycli is a Kotlin library for Android RecyclerView that simplifies complex multiple view types lists creation. With DiffUtils inside, annotation based adapter generator and MVI pattern as philosophy it provides both simple and powerful tool for fast RecyclerView based screens development.

![demo02](https://user-images.githubusercontent.com/1109620/115421675-d14d3900-a204-11eb-8448-597bb1d56435.gif)

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


## License
[MIT](https://choosealicense.com/licenses/mit/)
