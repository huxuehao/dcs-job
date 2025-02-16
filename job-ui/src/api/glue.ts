import request from "@/utils/request";

export const glueSave = (data: any) => {
  return request({
    url: "/job/glue/save",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const glueSavePublish = (data: any) => {
  return request({
    url: "/job/glue/save-publish",
    method: "post",
    headers: {
      unrepeat: true,
    },
    data: data,
  });
};

export const glueListByTaskId = (taskId: string) => {
  return request({
    url: "/job/glue/list-by-task-id",
    method: "get",
    params: {
      taskId
    },
  });
};

export const glueSelectOne = (taskId: string, createTime: string) => {
  return request({
    url: "/job/glue/selectOne",
    method: "get",
    params: {
      taskId,
      createTime
    },
  });
};
