# Logcat fly

## 参数

    - URL：https://materialdesignicons.com/
    - 图片：大小：72dip，内容：64


## 功能

- 展示Logcat
    - 设置log级别
    - 设置正则过滤
        - 标红关键字
    - 设置过滤当前包
    - 设置展示级别
- 界面要求
    - 半透明浮窗
    - 打开关闭展示界面
    - 设置高度
    - 设置Y坐标


## 技术
    
- https://developer.android.com/studio/command-line/logcat
- 读取

    ``` java
    Process process = Runtime.getRuntime().exec("logcat -d");  
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
    ``` 