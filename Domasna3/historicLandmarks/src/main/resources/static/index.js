jQuery(function($) {
    $(".carousel").slick({
        slidesToShow: 1,
        prevArrow: '<div style="font-size: 25px" class="slick-prev fa fa-chevron-left"></div>',
        nextArrow: '<div style="font-size: 25px" class="slick-next fa fa-chevron-right"></div>',
    });
    $("#accordion" ).accordion({
        header: "> div > h3",
        collapsible: true,
        icons: null,
        heightStyle: "content",
        active: false,
        activate: function (event, ui) {
            if (!ui.newHeader.length) return;
            var scrollPosition = ui.newHeader.offset().top - $("#landmark-list").offset().top;
            $('#landmark-list').animate({
                scrollTop: '+=' + (scrollPosition - 15)
            }, 'fast');
        }
    });
});

let reviewForms = document.querySelectorAll(".review-form");
reviewForms.forEach(formDiv => {
    const form = formDiv.querySelector("form");
    form.addEventListener("submit", function(event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch(this.action, {
            method: this.method,
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.error("Error adding review:", response.statusText);
                }
            })
            .then(data => {
                if (data !== " ") {
                    const reviewDisplay = createCommentElement(data);
                    reviewDisplay.style.borderBottom = "1px solid rgb(230 230 230)";
                    reviewDisplay.style.paddingBottom = "10px";

                    const editFormDisplay = createEditForm(data);

                    const cardBody = formDiv.closest(".card-body");
                    const reviewContainer = cardBody.querySelector(".review-container");
                    const end = cardBody.querySelector(".endOfReviews")

                    cardBody.insertBefore(reviewDisplay, end);
                    cardBody.insertBefore(editFormDisplay, end);

                    cardBody.querySelector(".landmark-rating").textContent = parseFloat(data.avgRating).toFixed(1);
                    cardBody.querySelector("progress.rating-bg").value = parseFloat(data.avgRating).toFixed(1);
                    cardBody.querySelector(".numReviews").textContent = data.numReviews;

                    form.querySelector("input").value = "";
                    form.querySelector("textarea").value = "";

                    attachEditReviewButtonsListener();
                    attachDeleteReviewButtonsListener();
                    attachEditFormSubmitListener();
                } else {
                    console.error("Error adding review:", data.message);
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    });
});
function createCommentElement(review) {
    const display = document.createElement("div");
    display.classList.add("review-display");
    display.setAttribute("style", "display: flex; align-items: flex-start; margin-bottom: 10px");
    display.setAttribute("data-review-id", review.id);

    const userPhotoDiv = document.createElement("div");
    userPhotoDiv.classList.add("user-photo");
    userPhotoDiv.setAttribute("style", "background-size:cover; height: 40px; width: 40px; border-radius: 50%; margin-right: 8px");
    userPhotoDiv.style.backgroundImage = "url('" + review.photoUrl + "')";

    const commentTextDiv = document.createElement("div");
    commentTextDiv.style.flex = "1";

    const bold = document.createElement("b");
    const username = document.createElement("span");
    username.textContent = review.username + " ";
    username.classList.add("review-username");
    bold.appendChild(username);

    const comment = document.createElement("span");
    comment.textContent = review.comment;
    comment.classList.add("review-comment");

    commentTextDiv.appendChild(bold);
    commentTextDiv.appendChild(comment);

    const buttons = document.createElement("div");
    buttons.style.marginLeft = "5px";

    const deleteButton = document.createElement("a");
    deleteButton.setAttribute("style", "font-size: 12px; padding: 2px 4px; width: 22px; margin-bottom: 4px;");
    deleteButton.setAttribute("data-review-id", review.id);
    deleteButton.setAttribute("data-landmark-id", review.landmarkId);
    deleteButton.classList.add("btn", "btn-light", "btn-outline-secondary", "fa", "fa-trash", "delete-review-button");

    const editButton = document.createElement("a");
    editButton.setAttribute("style", "font-size: 12px; padding: 2px 4px; width: 22px;");
    editButton.setAttribute("data-review-id", review.id);
    editButton.setAttribute("data-landmark-id", review.landmarkId);
    editButton.classList.add("btn", "btn-light", "btn-outline-secondary", "fa", "fa-edit", "edit-review-button");

    const lineBreak = document.createElement("br");
    buttons.appendChild(deleteButton);
    buttons.appendChild(lineBreak);
    buttons.appendChild(editButton);

    display.appendChild(userPhotoDiv);
    display.appendChild(commentTextDiv);
    display.appendChild(buttons);

    return display;
}

function createEditForm(review){
    let form = document.createElement("form");
    form.setAttribute("action", "/review/edit/" + review.id + "/" + review.landmarkId);
    form.setAttribute("method", "post");
    form.setAttribute("data-review-id", review.id);
    form.setAttribute("data-landmark-id", review.landmarkId);
    form.setAttribute("style", "display: none; border-bottom: 1px solid rgb(230 230 230); padding-bottom: 10px; margin-bottom: 10px;");
    form.classList.add("edit-form");

    let input = document.createElement("input");
    input.setAttribute("type", "number");
    input.setAttribute("name", "editRating");
    input.value = parseFloat(review.rating).toFixed(1);
    input.setAttribute("min", "1");
    input.setAttribute("max", "5");
    input.setAttribute("step", "0.1");
    input.classList.add("form-control");

    let textarea = document.createElement("textarea");
    textarea.setAttribute("name", "editComment");
    textarea.value = review.comment;
    textarea.textContent = review.comment;
    textarea.classList.add("form-control");

    let submit = document.createElement("button");
    submit.setAttribute("type", "submit");
    submit.textContent = "Save";
    submit.setAttribute("data-review-id", review.id);
    submit.classList.add("btn", "btn-light", "save-edited-review-button");

    form.appendChild(input);
    form.appendChild(textarea);
    form.appendChild(submit);

    return form;
}

attachEditReviewButtonsListener();
attachDeleteReviewButtonsListener();
attachEditFormSubmitListener();

function attachEditReviewButtonsListener(){
    let editReviewButtons = document.querySelectorAll(".edit-review-button");
    editReviewButtons.forEach(button => {
        button.removeEventListener("click", toggleEdit);
    });
    editReviewButtons.forEach(button => {
        button.addEventListener("click", toggleEdit);
    });
}
function toggleEdit(event) {
    event.preventDefault();
    const reviewId = this.getAttribute("data-review-id");
    const landmarkId = this.getAttribute("data-landmark-id");
    const editForm = document.querySelector(`form.edit-form[data-review-id="${reviewId}"]`);
    const reviewForm = document.querySelector(`div.review-form[data-landmark-id="${landmarkId}"]`);
    if(editForm.style.display === "none"){
        document.querySelectorAll('form.edit-form')
            .forEach(f => f.style.display = "none");
        editForm.style.display = "block";
        reviewForm.style.display = "none";
    }else{
        editForm.style.display = "none";
        reviewForm.style.display = "block";
    }
}
function attachDeleteReviewButtonsListener() {
    let deleteReviewButtons = document.querySelectorAll(".delete-review-button");
    deleteReviewButtons.forEach(button => {
        button.removeEventListener("click", deleteReview);
    });
    deleteReviewButtons.forEach(button => {
        button.addEventListener("click", deleteReview);
    });
}
function deleteReview(event) {
    event.preventDefault();
    const button = event.target;
    const reviewId = button.getAttribute("data-review-id");
    const landmarkId = button.getAttribute("data-landmark-id");
    fetch(`/review/delete/${reviewId}/${landmarkId}`, {
        method: 'POST',
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error deleting review:", response.statusText);
            }
        })
        .then(data => {
            if (data !== " ") {
                const cardBody = button.closest(".card-body");
                cardBody.querySelector(".landmark-rating").textContent = parseFloat(data.avgRating).toFixed(1);
                cardBody.querySelector("progress.rating-bg").value = parseFloat(data.avgRating).toFixed(1);
                cardBody.querySelector(".numReviews").textContent = data.numReviews;

                const reviewElement = button.closest('.review-display');
                reviewElement.remove();
            } else {
                console.error("Error deleting review:", data.message);
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}


function attachEditFormSubmitListener(){
    let editForms = document.querySelectorAll(".edit-form");
    editForms.forEach(form => {
        form.removeEventListener("submit", saveEditedReview);
    });
    editForms.forEach(form => {
        form.addEventListener("submit", saveEditedReview);
    });
}
function saveEditedReview(event){
    event.preventDefault();
    const formData = new FormData(this);

    fetch(this.action, {
        method: this.method,
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error editing review:", response.statusText);
            }
        })
        .then(data => {
            if (data !== " ") {
                const cardBody = this.closest(".card-body");
                cardBody.querySelector(".landmark-rating").textContent = parseFloat(data.avgRating).toFixed(1);
                cardBody.querySelector("progress.rating-bg").value = parseFloat(data.avgRating).toFixed(1);
                cardBody.querySelector(".numReviews").textContent = data.numReviews

                const reviewDisplay = document.querySelector(`div.review-display[data-review-id="${data.id}"]`);

                this.querySelector("input").value = data.rating;
                this.querySelector("textarea").text = data.comment;
                reviewDisplay.querySelector(".review-comment").textContent = data.comment;

                const reviewForm = document.querySelector(`div.review-form[data-landmark-id="${data.landmarkId}"]`);
                reviewForm.style.display = "block";
                this.style.display = "none";
            } else {
                console.error("Error editing review:", data.message);
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}




let landmarks = document.getElementById("landmarksVar").innerHTML
let listLandmarks = landmarks.split("), HistoricLandmark");
listLandmarks = listLandmarks.map(each => each.concat(")").replace("[","").replace(", HistoricLandmark(",'HistoricLandmark('));
landmarks = []
addresses=[]
names=[]
regions=[]
for(let l of listLandmarks){
    landmarks.push([parseFloat(l.split(", ")[1].split("=")[1]), parseFloat(l.split(", ")[2].split("=")[1])])
    addresses.push(l.split(", ")[5].split("=")[1])
    names.push(l.split(", ")[4].split("=")[1])
    regions.push(l.split(", ")[6].split("=")[1].replace(")", ""))
}

var map = L.map('map').setView([41.9946, 21.4266], 11);
mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: 'Leaflet &copy; ' + mapLink + ', contribution', maxZoom: 18 }).addTo(map);

// var taxiIcon = L.icon({
//     iconUrl: 'img/red.png',
//     iconSize: [30, 30]
// })

for(let i=0;i<landmarks.length;i++){
    L.marker(landmarks[i]).bindTooltip(names[i]+", "+regions[i]).addTo(map);
}

if(landmarks.length!==0)
    map.fitBounds(landmarks);
function selectLandmark(lat, lon){
    map.fitBounds([[parseFloat(lat), parseFloat(lon)]]);
}

// function addMarker(){
//     var newMarker = L.marker([42.0195738, 20.960153]).addTo(map);
//     L.Routing.control({
//         waypoints: [
//             L.latLng(41.9936737, 21.433319),
//             L.latLng(42.0195738, 20.960153)
//         ]
//     }).addTo(map);
// }