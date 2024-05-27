import * as React from "react";
// import "./style.css";
import { useEffect, useState } from "react";
import { ZegoUIKitPrebuilt } from "@zegocloud/zego-uikit-prebuilt";
import { useParams } from "react-router-dom";
import { getInfo } from "../service/userService";

function randomID(len) {
  let result = "";
  if (result) return result;
  var chars = "12345qwertyuiopasdfgh67890jklmnbvcxzMNBVCZXASDQWERTYHGFUIOLKJP",
    maxPos = chars.length,
    i;
  len = len || 5;
  for (i = 0; i < len; i++) {
    result += chars.charAt(Math.floor(Math.random() * maxPos));
  }
  return result;
}

export function getUrlParams(url = window.location.href) {
  let urlStr = url.split("?")[1];
  return new URLSearchParams(urlStr);
}

const VideoCall = () => {
  const roomID = useParams();
  const [userCount, setUserCount] = useState(2);
  const [user, setUser] = useState(null);
  console.log(roomID.roomID);

  const getUser = async () => {

    let res = await getInfo(localStorage.getItem('userId'));
    setUser(res.data);
  };

  useEffect(() => {
    getUser();
  },[])


  let myMeeting = async (element) => {
    // generate Kit Token
    const appID = 955057366;
    const serverSecret = "90c488d14948a5e9bf09da135bd69954";
    const kitToken = ZegoUIKitPrebuilt.generateKitTokenForTest(
      appID,
      serverSecret,
      roomID.roomID,
      Date.now().toString(),
      user.fullname
    );

    // Create instance object from Kit Token.
    const zp = ZegoUIKitPrebuilt.create(kitToken);
    // start the call
    const handleLeaveRoom = () => {
      // Redirect to home page when leaving room
      window.location.href = `http://localhost:3000/home/message_null`; // Thay đổi URL này thành URL bạn muốn chuyển hướng đến
    };

    const handleUserLeave = () => {
      setUserCount((prevCount) => prevCount - 1); // Decrease user count when someone leaves the room
    };

    zp.joinRoom({
      container: element,
      sharedLinks: [
        {
          name: "Personal link",
          url:
            window.location.protocol +
            "//" +
            window.location.host +
            window.location.pathname +
            "?roomID=" +
            roomID,
        },
      ],
      scenario: {
        mode: ZegoUIKitPrebuilt.OneONoneCall, // To implement 1-on-1 calls, modify the parameter here to [ZegoUIKitPrebuilt.OneONoneCall].
      },
      showPreJoinView: false,
      showTextChat: false,
      showUserList: true,
      showLeavingView: false,
      onLeaveRoom: handleLeaveRoom,
      onUserLeave: handleUserLeave,

    });
  };
  useEffect(() => {
    if (userCount === 1) {
      // Close room logic here
      // Example: Send request to server to close the room
      console.log("Closing room because no one is left");

    }
  }, [userCount]);
  return (
    user && (
      <div
        className="myCallContainer"
        ref={myMeeting}
        style={{ width: "100vw", height: "100vh" }}
      ></div>
    )
  );
};

export default VideoCall;
