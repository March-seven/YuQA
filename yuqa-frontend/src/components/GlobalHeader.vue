<template>
  <a-row class="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="titleBar">
            <img class="logo" src="../assets/logo.png" alt="logo" />
            <div class="title">YuQA</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="200px">
      <div v-if="loginUserStore.loginUser.id">
        <a-popover trigger="hover" position="bottom">
          <a-avatar>
            <img
              alt="avatar"
              :src="
                loginUserStore.loginUser.userAvatar ??
                'https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp'
              "
            />
          </a-avatar>
          <template #content>
            <div class="userInfo">
              <div class="name">
                {{ loginUserStore.loginUser.userName }}
              </div>
              <div class="box">
                <div class="info-item" @click="goPage('/pages/person/person')">
                  <div style="display: flex; align-items: end">
                    <icon-user size="20" />
                    <text
                      style="
                        margin-left: 10px;
                        font-size: 14px;
                        font-weight: bold;
                      "
                      >个人中心</text
                    >
                  </div>
                  <div style="align-items: center">
                    <icon-right size="14" stroke-width="7" />
                  </div>
                </div>
                <div class="info-item" @click="goPage('/pages/person/person')">
                  <div style="display: flex; align-items: end">
                    <icon-user size="20" />
                    <text
                      style="
                        margin-left: 10px;
                        font-size: 14px;
                        font-weight: bold;
                      "
                      >个人中心</text
                    >
                  </div>
                  <div style="align-items: center">
                    <icon-right size="14" stroke-width="7" />
                  </div>
                </div>
              </div>
              <a-divider size="1.5px" />
              <div class="info-item" @click="loginOut">
                <div style="display: flex; align-items: end">
                  <icon-export size="20" />
                  <text
                    style="
                      margin-left: 10px;
                      font-size: 14px;
                      font-weight: bold;
                    "
                    >退出登录</text
                  >
                </div>
              </div>
            </div>
          </template>
        </a-popover>
      </div>
      <div v-else>
        <a-avatar @click="loginIn" style="cursor: pointer; background: #00aeec">
          <span>登录</span>
        </a-avatar>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { userLogoutUsingPost } from "@/api/userController";
import message from "@arco-design/web-vue/es/message";

const loginUserStore = useLoginUserStore();

const router = useRouter();

//跳转至登入页面
const loginIn = () => {
  router.push({
    path: "/user/login",
    replace: true,
  });
};
//退出登录
const loginOut = async () => {
  console.log("退出登入");
  const res = await userLogoutUsingPost();
  if (res.data.code === 0) {
    await loginUserStore.fetchLoginUser();
    message.success("退出成功");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("退出失败," + res.data.message);
  }
};
//当前选中的菜单页面
const selectedKeys = ref(["/"]);
//路由跳转时，自动更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

//展示在菜单栏的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }

    //根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});
// 点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

//气泡卡片的用户信息和功能
const goPage = (path: string) => {
  console.log("页面跳转");
};
</script>
<style scoped>
#globalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  margin-left: 16px;
}

.logo {
  height: 48px;
}

.userInfo {
  width: 240px;
  display: flex;
  flex-direction: column;
}
.userInfo .name {
  align-items: center;
  text-align: center;
  font-size: 20px;
}
.userInfo .info-item {
  padding: 0 10px;
  height: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.userInfo .info-item:hover {
  background-color: #e3e5e7; /* 鼠标悬停时的灰色背景 */
  border-radius: 10px;
  cursor: pointer;
}
</style>
