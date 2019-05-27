## MongoTemplte

### geoNear


### 各方法的单位

#### 坐标对（经纬度）根据查询命令的不同，$maxDistance距离单位可能是 弧度 和 平面单位（经纬度的“度”）
```java
query.addCriteria(Criteria.where("coordinates").nearSphere(center).maxDistance(500 / EARTH_RADIUS));
List<Location> locations = mongoTemplate.find(query, Location.class);
```
>db.<collection>.find( { <location field> :
     { $nearSphere: [ <x> , <y> ] ,
       $maxDistance: <distance in radians>
 } } )


#### GeoJson $maxDistance距离单位默认为米：

> db.<collection>.find( { <location field> :
       { $nearSphere :
           { $geometry :
                 {type : "Point" ,
                  coordinates : [ <longitude> , <latitude> ]} ,
           $maxDistance : <distance in meters>
} } } ) 
