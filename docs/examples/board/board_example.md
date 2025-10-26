# 🧱 Spring MVC + JDBC + Thymeleaf 게시판 프로젝트

> **목표:**  
> 프론트엔드 HTML은 이미 준비되어 있다고 가정하고,  
> 백엔드는 Spring MVC + JDBC로 구현한다.  
> 데이터베이스는 MySQL, SSR(Server Side Rendering)은 Thymeleaf 사용.

---

## 📌 요구사항

### 🧩 UI 컴포넌트 요구사항 (프론트 가이드)
- **헤더/내비게이션 바:** 상단에 프로젝트명과 주요 링크(목록/글쓰기) 배치.
- **검색 바:** 목록 상단에 `keyword` 입력 필드 + 검색 버튼.
- **페이지네이션 컴포넌트:** 하단에 `[0] [1] [2]…` 형태. 현재 페이지는 굵게.
- **토스트/알림:** 저장/수정/삭제 성공 시 상단 우측에 2~3초 노출되는 토스트(선택). 초기엔 `alert()`로 대체 가능.
- **모달(필수):**
    - **삭제 확인 모달:** "이 글을 삭제할까요?" 확인/취소. (현재는 `confirm()` 임시 사용 → 후에 커스텀 모달로 교체 권장)
    - **검증 에러 모달(선택):** 서버 검증 실패 시 폼 상단에 인라인 오류와 함께 전체 요약 모달 표시 가능.
- **로딩 인디케이터(선택):** 목록/상세/저장 전송 중 스피너 노출.
- **폼 컴포넌트:** 제목/작성자/내용 필수. 각 필드 아래에 인라인 검증 메시지.
- **빈 상태 뷰:** 목록에 데이터 없을 때 "데이터 없음" 문구.
- **공통 버튼:** 저장, 수정, 삭제, 목록, 글쓰기 기본 버튼 스타일 통일.

### 🎯 핵심 기능
| 구분 | 기능 | 설명 |
|---:|---|---|
| 1 | 게시글 목록 | 페이지네이션, 제목 검색 |
| 2 | 게시글 상세 | 단건 조회 |
| 3 | 게시글 작성 | 제목, 내용, 작성자 입력 |
| 4 | 게시글 수정 | 제목/내용 수정 가능 |
| 5 | 게시글 삭제 | 게시글 삭제 가능 |

### 📄 API & 화면 설계
| 기능 | 요청 | URL | 설명 |
|---|---|---|---|
| 목록 | GET | `/posts` | 검색/페이지네이션 |
| 상세 | GET | `/posts/{id}` | 글 상세 보기 |
| 작성 폼 | GET | `/posts/new` | 새 글 작성 폼 |
| 작성 처리 | POST | `/posts` | 새 글 등록 |
| 수정 폼 | GET | `/posts/{id}/edit` | 수정 폼 표시 |
| 수정 처리 | POST | `/posts/{id}/edit` | 수정 저장 |
| 삭제 | POST | `/posts/{id}/delete` | 삭제 처리 |

### 🧮 데이터베이스
```sql
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  author VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_posts_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 🗂 프로젝트 구조도
```
src
 └─ main
    ├─ java
    │   └─ com.example.board
    │       ├─ BoardApplication.java
    │       │
    │       ├─ controller
    │       │    └─ PostController.java      # 웹 요청 처리 (MVC의 Controller)
    │       │
    │       ├─ service
    │       │    └─ PostService.java         # 비즈니스 로직 계층
    │       │
    │       ├─ repository
    │       │    ├─ PostRepository.java      # 인터페이스
    │       │    └─ JdbcPostRepository.java  # JdbcTemplate 구현체
    │       │
    │       ├─ domain
    │       │    └─ Post.java                # 엔티티(도메인 모델)
    │       │
    │       └─ dto
    │            └─ PostForm.java            # 폼 입력 DTO
    │
    └─ resources
        ├─ templates
        │   └─ posts
        │        ├─ list.html
        │        ├─ detail.html
        │        └─ form.html
        │
        ├─ application.yml
        └─ init.sql
```

---

## 🧱 정적 파일 (HTML) — **설명 주석 포함**

### `src/main/resources/templates/posts/list.html`
```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8"/>
  <title>Posts</title>
  <!-- SSR(Thymeleaf) 템플릿. 서버에서 모델 데이터를 주입해서 HTML을 그린다. -->
  <!-- th:* 속성은 클라이언트에 전달되기 전에 서버에서 처리되어 일반 HTML로 변환됨 -->
