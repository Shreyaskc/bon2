#!/bin/bash
mkdir -p ${CATALINA_HOME}/certs/
cp -Rf /tomcat.keystore  ${CATALINA_HOME}/certs/

#${JAVA_HOME}/bin/keytool -importkeystore -srckeystore ${CATALINA_HOME}/certs/server.p12 -destkeystore ${CATALINA_HOME}/certs/keystore.jks -srcstoretype pkcs12 -destkeypass 123456 -deststorepass 123456 -srcstorepass 123456
# Alternate
#openssl pkcs12 -export -in ${CATALINA_HOME}/certs/tomcatpem.cer -inkey ${CATALINA_HOME}/certs/tomcatkey.key -out ${CATALINA_HOME}/certs/keystore.jks -passin pass:123456 -passout pass:123456
#${JAVA_HOME}/bin/keytool -importkeystore -srckeystore ${CATALINA_HOME}/certs/server.p12 -srcstoretype PKCS12 -srcstorepass 123456 -deststorepass 123456 -destkeypass 123456 -destkeystore ${CATALINA_HOME}/certs/keystore.jks

# Create a SELF SIGNED CERT

#${JAVA_HOME}/bin/keytool -genkey -noprompt -alias client -dname "CN=websrv.cldrep.com, OU=CSP, O=DOVER, L=Backend, S=bon, C=IN" -keystore ${CATALINA_HOME}/certs/keystore.jks -storepass 123456 -keypass 123456
