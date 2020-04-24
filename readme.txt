1. Intellij IDEA: Setup project
2. PDMan: Import database
3. MyBatis: Generate pojo/mapper -> Reverse engineering with MyBatis
4. Spring: Implement data layer -> service, api
5. Postman: Debug restful api
6. Spring: Ensure data integrality -> @Transactional
7. Swagger2: Generate api documentation
8. Tomcat: Hosting the website
9. Cookie: Client side data cache
10. slf4j/log4j: Gather logs to console and file
11. Spring: Tracking method call and add log -> spring-boot-starter-aop
12. MyBatis: Config MyBatis to log sql query
13. MyBatis: Custom sql query using vo (View Object) to communicate with db
14. MyBatis: Apply mybatis-pagehelper to provide query result pagination.
15. WechatPay: Apply WechatPay.
16. NatApp: Apply NatApp to provide NAT server.
17. Sprint: Apply Scheduled Job to auto close timeout orders -> Scheduled Job, Cron: http://cron.qqe2.com/
18. Spring: Apply hibernate to validate data.
19. Spring: Apply properties file to provide environment config for dev/prod.
20. Spring: Apply WebMvcConfigurer to add resource handlers for local files.
21. Spring: Restrict file upload/download size.
22. MyBatis: Not support nested result: https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/Important.md.
23. MariaDB: https://mariadb.org/
24. Spring: Apply multiple application-xxx.yml for multiple build environment.
25. Spring: Config and package as war.
26. nginx: http://nginx.org/
27. crontab: Apply crontab to schedule some command: https://bencane.com/2012/09/03/cheat-sheet-crontab-by-example/
28. SwitchHosts: Manage hosts with SwithHosts. https://oldj.github.io/SwitchHosts/
29. JMeter: load test functional behavior and measure performance. https://jmeter.apache.org/
30. Consistent hashing: Resolve node add/remove but keep the data consistent. https://en.wikipedia.org/wiki/Consistent_hashing
31. Nginx: Upstream mode - load balancing algorithm (ip hash, url hash, least_conn) http://nginx.org/en/docs/http/ngx_http_upstream_module.html
32. Keepalived: Virtual IP, 双主热备 https://www.keepalived.org/
33. LVS: Linux Virtual Server. http://www.linux-vs.org/
34. Redis: https://redis.io/  free redis client: https://github.com/caoxinyu/RedisClient
35. Redis: master/slave 拓扑结构实现
36. Redis: Apply sentinel to monitor redis and auto promote when master dead.
37. Redis: Apply cluster to provide high-performance & high reliability cache.
38. Guava: BloomFilter https://guava.dev/releases/22.0/api/docs/com/google/common/hash/BloomFilter.html
39. Redis: multiGet, batch get.
40. Sesson: Save token to redis to manage session.
41: Spring: Apply EnableRedisHttpSession for http session.
42: Spring: Apply HandlerInterceptor to intercept http request.
43. Sprint: Apply thymeleaf to provide html template. https://www.thymeleaf.org/