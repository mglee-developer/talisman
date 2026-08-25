## 🔗 라이브 데모 (Live Demo)

- **배포 주소**: [https://trash-app-beta.vercel.app/](https://trash-app-beta.vercel.app/)

---

## ✨ 주요 기능 (Key Features)

- **✍️ 빡침 내용 작성 & 구겨서 버리기 (`v-model`, CSS Animation)**
  - 스트레스 받은 일이나 하고 싶은 말을 자유롭게 작성
  - '구겨서 버리기' 버튼 클릭 시 3D 회전 및 축소 CSS 애니메이션 연출 (`.crumple`)
- **🔊 종이 구겨지는 효과음 연출 (`Audio API`)**
  - 실제 종이를 구기는 듯한 바스락 효과음 연동
  - 상단 **음소거 토글 버튼 (🔊 소리 켬 / 🔇 소리 끔)** 제공
- **💬 무작위 사이다 / 위로 메시지 (`v-if`, Math.random)**
  - 감정을 버린 직후 나타나는 힐링 & 사이다 멘트 제공
- **📊 빡침 카운터 & 데이터 보존 (`localStorage`, `onMounted`)**
  - 오늘 버린 빡침 개수를 실시간 카운팅
  - 브라우저를 새로고침하거나 껐다 켜도 데이터가 유지되는 LocalStorage 영속성
  - 카운터를 '0'으로 초기화하는 **리셋 기능** 포함
- **📱 모바일 최적화 및 PWA 스타일 지원**
  - 반응형 카드 UI (최대 폭 420px 모바일 화면 최적화)
  - 스마트폰 '홈 화면에 추가'를 통해 실제 앱처럼 사용 가능

---

## 🛠️ 기술 스택 (Tech Stack)

| 구분 | 기술 / 도구 |
|---|---|
| **Framework** | Vue 3 (Composition API, `<script setup>`) |
| **Build Tool** | Vite |
| **Language** | HTML5, CSS3, JavaScript (ES6+) |
| **Persistence** | Web Storage API (`localStorage`) |
| **IDE / Version Control** | IntelliJ IDEA, Git, GitHub |
| **Deployment** | Vercel |

---

## 📅 개발 일정 (5 Days Roadmap)

- **[x] Day 1: 기본 UI 레이아웃 및 입력 제어**
  - `v-model` 기반 Textarea 입력창 및 카드 UI 구성
- **[x] Day 2: 구김 애니메이션 & 카운터 기능**
  - CSS Transition (`transform`, `opacity`) 기반 구겨짐 애니메이션 및 쓰레기 카운터 연동
- **[x] Day 3: 오디오 연동 & 랜덤 위로 메시지**
  - HTML Audio API를 활용한 종이 소리 연동 및 무작위 위로 멘트 출력
- **[x] Day 4: 데이터 지속성 & 유틸리티 기능**
  - `localStorage`를 이용한 카운터/음소거 상태 보존, 리셋 버튼 및 Mute 토글 추가
- **[x] Day 5: 배포 및 모바일 웹 앱 완성**
  - Vercel 플랫폼을 통한 웹 배포 및 모바일 환경 최적화

---

## 🚀 시작하기 (Getting Started)

### 개발 환경 설치 및 실행

```bash
# 1. 저장소 클론
git clone [https://github.com/your-username/trash-app.git](https://github.com/your-username/trash-app.git)

# 2. 프로젝트 폴더로 이동
cd trash-app

# 3. 패키지 설치
npm install

# 4. 개발 서버 실행
npm run dev