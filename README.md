### test project for German Standard

swagger: /swagger-ui/index.html

example of a /find request:

{
  "groupBy": [
    "CAMPAIGN"
  ],
  "aggregators": [
    "CTR"
  ],
  "startDate": "01/11/22",
  "endDate": "11/11/22",
  "datasource":["Google Ads"]
}


thoughts about improvements:

if trafic is hight, loader could be extracted to another service for better scaling
