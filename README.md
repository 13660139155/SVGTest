## 什么是SVG?
Google 在Android5.X中增加了对SVG矢量图形的支持，可以用来创建高效率的动画, 所以我们先来了解一下SVG的定义： <br>
* 可伸缩矢量图形（Scalable Vector Graphics）
* 使用XML格式定义图形
* 图像在放大或改变尺寸的情况下图片质量不会有所损失
* android中使用vector标签表示SVG
___
与bitmap相比，SVG最大的优点是放大不会失真，而bitmap需要为不同的分辨率准备很多套图标，而SVG则不需要，前面说了SVG要用vector表示，我们先来看看vector标签中属性的含义。
## vector的各个属性是什么意义？
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"  //命名空间
    android:height="200dp"  //这个是图片的intrinsic高度
    android:width="200dp"   //这个是图片的intrinsic宽度
    android:viewportHeight="100"    //这个是为这个图片设置的纵坐标,表示将图片分为100等份,主要下面的pathData需要依赖这个坐标的划分
    android:viewportWidth="100"     //同上,只不过这个是横坐标, heigh，width的比例和viewportHeight，viewportWidth的比例必须保持一致，不然图形就会发生形变
    android:alpha="0.2"     //这个是整个图像的透明度,取值范围0到1
    >

    <group      //这个标签中可以放入若干个<path/>标签,并给它们设置一些共同的属性
        android:name="group_name"   //这个name很有用,在设置objectAnimator的时候用来区分给那个部分施加动画
        android:pivotY="50"     //这个设置这个group的中心点的X坐标,取值范围为0到100,在做rotation时有用
        android:pivotX="50"     //这个设置这个group的中心点的Y坐标,取值范围为0到100,在做rotation时有用
        android:translateX="20" //将整个group在X轴方向平移多少像素
        android:translateY="30" //将整个group在Y轴方向平移多少像素
        android:rotation="90"   //将整个group以中心点左边旋转的角度,360为一圈
        android:scaleX="0.5"    //横坐标的缩放比例 , 取值1表示100%
        android:scaleY="0.3">   //纵坐标的缩放比例,取值0.5表示50%,取值1.5表示150%

        <path   //这个标签是重头戏,矢量图绘制的路径
            android:name="path_name"    //为这个path标记的名字,在使用objectAnimator的时候用来区分给哪个部分施加动画
            android:pathData="m 0,0 L50,0 L100,100 L0,100 z"    //这个是SVG的语法,下面讲
            android:fillColor="@color/red"  //定义填充图形的颜色，如果没有定义则不填充路径
            android:fillAlpha="1"       //定义填充图形的透明度，取值范围0到1
            android:strokeAlpha="0.5"   //定义路径的透明度,取值范围0到1
            android:strokeColor="#ff0000ff" //定义如何绘制路径，如果没有定义则不显示路径
            android:strokeWidth="20"    //线段的宽度
            android:strokeLineCap="butt|round|square"   //线的末端形状,butt严格到指定的坐标就截至,round是圆角,square是方形，到指定的坐标后还会再冒出一点来
            android:strokeLineJoin="round|bevel|miter"  //线的连接处形状,round是圆角的,bevel和miter貌似看不出来有什么区别....
            android:trimPathStart="0.5"    //顾名思义,从path开始的地方(0%)去除path,去除到指定的百分比位置,取值范围0到1
            android:trimPathEnd="0.5"      //顾名思义,从path结束的地方(100%的地方)去除path,去除到指定的百分比位置,取值范围0到1
            android:trimPathOffset="0.5"   //这个属性是和上面两个属性共同使用的,单独使用没有用,这个属性的意思是,在去除path的时候设置path原点的位置,按百分比设置,取值范围0到1
            />
    </group>
</vector>
```
***
下面就来讲解path标签，path标签是用来创建SVG的，就像用指令控制一只画笔，path标签所支持的指令有以下几种。
## \<path>指令
    M = moveto(M X, Y): 将画笔移动到指定的位置，但未发生绘制
    L = lineto(L X, Y): 画直线到指定位置
    H = horizontal(H X): 画水平线到指定X坐标
    V = vertical lineto(V Y): 画垂直线到指定Y坐标
    C = curveto(C X1,Y1,X1,Y2,ENDX,ENDY): 画三次贝塞尔曲线
    S = smooth curveto(S X2,Y2,ENDX,ENDY): 画三次贝塞尔曲线
    Q = quadratic Belzier curve(Q X,Y,ENDX,ENDY): 二次贝塞尔曲线
    T = smooth quadratic Belzier curveto(T ENDX,ENDY): 映射前面路径后的终点
    A = elliptical Arc(A RX,RY,XROTATION,FLAG1,FLAG2,X,Y): 画弧线
    Z = closepath(): 关闭路径，把前面的路径连起来
在使用以上指令时，需要注意：
1. 坐标轴以（0，0）为中心，X轴水平向右，Y轴水平向下
2. 所有指令大小写均可。大写绝对定位，参考全局坐标系；小写相对定位，参考父容器坐标系
3. 指令和数据间的空格可以省略，可以用逗号隔开，也可以用空格
4. 同一指令出现多次可以只用一个
***
SVG的指令参数非常复杂，但是在android中，不需要太多太复杂的SVG图形，所以我们先来掌握几个常用的指令，在以后的学习中，读者将会慢慢掌握更多的SVG绘制技巧和方法
## SVG常用指令
* M <br>
类似Android绘图中path类的moveTo方法，即将画笔移动到某一点但并没有发生绘制动作，下面配合L进行讲解
***
* L <br>
画一条直线
```xml
 <path
       ...省略一些代码
       android:pathData="M 20 50 L 80 50"/>
