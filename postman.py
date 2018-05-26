import requests
import json
url = "http://localhost:8000/api/get_bus_data_current_time/"

payload = {
    "chekc":"asdf"
    }
payload = json.dumps(payload)
headers = {
    'content-type': "application/json",
    }

response = requests.post(url, data=payload, headers=headers)

print(response.text)
