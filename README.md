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
Beside the mandatory PaymentRequest there are optional parameter that can be provided like Customer, ShoppingCart and StylingOptions.

### payment
Payment is used if the merchant wants the customer to enter the payment data on the merchants webpage. The merchant then sends all payment related data to mpay24 who executes the payment on behalf of the merchant. The merchant can also provide additional parameter like Customer, Address, ShoppingCart.

```java
Payment response = mpay24.payment(getTestPaymentRequest(), getVisaTestData());

  protected PaymentTypeData getVisaTestData() throws ParseException {
    CreditCardPaymentType paymentType = new CreditCardPaymentType();
    paymentType.setPan("4444333322221111");
    paymentType.setCvc("123");
    paymentType.setExpiry(getCreditCardMonthYearDate("12/2016"));
    paymentType.setBrand(CreditCardPaymentType.Brand.VISA);
    return paymentType;
  }
```

### paymentStatus 
You can retrieve the status for a single payment at any time using the paymentStatus method. As parameter to query a payment status you can either use the Payment object that was returned by the SDK.
Alternative is a method that uses either the mpaytid which is the unique transaction identifier provided by mpay24 or the transaction id provided by the merchant.

```java
Payment paymentStatus = mpay24.paymentStatus(payment);
```

Return paymentStatus using the mpaytid:
```java
Payment paymentStatus = mpay24.paymentStatus(new BigInteger(1));
```

Return paymentStatus using the merchant specified transaction identifier:
```java
Payment paymentStatus = mpay24.paymentStatus("83423984");
```

### cancel 
Cancelling a payment after authorization. This is only possible if no auto capture is enabled.

```java
mpay24.cancel(payment);
```

### capture 
Finishing a payment after authorization. This is only possible if no auto capture is enabled. This can also be a partial capture if the amount is smaller than the payment amount.

```java
payment = mpay24.capture(payment);
```

### refund 
Refunds a finished payment to the customer. This can also be a partial refund if the amount is smaller than the payment amount.

```java
Refund refund = mpay24.refund(payment);
```

### token 
Creates a payment token for an embedded credit card payment and returns the url to the iFrame.

```java
Token token = mpay24.token(getTestTokenRequest("EN"));
```