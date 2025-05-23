# sandbox app for OpenTelemetry Spring Boot starter

This is the sandbox app for OpenTelemetry Spring Boot starter.

## Overview

- The app's metrics are saved in the Prometheus server through the otel-collector
- The app's traces are saved in the Zipkin server through the otel-collector

```
this app
  |
  | (send metrics via HTTP and traces via gRPC)
  ↓
otel-collector [container] ---(send traces via HTTP)---→ Zipkin server [container]
  ↑
  | (scrape metrics via HTTP endpoint)
  |
Prometheus server [container]
```

## How to launch the app and the containers

Launch the otel-collector, Prometheus and Zipkin using Docker Compose.

```
$ cd infra
$ docker compose up
```

Launch the app using Gradle.

```
$ ./gradlew bootRun
```

## How to see the app's metrics

e.g.

The app's metrics are saved in the Prometheus server.
They can be seen at the following the URLs.

- [JVM heap used](http://localhost:9090/query?g0.expr=jvm_memory_used_bytes&g0.show_tree=0&g0.tab=graph&g0.range_input=1h&g0.res_type=auto&g0.res_density=medium&g0.display_mode=lines&g0.show_exemplars=0)
- [HTTP server's API latency](http://localhost:9090/query?g0.expr=histogram_quantile%280.99%2C+sum%28rate%28http_server_requests_milliseconds_bucket%5B1m%5D%29%29+by%28le%2C+http_route%2C+http_response_status_code%2C+http_request_method%29%29&g0.show_tree=0&g0.tab=graph&g0.range_input=1h&g0.res_type=auto&g0.res_density=medium&g0.display_mode=lines&g0.show_exemplars=0)

## How to see the app's traces

The app's metrics are saved in the Zipkin server.
They can be seen at the following the URLs.

http://localhost:9411/zipkin/
