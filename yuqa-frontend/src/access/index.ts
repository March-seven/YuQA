import router from "@/router";
import { useLoginUserStore } from "@/store/userStore";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";
//进入页面前，进行权限校验
router.beforeEach(async (to, from, next) => {
  //获取当前登入用户
  const loginUserStore = useLoginUserStore();
  let loginUser = loginUserStore.loginUser;

  //如果之前没有尝试获取过登入用户信息，才能自动登入
  if (!loginUser || !loginUser.userRole) {
    await loginUserStore.fetchLoginUser();
    loginUser = loginUserStore.loginUser;
  }

  //当前页面需要的权限
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;

  //要跳转的页面必须登入
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    //如果没登入，跳转到登入页面
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
    }
    //如果登入了，但是权限不够，跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next("noAuth");
      return;
    }
  }
  next();
});