</head>
<body>
<h1>게시판</h1>

<!-- 검색 폼: GET /posts?keyword=...&page=...&size=...  -->
<form th:action="@{/posts}" method="get">
  <!-- name=keyword 로 서버에 전달. th:value 로 현재 검색어 유지 -->
  <input type="text" name="keyword" th:value="${keyword}" placeholder="제목 검색"/>
  <button type="submit">검색</button>
  <!-- 글쓰기 링크: GET /posts/new 로 이동 (작성 폼 화면) -->
  <a th:href="@{/posts/new}">글쓰기</a>
</form>

<!-- 총 개수 표시: 서버 모델의 total 값을 출력 -->
<p th:text="'총 ' + ${total} + '건'"></p>

<!-- 목록 테이블: 서버에서 posts(List<Post>) 를 전달 -->
<table border="1" width="100%">
  <thead>
  <tr><th>ID</th><th>제목</th><th>작성자</th><th>작성일</th><th>액션</th></tr>
  </thead>
  <tbody>
  <!-- th:each 로 각 Post 를 순회하여 행을 만든다 -->
  <tr th:each="p : ${posts}">
    <td th:text="${p.id}"></td>
    <!-- 게시글 상세 링크: /posts/{id}  -->
    <td><a th:href="@{|/posts/${p.id}|}" th:text="${p.title}"></a></td>
    <td th:text="${p.author}"></td>
    <!-- 날짜 출력: 기본 LocalDateTime toString. 포맷 원하면 #temporals 사용 가능 -->
    <td th:text="${p.createdAt}"></td>
    <td>
      <!-- 수정 링크: /posts/{id}/edit  (수정 폼 화면) -->
      <a th:href="@{|/posts/${p.id}/edit|}">수정</a>
      <!-- 삭제 폼: POST /posts/{id}/delete  (서버에서 삭제 처리 후 목록으로 리다이렉트) -->
      <form th:action="@{|/posts/${p.id}/delete|}" method="post" style="display:inline;">
        <!-- 기본 confirm()은 브라우저 다이얼로그. 이후 모달 컴포넌트로 교체 예정 -->
        <button type="submit" onclick="return confirm('삭제할까요?')">삭제</button>
      </form>
    </td>
  </tr>
  <!-- 데이터 없을 때 표시용 행 -->
  <tr th:if="${#lists.isEmpty(posts)}"><td colspan="5">데이터 없음</td></tr>
  </tbody>
</table>

<!-- 페이지네이션: 0..totalPages-1 인덱스 링크를 만든다. 현재 페이지는 굵게 표시 -->
<div>
  <span th:each="i : ${#numbers.sequence(0, totalPages - 1)}">
    <a th:if="${i != page}"
       th:href="@{/posts(page=${i}, size=${size}, keyword=${keyword})}"
       th:text="${'[' + i + ']'}"></a>
    <b th:if="${i == page}" th:text="${'[' + i + ']'}"></b>
    <span> </span>
  </span>
</div>

</body>
</html>
```

### `src/main/resources/templates/posts/detail.html`
```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8"/>
  <!-- 페이지 제목을 글 제목으로 설정 (SSR 시 서버에서 치환) -->
  <title th:text="${post.title}">상세</title>
</head>
<body>
<!-- 목록으로 돌아가기 링크 -->
<a th:href="@{/posts}">← 목록</a>

<!-- 글 제목/메타 정보 출력 -->
<h1 th:text="${post.title}"></h1>
<div>작성자: <span th:text="${post.author}"></span></div>
<div>작성일: <span th:text="${post.createdAt}"></span></div>
<hr/>

<!-- 본문: 줄바꿈 보존을 위해 <pre> + white-space: pre-wrap 사용 -->
<pre th:text="${post.content}" style="white-space: pre-wrap;"></pre>
<hr/>

<!-- 수정/삭제 액션 -->
<a th:href="@{|/posts/${post.id}/edit|}">수정</a>
<form th:action="@{|/posts/${post.id}/delete|}" method="post" style="display:inline;">
  <button type="submit" onclick="return confirm('삭제할까요?')">삭제</button>
