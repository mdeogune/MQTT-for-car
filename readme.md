# API Documentation


* URL: "/api/update_location_and_parameters/"
* Method: POST


  * Json object Expected:

```json
{
  "key": "70b66a89929e93416d2ef535893ea14da331da8991cc7c74010b4f3d7fabfd62",
  "known_location": true,
  "place_name": "TextField",
  "time_recorded": "DateTimeField",
  "bus_number": "TextField",
  "latitude": "",
  "longitude": "",
  "fuel": "",
  "speed": "",
  "distance": ""
}
```


 * If successful, Json response:

```json
{
"status": "true"
}
```

 * If not successful, Json response:

```json
{
"status": "error"
}
```
