export default {
  systemName: "DCS-JOB",
  homePath: "/home",
  repeatReqInterval: 500, // 重复请求时间间隔
  tokenHeader: "Authorization",
  refreshTokenHeader: "RefreshAuthorization",
  accessToken: "accessToken",
  refreshToken: "refreshToken",
  permissions: "permissions",
  whiteList: [
    "/login"
  ],
};
