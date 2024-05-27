import axios from "./customAxios";

if (localStorage.getItem("jwtToken") != null) {
  const token = localStorage.getItem("jwtToken");

  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

const getAllCommentOfPost = (postId) => {
  return axios.get(`cmt/${postId}`);
};

const createComment = (content, postId, ancestor) => {
  return axios.post(`cmt/create`, {
    content,
    postId,
    ancestor,
  });
};

const getListCmt = (cmtIds) => {
  return axios.get(`cmt//get_descendant`, {
    list: cmtIds,
  });
};

const getAll1AncestorOfPost = (postId) => {
  return axios.get(`cmt/get_ancestor/${postId}`);
};

const getAll1DescendantOfCmt = (cmtIds) => {
  return axios.post(`cmt/get_descendant`, { "list": cmtIds });
};

export {
  getAllCommentOfPost,
  createComment,
  getListCmt,
  getAll1AncestorOfPost,
  getAll1DescendantOfCmt,
};
