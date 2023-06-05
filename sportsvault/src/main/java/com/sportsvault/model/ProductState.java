package com.sportsvault.model;

public enum ProductState {
    SELLING, //product is available for sale
    PENDING_AUTHENTICATION, //someone is waiting for the authentication to receive the product
    AUTHENTICATED, //product was authenticated and the buyer will receive it
    FAILED_AUTHENTICATION, //product failed authentication
    EXPIRED, //product was not bought by anyone
    SOLD
}
