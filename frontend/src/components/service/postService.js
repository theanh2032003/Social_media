import axios from "./customAxios";

if (localStorage.getItem("jwtToken") != null) {
  const token = localStorage.getItem("jwtToken");

  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

const getAllPostOfNewFeed = () => {
  return axios.get("post/post_newfeed");
};

const createPost = (formData) => {
  return axios.post("post/save", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Accept: "application/json",
    },
  });
};

const deletePost = (postId) => {
  return axios.delete(`post/delete/${postId}`);
};

const getLikesOfPost = (postId) => {
  return axios.get(`post/like_count/${postId}`);
};

const checkIfUserIsLikePost = (postId) => {
  return axios.get(`post/check_like/${postId}`);
};

const likePost = (postId) => {
  return axios.post(`post/like_post/${postId}`);
};

const getPostsOfProfile = (userId) => {
  return axios.get(
    "post/post_profile",
    {
      params: {
        userId,
      },
    },
    {
      headers: {
        "Content-Type": "multipart/form-data",
        Accept: "application/json",
      },
    }
  );
};

const getPostIsLiked = (userId) => {
  return axios.get(
    "post/post_like",
    {
      params: { userId },
    },
    {
      headers: {
        "Content-Type": "multipart/form-data",
        Accept: "application/json",
      },
    }
  );
};

const getPostById = (postId) => {
  return axios.get(`post/${postId}`);
}

export {
  getAllPostOfNewFeed,
  createPost,
  getLikesOfPost,
  checkIfUserIsLikePost,
  likePost,
  getPostsOfProfile,
  getPostIsLiked,
  deletePost,getPostById
};