</form>
</body>
</html>
```

### `src/main/resources/templates/posts/form.html`
```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8"/>
  <!-- 작성/수정 모드에 따라 제목을 바꾼다 -->
  <title th:text="${edit} ? '글 수정' : '글 작성'">글 작성</title>
</head>
<body>
<a th:href="@{/posts}">← 목록</a>
<h1 th:text="${edit} ? '글 수정' : '글 작성'"></h1>

<!-- th:object 로 서버에서 전달된 PostForm 바인딩 -->
<!-- 작성: POST /posts  /  수정: POST /posts/{id}/edit  -->
<form th:action="${edit} ? @{'/posts/' + ${postId} + '/edit'} : @{/posts}"
      method="post" th:object="${form}">
  <div>
    <label>제목</label><br/>
    <!-- th:field 는 name/value/error 바인딩을 자동 처리 -->
    <input type="text" th:field="*{title}" maxlength="200" style="width:400px;"/>
    <!-- 검증 오류 메시지 출력 -->
    <div th:if="${#fields.hasErrors('title')}" th:errors="*{title}"></div>
  </div>
  <div>
    <label>작성자</label><br/>
    <!-- 수정 시 작성자 고정(예시): th:disabled 로 비활성화만 함. 서버 검증은 별도 필요 -->
    <input type="text" th:field="*{author}" maxlength="50" style="width:200px;"
           th:disabled="${edit}"/>
    <div th:if="${#fields.hasErrors('author')}" th:errors="*{author}"></div>
  </div>
  <div>
    <label>내용</label><br/>
    <textarea th:field="*{content}" rows="12" cols="80"></textarea>
    <div th:if="${#fields.hasErrors('content')}" th:errors="*{content}"></div>
  </div>
  <!-- 제출 버튼: 작성/수정 구분 없이 동일 폼에서 처리 -->
  <button type="submit">저장</button>
</form>
</body>
</html>
```

---

## 💻 백엔드 코드 (정답)

### `src/main/java/com/example/board/BoardApplication.java`
```java
package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }
}
```

### `src/main/java/com/example/board/domain/Post.java`
```java
package com.example.board.domain;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

### `src/main/java/com/example/board/dto/PostForm.java`
```java
package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {
    @NotBlank @Size(max = 200)
    private String title;
    @NotBlank
    private String content;
    @NotBlank @Size(max = 50)
    private String author;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
```

### `src/main/java/com/example/board/repository/PostRepository.java`
```java
package com.example.board.repository;

import com.example.board.domain.Post;
import java.util.*;

public interface PostRepository {
    Long save(Post post);                   // insert, return id
    void update(Post post);                 // update title/content
    void delete(Long id);

    Optional<Post> findById(Long id);
    List<Post> findPage(String keyword, int page, int size); // DESC by id
    long count(String keyword);
}
```

### `src/main/java/com/example/board/repository/JdbcPostRepository.java`
```java
package com.example.board.repository;

import com.example.board.domain.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Post> mapper = (rs, rowNum) -> {
        Post p = new Post();
        p.setId(rs.getLong("id"));
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setAuthor(rs.getString("author"));
        Timestamp c = rs.getTimestamp("created_at");
        Timestamp u = rs.getTimestamp("updated_at");
        p.setCreatedAt(c != null ? c.toLocalDateTime() : null);
        p.setUpdatedAt(u != null ? u.toLocalDateTime() : null);
        return p;
    };

    @Override
    public Long save(Post post) {
        String sql = "INSERT INTO posts(title, content, author, created_at) VALUES(?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getAuthor());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, kh);
        Number key = kh.getKey();
        return key != null ? key.longValue() : null;
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public Optional<Post> findById(Long id) {
        List<Post> list = jdbcTemplate.query("SELECT * FROM posts WHERE id = ?", mapper, id);
        return list.stream().findFirst();
    }

    @Override
    public List<Post> findPage(String keyword, int page, int size) {
        int offset = page * size;
        String base = "FROM posts ";
        String where = (keyword == null || keyword.isBlank())
                ? ""
                : "WHERE title LIKE ? ";
        String orderLimit = "ORDER BY id DESC LIMIT ? OFFSET ?";

        if (where.isEmpty()) {
            return jdbcTemplate.query("SELECT * " + base + orderLimit, mapper, size, offset);
        } else {
            String k = "%" + keyword.trim() + "%";
            return jdbcTemplate.query("SELECT * " + base + where + orderLimit, mapper, k, size, offset);
        }
    }

    @Override
    public long count(String keyword) {
        String base = "SELECT COUNT(*) FROM posts ";
        if (keyword == null || keyword.isBlank()) {
            return jdbcTemplate.queryForObject(base, Long.class);
        } else {
            String k = "%" + keyword.trim() + "%";
            return jdbcTemplate.queryForObject(base + "WHERE title LIKE ?", Long.class, k);
        }
    }
}
```

