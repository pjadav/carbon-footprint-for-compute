Build docker image
```
make buildImage
```

export API key for co2 API
```
EXPORT API_KEY=[API_KEY]
```

Run API server
```
make runAPIServer
```

To get carbon emission per node type
Request
```
curl http://localhost:8080/carbonCalculationForEc2/eu-west-1
```
Response
```
{
    "zone": "IE",
    "carbonEmissionPerHourGram": {
        "m": 26.971999999999998,
        "r": 41.684000000000005,
        "c": 19.616
    },
    "datetime": "2024-11-02T00:00:00Z",
    "updatedAt": "2024-11-01T23:45:24.271Z",
    "createdAt": "2024-10-30T00:45:01.873Z"
}
```


To get carbon emission for LLM
Request
```
curl http://localhost:8080/carbonCalculationForLLM/eu-west-1
```

Response
```
{
    "zone": "IE",
    "carbonEmissionPerMilQueryInKg": 1777700,
    "datetime": "2024-11-02T00:00:00Z",
    "updatedAt": "2024-11-01T23:45:24.271Z",
    "createdAt": "2024-10-30T00:45:01.873Z"
}
```

To get available EU region

Request
```
curl http://localhost:8080/availableRegions
```
Response
```
{
    "::": [
        "eu-south-1",
        "eu-central-2",
        "eu-west-1",
        "eu-north-1",
        "eu-central-1",
        "eu-west-3",
        "eu-south-2",
        "eu-west-2"
    ]
}
```


