if(document.getElementById('mapPage')){
    // Process landmark data
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

// Initialize Leaflet map
    let map = L.map('map').setView([41.9946, 21.4266], 11);
    mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: 'Leaflet &copy; ' + mapLink + ', contribution', maxZoom: 18 }).addTo(map);

    let greenIcon = L.icon({
        iconUrl: 'img/greenIcon.png',
        iconSize: [25, 40]
    })

    for(let i=0;i<landmarks.length;i++){
        L.marker(landmarks[i]).bindTooltip(names[i]+", "+regions[i]).addTo(map);
    }

    if(landmarks.length!==0)
        map.fitBounds(landmarks);

// Get user location
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

// Initialize routing control
    let routing = L.Routing.control({
        waypoints: [
            userLocation,
            L.latLng(landmarks[0][0], landmarks[0][1])
        ],
        createMarker: function(i, waypoint) {
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

        let list2 = []
        let i= -1;
        let routeInterval = setInterval(function() {
            i+=1;
            progressBar.style.width = (i+1)*10 + "%";
            if(i===10){
                i=-1
                clearInterval(routeInterval);
            }
            let routeControl = L.Routing.control({
                waypoints: [
                    userLocation,
                    landmarks[list1[i][1]]
                ],
                lineOptions: {
                    styles: [{opacity: 0}]
                },
                fitSelectedRoutes: false,
                instructions: false,
                createMarker: function(i, waypoint) {
                    // Return an invisible marker for each waypoint
                    return L.marker(waypoint.latLng, {
                        opacity: 0,
                        icon: L.divIcon()
                    });
                }
            }).addTo(map);
            routeControl.getRouter().options.show = false;

            routeControl.on('routesfound', function(e) {
                let routes = e.routes;
                let summary = routes[0].summary;
                let distance = summary.totalDistance / 1000;
                list2.push([distance, list1[i][1]])
            });
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

            listIds = list2.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>ids[l[1]]);
            list2 = list2.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>landmarks[l[1]]);
            list2.push([userLocation.lat, userLocation.lng])

            let lms = document.querySelectorAll(".landmark");
            lms.forEach(function (landmark) {
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
}