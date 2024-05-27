import React, { useState, useEffect } from "react";
import "./WidgetsOption.css";
import { Avatar, Button } from "@mui/material";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import {
  addFriend,
  checkStatusFriendRequest,
  deleteFriend,
  deleteFriendRequest,
} from "../service/userService";
import { Link, useNavigate } from "react-router-dom";

const WidgetsOption = ({ userId, name, textId, avatar }) => {
  const [followText, setFollowText] = useState("Add");
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const navigate = useNavigate();

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

  useEffect(() => {
    console.log(userId);
    setTextFollowButton();
  }, []);

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

  return (
    <div
      onClick={(e) => {
        navigate(`/home/profile/${userId}`);
      }}
      className="widgetsOption"
    >
      <Avatar className="profile-pic" src={avatar} alt="Profile Picture" />
      <div className="user-info">
        <strong>{name}</strong>
        <span>{textId}</span>
      </div>
      <Button className="follow-btn" onClick={handleFollowBtn}>
        {followText}
      </Button>
    </div>
  );
};

export default WidgetsOption;
