import React, { useEffect, useState } from "react";
import "./Notification.css";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import MessageSearchOption from "../MessageSearchOption/MessageSearchOption";
import {
  getAllFriendRequest,
  confirmFriendRequest,
  deleteFriendRequest,
} from "../service/userService";

const Notification = () => {
  const [friendRequests, setFriendRequests] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");
    const client = Stomp.over(socket);
    client.debug = null;

    client.connect({}, () => {
      setStompClient(client);
      setIsConnected(true);
      let userId = localStorage.getItem("userId");
      client.subscribe(`/topic/friend-request/${userId}`, (message) => {
        console.log(message);
        if (message.body === "add friend") {
          allFriendRequest();
        }
      });
    });

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, [stompClient]);

  useEffect(() => {
    allFriendRequest();
  }, []);

  const allFriendRequest = async () => {
    let res = await getAllFriendRequest();
    console.log(res.data);
    setFriendRequests(res.data);
  };

  const handleAccept = async (friendRequestId) => {
    let res = await confirmFriendRequest(friendRequestId);
    console.log(res.data);
  };

  const handleDelete = async (friendRequestId) => {
    console.log(friendRequestId);
    let res = await deleteFriendRequest(friendRequestId);
    console.log(res.data);
  };

  return (
    <div className="notifications">
      {friendRequests.map((friendRequest, index) => (
        <div className="notification" key={index}>
          <MessageSearchOption
            userId={friendRequest.sender.id}
            avatar={friendRequest.sender.avatar}
            name={friendRequest.sender.fullname}
            textId={friendRequest.sender.textId}
          />
          <div className="btnNotification">
            <button
              className="accept"
              onClick={() => handleAccept(friendRequest.id)}
            >
              Đồng ý
            </button>
            <button
              className="deleteRequest"
              onClick={() => handleDelete(friendRequest.id)}
            >
              Xóa lời mời kết bạn
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default Notification;
