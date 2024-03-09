module weaver.main {
    requires static lombok;
    requires spring.data.commons;
    requires spring.context;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.tx;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires spring.security.core;
    requires spring.core;
    //requires jjwt.api;
    requires org.apache.tomcat.embed.core;
    requires spring.security.web;
    requires spring.messaging;
}