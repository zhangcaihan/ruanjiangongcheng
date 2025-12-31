import request from "../utils/request.js";

export const getTravelInfo = (token) => {
  return request({
    url: `/user/getTravelInfo?token=${token}`,
    method: "GET",
  });
};