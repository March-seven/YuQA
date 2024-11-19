import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限
 * @param loginUser
 * @param needAccess
 */
const checkAccess = (
  loginUser: API.LoginUserVO,
  needAccess = ACCESS_ENUM.NOT_LOGIN
) => {
  //获取当前登入用户具有的权限（如果没有loginUser，则表示为登入）
  const loginUserAccess = loginUser.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  //如果用户要登入才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    return loginUserAccess !== ACCESS_ENUM.NOT_LOGIN;
  }
  //如果管理员才能访问
  if (needAccess === ACCESS_ENUM.ADMIN) {
    return loginUserAccess === ACCESS_ENUM.ADMIN;
  }
};

export default checkAccess;
