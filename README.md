# 하루도우미

실생활을 위한 간단한 일정·지출·집중 관리 서비스입니다.

## Android 앱

Android 앱에서는 전체 기능을 제공합니다.

- 할 일 추가/완료/삭제
- 앱 종료 후에도 저장되는 할 일/지출 데이터
- 오늘의 지출 기록
- 25분 집중 타이머
- 타이머 일시정지/리셋

GitHub Actions가 `assembleDebug`로 APK를 빌드하고 `DailyHelper-debug` artifact로 올립니다.

## Web 체험판

`web/`에 있는 웹 버전은 가볍게 체험하는 용도입니다.

- 할 일 최대 3개
- 지출은 총액만 기록
- 집중 타이머 제한
- 알림 기능 제한
- 브라우저 localStorage에만 저장

GitHub Pages workflow가 `main` 브랜치에 올라간 `web/`을 사이트로 배포하도록 구성되어 있습니다.

## 프로젝트 구조

```text
app/                 Android 전체 기능판
web/                 웹 제한 기능판
.github/workflows/
  android.yml        APK 자동 빌드
  pages.yml          웹 자동 배포
```
