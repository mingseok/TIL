## CheckedException과 UnCheckedException의 차이는?

<br/>

## CheckedException

실행하기 전에 예측 가능한 예외를 말하고, 반드시 예외 처리를 해야 합니다.

- 대표적인 Exception - IOException, ClassNotFoundException 등

<br/>

## UncheckedException

실행하고 난 후에 알 수 있는 예외를 말하고, 따로 예외처리를 하지 않아도 됩니다.

- 대표적인 Exception - NullPointerException, ArrayIndexOutOfBoundException 등

- RuntimeException은 UncheckedException을 상속한 클래스이고, 

    RuntimeException이 아닌 것은 CheckedException을 상속한 클래스 입니다.