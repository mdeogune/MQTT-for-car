
  <script async defer
  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCm24VQsd__wlG2Q3NnxernN67KMbvabas">
  </script>

  function geocodeLatLng(latitude, longitude, id) {
        var geocoder = new google.maps.Geocoder;
        var latlng = {lat: latitude, lng: longitude};

        geocoder.geocode({'location': latlng}, function(results, status) {
          if (status === 'OK') {
            if (results[1]) {
              console.log(results);
              var i = "location" + id;
              console.log(i);
              document.getElementById(i).innerHTML= results[0]['formatted_address'];
            } else {
              console.log('No results found');
            }
          } else {
            console.log('Geocoder failed due to: ' + status);
          }
        });
      }


      <td id="location{{forloop.counter}}">
        <script type="text/javascript">geocodeLatLng({{bus.location.latitude}},{{bus.location.longitude}}, {{forloop.counter}});
        </script>
      </td>
