Carbon Footprint Estimator Backend








A Spring Boot backend service to estimate the carbon footprint of dishes via text or image input.

Deployed API: https://carbonfootprintestimator-backend-1.onrender.com

Features

Analyze dish text input.

Analyze images of dishes.

Returns ingredient-level carbon footprint and total footprint.

Proper error handling for unrecognized dishes.

Dockerized for easy deployment.

API Endpoints
1. Text Analysis

POST /estimate

Request Body (JSON):

{
  "dish": "Aloo Chaat"
}


Success Response:

{
  "dish": "Aloo Chaat",
  "estimated_carbon_kg": 2.3,
  "ingredients": [
    { "name": "Potato", "carbon_kg": 1.0 },
    { "name": "Chaat Masala", "carbon_kg": 0.3 },
    { "name": "Oil", "carbon_kg": 1.0 }
  ]
}


Error Response:

{
  "error": "Could not confidently identify dish or ingredients"
}

2. Image Analysis

POST /estimate/image

Request: multipart/form-data

file → Upload an image of the dish.

Response: Same as text analysis endpoint.

Testing with Postman

Open Postman.

Text endpoint:

Method: POST

URL: https://carbonfootprintestimator-backend-1.onrender.com/estimate

Body: JSON → { "dish": "Aloo Chaat" }

Image endpoint:

Method: POST

URL: https://carbonfootprintestimator-backend-1.onrender.com/estimate/image

Body: Form-data → Key: file, Value: upload image file

Postman ignores CORS, so it works directly. Browsers require CORS to be enabled.
