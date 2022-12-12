# solo-project
## ToDo Application
**목표**
1. 레퍼런스 코드 최대한 참고하지 않고 구현
2. 구글링 대신 공식 문서 참고
3. 테스팅 코드 먼저 작성

***
#### HttpMediaTypeNotAcceptableException
- Controller 테스팅 코드를 구현하다 발생한 예외!
  `Response`의 타입을 **제네릭 타입**으로 반환하게 구현하였는데 그 `ResponseDto`에 **@Getter**가 없어서 발생한 에러였다.