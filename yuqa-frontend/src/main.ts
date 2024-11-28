import { createApp } from "vue";
import ArcoVue from "@arco-design/web-vue";
// 额外引入图标库
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import App from "./App.vue";
import "@arco-design/web-vue/dist/arco.css";

import { createPinia } from "pinia";
import "@arco-design/web-vue/dist/arco.css";
import router from "./router";
import "@/access";

const pinia = createPinia();

createApp(App)
  .use(ArcoVue)
  .use(pinia)
  .use(router)
  .use(ArcoVueIcon)
  .mount("#app");
