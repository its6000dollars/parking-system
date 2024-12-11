window.onload = function () {
    const messageElement = document.getElementById("message");
    if (messageElement) {
        setTimeout(() => {
            messageElement.style.display = "none";
        }, 2000);
    }
};

document.getElementById("parkButton").addEventListener("click", function () {
    submitForm("/park");
});

document.getElementById("removeButton").addEventListener("click", function () {
    submitForm("/remove");
});

function submitForm(action) {
    const form = document.getElementById("parkingForm");
    const formData = new FormData(form);
    fetch(action, {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(html => {
            document.open();
            document.write(html);
            document.close();
        })
        .catch(error => console.error("Error:", error));
}