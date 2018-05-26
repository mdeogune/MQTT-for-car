jQuery(function($) {
    // Asynchronously Load the map API
    var script = document.createElement('script');
    script.src = "https://maps.googleapis.com/maps/api/js?key=AIzaSyCm24VQsd__wlG2Q3NnxernN67KMbvabas&callback=initialize&language=hi&region=IN";
    document.body.appendChild(script);
});



function show_the_map(data){

  var map;
  var no_of_buses = Object.keys(data).length;
  // console.log(data);
  if (no_of_buses>0){

    var cordi = {
      'lat':parseFloat(data[0]['latitude']),
      'lng': parseFloat(data[0]['longitude'])
    };
  }
    else{
      var cordi = {
        'lat':28.7041,
        'lng': 77.1025
      };
    }

    var myLatlng = new google.maps.LatLng(cordi)

  var mapOptions = {
      center: myLatlng, // Set our point as the centre location
      zoom: 12, // Set the zoom level
      mapTypeId: 'roadmap' // set the default map type
  };


  // Display a map on the page
  map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
  // Allow our satellite view have a tilted display (This only works for certain locations)
  map.setTilt(45);

  var infoWindow = new google.maps.InfoWindow({});

  var marker, i;
  var markers = new Array();
  for (i = 0; i < no_of_buses; i++) {

    marker = new google.maps.Marker({
			position: new google.maps.LatLng(data[i]['latitude'], data[i]['longitude']),
			map: map,
      bus_number: data[i]['bus_number'],
		});

		google.maps.event.addListener(marker, 'click', (function (marker, i) {
      setInterval(changeMarkerPosition(marker, data[i]['bus_number'], 1000));
			return function () {
        var html = `
        <div>
          <span>Bus Number: <b>` + data[i]['bus_number'] + `</b></span><br>
          <span>Driver Name: <b>`+ data[i]['driver']+ `</b></span><br>
          <span>Running Status: <b>`+ data[i]['running_status'] +`</b></span><br>
          <span>Shifts: <b>`+ data[i]['shifts'] + `</b></span><br>
        </div>
  `;
				infoWindow.setContent(html);
				infoWindow.open(map, marker);
			}
		})(marker, i));

    markers.push(marker);
	}
  return markers;
};

function changeMarkerPosition(marker) {
  // console.log(marker);
  var bus_number = marker.bus_number;
    $.ajax({
      url: '/api/web/marker_update/',
      type: 'GET',
      dataType: 'json',
      data: {'bus_number': bus_number}
    })
    .done(function(data) {
      // console.log("marker update success", data);
        lat = data['lat'];
        lng = data['lng'];
        var latlng = new google.maps.LatLng(lat, lng);
        marker.setPosition(latlng);
    })
    .fail(function() {
        console.log("error");
    })
    .always(function() {
      console.log("complete");
    });

};
function initialize() {

  $.ajax({
    url: '/api/web/get_bus_locations/',
    type: 'GET',
    dataType: 'json',
  })
  .done(function(data) {
    var markers = new Array();
    var markers = show_the_map(data);
    for(var i=0; i<markers.length; i++){
      setInterval(changeMarkerPosition, 1000, markers[i]);
    }
  })
  .fail(function() {
    console.log("error");
  })
  .always(function() {
    console.log("complete");
  });
 }
