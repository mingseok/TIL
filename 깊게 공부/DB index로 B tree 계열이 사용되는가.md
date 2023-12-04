## 왜 DB index로 B tree 계열이 사용되는가?



### 핵심

![이미지](/programming/img/입문517.PNG)

저런 block의 특성 때문에, DB index는 B tree를 사용하는 것이다.

- 왜? → 블록은 남은 공간까지 가져오기에, 노드 개수가 많은 B tree 특성을 사용하면 효율을 극대화 할 수 있기 때문이다.
    

```java
"핵심은, 데이터를 찾을 때 탐색 범위를 빠르게 좁힐 수 있는 것이다"
```

<br/><br/>

## B tree index vs AVL tree index 비교

- B tree index는 5차라고 생각하기.

- AVL 트리는 self-balancing BST (AVL 트리, 레드-블랙 트리) 등의 종류가 있다.

![이미지](/programming/img/입문518.PNG)

<br/>

### AVL 트리와 B tree(5차)를 비교하면 이렇다.

B tree(5차)가 성능 적인 측면에서 훨씬 빠르게 데이터를 조회가 된 걸 알 수 있다.

- `이유 1)` AVL tree index 는 자녀 노드 수 1~2개만 가질 수 있는데,

    - 5차 B tree index 같은 경우는 3~5개를 가질 수 있기 때문이다.

- `이유 2)` AVL tree index 는 각 노드가 하나의 데이터만 가질 수 있지만,

    - 5차 B tree index 같은 경우는 최소 2개에서 최대 4개까지 가질 수 있기 때문이다.

<br/><br/>

## secondary storage 장점

block의 특성 때문에도, B tree는 적합하다.

- 왜? → 블록의 특성은 남은 공간까지 가져 오는 것인데,
    
    노드 개수가 많은 B tree 특성을 사용하면 효율을 극대화 할 수 있기 때문이다.
    

![이미지](/programming/img/입문519.PNG)

<br/><br/>

## B tree 장점

4개의 level만으로 수 백만, 수 천만 개의 데이터를 저장할 수 있다.

- root 노드에서 가장 멀리 있는 데이터도 세 번의 이동만으로 접근 할 수 있다

- 엄청나게 많은 양의 데이터를 저장해야 될 때 데이터 베이스에서 인덱스로 사용하기에 가장 적합한 것이다.
    
- DB는 기본적으로 secondary storage에 저장된다.

    - 같은 기능을 수행 하더라도 secondary storage에 적게 접근하는 방식 더 좋은 방식이다.
        - 이유는? → secondary storage는 데이터를 처리하는 속도가 느리기 때문이다

- B tree index는 AVL tree index 에 비해 secondary storage 접근을 적게 하면서도, 데이터를 조회 할 수 있는 장점이 있다.
    
- B tree 노드는 block 단위의 저장 공간을 알차게 사용할 수 있다