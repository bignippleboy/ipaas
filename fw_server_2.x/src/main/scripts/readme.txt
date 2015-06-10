Ice server控制脚本：
bin/ice.sh start|stop|restart
默认监听端口号 15212


Mq consumer控制脚本：
bin/consumer.sh start|stop|restart


系统启动自动运行：
注意：在/usr/bin目录要提供java的命令指向，通过/etc/profile指定的环境变量在开机启动运行时是找不到的。

方法1(推荐)：
copy bin/init.d/目录下的ty_fw_ice或者ty_fw_consumer到/etc/init.d目录
执行：
chkcofig --add ty_fw_ice
chkcofig --add ty_fw_consumer
添加相关服务

方法2：
直接修改rc.local，添加
/www/client/ifw_server/bin/ice.sh
或
/www/client/ifw_server/bin/consumer.sh

