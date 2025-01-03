import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 登入用户信息全局状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: "未登录",
    userAvatar: "",
  });

  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser;
  }

  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      // setTimeout(() => {
      //   loginUser.value = {
      //     id: 1,
      //     userName: "已登入",
      //     userRole: ACCESS_ENUM.ADMIN,
      //   };
      // }, 3000);
      loginUser.value = { userRole: ACCESS_ENUM.NOT_LOGIN };
    }
  }
  return { loginUser, setLoginUser, fetchLoginUser };
});
