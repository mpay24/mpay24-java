# mpay24-java
Offical mPAY24 Java Payment SDK

## Installation
```xml
<dependency>
    <groupId>com.mpay24.payment</groupId>
    <artifactId>mpay24-payment-sdk</artifactId>
    <version>1.0.0.0</version>
</dependency>
```

`mvn install`

## Documentation

A short demo implementation guide is available at https://docs.mpay24.com/docs/get-started</br>
Documentation is available at https://docs.mpay24.com/docs/java-sdk.

## SDK Overview
### mpay24 class
The Mpay24 class is instantiated using your merchant id, your soap password and the environment you are connecting to (TEST or PRODUCTION):
```java
Mpay24 mpay24 = new Mpay24("93975", "xxx", Environment.TEST);
```

### payment page (redirect integration)
The easiest possible payment is to provide a PaymentRequest and forward the end customer to the payment page using the redirectLocation (see Unit Test testMostSimpleRedirectPayment):

```java
Payment response = mpay24.paymentPage(getTestPaymentRequest());
String redirectURL = response.getRedirectLocation();
  
protected PaymentRequest getTestPaymentRequest() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(new BigDecimal(1));
    paymentRequest.setTransactionID("1");
    return paymentRequest;
}
```