```
![lineImage](https://github.com/13660139155/SVGTest/raw/master/image/lineImage.png)
<br> 上面表示把画笔放在（20,50）位置，连直线到80，50点 <br>
同时L后面还可以跟H或V指令来绘制水平、竖直线，后面的参数是x坐标（H指令）或y坐标（V指令）,如下
```xml
<path
        ...省略一些代码
        android:pathData="M 20 50 L 80 50 V 80 H 20"/>
```
![lineImageVH](https://github.com/13660139155/SVGTest/raw/master/image/lineImageVH.png)
***
* A <br>
绘制一段弧线，且弧线不允许闭合，可以把弧线想象成椭圆的某一段，A指令有以下7个参数：
1. RX，RY 指所在椭圆的半轴大小
2. XROTATION 指椭圆的X轴与水平方向的顺时针方向夹角，可以想象成一个水平的椭圆绕中心点顺时针旋转XRORATION的额角度
3. FLAG1 只有俩个值，1表示大角度弧线，0表示小角度弧线
4. FLAG2 只有俩个值，1为顺时针，0反之
5. X，Y 为终点坐标 <br> <br>
看代码：
```xml
<path
        ...省略一些代码
        android:pathData="
        M 50 50
        a 30 15 0 1 0 1 0"/>
```
再看图：<br>
![ellipticalImage](https://github.com/13660139155/SVGTest/raw/master/image/ellipticalImage.png)
 <br> **图一** <br><br>
上面表示把画笔放在（50,50）位置；30, 15分别表示椭圆的x，y半轴大小；0表示x轴不旋转；1表示用大角度弧线绘制；0表示顺时针：1，0表示相对与以（50，50）为起始点的坐标轴的坐标，因为a是小写 <br> <br>
再看一段代码：
```xml
 <path
      ...省略一些代码
      android:pathData="
      M 25 50
      a 25 25 0 1 0 50 0" />
```
再看图：<br>
![ellipticalImage2](https://github.com/13660139155/SVGTest/raw/master/image/ellipticalImage2.png)
 <br> **图二** <br><br>
可以看到这里显示了一个半圆，因为这里的X，Y轴大小相等 <br> <br>
再看一段代码：
```xml
 <path
       ...省略一些代码
       android:pathData="M 25 50
       a 25 25 0 1 0 40 0" />
```
再看图：<br>
![ellipticalImage3](https://github.com/13660139155/SVGTest/raw/master/image/ellipticalImage3.png)
 <br> **图三** <br><br>
这里把终点x轴坐标改为40，图中显示了圆的大部分 <br> <br>
看一段代码：
```xml
 <path
        ...省略一些代码
        android:pathData="M 25 50
        a 25 25 0 0 0 40 0" />
```
再看图：<br>
![ellipticalImage4](https://github.com/13660139155/SVGTest/raw/master/image/ellipticalImage4.png)
 <br> **图四** <br><br>
这里把FLAG1改为0，与图三相比，发现弧度变小了，因为用小弧度画 <br> <br>
看一段代码：
```xml
  <path
        ...省略一些代码  
        android:pathData="M 25 50
        a 25 25 0 0 1 40 0" />
