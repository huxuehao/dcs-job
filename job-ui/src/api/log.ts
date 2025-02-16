import request from "@/utils/request";

export const logPage = (searchFrom: any) => {
  return request({
    url: "/job/log/page",
    method: "get",
    params: {
      ...searchFrom
    },
  });
};

export const logSelectOne = (id: string) => {
  return request({
    url: "/job/log/selectOne",
    method: "get",
    params: {
      id
    },
  });
};

export const logDelete = (ids: string[]) => {
  return request({
    url: "/job/log/delete",
    method: "post",
    data: ids,
  });
};
