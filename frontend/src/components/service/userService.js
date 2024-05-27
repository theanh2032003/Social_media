import axios from "./customAxios";

if (localStorage.getItem("jwtToken") != null) {
  const token = localStorage.getItem("jwtToken");

  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

const loginApi = (email, password) => {
  return axios.post("auth/login", {
    email,
    password,
  });
};

const registerApi = (email, password, fullname, textId) => {
  return axios.post("auth/register", {
    email,
    password,
    fullname,
    textId,
  });
};

const confirmRegister = (token) => {
  return axios.get(
    "auth/register/confirm",
    {
      params: {
        token: token,
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

const reSendConfirmToken = (email) => {
  return axios.post(
    "auth/re_send_token",
    {
      email,
    },
    {
      headers: {
        "Content-Type": "multipart/form-data",
        Accept: "application/json",
      },
    }
  );
};

// const confirmResigter = () => {
//     return axios.get("auth/confirm");
// }

const changePasswordApi = (email) => {
  return axios.patch(
    "auth/changePassword",
    {
      email,
    },
    {
      headers: {
        "Content-Type": "multipart/form-data",
        Accept: "application/json",
      },
    }
  );
};

const confirmTokenChangePassword = (email, token, password) => {
  return axios.get(
    "auth/changePassword/confirm",
    {
      params: {
        email: email,
        token: token,
        password: password,
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

const searchUserApi = (userName) => {
  return axios.get("user/search", {
    params: {
      name: userName,
    },
  });
};

const addFriend = (receiverId) => {
  return axios.post(`user/add_friend/${receiverId}`);
};

const checkStatusFriendRequest = (receiverId) => {
  return axios.get("user/check_add_friend", {
    params: {
      receiveId: receiverId,
    },
  });
};

const getAllFriendRequest = () => {
  return axios.get("user/get_all_friend_request");
};

const confirmFriendRequest = (friendRequestId) => {
  return axios.patch(`user/confirm_add_friend/${friendRequestId}`);
};

const deleteFriendRequest = (friendRequestId) => {
  return axios.delete(
    "user/delete_friend_request",
    {
      params: {
        "friendRequestId": friendRequestId,
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

const deleteFriend = (receiverId) => {
  return axios.delete(
    "user/delete_friend",
    {
      params: {
       "friendId": receiverId,
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

const getInfo = (userId) => {
  return axios.get(`user/${userId}`);
};

const editInfo = (formData) => {
  return axios.patch("user/change_info", formData, {
    headers: {
      "Content-Type": "multipart/form-data", // Đảm bảo định dạng yêu cầu là multipart/form-data
    },
  });
};

const getAllFriend = () =>{
  return axios.get("user/all_friend");
}


export {
  loginApi,
  registerApi,
  confirmTokenChangePassword,
  confirmRegister,
  reSendConfirmToken,
  changePasswordApi,
  searchUserApi,
  addFriend,
  checkStatusFriendRequest,
  getAllFriendRequest,
  confirmFriendRequest,
  deleteFriendRequest,
  getInfo,
  editInfo,
  deleteFriend,
  getAllFriend
};
