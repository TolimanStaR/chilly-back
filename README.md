# chilly-back

swagger url: https://toliman.st4r.fvds.ru:8085/swagger-ui.html

--- 
### Gateway
Every public endpoint need to be configured on a gateway in order to be accessible from the Internet.
1. add endpoint in `RouteLocator` bean, preferably use `authFilter`
2. all endpoints can either open (do not need authorization header) or require JWT token in Authorization header
3. if endpoint uses JWT, you must specify roles that are required to access the endpoint in `RouteAuthorityChecker.allowedByRole` map.

Gateway exception can by distinguished from Service exception by response body. if error response doesn't contain body at all, 
it means, that logic (not always right) worked correctly as written.

---
### SSL certificate 

- application uses a certificate provided by *Let's Encrypt* via *Certbot*
- certificate is valid for 90 days and have to be renewed (*last issue date: 02.05.2025*)
- certbot issues certificate in `*.pem` format, however spring application uses keystore in `*.p12` format, thus the certificate must be packaged into keystore.
- currently keystore is added at build stage and stored in the container. (TODO: store keystore on host machine to automate renewing process)

export is done via openssl, it needs `domain`, `key-name`, `key-password`:
```shell

sudo openssl pkcs12 -export \
  -in /etc/letsencrypt/live/<domain>/fullchain.pem \
  -inkey /etc/letsencrypt/live/<domain>/privkey.pem \
  -out keystore.p12 \
  -name <key-name>
  -password pass:<key-password>
```
