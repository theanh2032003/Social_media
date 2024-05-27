import React, { useState, useEffect } from "react";
import Post from "../post/Post";
import { useParams, useNavigate } from "react-router-dom";
import { getPostById } from "../service/postService";
import {
  getAllCommentOfPost,
  createComment,
  getAll1AncestorOfPost,
} from "../service/commentService";
import { getInfo } from "../service/userService";
import style from "./PostDetail.module.scss";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import SendIcon from "@mui/icons-material/Send";
import { Avatar } from "@mui/material";
import Comment from "../Comment/Comment";
const PostDetail = () => {
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const { postId } = useParams();
  const [text, setText] = useState("");
  const [user, setUser] = useState();
  const navigate = useNavigate();

  const goBack = () => {
    navigate(-1);
  };

  const getUserInfo = async () => {
    let userId = localStorage.getItem("userId");
    let res = await getInfo(userId);
    console.log(res.data);
    setUser(res.data);
  };

  const getPost = async () => {
    let res = await getPostById(postId);
    console.log(res.data);
    setPost(res.data);
  };

  const getComment = async () => {
    let res = await getAll1AncestorOfPost(postId);
    setComments(res.data);
    console.log(res.data);
  };

  const handleCreateComment = async () => {
    let res = await createComment(text, postId, null);
    setText("");
    setComments([...comments, res.data]);
    console.log(res.data);
  };

  useEffect(() => {
    console.log(postId);
    getPost();
    getComment();
    getUserInfo();
  }, [postId]);

  useEffect(() => {
    const textarea = document.getElementById("textareaId"); // lấy ra textarea bằng id

    // Cập nhật kích thước của textarea dựa trên scrollHeight
    textarea.style.height = "18px";
    textarea.style.height = `${textarea.scrollHeight}px`;
  }, [text]);

  return (
    <div className={style.postDetail}>
      <div className={style.backHome} onClick={goBack}>
        <ArrowBackIcon />
      </div>
      {post && (
        <Post
          postId={postId}
          displayName={post.user.fullname}
          username={post.user.textId}
          text={post.content}
          avatar={post.user.avatar}
          image={post.images || []}
          userId={post.user.id}
        />
      )}

      <div className={style.createComment}>
        {user && <Avatar src={user.avatar} sx={{ fontSize: "25px" }} />}

        <div className={style.inputCommentArae}>
          <textarea
            id="textareaId"
            className={style.cmtInput} // sử dụng className từ stylesheet
            value={text}
            onChange={(e) => setText(e.target.value)} // cập nhật state khi nội dung thay đổi
          />

          <div className={style.sendCmt} onClick={handleCreateComment}>
            <SendIcon />
          </div>
        </div>
      </div>

      {comments &&
        comments.map((cmt, index) => (
          <Comment
            cmtId={cmt.id}
            avatar={cmt.userDto.avatar}
            fullname={cmt.userDto.fullname}
            content={cmt.content}
            time={cmt.createAt}
            descendants={cmt.descendants}
            postId={cmt.postId}
          />
        ))}

      {!post && <p>Loading...</p>}
    </div>
  );
};

export default PostDetail;
