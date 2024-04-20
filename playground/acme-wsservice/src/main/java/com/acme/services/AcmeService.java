package com.acme.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

/**
 * The simplest AcmeService.
 */
@WebService(name = "AcmeService", serviceName = "AcmeService")
public interface AcmeService {

    @WebMethod
    String hello(String text);

}