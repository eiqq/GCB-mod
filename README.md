# GCB Mod (게임캐릭터배틀 모드)

커스텀 컨텐츠 서버를 위한 클라이언트 API 모드입니다. 서버와 클라이언트 간 파티클, 키입력, 카메라, 플레이어 제어 등의 통신을 지원합니다.

A client-side API mod for custom content servers. Provides communication between server and client for particles, key input, camera, and player control.

**Minecraft:** 26.3 | **Loader:** Fabric (Fabric API 필요 / requires Fabric API) | **Java:** 25

## 기능 / Features

### 파티클 시스템 / Particle System
서버에서 클라이언트로 파티클 렌더링 명령을 전송하여 다양한 형태의 파티클을 표시합니다.

Renders various particle shapes on the client based on server-sent commands.

- Circle, Line, Trail, Sphere, Spiral, SpiralLine
- 엔티티 추적, 변환 매트릭스, 시간 기반 애니메이션 지원
- Entity tracking, transformation matrix, time-based animation support

### 키입력 전송 / Key Input
13개의 키바인딩(Z, X, C, V, 0, 1-9, 공격, 사용, 선택, 드롭)의 입력 상태를 서버에 실시간 전송합니다.

Sends real-time press/release states of 13 keybindings to the server.

### 카메라 시점 / Camera Perspective
플레이어의 시점 전환(1인칭/3인칭)을 감지하여 서버에 알립니다.

Detects and notifies the server when the player switches camera perspective.

### ModelEngine 본 데이터 채널 / ModelEngine Bone Data Channel
GCB 서버의 패치된 ModelEngine이 보내는 `gcb:bulk_data` 채널을 수신해 모델 본(Display 엔티티)의 변환을 한 패킷으로 갱신합니다. 바닐라 경로(본마다 메타데이터 패킷)와 같은 fp32 정밀도를 유지하면서 패킷 수와 바이트를 줄입니다.

Receives the `gcb:bulk_data` channel from the GCB-patched ModelEngine and updates model bone (Display entity) transforms in a single packet per model, keeping vanilla fp32 precision while reducing packet count and bytes.

### 3인칭 마운트 예측 / Third-Person Mount Prediction
서버 권한 탈것(ModelEngine 마운트)의 표시 보간을 3틱에서 1틱으로 줄이고, 서버가 보낸 속도로 1틱 앞을 그려 3인칭 캐릭터의 조작 체감 지연을 줄입니다.

Reduces display interpolation of server-authoritative mounts (ModelEngine mounts) from 3 ticks to 1 and leads by 1 tick using the server-sent velocity, lowering perceived input latency for third-person characters.

## 설치 / Installation

`mods/` 폴더에 모드 JAR 파일을 넣으세요. Fabric API가 필요합니다.

Drop the mod JAR into your `mods/` folder. Requires Fabric API.

## 라이선스 / License

[AGPL-3.0](LICENSE)
