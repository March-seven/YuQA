# 智能问答小程序平台 YuQA

## Web 前端技术选型

* Vue 3
* Vue-CLI 脚手架  [介绍 | Vue CLI (vuejs.org)](https://cli.vuejs.org/zh/guide/)
* Arco Design 组件库  [Arco Design Vue](https://arco.design/vue/docs/start)
  * dayjs [显示 | Day.js中文网 (fenxianglu.cn)](https://dayjs.fenxianglu.cn/category/display.html#格式化)
* Axios 请求库 [起步 | Axios中文文档 | Axios中文网 (axios-http.cn)](https://www.axios-http.cn/docs/intro)
* Pinia 状态管理 [开始 | Pinia (vuejs.org)](https://pinia.vuejs.org/zh/getting-started.html)



## AI 生成题目

### 需求分析

原本创建题目需要人工一个个添加标题和选项，比较麻烦。
可以使用 AI，根据已经填写的应用信息，自动生成题目，然后再由人工进行编辑确认，提高
创建题目的效率。

### 方案设计

AI 生成内容的核心是编写 Prompt，好的、精准的 Prompt 才能帮助我们得到预期的结果。
首先明确我们能提供或者需要输入给 A1 的参数，然后构建 Prompt 并输入给 AI，让 AI 生成
题目并处理成我们需要的格式。





## 性能优化
