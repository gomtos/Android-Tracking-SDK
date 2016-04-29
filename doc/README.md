Cauly 리타겟팅 Android 연동 가이드
=========================

--------------------------
### 개요
이 문서는 광고주가 Cauly 와 feed 형 리타겟팅 연동을 할 때 Android tracking SDK 를 삽입하는 방법에 대해서 설명하는 문서입니다.

### 문서 버전 
| 문서 버전 | 작성 날짜 | 작성자 및 내용|
 --- | --- | --- 
| 1.0.0 | 2016. 04. 26. | 권순국(nezy@fsn.co.kr) 초안 작성|



### 목차
- [연동 절차](#연동-절차)
- [연동 상세](#연동-상세)


### 연동 절차
1. Cauly 담당자 혹은 cauly@fsn.co.kr로 연락하여 Cauly 리타겟팅 연동에 대해서 협의합니다.
1. 협의 완료 후, track_code 를 발급받습니다.
1. track_code 를 포함한 tracker 코드를 Android 앱의 필요한 곳에 삽입합니다.
1. 코드 삽입 이후 연동이 되었는지 Cauly 에서 테스트를 진행합니다.
1. 테스트가 완료되면 광고를 집행합니다.


### 연동 상세

- Setting
 - proguard: https://github.com/CaulyTracker/Android-Tracking-SDK#proguard
 - AndroidManifest.xml
   - track_code 추가: https://github.com/CaulyTracker/Android-Tracking-SDK#initialize
    - Install Referrer 추가: https://github.com/CaulyTracker/Android-Tracking-SDK#install-referrer-check
- 초기화: https://github.com/CaulyTracker/Android-Tracking-SDK#session

#### DeepLink 처리
```java
// 아래 String 처럼 DeekLink 가 들어왔다고 가정하면
// String deepLinkStr = "someapp://app/?cauly_rt_code=1234&cauly_egmt_sec=8600"; 
CaulyTracker caulyTracker = CaulyTrackerBuilder.getTrackerInstance();
caulyTracker.traceDeepLink(deepLinkStr);
```

#### Event 전송
##### OPEN 이벤트
```java
CaulyTracker caulyTracker = CaulyTrackerBuilder.getTrackerInstance();
caulyTracker.trackEvent("OPEN");
```
##### 상품 view 이벤트
```java
String productId = "987654321"; // 광고주의 product id 를 987654321 라 가정하면
CaulyTracker caulyTracker = CaulyTrackerBuilder.getTrackerInstance();
caulyTracker.trackEvent("PRODUCT", productId);
```
##### Conversion 이벤트
```java
CaulyTracker caulyTracker = CaulyTrackerBuilder.getTrackerInstance();
caulyTracker.trackEvent("CA_APPLY");
```
##### Purchase 이벤트
```java
// 유저가 구매한 20000원짜리 (product id "987654321") 3개와 10000원짜리 (product id "887654321") 1개를 샀고,
// 그래서 총 구매액은 70000원이고,
// 광고주가 발급한 구매 id(order id) 가 "order_20160430" 라고 가정하면,
CaulyTrackerPurchaseEvent purchaseEvent = new CaulyTrackerPurchaseEvent();

String productId = "987654321";
String productPrice = "20000";
String productQuantity = "3";
Product product = new Product(productId, productPrice, productQuantity);

String productId2 = "887654321";
String productPrice2 = "10000";
String productQuantity2 = "1";
Product product = new Product(productId2, productPrice2, productQuantity2);

purchaseEvent.setOrderId("order_20160430");
purchaseEvent.setOrderPrice("70000");
purchaseEvent.addProuduct(product);
purchaseEvent.addProuduct(product2);
purchaseEvent.setCurrencyCode(TrackerConst.CURRENCY_KRW);

CaulyTrackerBuilder.getTrackerInstance().trackEvent(purchaseEvent);
```
상세 설명: https://github.com/CaulyTracker/Android-Tracking-SDK/blob/master/README.md#purchase

##### Re-Purchase 이벤트
재구매 유저를 골라서 분류해보고 싶으면 아래처럼 한 줄 추가된 코드를 사용합니다.
```java
CaulyTrackerPurchaseEvent purchaseEvent = new CaulyTrackerPurchaseEvent();

String productId = "p_0344411";
String productPrice = "20000";
String productQuantity = "3";
Product product = new Product(productId, productPrice, productQuantity);

String productId2 = "p_0344412";
String productPrice2 = "10000";
String productQuantity2 = "1";
Product product = new Product(productId2, productPrice2, productQuantity2);

// 아래 한 줄을 추가합니다
purchaseEvent.setPurchaseType("RE-PURCHASE");
// 한 줄 추가 끝
purchaseEvent.setOrderId("order_20160430");
purchaseEvent.setOrderPrice("70000");
purchaseEvent.addProuduct(product);
purchaseEvent.addProuduct(product2);
purchaseEvent.setCurrencyCode(TrackerConst.CURRENCY_KRW);

CaulyTrackerBuilder.getTrackerInstance().trackEvent(purchaseEvent);
```
