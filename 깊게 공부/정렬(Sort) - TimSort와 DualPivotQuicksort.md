## 정렬(Sort) - TimSort와 DualPivotQuicksort

<br/>

## Arrays.sort 가 하나가 아니다

```java
Arrays.sort(int[])       -> DualPivotQuicksort
Arrays.sort(Object[])    -> TimSort
Collections.sort(List)   -> TimSort
list.sort(comparator)    -> TimSort
```

<br/>

원시형 배열과 객체 배열에 다른 알고리즘을 쓴다.

<br/><br/>

## 궁금증!

```java
둘이 얼마나 다른가
```

200만 개 무작위 정수를 `int[]` 와 `Integer[]` 로 정렬했다.

<br/>

### 결과

```java
int[]     Arrays.sort = 144 ms   (DualPivotQuicksort)
Integer[] Arrays.sort = 509 ms   (TimSort)
```

<br/>

3.5배 차이다. 알고리즘 차이만은 아니다.

<br/>

앞의 객체 헤더와 메모리 정렬 글에서 본 대로 `Integer` 는 16바이트 객체고, 배열에는 참조만 있다.

비교하려면 참조를 따라가서 값을 읽어야 한다. 앞의 ‘메모리에 적재’ CPU의 연산 글에서 본 캐시 미스가 매 비교마다 난다.

`int[]` 는 값이 배열에 바로 있어서 순서대로 읽힌다.

<br/><br/>

## 왜 알고리즘을 나눴나

안정성 때문이다.

```java
안정 정렬   - 같은 키의 원래 순서가 유지된다
불안정 정렬 - 유지되지 않을 수 있다
```

<br/>

```java
나이순 정렬 = [나20, 라20, 가30, 다30]
```

<br/>

원래 순서가 `가30, 나20, 다30, 라20` 이었다.

정렬 뒤 20살 둘은 `나, 라` 순서, 30살 둘은 `가, 다` 순서다. 원래 순서 그대로다.

<br/>

앞의 합병정렬 글에서 본 대로 TimSort는 합병 정렬 기반이라 안정 정렬이다.

퀵 정렬은 불안정하다.

<br/>

`int[]` 는 값만 있어서 안정성이 의미가 없다. `3` 과 `3` 은 구분이 안 된다.

그래서 더 빠른 퀵 정렬 계열을 쓴다.

객체는 같은 키여도 다른 객체라 순서가 의미 있으니 안정 정렬을 쓴다.

<br/><br/>

## TimSort 는 이미 정렬된 구간을 찾는다

200만 개를 정렬해두고 100개만 뒤섞은 배열을 다시 정렬했다.

```java
Integer[] TimSort        = 44 ms    (무작위였을 때 509 ms)
int[] DualPivotQuicksort = 28 ms    (무작위였을 때 144 ms)
```

<br/>

TimSort가 11배 빨라졌다.

<br/>

TimSort는 먼저 배열을 훑으면서 이미 정렬된 구간(run)을 찾는다.

거의 정렬된 배열은 긴 run 몇 개로 나뉘고, 그것들을 합치기만 하면 끝이다.

<br/>

앞의 삽입 정렬 글에서 본 `거의 정렬된 데이터에 강하다` 는 성질을 합병 정렬에 심은 것이다.

작은 run은 이진 삽입 정렬로 만들고(32개 이하), 큰 것은 합병한다.

<br/>

실무 데이터는 완전 무작위인 경우가 드물다.

앞의 정렬 - ORDER BY 글에서 본 대로 DB에서 정렬해서 가져온 것에 몇 개 추가하고 다시 정렬하는 식이다.

이때 TimSort가 유리하다.

<br/><br/>

## DualPivotQuicksort

앞의 퀵정렬 글에서 본 대로 피벗을 두 개 잡아서 세 구간으로 나눈다.

```java
[피벗1 보다 작은] [피벗1 ~ 피벗2] [피벗2 보다 큰]
```

<br/>

재귀 깊이가 `log₃ n` 으로 줄고, 캐시 친화적이다.

<br/>

작은 구간은 삽입 정렬로, 이미 정렬된 구간을 만나면 합병으로 바꾼다.

이름은 퀵정렬인데 실제로는 여러 알고리즘의 혼합이다.

<br/>

자바 14에서 크게 개선됐다. 병렬 정렬 경로도 이걸 쓴다.

```java
Arrays.parallelSort(int[])     // 코어 여러 개로. 수십만 개 이상일 때 이득
```

<br/><br/>

## Comparator 에서 조심할 것

```java
list.sort((a, b) -> a - b);            // 오버플로. 앞의 Comparable과 Comparator 글
list.sort(Comparator.comparingInt(Item::getPrice));     // 이렇게
```

<br/>

그리고 `compare` 가 규약을 어기면 TimSort가 예외를 던진다.

```java
IllegalArgumentException: Comparison method violates its general contract!
```

<br/>

`a < b` 이고 `b < c` 인데 `c < a` 라고 답하는 비교기다.

옛 합병 정렬은 조용히 틀린 결과를 냈는데, TimSort는 그걸 감지한다.

자바 7로 올릴 때 이 예외가 갑자기 나던 이유가 이것이다.

<br/>

앞의 Comparable과 Comparator 글에서 본 `compareTo 와 equals 규약` 이 여기서 실제로 검사되는 것이다.

<br/><br/>

## 정리

```java
int[]     - DualPivotQuicksort. 빠르다. 불안정이어도 상관없다
Object[]  - TimSort. 안정 정렬. 거의 정렬된 데이터에 아주 빠르다
비교기    - 뺄셈 금지. 규약 위반은 예외로 잡힌다
```

<br/>

앞의 Big_O 글에서 본 대로 둘 다 `O(n log n)` 인데 실측이 3.5배 갈리는 것은,

알고리즘보다 `메모리에 어떻게 놓여 있는가` 가 더 크게 작용하기 때문이다.
