/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.*;
        import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig {

    static {
        // https://stackoverflow.com/questions/4072585/disabling-ssl-certificate-validation-in-spring-resttemplate
        // HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Bean
    public SSLConnectionSocketFactory sslConnectionSocketFactory()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // http://progressivecoder.com/avoid-ssl-validation-spring-boot-resttemplate/
        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain,
                                                      String authType) -> true;

        final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy).build();

        final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        return csf;
    }

    @Bean
    public CloseableHttpClient httpClient()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final SSLConnectionSocketFactory csf = sslConnectionSocketFactory();

        // FIXME
        // final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf)
        // .build();

        final CloseableHttpClient httpClient = HttpClients.custom().build();

        return httpClient;
    }

    @Bean
    public RestTemplate restTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = httpClient();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // new HttpComponentsClientHttpRequestFactoryBasicAuth(host);

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    public RestTemplate restTemplate(String hostname)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = httpClient();

        HttpComponentsClientHttpRequestFactory requestFactory = new PreAuthHttpComponentsClientHttpRequestFactory();
        // new HttpComponentsClientHttpRequestFactoryBasicAuth(host);

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    public static class NullX509TrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    public void setSslContext(String truststoreLocationFile, char[] truststorePassword) {
        // https://stackoverflow.com/questions/1725863/why-cant-i-find-the-truststore-for-an-ssl-handshake
        // https://stackoverflow.com/questions/4072585/disabling-ssl-certificate-validation-in-spring-resttemplate
        String keystoreType = "JKS";
        InputStream keystoreLocation = null;
        char[] keystorePassword = null;
        char[] keyPassword = null;

        // String truststoreLocationFile =
        // "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts";
        // char[] truststorePassword = "changeit".toCharArray();
        String truststoreType = "JKS";

        try (InputStream truststoreLocation = new FileInputStream(truststoreLocationFile)) {

            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(keystoreLocation, keystorePassword);
            KeyManagerFactory kmfactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, keyPassword);

            KeyStore truststore = KeyStore.getInstance(truststoreType);
            truststore.load(truststoreLocation, truststorePassword);
            TrustManagerFactory tmfactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmfactory.init(truststore);

            KeyManager[] keymanagers = kmfactory.getKeyManagers();
            TrustManager[] trustmanagers = tmfactory.getTrustManagers();

            // override...
            trustmanagers = new TrustManager[1];
            trustmanagers[0] = new NullX509TrustManager();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keymanagers, trustmanagers, new SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (SecurityException | CertificateException | IOException | KeyStoreException
                 | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace(System.err);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Class that adds support for basic auth.
     *
     * Ref: https://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring
     */
    public class HttpComponentsClientHttpRequestFactoryBasicAuth
            extends HttpComponentsClientHttpRequestFactory {

        private final HttpHost host;

        public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpHost host) {
            super();
            this.host = host;
        }

        protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
            return createHttpContext();
        }

        private HttpContext createHttpContext() {
            AuthCache authCache = new BasicAuthCache();

            BasicScheme basicAuth = new BasicScheme();
            authCache.put(host, basicAuth);

            BasicHttpContext localcontext = new BasicHttpContext();
            localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
            return localcontext;
        }
    }

    /**
     * Class that adds support for basic auth with preemptive authorization
     * @author bgiles
     *
     */
    public class PreAuthHttpComponentsClientHttpRequestFactory
            extends HttpComponentsClientHttpRequestFactory {

        private String hostName;
        private boolean matchSubDomains;
        private Credentials credentials;

        public PreAuthHttpComponentsClientHttpRequestFactory() {
        }

        @Override
        protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
            // Add AuthCache to the execution context
            final HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(new PreAuthCredentialsProvider());
            context.setAuthCache(new PreAuthAuthCache());
            return context;
        }

        /**
         * @param host host name
         * @return whether the configured credentials should be used for the given host
         */
        protected boolean hostNameMatches(String host) {
            return host.equals(hostName) || (matchSubDomains && host.endsWith("." + hostName));
        }

        private class PreAuthCredentialsProvider extends BasicCredentialsProvider {
            @Override
            public Credentials getCredentials(AuthScope authScope, HttpContext context) {
                if (hostNameMatches(authScope.getHost())) {
                    // Simulate a basic authenticationcredentials entry in the
                    // credentials provider.
                    return credentials;
                }
                return super.getCredentials(authScope, context);
            }
        }

        private class PreAuthAuthCache extends BasicAuthCache {
            @Override
            public AuthScheme get(HttpHost host) {
                if (hostNameMatches(host.getHostName())) {
                    // Simulate a cache entry for this host. This instructs
                    // HttpClient to use basic authentication for this host.
                    return new BasicScheme();
                }
                return super.get(host);
            }
        }
    }
}
