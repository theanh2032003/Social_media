import "./App.css";
import React, { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
import Home from "./components/home/Home";
import Login from "./components/login/Login";
import Notification from "./components/notification/Notification";
import Profile from "./components/profile/Profile";
import Feed from "./components/feed/Feed";
import EditProfile from "./components/editProfile/EditProfile";
import PostDetail from "./components/PostDetail/PostDetail";
import MessagePage from "./components/Message/MessagePage";
import FriendPage from "./components/friend/FriendPage";
import VideoCall from "./components/videoCall/videoCall";
import CallNotificationComponent from "./components/PreviewJoinCall/CallNotificationComponent";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { getNotificationCall } from "./components/service/chatService";
import { getInfo } from "./components/service/userService";
function App() {
  const [isOpen, setIsOpen] = useState(false);
  const [caller, setCaller] = useState(null);
  const [called, setCalled] = useState(null);

  const [groupChatId, setGroupChatId] = useState("");
  // const [caller, setCaller] = useState();

  const getUser = async (userId) => {
    console.log(userId);
    let res = await getInfo(userId);
    console.log(res.data);
    return res.data;
  };

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log("Đã kết nối");
        stompClient.subscribe("/topic/call", async(notification) => {
          console.log(notification.body);
          const callInfo = JSON.parse(notification.body);

          console.log(callInfo);
          if (
            callInfo.type === "join" &&
            callInfo.message.includes(localStorage.getItem("userId"))
          ) {
            let id = callInfo.message.split("_");
            let userId =
              callInfo.callerId == parseInt(id[0], 10)
                ? parseInt(id[1], 10)
                : parseInt(id[0], 10);
            console.log(callInfo.caller);
            
            const calledUser = await getUser(userId);
            const callerUser = await getUser(callInfo.callerId);
            setCalled(calledUser);
            setCaller(callerUser);
            setGroupChatId(callInfo.message);
            setIsOpen(true);
          }

          if (
            callInfo.type === "accept" &&
            callInfo.message.includes(localStorage.getItem("userId"))
          ) {
            window.location.href = `/video_call/${callInfo.message}`;
          }

          if (
            callInfo.type === "close" &&
            callInfo.message.includes(localStorage.getItem("userId"))
          ) {
            setCaller();
            setCalled();
            setGroupChatId("");
            setIsOpen(false);
          }
        });
      },
      onDisconnect: () => console.log("Đã ngắt kết nối"),
    });

    stompClient.activate();

    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []);

  const handleAcceptCall = async () => {
    // Điều hướng tới phòng gọi video
    // window.location.href = `/video_call/${groupChatId}`;
    await getNotificationCall("accept", groupChatId, caller.id);
  };

  const onClose = async () => {
    console.log(caller.id);
    let res = await getNotificationCall("close", groupChatId, caller.id);
    console.log(res.data);
    // navigate(`/video_call/${id1}_${id2}`);
  };

  return (
    <>
      <Routes>
        <Route path="/home/*" element={<Home />}>
          <Route path="" element={<Feed />} />
          <Route path="notification" element={<Notification />} />
          {/* <Route path="/messages" element={<Messages />} /> */}
          <Route path="profile/:userId" element={<Profile />} />
          <Route path="edit_profile" element={<EditProfile />} />
          <Route path="post/:postId" element={<PostDetail />} />
          <Route path="message/:userId" element={<MessagePage />} />
          <Route path="message_null" element={<div></div>} />
          <Route path="friends" element={<FriendPage />} />
        </Route>
        <Route path="/" element={<Login />} />
        <Route path="/video_call/:roomID" element={<VideoCall />} />
      </Routes>
      {caller && called &&
      <CallNotificationComponent
        caller={caller}
        isOpen={isOpen}
        onClose={onClose}
        onAcceptCall={handleAcceptCall}
        isCalled={called}
      />      
      }

    </>
  );
}

export default App;