### `src/main/java/com/example/board/service/PostService.java`
```java
package com.example.board.service;

import com.example.board.domain.Post;
import com.example.board.dto.PostForm;
import com.example.board.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repo;

    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Long create(PostForm form) {
        Post p = new Post();
        p.setTitle(form.getTitle());
        p.setContent(form.getContent());
        p.setAuthor(form.getAuthor());
        return repo.save(p);
    }

    @Transactional
    public void update(Long id, PostForm form) {
        Post p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
        p.setTitle(form.getTitle());
        p.setContent(form.getContent());
        repo.update(p);
    }

    @Transactional
    public void delete(Long id) {
        repo.delete(id);
    }

    @Transactional(readOnly = true)
    public Post get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    }

    @Transactional(readOnly = true)
    public List<Post> findPage(String keyword, int page, int size) {
        return repo.findPage(keyword, page, size);
    }

    @Transactional(readOnly = true)
    public long count(String keyword) {
        return repo.count(keyword);
    }
}
```

### `src/main/java/com/example/board/controller/PostController.java`
```java
package com.example.board.controller;

import com.example.board.domain.Post;
import com.example.board.dto.PostForm;
import com.example.board.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    // 목록 + 검색 + 페이지네이션
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<Post> content = service.findPage(keyword, page, size);
        long total = service.count(keyword);
        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("posts", content);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        return "posts/list";
    }

    // 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", service.get(id));
        return "posts/detail";
    }

    // 작성 폼
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "posts/form";
    }

    // 작성 처리
    @PostMapping
    public String create(@Valid @ModelAttribute("form") PostForm form, BindingResult binding) {
        if (binding.hasErrors()) {
            return "posts/form";
        }
        Long id = service.create(form);
        return "redirect:/posts/" + id;
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post p = service.get(id);
        PostForm form = new PostForm();
        form.setTitle(p.getTitle());
        form.setContent(p.getContent());
        form.setAuthor(p.getAuthor()); // 예시: 수정 비활성화는 뷰에서 처리
        model.addAttribute("form", form);
        model.addAttribute("postId", id);
        model.addAttribute("edit", true);
        return "posts/form";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("form") PostForm form, BindingResult binding) {
        if (binding.hasErrors()) {
            return "posts/form";
        }
        service.update(id, form);
        return "redirect:/posts/" + id;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/posts";
    }
}
```

### `src/main/resources/application.yml` (예시: 로컬 도커 MySQL)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/boarddb?useSSL=false&serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true
    username: boarduser
    password: boardpass
    driver-class-name: com.mysql.cj.jdbc.Driver

  jdbc:
    template:
      fetch-size: 100
      max-rows: 1000

  thymeleaf:
    cache: false

server:
  port: 8080

logging:
  level:
    org.springframework.jdbc.core: debug
```

### `src/main/resources/init.sql`
```sql
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  author VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_posts_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## ▶️ 실행 순서 요약
1) **MySQL 도커** 띄우기 (예시 `.env` / `docker-compose.yml`은 이전 대화 참고)
2) `./gradlew bootRun`
3) 브라우저에서 `http://localhost:8080/posts` 접속

---

## 📝 메모
- 작성자 필드는 수정 폼에서 비활성화만 해둠(뷰). **서버 검증(변경 금지)**을 추가하고 싶으면 `PostService.update`에서 기존 author와 비교하여 변경 시 예외를 던지면 된다.
- 날짜 포맷: Thymeleaf의 `#temporals`를 사용하거나 `@Configuration`으로 `FormattingConversionService`를 등록해 통일 가능.
- 모달/토스트는 초기엔 기본 브라우저 대화상자 사용 → 나중에 공통 컴포넌트로 치환.
