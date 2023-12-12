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
            if (!ui.newHeader.length) return; // No new header

            // Calculate the target scroll position relative to the top of the page
            var scrollPosition = ui.newHeader.offset().top - $("#landmark-list").offset().top;

            // Scroll to the target position
            $('#landmark-list').animate({
                scrollTop: '+=' + (scrollPosition - 15)
            }, 'fast');
        }
    });
});

const editButtons = document.querySelectorAll(".edit-btn");
const reviewForms = document.querySelectorAll(".review-form");
const reviewDisplays = document.querySelectorAll(".review-display");
editButtons.forEach(button => {
    button.addEventListener("click", function(event) {
        event.preventDefault();
        const reviewId = this.getAttribute("data-review-id");
        const landmarkId = this.getAttribute("data-landmark-id");
        const editForm = document.querySelector(`form.edit-form[data-review-id="${reviewId}"]`);
        const reviewForm = document.querySelector(`div.review-form[data-review-id="${landmarkId}"]`);
        const display = document.querySelector(`div.review-display[data-review-id="${reviewId}"]`);
        if(editForm.style.display === "none"){
            editForm.style.display = "block";
            reviewForm.style.display = "none";
            // display.style.display = "none";
        }else{
            editForm.style.display = "none";
            reviewForm.style.display = "block";
            // display.style.display = "block";
        }
    });
});

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