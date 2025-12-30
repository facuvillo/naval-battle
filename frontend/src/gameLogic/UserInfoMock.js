export let userInfoMock = {
  userId: "2c681643-40fb-4dad-9caa-a9b345cfa4c4",
  username: "PlayerTwo",
  email: "jugador2@gmail.com",
  currentGame: null,
};

export function getUserInfo() {
  const stored = localStorage.getItem("userInfo");
  return stored ? JSON.parse(stored) : null;
}

export function setUserInfo(newUserInfo) {
  localStorage.setItem("userInfo", JSON.stringify(newUserInfo));
}

export function clearUserInfo() {
  localStorage.removeItem("userInfo");
}

export function setCurrentTopicBase(topicBase) {
  const userInfo = getUserInfo();
  if (userInfo) {
    userInfo.currentGame = topicBase;
    setUserInfo(userInfo);
  }
}

export function removeCurrentTopicBase() {
  const userInfo = getUserInfo();
  if (userInfo) {
    userInfo.currentGame = null;
    setUserInfo(userInfo);
  }
}
