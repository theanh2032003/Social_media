import React, { useState, useEffect, useRef } from "react";
import style from "./Comment.module.scss";
import { Avatar } from "@mui/material";
import { format } from "date-fns";
import SendIcon from "@mui/icons-material/Send";
import SubdirectoryArrowRightIcon from "@mui/icons-material/SubdirectoryArrowRight";
import {
  getAll1DescendantOfCmt,
  createComment,
} from "../service/commentService";
import { getInfo } from "../service/userService";
const Comment = ({
  cmtId,
  avatar,
  fullname,
  content,
  time,
  descendants,
  postId
}) => {
  const [isOpenRepCmt, setIsOpenRepCmt] = useState(false);
  const [isOpenChildCmt, setIsOpenChildCmt] = useState(false);
  const [isOpenShowNumOfDescendant, setIsOpenShowNumOfDescendant] =
    useState(false);
  const [childCmt, setChildCmt] = useState([]);
  const [user, setUser] = useState();
  const [textAtCmt, setTextAtCmt] = useState("");
  const textareaRef = useRef(null);
  const formatedTime = `${time[2]} tháng ${time[1]}, ${time[0]} lúc ${time[3]}:${time[4]}`;
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const textarea = textareaRef.current; 
    console.log(textarea);
    if (textarea) {
      textarea.style.height = "18px";
      textarea.style.height = `${textarea.scrollHeight}px`;
      console.log(textarea.scrollHeight);
    }
  }, [textAtCmt]);

  const getDescendantCmt = async () => {
    let res = await getAll1DescendantOfCmt(descendants);
    setChildCmt(res.data);
    console.log(res.data);
  };

  const handleCreateComment = async () => {
    let res = await createComment(textAtCmt, postId, cmtId);
    setChildCmt(prevChildCmt => [...prevChildCmt, res.data]);
    setTextAtCmt("");
    console.log(res.data);
    setIsOpenRepCmt(false);
    setIsOpenShowNumOfDescendant(false);
    setIsOpenChildCmt(true);
  };

  const getUser = async () => {
    let res = await getInfo(userId);
    setUser(res.data);
  };

  useEffect(() => {
    console.log(descendants);
    if (descendants.length > 0) {
      setIsOpenShowNumOfDescendant(true);
    }
    getUser();
  }, []);

  const handleClickShowDescendantCmt = () => {
    setIsOpenShowNumOfDescendant(false);
    getDescendantCmt();
    setIsOpenChildCmt(true);
  };

  return (
    <div className={style.commentContainer}>
      <Avatar src={avatar} />
      <div className={style.comment}>
        <div className={style.content}>
          <p className={style.fullname}>{fullname}</p>
          <p className={style.cmtContent}>{content}</p>
        </div>

        <div className={style.propertie}>
          <p className={style.time}>{formatedTime}</p>
          <p className={style.rep} onClick={() => setIsOpenRepCmt(true)}>
            Phản hồi
          </p>
        </div>
        {isOpenShowNumOfDescendant && (
          <div
            className={style.numOfDescendants}
            onClick={handleClickShowDescendantCmt}
          >
            <SubdirectoryArrowRightIcon />
            <p className={style.NumOfDescendantsText}>
              Xem tất cả {descendants.length} phản hồi
            </p>
          </div>
        )}

        {isOpenChildCmt && (
          <div className={style.childCmt}>
            {childCmt.map((cmt, index) => {
              return (
                <Comment
                  cmtId={cmt.id}
                  avatar={cmt.userDto.avatar}
                  fullname={cmt.userDto.fullname}
                  content={cmt.content}
                  time={cmt.createAt}
                  descendants={cmt.descendants}
                  postId={postId}
                />
              );
            })}
          </div>
        )}
        {user && (
          <div
            className={style.createCommentAtCmt}
            style={{ display: isOpenRepCmt ? "flex" : "none" }}
          >
            <Avatar src={user.avatar} />

            <div className={style.inputCommentAraeAtCmt}>
              <textarea
                id="textareaIdAtCmt"
                ref={textareaRef}
                className={style.cmtInputAtCmt} // sử dụng className từ stylesheet
                value={textAtCmt}
                onChange={(e) => setTextAtCmt(e.target.value)}
              />

              <div className={style.sendCmtAtCmt} onClick={handleCreateComment}>
                <SendIcon />
              </div>
            </div>
          </div>
        )}
      </div>
      {/* {isLastComment && <div style={{ height: "200px" }}></div>} */}
    </div>
  );
};

export default Comment;
