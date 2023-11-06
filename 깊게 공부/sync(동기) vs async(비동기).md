## sync(동기) vs async(비동기)

`Sync / Async` 는 호출한 함수와 호출 받은 함수 중에서 누가 `return`을 처리하는가 의 차이 입니다!

<br/><br/>

## sync(동기)란?

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `A()` 함수가 처리하는 것입니다.

`Synchronous`은 `Return`을 기다리는 동안 머물 수도 아닐 수도 있습니다.

제어권을 반환하는 시간과 결과값을 전달하는 시간이 같을 때가 동기입니다.

```java
Blocking / sync 조합은 자바에서 사용됩니다.
```

<br/><br/>

## async(비동기)란?

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `B()` 함수가 처리하는 것입니다.

- `async`는 결과를 바로 처리하는 것이 아닙니다.

즉, 다른 작업에서 끝난 결과를 바로 처리하지 않고 자신의 일이 

끝나게 되면 그때서야 처리를 하는 느낌입니다.

```java
Non-Blocking / Async 조합은 자바스크립트에서 사용됩니다.
```

<br/><br/>

## Blocking / sync **설명, NonBlocking / Async 설명**

![이미지](/programming/img/입문446.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

<br/>

### Blocking / sync 설명

`Blocking`의 관점은 제어권에 있고, 다른 작업이 진행되는 동안 자신의 작업을 처리하지 않습니다.

- 대표적인 예시)

    - `Scanner`를 통해 입력을 받는 동안은 제어권이 넘어갔기 때문에 `Blocking`이고,
        
        그 결과를 `리턴`받아서 다음의 작업을 바로 처리하고 있기 때문에 Sync 개념입니다.


<br/>

### NonBlocking / Async 설명

`Non-Blocking`은 다른 작업이 진행되는 동안에도 자신의 작업을 처리합니다. 

그리고 `Async`이기 때문에 다른 작업의 결과 역시도 바로 처리하지 않아도 됩니다.

- 대표적인 예시로 `자바스크립트`에서 API 요청을 하고, 다른 작업을 하다가
    
    콜백을 통해 추가적인 작업을 처리할 때 사용하는 것을 들 수 있습니다.
    

<br/><br/>

## Blocking / a**sync 설명**

![이미지](/programming/img/입문447.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

<br/>

`Blocking`은 다른 작업이 진행되는 동안 자신의 작업을 처리하지 않습니다. 

그리고 `Async`는 결과를 바로 처리하지 않아도 되는 것입니다.

- 가장 비효율적인 방법입니다.

    - 개발자의 실수나 기타 이유로 이렇게 동작할 수도 있다고 합니다.

<br/><br/>

## Non-Blocking / sync 설명

![이미지](/programming/img/입문448.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

`Non-Blocking`은 다른 작업이 있어도 자신의 작업을 처리하는 개념입니다.

`Sync`는 그 결과를 리턴 받았을 때 바로 그 결과에 집중하는 개념이었습니다.

<br/>

`B()` 함수가 바로 제어권을 돌려주기에 `A()` 함수는 다른 작업을 수행할 수 있지만,

언제 종료되는지 알 수 없는 `B()` 함수의 종료를 `A()` 함수가 처리해야 합니다.

<br/>

`A()` 함수가 직접 결과를 처리해야 하는 상황이기에 

`B()` 함수의 종료를 반복적으로 물어봐야 하는 것입니다.