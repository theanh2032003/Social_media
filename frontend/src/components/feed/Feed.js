import React, { useState, useEffect } from "react";
import "./Feed.module.scss";
import TweetBox from "../tweetbox/TweetBox";
import Post from "../post/Post";
import FlipMove from "react-flip-move";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { getAllPostOfNewFeed } from "../service/postService";

const Feed = () => {
  const [posts, setPosts] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const currentState = {
    scrollY: window.scrollY
  };


  const navigateToPost = () => {
    window.history.pushState(currentState, "", "/new-page");
  };


  const handlePopState = (event) => {
    if (event.state) {
      window.scrollTo(event.state.scrollY);
    }
  };

  useEffect(() => {
    window.onpopstate = handlePopState;
    getPost();
  }, []);

  const getPost = async () => {
    let res = await getAllPostOfNewFeed();
    setPosts(res.data);
    console.log(res.data);
  };

  return (
    <div className="feed">
      <div className="feed__header">
        <h2>Home</h2>
      </div>

      <TweetBox />

      <div>
        {posts.map((post) => {
          return (
            <Post
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
      </div>
    </div>
  );
};

export default Feed;
