# employee-api

<h3>Spec.</h3>
- java: 17
- springboot: 3.1.1
- db: H2

<h3>Endpoints</h3>

- **GET** /api/employee?page={page}&pageSize={pageSize}
  - response:
    ```
    {
      "pageInfo":{
        "page":현재 페이지,
        "pageSize":페이지당 출력 수,
        "totalCount":전체 데이터 개수
      },
      data:[
        {
          "employeeId":직원 정보 PK,
          "name":직원 이름,
          "email":직원 이메일,
          "tel":직원 연락처,
          "joined":직원 입사일
        },
        ...
      ]
    }
    ```
    
- **GET** /api/employee/{name}
  - response:
    ```
    [
      {
        "employeeId":직원 정보 PK,
        "name":직원 이름,
        "email":직원 이메일,
        "tel":직원 연락처,
        "joined":직원 입사일
      },
      ...
    ]
    ```

- **POST** /api/employee/upload
  - requestBody : csv 파일 또는 json 파일
  - response: 201(Created)


- **POST** /api/employee
  - requestBody : csv 또는 json 형식의 직원 정보
  - response: 201(Created)


- **참고**
  - swagger-ui: http://localhost:8080/swagger-ui/index.html
    - 로컬 환경에서 swagger-ui 접속 시 build.gradle 에서 `createOpenApiLocal` 실행
  - log: ./logs/employee-api.log 
