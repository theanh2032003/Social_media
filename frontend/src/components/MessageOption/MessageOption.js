import React, { useState, useEffect } from "react";
import { Avatar } from "@mui/material";
import style from "./MessageOption.module.scss";
import { deleteMessage } from "../service/chatService";
import DeleteIcon from "@mui/icons-material/Delete";
const MessageOption = ({
  id,
  avatar,
  content,
  images,
  userId,
  mes_userId,
  isDeleted,
}) => {
  const [isLeft, setIsLeft] = useState(false);
  const [isMesDeleted, setIsMesDeleted] = useState();
  const handleDeleteMessage = async () => {
    let res = await deleteMessage(id, userId, mes_userId);
  };

  useEffect(() => {
    if (userId != mes_userId) {
      console.log(userId);
      console.log(mes_userId);
      setIsLeft(true);
      console.log(isLeft);
    }
    // setIsMesDeleted(isDeleted);
  }, []);

  return (
    <div className={style.messageContainer}>
      {isLeft && (
        <div className={style.leftMessage}>
          <Avatar className={style.avatar} src={avatar} />
          <div className={style.content}>
          {images &&
              images.map((img, index) => (
                <>
                  {img.endsWith(".png") ||
                  img.endsWith(".jpg") ||
                  img.endsWith(".jpeg") ? (
                    <img src={img} className={style.image} alt={`Image ${index + 1}`} />
                  ) : img.endsWith(".mp4") ||
                    img.endsWith(".avi") ||
                    img.endsWith(".mov") ? (
                    <video className={style.image} controls>
                      <source src={img} />
                    </video>
                  ) : (
                    <p>Unsupported file format</p>
                  )}
                </>
              ))}
            {content != "" && <p className={style.text}>{content}</p>}
          </div>
        </div>
      )}

      {!isLeft && (
        <div className={style.rightMessage}>
          {/* <Avatar className={style.avatar} src={avatar} /> */}
          <div className={style.content}>
            {images &&
              images.map((img, index) => (
                <>
                  {img.endsWith(".png") ||
                  img.endsWith(".jpg") ||
                  img.endsWith(".jpeg") ? (
                    <img src={img} className={style.image} alt={`Image ${index + 1}`} />
                  ) : img.endsWith(".mp4") ||
                    img.endsWith(".avi") ||
                    img.endsWith(".mov") ? (
                    <video className={style.image} controls>
                      <source src={img} />
                    </video>
                  ) : (
                    <p>Unsupported file format</p>
                  )}
                </>
              ))}
            {content != "" && <p className={style.text}>{content}</p>}
          </div>
        </div>
      )}
    </div>
  );
};

export default MessageOption;
