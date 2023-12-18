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
    $('#edit-landmark-table').DataTable();
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
addresses = []
names = []
regions = []
manhDist = []
ids = []

for(let l of listLandmarks){
    ids.push(parseInt(l.split(", ")[0].split("=")[1]))
    landmarks.push([parseFloat(l.split(", ")[1].split("=")[1]), parseFloat(l.split(", ")[2].split("=")[1])])
    addresses.push(l.split(", ")[5].split("=")[1])
    names.push(l.split(", ")[4].split("=")[1])
    regions.push(l.split(", ")[6].split("=")[1].replace(")", ""))
}

var map = L.map('map').setView([41.9946, 21.4266], 11);
mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: 'Leaflet &copy; ' + mapLink + ', contribution', maxZoom: 18 }).addTo(map);

var greenIcon = L.icon({
    iconUrl: 'img/greenIcon.png',
    iconSize: [25, 40]
})

for(let i=0;i<landmarks.length;i++){
    L.marker(landmarks[i]).bindTooltip(names[i]+", "+regions[i]).addTo(map);
}

if(landmarks.length!==0)
    map.fitBounds(landmarks);

//get user location
let userLocation;
L.control.locate({setView: false, drawCircle: false, drawMarker: false, locateOptions: {
        enableHighAccuracy: true
    }}).addTo(map).start( );

map.on('locationfound', function(e) {
    userLocation = e.latlng
    manhDist = []
    manhDistIds = []
    for(let i=0;i<landmarks.length;i++){
        manhDist.push([Math.abs(parseFloat(userLocation.lat)-landmarks[i][0])+Math.abs(parseFloat(userLocation.lng)-landmarks[i][1]), i])
    }
});

let routing = L.Routing.control({
    waypoints: [
        userLocation,
        L.latLng(landmarks[0][0], landmarks[0][1])
    ],
    createMarker: function(i, waypoint, n) {
        // Return an invisible marker for each waypoint
        if (i === 0) {
            return L.marker(waypoint.latLng, {
                draggable: true,
                icon: greenIcon
            });
        } else {
            return L.marker(waypoint.latLng, {
                draggable: false
            });
        }
    }
}).addTo(map);

function selectLandmark(lat, lon){
    //remove all markers
    map.eachLayer(function (layer) {
        if (layer instanceof L.Marker) {
            map.removeLayer(layer);
        }
    });

    routing.getPlan().setWaypoints([
    ]).addTo(map);

    let markerLoc = [lat, lon]
    L.marker(markerLoc).addTo(map);
    map.fitBounds([[parseFloat(lat), parseFloat(lon)]]);
}

function getRouteToLandmark(lat, lon){
    //remove all markers
    map.eachLayer(function (layer) {
        if (layer instanceof L.Marker) {
            map.removeLayer(layer);
        }
    });

    console.log(lat)
    console.log(lon)
    routing.getPlan().setWaypoints([
        L.latLng(userLocation.lat, userLocation.lng),
        L.latLng(lat, lon)
    ]).addTo(map);
    routing._container.style.display = "block";
    map.fitBounds([L.latLng(userLocation.lat, userLocation.lng), L.latLng(lat, lon)]);
}

function getLocations(){
    //show progress bar
    let progressBarContainer = document.getElementById("progress-bar-container");
    progressBarContainer.style.display = "inline-block";
    let progressBar = document.getElementsByClassName("progress-bar");
    progressBar = progressBar[0];
    progressBar.style.width = "0%";

    let list1 = manhDist.sort((a,b) => a[0]-b[0]).slice(0,10)
    // console.log(list1)
    // list1.forEach(l=>console.log(names[l[1]]));

    let list2 = []
    let i= 0;
    let routeInterval = setInterval(function() {
        console.log(names[list1[i][1]])
        let routeControl = L.Routing.control({
            waypoints: [
                userLocation,
                landmarks[list1[i][1]]
            ],
            lineOptions: {
                styles: []  // Set styles to an empty array to hide the path
            },
            fitSelectedRoutes: false,
            instructions: false,
            createMarker: function(i, waypoint, n) {
                // Return an invisible marker for each waypoint
                return L.marker(waypoint.latLng, {
                    opacity: 0,
                    icon: L.divIcon({ className: 'invisible-marker' })  // You can use a custom CSS class for styling
                });
            }
        }).addTo(map);

        routeControl.on('routesfound', function(e) {
            var routes = e.routes;
            var summary = routes[0].summary;
            let distance = summary.totalDistance / 1000;
            list2.push([distance, list1[i-1][1]])
            // routeControl.getWaypoints().spliceWaypoints(0, 1);
            // control.route();
        });
        progressBar.style.width = (i+1)*10 + "%";
        i+=1;
        if(i===10)
            clearInterval(routeInterval);
    }, 600);

    setTimeout(function() {
        //remove all markers
        map.eachLayer(function (layer) {
            if (layer instanceof L.Marker) {
                map.removeLayer(layer);
            }
        });

        //remove routes
        routing.getPlan().setWaypoints([]).addTo(map);

        list2.forEach(l=>console.log(names[l[1]]));
        console.log(list2.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>names[l[1]]));
        listIds = list2.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>ids[l[1]]);
        list2 = list2.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>landmarks[l[1]]);
        list2.push([userLocation.lat, userLocation.lng])

        // console.log(listIds);
        var lms = document.querySelectorAll(".landmark");
        lms.forEach(function (landmark) {
            // console.log(landmark.getAttribute("id"));
            let id = parseInt(landmark.getAttribute("id"));
            if (listIds.includes(id)) {
                L.marker(landmarks[ids.indexOf(id)]).bindTooltip(names[ids.indexOf(id)]+", "+regions[ids.indexOf(id)]).addTo(map);
                landmark.style.display = 'inline-block';
            } else {
                landmark.style.display = 'none';
            }
        });
        L.marker(userLocation, {
            icon: greenIcon
        }).bindTooltip("You are here.").addTo(map);

        map.fitBounds(list2);

        progressBarContainer.style.display = "none";

        list1 = [];
        list2 = [];
    }, 6500);
}

