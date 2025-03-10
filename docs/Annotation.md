## ArgsConstructor
- 생성자(Constructor)를 자동으로 생성

| 이름                       | 설명                                 |
|--------------------------|------------------------------------|
| @AllArgsConstructor      | 클래스의 모든 필드를 포함하는 생성자를 자동 생성        |
| @NoArgsConstructor       | - 파라미터가 없는 기본 생성자를 자동으로 생성<br/> - force=true 옵션을 사용하면 final 필드도 초기화 가능 |
| @RequiredArgsConstructor | final 필드 또는 @NonNull이 붙은 필드만 포함하는 생성자 생성 |

## Configuration
- 스프링 애플리케이션에서 설정을 담당하는 클래스
- @Bean을 포함하는 클래스에 사용
- 
## Bean