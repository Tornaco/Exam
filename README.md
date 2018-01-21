# Exam Android端

## 冲顶大会等类似节目的解决方案：
* Android端，利用xp框架，获取题目，发送到PC服务端。
* PC端，接收题目，利用浏览器进行搜索，呈现。

> 优势：搜索速度快，PC屏幕独立，便于快速参考。

> 劣势：不够只能，无法给出确切答案，只是给予搜索参考。


## Android端设计思路：
1. 题目获取与回调：基于Xposed框架，TextView的hook，与Binder通讯。
2. 与PC数据传输，Socket。
