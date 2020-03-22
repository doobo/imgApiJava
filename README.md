
超简Api图床(Java版)  —— 专为Api而生
===============


超简Api图床 是基于SpringBoot 2.2.2.RELEASE 实现的一套Api图床程序，主要包含以下特色：

 + 无数据库模式，简单配置，一键搭建
 + 第三方接口接入，不占用服务器空间
 + 接入搜狗Api平台，需配置代理，全球CDN加速，永久不限量图片存储
 + [接入](https://sm.ms/)SM.MS，无需配置，全球CDN加速，永久不限量图片存储
 + 超简单Api使用，提供统一Api实现图片上传
 + 调用Api的时候需要通讯密钥，可以过滤其他人恶意上传
 + 支持跨域提交访问
 + 免费、开源
 + 支持简单返回，直接返回图片网址

> 超简Api图床的运行环境为JDK版本1.8。

## 安装

 + 下载代码,执行mvn clean install,在target目录下，有jar文件，执行即可
 + 确认本机已经拥有java的运行环境（JDK>=1.8）,如果没有，请您安装java的运行环境
 + 在jar包的同级目录，在控制台输入启动命令 java -jar img-api-0.0.1-SNAPSHOT.jar
    + 请将img-api-0.0.1-SNAPSHOT.jar替换成您下载的jar包的名字
    + 自定义登录密码和密钥以及代理域名，请您在启动的时候使用：java -DsmToken=akdsiewlkdka -Dsg.proxies=https://5fu8.com -Dspring.security.user.password=root -Dtoken=12345678 -jar img-api-0.0.1-SNAPSHOT.jar
 + 打开浏览器，访问 localhost:8080
 + 点击系统设置，进入设置页面，进行系统的首次配置，并修改管理员密码和通讯密钥
 + 默认管理密码为：admin
 + 默认通讯密钥为：admin
 + 保存配置后，即可开始使用


 > 升级说明：请您直接下载新版本覆盖旧版本即可！
 

## 使用

 + 根据主页显示的Api接口，调用Api接口，将会返回对应的图片地址
 + 使用主页提供的测试工具，手动选择图片上传，会显示对应的图片地址

 > 如果有新的接口，只需要在vip.ipav.img里面新建一个类，类似Cn6ImgUploadApiRepository.class,这样新的接口立即可以在接口和页面看到，不需要修改以前的代码,欢迎pull
 > 现有3个接口,新的接口,请用3以后的数字,接口说明可以自定义
 
## Api接口说明
 + 请求地址：http://localhost:8080/api  (localhost请自行替换成您的域名)
 + 请求方式：POST
 + 请求参数：
   + key=通讯密钥  （后台设置的通讯密钥，默认为123456）
   + imgBase64=需要上传图片的base64编码（请对该字段使用urlencode编码）
   + onlyUrl=0 （传入1则调用接口只会返回图片地址，传入其他或者不传会返回完整的json数据）
   
 + 返回数据：
    {"code":1,"msg":"操作成功","img":"https://i.loli.net/2020/03/20/L7G3jyzk1cXVKPT.png"}
    + code：返回1代表成功，-1代表失败
    + msg：返回接口调用的具体说明
    + img：失败返回null，成功返回图片的图床网址
 

## 注意

 <del>因本系统为无数据库模式，所以每次重启服务器配置都会丢失，所以请您正常使用过程中不要重启服务器，或者重启服务器后及时配置好您的图床运行模式和通讯密钥，以免影响正常使用（正在想办法让配置持久化，不会随着服务器重启而丢失，敬请期待）</del>
 + 因本系统为无数据库模式，所以在jar包的同级目录会生成一个vone.txt来保存系统配置，请您不要删除该文件，否则会导致配置丢失，系统会恢复默认配置！

## 版权信息

超简Api图床遵循 MIT License 开源协议发布，并提供免费使用。