```
再看图：<br>
![ellipticalImage5](https://github.com/13660139155/SVGTest/raw/master/image/ellipticalImage5.png)
 <br> **图五** <br><br>
这里把FLAG2改为1，与图四相比，图形翻转了，因为画的方向不一样了 <br>
    把A指令的几个图结合看一下，就能弄懂A这个指令了
***
关于贝塞尔指令的，这里就不过多介绍了，放出几个链接供大家学习：<br>
[贝塞尔曲线初探](http://www.cnblogs.com/jay-dong/archive/2012/09/26/2704188.html) <br>
[SVG讲解](https://github.com/OCNYang/Android-Animation-Set/wiki/SVG-讲解) <br>
***
Coogle在Android5.0X中提供了俩个API来帮助支持SVG：<br>
* VectorDrawable
* AnimatedVectorDrawable
其中VectorDrawable用于创建XML文件的SVG图形即前面的vector标签，并结合AnimatedVectorDrawable来完成动画效果
***
## VectorDrawable
在XML中创建一个静态的XMLSVG图形，通常会形成如下的树形结构: <br>
![tree](https://github.com/13660139155/SVGTest/raw/master/image/tree.png)
<br> **树形结构** <br> <br>
path是树形结构中最小的单位，而通过Group可以将不同的path进行组合，接下来我们使用vector标签创建SVG图形，代码如下： <br>
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="200dp"
    android:height="200dp"
    android:viewportWidth="100"
    android:viewportHeight="100">
    <group
        android:name="line">
        <path
            android:name="path1"
            android:strokeColor="@android:color/holo_green_dark"
            android:strokeWidth="5"
            android:strokeLineCap="round"
            android:pathData="
            M 20 20
            L 50 20 80 20"/>
        <path
            android:name="path2"
            android:strokeLineCap="round"
            android:strokeWidth="5"
            android:strokeColor="@android:color/holo_green_dark"
            android:pathData="
            M 20 80
            L 50 80 80 80"/>
    </group>
</vector>
```
上面的代码画了俩条线，每条线由三个点控制，形成初始状态，下面立马通过AnimatedVectorDrawable来实现动画效果
## AnimatedVectorDrawable
AnimatedVectorDrawable就是通过连接静态的VectorDrawable和动态的objectAninmator来为VectorDrawable提供动画效果，分几个步骤来使用：
1. 在XML中通过animated-vector标签来声明对AnimatedVectorDrawable的使用，并指定它的drawable属性，target标签中的name属性和animation属性 <br>
代码如下：<br>
```xml
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/svg_path">
    <target
        android:animation="@animator/anim_path1"
        android:name="path1"/>
    <target
        android:animation="@animator/anim_path2"
        android:name="path2"/>
</animated-vector>
```
android:drawable="@drawable/svg_path"指定了上面创建的VectorDrawable即画的俩条线；target标签中的name指定了要作用动画的path或Group的name, 即俩者的name要保持一致，这样系统才能找到要实现动画的元素;taret标签中的animation指定了要作用的都动画，在本例中，path1的动画代码如下: <br>
```xml
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="500"
    android:propertyName="pathData"
    android:valueType="pathType"
    android:valueFrom=
    "M 20 20
     L 50 20 80 20"
    android:valueTo=
    "M 20 20
     L 50 50 80 20"
    android:interpolator="@android:anim/bounce_interpolator">
</objectAnimator>
```
path2的动画代码如下； <br>
```xml 
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="500"
    android:propertyName="pathData"
    android:valueType="pathType"
    android:valueFrom=
    "M 20 80
     L 50 80 80 80"
    android:valueTo=
    "M 20 80
     L 50 50 80 80"
    android:interpolator="@android:anim/bounce_interpolator">
</objectAnimator>
```
以上的俩个动画代码中都定义了一个pathType的属性动画，并指定了变换的初始值分别为：<br>
```xml
//path1
 android:valueFrom=
    "M 20 20
     L 50 20 80 20"
//path2
 android:valueFrom=
    "M 20 80
     L 50 80 80 80"
```
结束值为: <br>
```xml
//path1
 android:valueTo=
    "M 20 20
     L 50 50 80 20"
//path2
  android:valueTo=
    "M 20 80
     L 50 50 80 80"
```
这里要注意的是，SVG的路径变换属性动画中，变换前后的节点数必须相同，这也是为什么前面需要使用三个点来绘制一条直线，因为后面需要中点进行动画变换 <br>
2. 把AnimatedVectorDrawable的XML文件设置给ImageView
```xml
 <ImageView
        android:id="@+id/iv_path"
        android:src="@drawable/svg_path_anim"
        .../>
```
3. 代码中启动AnimatedVectorDrawable动画
```xml
 ImageView ivPath;
 ...
 ivPath = findViewById(R.id.iv_path);
        ivPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = ivPath.getDrawable();
                if(drawable instanceof Animatable){
                    ((Animatable)drawable).start();
                }
            }
        });
```
这样俩个path就实现了动画效果，如图：<br>
<iframe height=100 width=100 src="https://github.com/13660139155/SVGTest/raw/master/gif/pathGif.gif"> <br>
****
在本文源码中还有俩个动画，就不贴代码了
<iframe height=500 width=500 src="https://github.com/13660139155/SVGTest/raw/master/gif/systemGif.gif">   
