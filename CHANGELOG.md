### Unreleased
  - Pre validate survey ids so that invalid/empty strings aren't routed to census
  - Change logging messages to add the service called or returned from
  - Remove default settings and add examples of all env vars to README
  
### 1.3.1 2017-03-15
  - Add version number to log

### 1.3.0 2017-02-16
  - Add explicit message ack/nack
  - Add quarantine queue for bad decrypt messages
  - Add change log
  - Remove reject on max retries. Stops message being rejected if endpoint is down for prolonged period
  - Add queue name to log message
  - Add `PREFETCH=1` to rabbit config to address '104 Socket' errors
  - Update env vars for receipt queue names

### 1.2.0 2016-12-13
  - Add new queues for rrm and ctp receipting services

### 1.1.0 2016-11-28
  - Remove logging of failed data. Now sets flag and stores failed submission to DB.

### 1.0.1 2016-11-10
  - Remove sensitive data from logging

### 1.0.0 2016-08-09
  - Initial release