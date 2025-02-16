import request from "@/utils/request";

export const classifyAdd = (data: any) => {
    return request({
      url: "/job/classify/add",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: data,
    });
  };

  export const classifyUpdate = (data: any) => {
    return request({
      url: "/job/classify/update",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: data,
    });
  };


  export const classifyDelete = (ids: string[]) => {
    return request({
      url: "/job/classify/delete",
      method: "post",
      headers: {
        unrepeat: true
      },
      data: ids,
    });
  };

export const classifyTree = (searchConfig: any) => {
  return request({
    url: "/job/classify/tree",
    method: "get",
    params: {
      ...searchConfig,
    },
  });
};

export const classifySelectOne = (id: string) => {
    return request({
      url: "/job/classify/selectOne",
      method: "get",
      params: {
        id
      },
    });
  };