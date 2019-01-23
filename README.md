# expense
### gaoqiong
## Why did I do this project?
```
> 因为不精 excel 所以用了 mysql
> 因为每天加数据太麻烦所以写了一个网页
> 本来只是想简单往里面写数据
> 然后想要展示，修改，或者删除，各种条件查询以及下载 excel
> 还想要有图标，看上去对比明显


> 相当一部分代码借鉴 @hmc 代码库、搜索引擎，感谢贡献者们
```

## 系统参数配置
```
MYSQL_HOST
MYSQL_PORT
MYSQL_DB_NAME
MYSQL_DB_USERNAME
MYSQL_DB_PASSWORD
LOG4J_FILE_PATH
```

## 启动项目
```
java -DMYSQL.HOST=127.0.0.1 -DMYSQL.PORT=3306 -DMYSQL_DB_NAME=daily -DMYSQL_DB_USERNAME=root -DMYSQL_DB_PASSWORD=123456 -DLOG4J_FILE_PATH=E:\\logs -jar expense.war
```