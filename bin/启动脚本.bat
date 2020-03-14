@echo off
::获取当前目录下的 jar 文件名，赋值给变量 jar_name
for /F %%i in ('dir /b *.jar') do ( set jar_name=%%i)
:: %jar_name% 两边加%%即可引用
java -jar %jar_name%
@pause