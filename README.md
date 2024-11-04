<h3> Build docker image </h3> 
```
make buildImage
```

<h3>export API key for co2 API</h3>
```
EXPORT API_KEY=[API_KEY]
```

<h3> Run API server </h3> 
```
make runAPIServer
```

<h3> To get carbon emission per node type </h3> 
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


<h3>  To get carbon emission for LLM </h3> 
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

<h3> To get available EU region </h3> 

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

<h3> To get all LLM available on platform </h3> 
Request
```
http://localhost:8080/llmAvailable
```
Response
```
{
  "::": [
    "Anthropic Claude v2 (18K context window)",
    "Anthropic Claude v2.1 (200K context window)",
    "Amazon Titan Image Generator G1 V2",
    "Anthropic Claude 3 Opus",
    "Meta Llama 3.2 11B Instruct",
    "Meta Llama 3 8B Instruct",
    "Anthropic Claude 3.5 Sonnet",
    "Meta Llama 3.1 405B Instruct",
    "Meta Llama 3 70B Instruct",
    "Anthropic Claude Instant v1.x (100K context window)",
    "Mistral AI Mixtral 8X7B Instruct",
    "Mistral AI Mistral 7B Instruct",
    "Cohere Embed Multilingual",
    "Amazon Titan Multimodal Embeddings G1",
    "Amazon Titan Text Embeddings V2",
    "Meta Llama 3.2 3B Instruct",
    "Anthropic Claude 3 Haiku",
    "Anthropic Claude Instant v1.x (18K context window)",
    "Anthropic Claude 3 Sonnet",
    "Meta Llama 3.1 8B Instruct",
    "Mistral AI Mistral Small",
    "Amazon Titan Text G1 - Express",
    "Meta Llama 3.2 90B Instruct",
    "Amazon Titan Embeddings G1 - Text",
    "Amazon Titan Image Generator G1 V1",
    "Amazon Titan Text Premier",
    "Meta Llama 3.1 70B Instruct",
    "Mistral AI Mistral Large",
    "Cohere Embed English",
    "Meta Llama 3.2 1B Instruct",
    "Amazon Titan Text G1 - Lite"
  ]
}
```

<h3> To get Carbon intensity in region if LLM model is available </h3> 
Request
```
http://localhost:8080/llmPerRegion/Cohere%20Embed%20English
```
Response
```
[
  {
    "zone": "DE",
    "carbonEmissionPerMilQueryInKg": 465,
    "datetime": "2024-11-03T18:00:00Z",
    "updatedAt": "2024-11-03T17:46:37.772Z",
    "createdAt": "2024-10-31T18:46:01.455Z"
  },
  {
    "zone": "IE",
    "carbonEmissionPerMilQueryInKg": 599,
    "datetime": "2024-11-03T18:00:00Z",
    "updatedAt": "2024-11-03T17:46:37.772Z",
    "createdAt": "2024-10-31T18:45:45.919Z"
  },
  {
    "zone": "FR",
    "carbonEmissionPerMilQueryInKg": 33,
    "datetime": "2024-11-03T18:00:00Z",
    "updatedAt": "2024-11-03T17:46:37.772Z",
    "createdAt": "2024-10-31T18:46:01.455Z"
  }
]
```


