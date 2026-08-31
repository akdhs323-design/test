# 하루도우미

실생활에 바로 쓰는 일정·지출·집중 관리 서비스입니다.

## Android 앱 — 전체 기능판

- 할 일: 개수 제한 없이 추가/완료/삭제
- 자동 저장: 앱을 종료해도 할 일과 지출 내역 유지
- 상세 지출: 금액 + 메모, 최근 내역 표시
- 집중 타이머: 10/25/50분 프리셋, 시작/일시정지/리셋
- 개인 메모: 최대 500자 저장/편집
- 오늘의 요약: 완료 현황 + 전체 지출 합계

## Web — 가벼운 체험판

사이트에서는 서비스의 핵심만 사용할 수 있도록 일부 기능을 제한했습니다.

- 할 일 최대 3개
- 지출은 금액 총액만 기록
- 상세 지출 내역/메모 없음
- 집중 타이머 잠금
- 알림/백그라운드 기능 잠금
- 브라우저 localStorage 저장

## Web 도메인

- `aon69446.kro.kr`

GitHub Pages용 `web/CNAME`을 포함해 커스텀 도메인 배포를 준비했습니다.

## 빌드

GitHub Actions에서 Android Debug APK를 자동 빌드하고 APK 파일이 실제로 생성됐는지 검증한 뒤 `DailyHelper-debug` artifact로 업로드합니다.

웹은 `pages.yml`로 `web/`을 GitHub Pages에 배포하도록 구성했습니다.

## 구조

```text
app/                 Android 전체 기능판
web/                 웹 제한 기능판
web/CNAME            aon69446.kro.kr
.github/workflows/
  android.yml        APK 자동 빌드 + APK 존재 검증
  pages.yml          웹 자동 배포
```
