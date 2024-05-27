import React, { useState, useEffect, useRef } from "react";
import style from "./MessagePage.module.scss";
import { Avatar } from "@mui/material";
import MessageOption from "../MessageOption/MessageOption";
import SendIcon from "@mui/icons-material/Send";
import InsertPhotoIcon from "@mui/icons-material/InsertPhoto";
import CloseIcon from "@mui/icons-material/Close";
import VideoCallIcon from "@mui/icons-material/VideoCall";
import { useParams, useNavigate } from "react-router-dom";
import {
  getMessagesOfGroupChat,
  sendMessage,
  getNotificationCall,
} from "../service/chatService";
import { getInfo } from "../service/userService";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

const MessagePage = () => {
  const contentRef = useRef(null);
  const messagesWrapperRef = useRef(null);
  const sendMessageRef = useRef(null);
  const [content, setContent] = useState("");
  const [images, setImages] = useState([]);
  const [inputImages, setInputImages] = useState([]);
  const { userId } = useParams();
  const userId2 = localStorage.getItem("userId");
  const [messages, setMessages] = useState([]);
  const [user, setUser] = useState();
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [inputHeight, setInputHeight] = useState(0);
  let id1 = userId > userId2 ? userId2 : userId;
  let id2 = userId < userId2 ? userId2 : userId;
  const navigate = useNavigate();

  const handleInsertImage = () => {
    let input = document.getElementById("imageInput");
    if (input) {
      input.click();
    }
  };
  const editMessage = (messageId, isDeleted) => {
    const messageIndex = messages.findIndex(
      (message) => message.id === messageId
    );

    if (messageIndex === -1) {
      console.log("Không tìm thấy tin nhắn cần sửa");
      return;
    }

    const updatedMessages = [...messages];

    updatedMessages[messageIndex] = {
      ...updatedMessages[messageIndex],
      isDeleted: isDeleted,
    };

    setMessages(updatedMessages);
  };

  const getUser = async () => {
    console.log(userId);
    console.log(userId2);
    let res = await getInfo(userId);
    console.log(res.data);
    setUser(res.data);
  };

  const getMessages = async () => {
    console.log(userId);
    let res = await getMessagesOfGroupChat(userId);
    console.log(res.data);
    setMessages(res.data);
  };

  const send = async () => {
    const formData = new FormData();
    formData.append("content", content);
    formData.append("receiver", userId);
    formData.append("sender", userId2);
    inputImages.forEach((image, index) => {
      formData.append("images", image);
    });
    console.log(inputImages);
    let res = await sendMessage(formData);
  };

  const handleSendMessage = () => {
    if (content.trim() === "" && images.length === 0) {
      return; // Không cho phép gửi tin nhắn khi nội dung và hình ảnh đều trống
    }
    send();
    setContent("");
    setImages([]);
    setInputImages([]);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const handleImageInput = (e) => {
    const files = e.target.files;
    console.log(files);
    if (files) {
      const linkImageArray = Array.from(files).map((file) =>
        URL.createObjectURL(file)
      );
      const imageArray = Array.from(files);
      setImages((prevImages) => [...prevImages, ...linkImageArray]);
      setInputImages((prevImages) => [...prevImages, ...imageArray]);

      console.log(images);
    }
  };

  const handleImageDelete = (index) => {
    const newImages = [...images];
    newImages.splice(index, 1);
    setImages(newImages);

    const newInputImages = [...inputImages];
    newInputImages.splice(index, 1);
    setInputImages(newInputImages);
  };

  useEffect(() => {
    const textarea = contentRef.current;
    console.log(textarea);
    if (textarea) {
      textarea.style.height = "18px";
      textarea.style.height = `${textarea.scrollHeight}px`;
      // console.log(textarea.scrollHeight);
    }
  }, [content]);

  useEffect(() => {
    getUser();
    getMessages();
  }, [userId]);

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");
    const client = Stomp.over(socket);
    client.debug = null;

    client.connect({}, () => {
      setStompClient(client);
      setIsConnected(true);
      // console.log(`/topic/groupchat/${id1}_${id2}`);
      client.subscribe(`/topic/groupchat/${id1}_${id2}`, (message) => {
        console.log(message.body);
        const messageBody = JSON.parse(message.body);
        if (messageBody.type == "new message") {
          setMessages([messageBody.message, ...messages]);
        }

        if (messageBody.type == "delete message") {
          setMessages([messageBody.message, ...messages]);
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
    // Nếu có thay đổi trong messages và messagesWrapper đã được render
    if (messagesWrapperRef.current) {
      // Cuộn xuống dưới cùng của messagesWrapper
      messagesWrapperRef.current.scrollTop =
        messagesWrapperRef.current.scrollHeight;
    }
  }, [messages]);

  useEffect(() => {
    const updateMarginBottom = () => {
      const sendMessageHeight = sendMessageRef.current.clientHeight;
      messagesWrapperRef.current.style.marginBottom = `${sendMessageHeight}px`;
    };

    updateMarginBottom();

    window.addEventListener("resize", updateMarginBottom);

    return () => {
      window.removeEventListener("resize", updateMarginBottom);
    };
  }, [content, images]);

  const handleVideoCall = async () => {
    let res = await getNotificationCall(
      "join",
      `${id1}_${id2}`,
      localStorage.getItem("userId")
    );
    console.log(res.data);
    // navigate(`/video_call/${id1}_${id2}`);
  };

  return (
    <div className={style.MessagePage}>
      {user && (
        <div className={style.header}>
          <div style={{ display: "flex", alignItems: "center" }}>
            <Avatar className={style.avatar} src={user.avatar} />
            <p className={style.name}>{user.fullname}</p>
          </div>

          <VideoCallIcon className={style.call} onClick={handleVideoCall} />
        </div>
      )}
      <div
        className={style.messagesWrapper}
        id="messagesWrapper"
        ref={messagesWrapperRef}
      >
        <div className={style.messagesContainer}>
          {messages.length > 0 &&
            messages.map((message) => (
              <MessageOption
                key={message.id}
                id={message.id}
                avatar={message.user.avatar}
                content={message.text}
                images={message.images}
                userId={userId2}
                mes_userId={message.user.id}
                isDeleted={message.isDeleted}
              />
            ))}
        </div>
      </div>

      <div className={style.sendMessage} ref={sendMessageRef}>
        <div className={style.image}>
          <InsertPhotoIcon
            className={style.insertIcon}
            onClick={handleInsertImage}
          />
          <input
            id="imageInput"
            onChange={handleImageInput}
            type="file"
            style={{ display: "none" }}
            multiple
          />
        </div>
        <div className={style.contentInput}>
          <div className={style.imagePreview}>
            {inputImages.map((image, index) => (
              <div key={index} className={style.imageContainer}>
                {/* <img
                  className={style.image}
                  src={image}
                  alt={`Image ${index}`}
                /> */}

                {image.type === "image/png" && <img className={style.image} src={images[index]} />}
                {image.type === "video/mp4" && (
                  <video className={style.image} controls>
                    <source src={images[index]} />
                  </video>
                )}
                <CloseIcon
                  className={style.deleteIcon}
                  onClick={() => handleImageDelete(index)}
                />
              </div>
            ))}
          </div>

          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            onKeyDown={handleKeyDown}
            ref={contentRef}
            className={style.content}
            type="text"
          />
        </div>
      </div>
    </div>
  );
};

export default MessagePage;
