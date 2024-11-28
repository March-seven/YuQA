<template>
  <div class="userLoginPage">
    <h2 style="margin-bottom: 16px">用户登入</h2>
    <a-form
      :model="form"
      :style="{ width: '400px', margin: '0 auto' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item field="Account" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码不小于 8 位" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item field="isRead">
        <a-checkbox v-model="form.isRead"> I have read the manual </a-checkbox>
      </a-form-item>

      <a-form-item>
        <div style="display: flex; justify-content: space-between; width: 100%">
          <a-button type="primary" html-type="submit" style="width: 120px">
            登录
          </a-button>
          <a-button
            @click="goToRegisterPage"
            type="primary"
            style="width: 120px"
          >
            注册
          </a-button>
        </div>
      </a-form-item>
    </a-form>
    <!--    {{ form }}-->
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import API from "@/api";
import { userLoginUsingPost } from "@/api/userController";
import { useLoginUserStore } from "@/store/userStore";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const loginUserStore = useLoginUserStore();
const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as API.UserLoginRequest);

/**
 * 提交
 */
const handleSubmit = async () => {
  console.log(form);
  const res = await userLoginUsingPost(form);
  if (res.data.code === 0) {
    await loginUserStore.fetchLoginUser();
    message.success("登入成功");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登入失败," + res.data.message);
  }
};

const goToRegisterPage = () => {
  router.push({
    path: "/user/register",
    replace: true,
  });
};
</script>
