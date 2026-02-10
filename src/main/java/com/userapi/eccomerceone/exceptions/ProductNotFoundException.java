package com.userapi.eccomerceone.exceptions;

//spring provide Exception----
public class ProductNotFoundException extends Exception {
   public ProductNotFoundException(String message)
   {
       super(message);
   }
}
