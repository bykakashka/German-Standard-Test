### test project for German Standard

swagger: ec2-52-208-223-96.eu-west-1.compute.amazonaws.com:8081/swagger-ui/index.html

request date pattern: "MM/dd/yy"


example of a /find request:

{
  "groupBy": [
    "CAMPAIGN"
  ],
  "aggregators": [
    "CLICKS"
  ],
  "startDate": "01/11/22",
  "endDate": "11/11/22",
  "datasource":["Google Ads"]
}


thoughts about improvements:

if trafic is hight, loader could be extracted to another service for better scaling

pagination for /find could be added if necessary 
