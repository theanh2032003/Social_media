import React, { useState, useEffect } from "react";
import styles from "./Home.module.scss";
import SideBar from "../sidebar/SideBar";
import Feed from "../feed/Feed";
import Widgets from "../widget/Widgets";
import Profile from "../profile/Profile";
import Notification from "../notification/Notification";
import EditProfile from "../editProfile/EditProfile";
import PostDetail from "../PostDetail/PostDetail";
import MessagePage from "../Message/MessagePage";
import { getAllPostOfNewFeed } from "../service/postService";
import { searchUserApi } from "../service/userService";
import { Route, Routes, useLocation } from "react-router-dom";
import MessageSearch from "../MessageSearch/MessageSearch";
import FriendPage from "../friend/FriendPage";
const Home = () => {
  const location = useLocation();
  const showWidget =
    location.pathname.startsWith("/home") &&
    !location.pathname.startsWith("/home/message");
  return (
    <div className={styles.homePage}>
      <div className={styles.sidebar}>
        <SideBar />
      </div>
      {showWidget && (
        <div className={styles.content}>
          <Routes>
            <Route path="/profile/:userId" element={<Profile />} />
            <Route path="/" element={<Feed />} />
            <Route path="/notification" element={<Notification />} />
            <Route path="/edit_profile" element={<EditProfile />} />
            <Route path="/post/:postId" element={<PostDetail />} />
            <Route path="/friends" element={<FriendPage />} />

          </Routes>
        </div>
      )}

      {showWidget && (
        <div className={styles.widget}>
          <Widgets />
        </div>
      )}

      {!showWidget && (
        <div className={styles.messageSearch}>
          <MessageSearch/>
        </div>
      )}

      {!showWidget && (
        <div className={styles.message}>
          <Routes>
            <Route path="/message/:userId" element={<MessagePage />} />
            <Route path="/message_null" element={<div style={{height:"100vh"}}></div>} />

          </Routes>
        </div>
      )}
    </div>
  );
};

export default Home;
