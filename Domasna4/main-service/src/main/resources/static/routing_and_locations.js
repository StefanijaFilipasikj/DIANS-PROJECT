if(document.getElementById('mapPage')){
    // Process landmark data
    let landmarksData = document.getElementById("landmarksVar").innerHTML
    let listLandmarks = landmarksData.split("), HistoricLandmark");
    listLandmarks = listLandmarks.map(each => each.concat(")").replace("[","").replace(", HistoricLandmark(",'HistoricLandmark('));

    if(listLandmarks.length <= 3){
        // Disable route button
        document.getElementById("route").disabled = true
    }

    // Landmark coordinates
    coordinates = []
    addresses = []
    names = []
    regions = []
    manhDist = []
    ids = []

    for(let l of listLandmarks){
        ids.push(parseInt(l.split(", ")[0].split("=")[1]))
        coordinates.push([parseFloat(l.split(", ")[1].split("=")[1]), parseFloat(l.split(", ")[2].split("=")[1])])
        addresses.push(l.split(", ")[5].split("=")[1])
        names.push(l.split(", ")[4].split("=")[1])
        regions.push(l.split(", ")[6].split("=")[1].replace(")", ""))
    }

// Initialize Leaflet map
    let map = L.map('map').setView([41.9946, 21.4266], 11);
    mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: 'Leaflet &copy; ' + mapLink + ', contribution', maxZoom: 18 }).addTo(map);

// User location marker icon
    let greenIcon = L.icon({
        iconUrl: 'img/greenIcon.png',
        iconSize: [25, 40]
    })

// Add all landmark markers
    for(let i=0;i<coordinates.length;i++){
        L.marker(coordinates[i]).bindTooltip(names[i]+", "+regions[i]).addTo(map);
    }

// Set zoom to fit all landmarks
    if(coordinates.length!==0)
        map.fitBounds(coordinates);

// Get user location
    let userLocation;
    L.control.locate({setView: false, drawCircle: false, drawMarker: false, locateOptions: {
            enableHighAccuracy: true
        }}).addTo(map).start( );

// Get manhattan distance for all landmarks
    map.on('locationfound', function(e) {
        userLocation = e.latlng
        manhDist = []
        for(let i=0;i<coordinates.length;i++){
            manhDist.push([Math.abs(parseFloat(userLocation.lat)-coordinates[i][0])+Math.abs(parseFloat(userLocation.lng)-coordinates[i][1]), i])
        }
    });

// Initialize routing control
    let routing = L.Routing.control({
        waypoints: [
            userLocation,
            L.latLng(coordinates[0][0], coordinates[0][1])
        ],
        createMarker: function(i, waypoint) {
            // Make user marker draggable and with custom icon
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

// Center map on landmark
    function selectLandmark(lat, lon){
        // Remove all markers
        map.eachLayer(function (layer) {
            if (layer instanceof L.Marker) {
                map.removeLayer(layer);
            }
        });

        // Remove all routes
        routing.getPlan().setWaypoints([
        ]).addTo(map);

        // Add marker to map and center
        L.marker([lat, lon]).addTo(map);
        map.fitBounds([[parseFloat(lat), parseFloat(lon)]]);
    }

    function getRouteToLandmark(lat, lon){
        // Remove all markers
        map.eachLayer(function (layer) {
            if (layer instanceof L.Marker) {
                map.removeLayer(layer);
            }
        });

        // Add route to landmark
        routing.getPlan().setWaypoints([
            L.latLng(userLocation.lat, userLocation.lng),
            L.latLng(lat, lon)
        ]).addTo(map);
        map.fitBounds([L.latLng(userLocation.lat, userLocation.lng), L.latLng(lat, lon)]);

        // For route summary styling
        routing._container.style.display = "block";
    }

// Get 3 closest locations
    function getLocations(){
        // Show progress bar
        let progressBarContainer = document.getElementById("progress-bar-container");
        progressBarContainer.style.display = "inline-block";
        let progressBar = document.getElementsByClassName("progress-bar");
        progressBar = progressBar[0];
        progressBar.style.width = "0%";

        // Disable route button
        document.getElementById("route").disabled = true

        // Get 10 closest locations by manhattan distance
        let manhClosest = manhDist.sort((a,b) => a[0]-b[0]).slice(0,10);

        let distances = []
        let i= -1;

        // Get distance to each location every 600ms
        let routeInterval = setInterval(function() {
            // Set progress bar percentage
            progressBar.style.width = (i+1)*10 + "%";

            i+=1;
            if(i===10){
                i=-1
                clearInterval(routeInterval);
            }

            if(i < manhClosest.length && i !== -1){
                // Create a route to location
                let routeControl = L.Routing.control({
                    waypoints: [
                        userLocation,
                        coordinates[manhClosest[i][1]]
                    ],
                    lineOptions: {
                        styles: [{opacity: 0}]
                    },
                    fitSelectedRoutes: false,
                    instructions: false,
                    createMarker: function(i, waypoint) {
                        // Return an invisible marker for each waypoint
                        return L.marker(waypoint.latLng, {
                            opacity: 0
                        });
                    }
                }).addTo(map);

                // When a route is found, add distance to list
                routeControl.on('routesfound', function(e) {
                    let routes = e.routes;
                    let summary = routes[0].summary;
                    let distance = summary.totalDistance / 1000;
                    distances.push([distance, manhClosest[i][1]])
                });
            }
        }, 600);

        // After all distances are found display 3 closest
        setTimeout(function() {
            // Remove all markers
            map.eachLayer(function (layer) {
                if (layer instanceof L.Marker) {
                    map.removeLayer(layer);
                }
            });

            // Remove all routes
            routing.getPlan().setWaypoints([]).addTo(map);

            // Get ids of 3 closest locations
            let listIds = distances.sort((a, b) => a[0] - b[0]).slice(0, 3).map(l => ids[l[1]]);

            // Get coordinates of 3 closest locations
            let closest3 = distances.sort((a,b) => a[0]-b[0]).slice(0,3).map(l=>coordinates[l[1]]);
            closest3.push([userLocation.lat, userLocation.lng]);

            // Hide all landmarks except closest 3
            let lms = document.querySelectorAll(".landmark");
            lms.forEach(function (landmark) {
                let id = parseInt(landmark.getAttribute("id"));
                if (listIds.includes(id)) {
                    L.marker(coordinates[ids.indexOf(id)]).bindTooltip(names[ids.indexOf(id)]+", "+regions[ids.indexOf(id)]).addTo(map);
                    landmark.style.display = 'inline-block';
                } else {
                    landmark.style.display = 'none';
                }
            });

            // Add user location marker
            L.marker(userLocation, {
                icon: greenIcon
            }).bindTooltip("You are here.").addTo(map);

            map.fitBounds(closest3);

            progressBarContainer.style.display = "none";

            manhClosest = [];
            distances = [];
            closest3 = []
            document.getElementById("route").disabled = false

        }, 6500);
    }
}