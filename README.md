# 无页数限制的ViewPager。
### 注意：不是循环形式

Step 1. Add it in your root build.gradle at the end of repositories:
``` gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency
``` gradle
dependencies {
	 compile 'com.github.zyl0501:viewpager-unlimite:1.0.7'
}
```

#### 使用方式：

用 NoLimitViewPager 替换原来的 ViewPager就可以，PagerAdapter 不需要修改
由于是无页面限制，所以原先 PagerAdapter 中的 getCount 将会失效


#### 特别感谢：
[LoopingViewPager](https://github.com/imbryk/LoopingViewPager)
