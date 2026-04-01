# GCB Mod (게임캐릭터배틀 모드)

커스텀 컨텐츠 서버를 위한 클라이언트 API 모드입니다. 서버와 클라이언트 간 파티클, 키입력, 카메라, 플레이어 제어 등의 통신을 지원합니다.

A client-side API mod for custom content servers. Provides communication between server and client for particles, key input, camera, and player control.

**Minecraft:** 1.21.8 | **Loader:** Fabric

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

### 플레이어 속도 제어 / Player Velocity
서버에서 클라이언트 플레이어에게 커스텀 속도 벡터를 적용합니다. 넉백, 발사 등의 서버 주도 이동에 사용됩니다.

Applies custom velocity vectors from the server to the client player. Used for server-driven knockback, launches, etc.

## 설치 / Installation

`mods/` 폴더에 모드 JAR 파일을 넣으세요. Fabric API가 필요합니다.

Drop the mod JAR into your `mods/` folder. Requires Fabric API.

## 포함된 서드파티 코드 / Included Third-Party Code

이 프로젝트에는 [meg-client-mod](https://github.com/Taiyou06/meg-client-mod) (Copyright (c) 2026 Taiyouh, AGPL-3.0)의 코드가 포함되어 있습니다. Model Engine 애니메이션 패킷 최적화를 위한 클라이언트 모듈입니다.

This project includes code from [meg-client-mod](https://github.com/Taiyou06/meg-client-mod) (Copyright (c) 2026 Taiyouh, AGPL-3.0), a client module for optimizing Model Engine animation packets.

## 라이선스 / License

[AGPL-3.0](LICENSE)
