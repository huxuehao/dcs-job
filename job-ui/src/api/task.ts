import request from "@/utils/request";

export const taskRefreshResult = (id: string) => {
  return request({
    url: "/job/task/refresh-result",
    method: "get",
    params: {
      id,
    },
  });
};

export const taskAdd = (data: any) => {
  return request({
    url: "/job/task/add",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const taskUpdate = (data: any) => {
  return request({
    url: "/job/task/update",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const taskDelete = (ids: string[]) => {
  return request({
    url: "/job/task/delete",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const taskDisable = (ids: string[]) => {
  return request({
    url: "/job/task/disable",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};

export const taskEnable = (ids: string[]) => {
  return request({
    url: "/job/task/enable",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: ids,
  });
};


export const taskSelectOne = (id: string) => {
  return request({
    url: "/job/task/selectOne",
    method: "get",
    params: {
      id,
    },
  });
};

export const taskList = () => {
  return request({
    url: "/job/task/list",
    method: "get",
  });
};

export const taskPage = (searchConfig: any) => {
  return request({
    url: "/job/task/page",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const taskFutureExecutionPlan = (cron: any) => {
  return request({
    url: "/job/task/future-execution-plan",
    method: "get",
    params: {
      cron,
      numTimes: 10
    },
  });
};
export const taskExecuteOnece = (id: any) => {
  return request({
    url: "/job/task/execute",
    method: "get",
    params: {
      id,
    },
  });
};
