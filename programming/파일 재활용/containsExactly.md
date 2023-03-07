## containsExactly

순서를 포함해서 정확히 일치

<br/>

```java

/*
 * containsExactly 실패 케이스
 *
 * assertThat(list).containsExactly(1, 2);       -> 원소 3 이 일치하지 않아서
 * assertThat(list).containsExactly(3, 2, 1);    -> list 의 순서가 달라서 실패
 * assertThat(list).containsExactly(1, 2, 3, 3); -> list 에 중복된 원소가 있어서 실패
 */
@Test
void containsExactlyTest() {
    List<Integer> list = Arrays.asList(1, 2, 3);

    assertThat(list).containsExactly(1, 2, 3);
}
```


containsExactly 는 원소가 정확히 일치해야 합니다.

중복된 값이 있어도 안되고 순서가 달라져도 안됩니다.

특정 자료구조의 정확한 값을 테스트 하고 싶은 경우에는 이 메소드를 사용할 수 있습니다

<br/>

참고 : https://bcp0109.tistory.com/317