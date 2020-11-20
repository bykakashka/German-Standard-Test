### test project for German Standard

swagger: /swagger-ui/index.html


thoughts about improvements:

if trafic is hight, loader could be extracted to another service for better scaling

queries used in /clicks and /clicksThroughRate can be rewritten with a native query, if perfomance is requered

result of /clicks and /clicksThroughRate can be wrapped into an object, depending on the api requeriments
