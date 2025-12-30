const showError = (message, htmlElement) => {
    htmlElement.textContent = message;

    htmlElement.classList.remove("shake");
    void htmlElement.offsetWidth;
    htmlElement.classList.add("shake");

    setTimeout(() => {
      errorText.classList.remove("shake");
    }, 500);
}

export default showError;