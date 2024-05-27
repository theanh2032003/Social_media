import React, { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import style from "./Profile.module.scss";
import anh from "./avatar-dep-84.jpg";
import Post from "../post/Post";
import EditProfile from "../editProfile/EditProfile";
import { Avatar } from "@mui/material";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { Link } from "react-router-dom";
import {
  getInfo,
  addFriend,
  checkStatusFriendRequest,
  deleteFriend,
  deleteFriendRequest,
} from "../service/userService";
import { getPostsOfProfile, getPostIsLiked } from "../service/postService";

const Profile = ({}) => {
  const [user, setUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [postsIsLiked, setPostsIsLiked] = useState([]);
  const [isUser, setIsUser] = useState(false);
  const [isPostActive, setIsPostActive] = useState(true);
  const [isLikePostActive, setIsLikePostActive] = useState(false);
  const [scrollDisabled, setScrollDisabled] = useState(false);
  const scrollContainerRef = useRef(null);
  const [followText, setFollowText] = useState("Add");
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const { userId } = useParams();
  const getUserInfo = async (userId) => {
    let res = await getInfo(userId);
    console.log(res.data);
    setUser(res.data);
  };

  const getPosts = async (userId) => {
    let res = await getPostsOfProfile(userId);
    setPosts(res.data);
  };

  const getPostWhatIsLikedByUser = async (userId) => {
    let res = await getPostIsLiked(userId);
    setPostsIsLiked(res.data);
  };

  const handleFocusPost = () => {
    if (isPostActive === false) {
      setIsPostActive(true);
      setIsLikePostActive(false);
    }
  };

  const handleFocusLikePost = () => {
    if (isLikePostActive === false) {
      setIsPostActive(false);
      setIsLikePostActive(true);
    }
  };

  const handleAddFriend = async () => {
    let res = await addFriend(userId);
    console.log(res.data);
  };

  const handleDeleteFriend = async () => {
    let res = await deleteFriend(userId);
    console.log(res.data);
  };

  const handleDeleteFriendRequest = async () => {
    let res = await deleteFriendRequest(userId);
    console.log(res.data);
  };

  const handleFollowBtn = (e) => {
    if (followText === "Add") {
      handleAddFriend();
    }
    if (followText === "Sended") {
      handleDeleteFriendRequest();
    }

    if (followText == "Friend") {
      handleDeleteFriend();
    }
  };

  const setTextFollowButton = async () => {
    let res = await checkStatusFriendRequest(userId);
    setFollowText(res.data);
  };

  useEffect(() => {
    console.log(userId);
    if (localStorage.getItem("userId") != null) {
      const userIdInLocalStorage = localStorage.getItem("userId");
      if (userIdInLocalStorage === userId) {
        setIsUser(true);
      } else {
        setTextFollowButton();
        setIsUser(false);
      }
      getUserInfo(userId);
      console.log(user);
    }

    getPosts(userId);
  }, [userId]);

  
  useEffect(() => {
    console.log(userId);
    if (localStorage.getItem("userId") != null) {
      const userIdInLocalStorage = localStorage.getItem("userId");
      if (userIdInLocalStorage === userId) {
        setIsUser(true);
      } else {
        setTextFollowButton();
        setIsUser(false);
      }
      getUserInfo(userId);
      console.log(user);
    }

    getPosts(userId);
  }, []);

  useEffect(() => {
    if (isPostActive === true) {
      getPosts(userId);
    } else {
      getPostWhatIsLikedByUser(userId);
    }
    console.log("isActive đã thay đổi");
  }, [isPostActive]);



  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");
    const client = Stomp.over(socket);
    client.debug = null;
    client.connect({}, () => {
      setStompClient(client);
      setIsConnected(true);
      client.subscribe(`/topic/friend-request/${userId}`, (message) => {
        console.log("kết nối");
        if (message.body === "add friend") {
          setTextFollowButton();
        }
      });
    });

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  });
  return (
    <div className={style.wrapper}>
      <div className={style.profile}>
        <div className={style.header}>
          <div className={style.banner}>
            <img
              className={style.bannerImage}
              src={user?.banner || "/default/defaultBanner.jpg"}
            />
          </div>

          <div className={style.userDetail}>
            <div className={style.avatar}>
              <Avatar
                className={style.picture}
                src={user?.avatar || "/default/nullAvatar.jpg"}
                alt="Profile Picture"
              />
            </div>
            <div className={style.infoButton}>
              {isUser ? (
                <Link
                  to={"/home/edit_profile"}
                  style={{ textDecoration: "none" }}
                  className={`${style.followButton} ${style.btn}`}
                >
                  Edit profile
                </Link>
              ) : (
                <div className={`${style.followButton} ${style.btn}`}>
                  {followText}
                </div>
              )}
            </div>
          </div>

          <div className={style.info}>
            <div className={style.infoText}>
              <p className={style.fullname}>{user?.fullname}</p>
              <p className={style.description}>{user?.textId}</p>
              <p>{user?.bio}</p>
              <p>{user?.location}</p>
            </div>
            <ul className={style.navbar}>
              <li
                className={isPostActive ? style.active : ""}
                onClick={handleFocusPost}
              >
                Posts
              </li>
              <li
                className={isLikePostActive ? style.active : ""}
                onClick={handleFocusLikePost}
              >
                Likes
              </li>
            </ul>
          </div>
        </div>

        <div className={style.container}>
          {isPostActive && (
            <>
              {posts.map((post) => {
                return (
                  <Post
                    key={post.id} // Đảm bảo mỗi phần tử trong danh sách có một key duy nhất
                    postId={post.id}
                    displayName={post.user.fullname}
                    username={post.user.textId}
                    text={post.content}
                    avatar={post.user.avatar}
                    image={post.images || []}
                    userId={post.user.id}
                  />
                );
              })}
            </>
          )}

          {isLikePostActive && (
            <>
              {postsIsLiked.map((post) => {
                return (
                  <Post
                    key={post.id} // Đảm bảo mỗi phần tử trong danh sách có một key duy nhất
                    postId={post.id}
                    displayName={post.user.fullname}
                    username={post.user.textId}
                    text={post.content}
                    avatar={post.user.avatar}
                    image={post.images}
                    userId={post.user.id}
                  />
                );
              })}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;
