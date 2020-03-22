
超简Api图床  —— 专为Api而生
===============


超简Api图床 是基于SpringBoot 2.2.2.RELEASE 实现的一套Api图床程序，主要包含以下特色：

 + 无数据库模式，简单配置，一键搭建
 + 第三方接口接入，不占用服务器空间
 + 接入搜狗Api平台，需配置代理，全球CDN加速，永久不限量图片存储,目测会掉图
 + [接入](https://sm.ms/)SM.MS，需邮箱注册，全球CDN加速，永久5G免费图片存储
 + 六间房间接口，会掉图，用作临时文件,如机器学习等
 + 超简单Api使用，提供统一Api实现图片上传
 + 调用Api的时候需要通讯密钥，可以过滤其他人恶意上传
 + 支持跨域提交访问
 + 免费、开源
 + 支持简单返回,直接返回图片网址

> 超简Api图床的运行环境为JDK版本1.8。

## 安装

 + 下载代码,执行mvn clean install,在target目录下，有jar文件，执行即可
 + 确认本机已经拥有java的运行环境（JDK>=1.8）,如果没有，请您安装java的运行环境
 + 在jar包的同级目录，在控制台输入启动命令 java -jar img-api-0.0.1-SNAPSHOT.jar
    + 自定义登录密码和密钥以及代理域名，请您在启动的时候使用：
    java -DsmToken=akdsiewlkdka -Dsg.proxies=https://5fu8.com -Dspring.security.user.password=root -Dtoken=12345678 -jar img-api-0.0.1-SNAPSHOT.jar
 + 打开浏览器，访问 localhost:8080
 + 点击系统设置，进入设置页面，进行系统的首次配置，并修改管理员密码和通讯密钥
 + 默认管理密码为：admin
 + 默认通讯密钥为：admin
 + 保存配置后，即可开始使用

 > 升级说明：请您直接下载新版本覆盖旧版本即可！

## 使用

 + 根据主页显示的Api接口，调用Api接口，将会返回对应的图片地址
 + 使用主页提供的测试工具，手动选择图片上传，会显示对应的图片地址

 > 如果有新的接口，只需要在vip.ipav.img.api里面新建一个类,继承ImgUploadApiRepository接口，类似SmMsImgUploadApiRepository.java,这样新的接口立即可以在接口和页面看到，不需要修改以前的代码,欢迎pull
 > 现有3个接口,新的接口,请用3以后的数字,接口说明可以自定义
 
## Api接口说明
 + 请求地址：http://localhost:8080/upload  (localhost请自行替换成您的域名)
 + 请求方式：POST
 + 请求参数：
   + key=通讯密钥  （后台设置的通讯密钥，默认为1234567）
   + file=需要上传图片
   + type=需使用的上传接口类别,现在有1,2,3三个接口,默认使用1,搜狗的CDN
   + onlyUrl传入则调用接口只会返回图片地址,不传会返回完整的json数据）
   
 + 返回数据：
    {"code":1,"msg":"操作成功","img":"https://i.loli.net/2020/03/20/L7G3jyzk1cXVKPT.png"}
    + code：返回1代表成功，-1代表失败
    + msg：返回接口调用的具体说明
    + img：失败返回null，成功返回图片的图床网址
 
## 搜狗的代理Nginx配置示例
```nginx
upstream image-server {
    server 127.0.0.1:8080;
    #server img02.sogoucdn.com;
}
# 反向代理参数，具体自行搜索按需配置吧，懒得说明了
proxy_connect_timeout    5;
proxy_read_timeout       60;
proxy_send_timeout       5;
proxy_buffer_size        16k;
proxy_buffers            4 64k;
proxy_busy_buffers_size 128k;
proxy_temp_file_write_size 128k;

# 配置临时目录、缓存路径（注意要先建立这2个目录，要在同一个硬盘分区，注意权限）
proxy_temp_path   /tmp/nginx_proxy_temp 1 2;
proxy_cache_path  /tmp/nginx_proxy_cache levels=1:2 keys_zone=OOXX:32m inactive=7d max_size=1g;
# keys_zone=OOXX:32m 表示这个 zone 名称为 OOXX，分配的内存大小为 32MB
# levels=1:2 表示缓存目录的第一级目录是 1 个字符，第二级目录是 2 个字符
# inactive=7d 表示这个zone中的缓存文件如果在 7 天内都没有被访问，那么文件会被cache manager 进程删除
# max_size=1G 表示这个zone的硬盘容量为 1G
server{
        listen 80;
        server_name img.ipav.vip;    
        index index.html;      
        access_log off;        
        location / {
                proxy_pass         http://image-server;
                #proxy_redirect     off;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;    
        }

        # nginx 中的 Purge 配置
        location ~ /purge(/.*) {
          #允许的IP
          allow 127.0.0.1;
          deny all;
          #proxy_cache_purge OOXX "$scheme://$host$1";
        }
        # 配置好后，可以手动清理某个缓存页面/文件，例如：
        # http://ooxx.com/abc.png
        # 改为 http://ooxx.com/purge/abc.png 就可以清理这个文件的缓存了
        # 只对图片、js、css 等静态文件进行缓存
        location  ~* \.(png|jpg|jpeg|gif|ico|js|css)$ {
                #-------------------------------------
                proxy_cache OOXX;
                proxy_cache_key "$scheme://$host$request_uri";
                proxy_cache_valid 200 304 7d;
                proxy_cache_valid 301 3d;
                proxy_cache_valid any 10s;
                #--------------------------------------
                proxy_pass         http://image-server;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;
        }
        
         #sg的cdn代理配置
        location ~ /sg(/.*) {
          proxy_cache OOXX;
                proxy_cache_key "$scheme://$host$request_uri";
                proxy_cache_valid 200 304 7d;
                proxy_cache_valid 301 3d;
                proxy_cache_valid any 10s;
                #--------------------------------------
                proxy_pass         http://image-server;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;
        }
}

server{
        listen 443 ssl;
        server_name 5fu8.com;    
        index index.html;      
        access_log off;
        ssl_certificate 5fu8.com.pem;
        ssl_certificate_key 5fu8.com.key;
        keepalive_timeout 70;
        ssl_session_cache shared:SSL:10m;
        ssl_session_timeout 10m;
        #全站 HTTPS,加入 HSTS 
        add_header Strict-Transport-Security max-age=63072000;
        add_header X-Frame-Options DENY;
        add_header X-Content-Type-Options nosniff;
        location / {
                proxy_pass         http://image-server;
                #proxy_redirect     off;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;    
        }

        # nginx 中的 Purge 配置
        location ~ /purge(/.*) {
          #允许的IP
          allow 127.0.0.1;
          deny all;
          #proxy_cache_purge OOXX "$scheme://$host$1";
        }
        # 配置好后，可以手动清理某个缓存页面/文件，例如：
        # http://ooxx.com/abc.png
        # 改为 http://ooxx.com/purge/abc.png 就可以清理这个文件的缓存了
        # 只对图片、js、css 等静态文件进行缓存
        location  ~* \.(png|jpg|jpeg|gif|ico|js|css)$ {
                #-------------------------------------
                proxy_cache OOXX;
                proxy_cache_key "$scheme://$host$request_uri";
                proxy_cache_valid 200 304 7d;
                proxy_cache_valid 301 3d;
                proxy_cache_valid any 10s;
                #--------------------------------------
                proxy_pass         http://image-server;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;
        }
        
         #sg的cdn代理配置
        location ~ /sg(/.*) {
                proxy_cache OOXX;
                proxy_cache_key "$scheme://$host$request_uri";
                proxy_cache_valid 200 304 7d;
                proxy_cache_valid 301 3d;
                proxy_cache_valid any 10s;
                #--------------------------------------
                proxy_pass         http://image-server;
                proxy_set_header   Host $host;
                proxy_set_header   X-Real-IP  $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   Referer http://$host;
        }
}
```
## 注意

 因本系统为无数据库模式，所以每次重启服务器配置都会丢失,尽量使用启动命令来配置token,这样重启也不会有问题
## 版权信息

超简Api图床遵循 MIT License 开源协议发布，并提供免费使用。

