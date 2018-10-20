## Peach Android Networking Library

### In Project Level :

allprojects {
   repositories {
	...
	maven { url 'https://www.jitpack.io' }
   }
}

### In App Level :

Note that the minSdkVersion must be 16 

dependencies {
   ...
   implementation 'com.github.AndroThink:Peach:1.0.0'
}
