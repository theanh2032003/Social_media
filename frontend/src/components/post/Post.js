import React, { forwardRef, useState, useEffect } from "react";
import style from "./Post.module.scss";
import { Avatar } from "@mui/material";
import VerifiedUserIcon from "@mui/icons-material/VerifiedUser";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import RepeatIcon from "@mui/icons-material/Repeat";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import PublishIcon from "@mui/icons-material/Publish";
import FavoriteIcon from "@mui/icons-material/Favorite";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { Link, useNavigate } from "react-router-dom";
import {
  likePost,
  getLikesOfPost,
  checkIfUserIsLikePost,deletePost
} from "../service/postService";
const Post = forwardRef(
  ({ postId, displayName, userId, username, text, image, avatar }, ref) => {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);
    const [hasImage, setHasImage] = useState(false);
    const [isOwnerPost, setIsOwnerPost] = useState(null);
    const navigate = useNavigate();

    const goToPostDetail = () =>{
      navigate(`/home/post/${postId}`)
    }

    useEffect(() => {
      const userIdInLocalStorage = localStorage.getItem("userId");
      if (userIdInLocalStorage == userId) {
        setIsOwnerPost(true);
      } else {
        setIsOwnerPost(false);
      }
      checkLiked();
      console.log(isLiked);
      getLikes();
      if (image.length > 1) {
        setHasImage(true);
      }
    }, []);

    const getLikes = async () => {
      let res = await getLikesOfPost(postId);
      setLikeCount(res.data);
    };

    const handleDeletePost = async () => {
      let res = await deletePost(postId);
    }

    const checkLiked = async () => {
      let res = await checkIfUserIsLikePost(postId);
      setIsLiked(res.data);
    };

    const handleLike = async () => {
      let res = await likePost(postId);
      console.log(res.data);
      // setIsLiked(!isLiked);
      checkLiked();
      getLikes();
    };
    const showPreviousImage = () => {
      setCurrentIndex((currentIndex - 1 + image.length) % image.length);
    };

    const showNextImage = () => {
      setCurrentIndex((currentIndex + 1) % image.length);
    };

    return (
      <div className={style.post} ref={ref}>
        <div className={style.body}>
          <div className={style.header}>
            <div className={style.info}>
              <div className={style.avatar}>
                <Avatar src={avatar || "./default/nullAvatar.jpg"} />
              </div>
              <Link
                className={style.headerText}
                to={`/home/profile/${userId}`}
                style={{ textDecoration: "none", color: "inherit" }}
              >
                <h3>{displayName}</h3>
                <span className={style.textId}>{username}</span>
              </Link>
            </div>
            {isOwnerPost && (
              <div className={style.deleteBtn} onClick={handleDeletePost}>
                <DeleteOutlineIcon />
              </div>
            )}
          </div>
          <div className={style.content}>
            <p>{text}</p>
          </div>
          <div className={style.imagesWrapper}>
            <div
              className={style.images}
              style={{ transform: `translateX(-${currentIndex * 100}%)` }}
            >
              {image.map((img, index) => (
                      <div key={index} className={style.di}>
                      {img.endsWith(".png") || img.endsWith(".jpg") || img.endsWith(".jpeg") ? (
                        <img src={img} alt={`Image ${index + 1}`} />
                      ) : img.endsWith(".mp4") || img.endsWith(".avi") || img.endsWith(".mov") ? (
                        <video controls>
                          <source src={img} />
                        </video>
                      ) : (
                        <p>Unsupported file format</p>
                      )}
                    </div>
              ))}
            </div>
          </div>

          {hasImage && (
            <div className={style.scrollBtn}>
              <button className={style.prevButton} onClick={showPreviousImage}>
                Prev
              </button>
              <button className={style.nextButton} onClick={showNextImage}>
                Next
              </button>
            </div>
          )}

          <div className={style.footer}>
            <div>
              {isLiked ? (
                <FavoriteIcon
                  className={style.like_btn}
                  fontSize="small"
                  onClick={handleLike}
                />
              ) : (
                <FavoriteBorderIcon fontSize="small" onClick={handleLike} />
              )}
              <p>{likeCount}</p>
            </div>

            <ChatBubbleOutlineIcon fontSize="small" onClick = {goToPostDetail}/>
          </div>
        </div>
      </div>
    );
  }
);

export default Post;
